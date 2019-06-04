package ru.hse.team;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SimpleFtpServer {
    private static final int INT_SIZE = 4;
    private static int serverPort = 9999;
    private static int bufferSize = 4048;
    private final ServerSocketChannel serverSocketChannel;

    private final Selector selector = Selector.open();

    private ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    private boolean serverRunning = true;

    public SimpleFtpServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(serverPort));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() {
        new Thread(() -> {
            try {
                while (serverRunning) {
                    System.out.println("Waiting for clients...");
                    int n = selector.select(1000);
                    if (n == 0) {
                        continue;
                    }
                    System.out.println("Got a job!");
                    var selectedKeys = selector.selectedKeys();
                    var it = selectedKeys.iterator();
                    while (it.hasNext()) {
                        var key = it.next();
                        if (key.isAcceptable()) {
                            var server = (ServerSocketChannel) key.channel();
                            var channel = server.accept();
                            System.out.println("Got a client!");
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ, new Client(channel));
                        } else if (key.isReadable()) {
                            var client = (Client) key.attachment();
                            System.out.println("Reading from client " + client.toString());
                            client.channel.read(client.buffers);
                            if (client.size == -1 && client.sizeBuffer.position() == INT_SIZE) {
                                client.sizeBuffer.flip();
                                client.size = client.sizeBuffer.getInt();
                            }
                            if (client.size != -1 && client.buffer.position() == client.size) {
                                key.cancel();
                                System.out.println("Read from client " + client.toString() + " fully.");
                                threadPool.submit(new ClientTask(client));
                            }
                        } else if (key.isWritable()) {
                            var client = (Client) key.attachment();
                            System.out.println("Writing client " + client.toString());
                            client.channel.write(client.buffers);
                            if (client.sizeBuffer.position() == INT_SIZE && client.buffer.position() == client.buffer.limit()) {
                                System.out.println("Write client " + client.toString() + " fully.");
                                client.buffer.clear();
                                client.sizeBuffer.clear();
                                client.size = -1;
                                key.cancel();
                            }
                        }
                        it.remove();
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class ClientTask implements Runnable {
        private Client client;

        private ClientTask(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                System.out.println("Got work request from client " + client.toString());
                client.buffer.flip();
                var data = new byte[client.size];
                client.buffer.get(data);
                client.buffer.clear();
                client.sizeBuffer.clear();
                String request = new String(data, StandardCharsets.UTF_8);
                byte[] respondData;
                assert request.length() > 2 && (request.startsWith("1 ") || request.startsWith("2 "));
                if (request.charAt(0) == '1') {
                    respondData = list(request.substring(2));
                } else {
                    respondData = get(request.substring(2));
                }
                client.sizeBuffer.putInt(respondData.length);
                client.buffer.put(respondData);
                client.sizeBuffer.flip();
                client.buffer.flip();
                System.out.println("Work for client " + client.toString() + " done!");
                client.channel.register(selector, SelectionKey.OP_WRITE, client);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class Client {
        private int size = -1;
        private final ByteBuffer sizeBuffer = ByteBuffer.allocate(INT_SIZE);
        private final ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        private final SocketChannel channel;
        private final ByteBuffer[] buffers = new ByteBuffer[] {sizeBuffer, buffer};

        private Client(SocketChannel channel) {
            sizeBuffer.clear();
            buffer.clear();
            this.channel = channel;
        }
    }



    public static void main(String[] args) throws IOException {
        SimpleFtpServer server = new SimpleFtpServer();
        server.start();
    }

    public void stop() throws IOException {
        serverRunning = false;
        threadPool.shutdown();
        serverSocketChannel.close();
        selector.close();
    }


    private static char isDirectoryChar(boolean flag) {
        if (flag)
            return '1';
        else
            return '0';
    }

    private static byte[] list(String pathName) throws IOException {
        System.out.println("Answer list request... " + pathName);
        String message;
        Path path = Paths.get(pathName);
        if (!Files.exists(path)) {
            message = "-1";
        } else {
            List<String> list = Files.walk(path, 1).map(x -> x + " " + isDirectoryChar(x.toFile().isDirectory())).collect(Collectors.toList());
            message = list.size() + " " + String.join(" ", list);
        }
        System.out.println("List answer is ready.");
        return message.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] get(String pathName) {
        System.out.println("Answer get request... " + pathName);
        String message = "";
        Path path = Paths.get(pathName);

        try {
            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                message = "-1";
            } else {
                try (var input = new FileInputStream(pathName)) {
                    byte[] bytes = input.readAllBytes();
                    message = bytes.length + " " + new String(bytes, StandardCharsets.UTF_8);
                }
            }
        } catch (IOException exception) {
            //AA
        }
        System.out.println("Get answer is ready.");
        return message.getBytes(StandardCharsets.UTF_8);
    }
}
