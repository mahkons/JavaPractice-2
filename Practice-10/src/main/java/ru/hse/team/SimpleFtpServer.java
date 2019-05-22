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

    private static int serverPort = 9999;
    private static int bufferSize = 256;
    private final ServerSocketChannel serverSocketChannel;

    private Selector selector = Selector.open();

    private ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    private volatile boolean serverRunning = true;

    public SimpleFtpServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(serverPort));
        serverSocketChannel.configureBlocking(false);

        int options = serverSocketChannel.validOps();
        serverSocketChannel.register(selector, options, null);
    }

    private void handleRequest(ByteBuffer buffer, SocketChannel connection) {
        String request = new String(buffer.array(), StandardCharsets.UTF_8);

        try {

            byte[] message;
            if (request.charAt(0) == '1') {
                message = list(request.substring(2));
            } else {
                message = get(request.substring(2));
            }

            ByteBuffer someBuffer = ByteBuffer.wrap(message);
            while (someBuffer.hasRemaining()) {
                connection.write(someBuffer);
            }
        } catch (IOException exception) {
            //AAA
        }
        buffer.clear();
    }

    private char isDirectoryChar(boolean flag) {
        if (flag)
            return '1';
        else
            return '0';
    }

    private byte[] list(String pathName) throws IOException {
        String message;
        Path path = Paths.get(pathName);
        if (!Files.exists(path)) {
            message = "-1";
        } else {
            List<String> list = Files.walk(path).map(x -> x + " " + isDirectoryChar(Files.isDirectory(path))).collect(Collectors.toList());
            message = list.size() + " " + String.join(" ", list);
        }
        return message.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] get(String pathName) {
        String message = "";
        Path path = Paths.get(pathName);

        try {
            if (!Files.exists(path)) {
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
        return message.getBytes(StandardCharsets.UTF_8);
    }

    public void start() throws IOException {

        while(serverRunning) {
            selector.select();

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keysIterator = selectedKeys.iterator();

            while (keysIterator.hasNext()) {
                SelectionKey key = keysIterator.next();

                if (key.isAcceptable()) {
                    SocketChannel clientChannel = serverSocketChannel.accept();
                    clientChannel.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                    clientChannel.register(selector, SelectionKey.OP_READ, buffer);
                } else if (key.isReadable()) {

                    SocketChannel connection = (SocketChannel) key.channel();
                    if (connection.read((ByteBuffer)key.attachment()) == -1) {
                        threadPool.submit(() -> handleRequest((ByteBuffer)key.attachment(), connection));
                    }

                }
                keysIterator.remove();
            }
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
}
