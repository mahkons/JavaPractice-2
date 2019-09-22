package ru.hse.team;

import ru.hse.team.gui.FileItem;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Client for FTP server.
 * Supports list and get queries.
 */
public class SimpleFtpClient {

    private SocketChannel socketChannel;
    private final String hostName;
    private final int port;

    public SimpleFtpClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public void connect() throws IOException {
        InetSocketAddress address = new InetSocketAddress(hostName, port);
        socketChannel = SocketChannel.open(address);
    }

    public void disconnect() throws IOException {
        socketChannel.close();
    }

    private String execute(int id, String path) throws IOException {
        connect();
        var message = (id + " " + path).getBytes(StandardCharsets.UTF_8);
        var length = message.length;
        var sizeBuffer = ByteBuffer.allocate(4);
        var buffer = ByteBuffer.allocate(4048);
        var buffers = new ByteBuffer[] {sizeBuffer, buffer};

        sizeBuffer.clear();
        buffer.clear();
        sizeBuffer.putInt(length);
        buffer.put(message);
        sizeBuffer.flip();
        buffer.flip();
        System.out.println("Writing data...");
        socketChannel.write(buffers);
        System.out.println("Data is written.");
        sizeBuffer.clear();
        buffer.clear();
        System.out.println("Waiting for response...");
        socketChannel.read(buffers);
        System.out.println("Got response!");
        sizeBuffer.flip();
        buffer.flip();
        length = sizeBuffer.getInt();
        String response = new String(buffer.array(), 0, length, StandardCharsets.UTF_8);
        buffer.clear();
        disconnect();
        return response;
    }

    /**
     * List all files in the given directory on server.
     */
    public ArrayList<FileItem> executeList(String path) throws IOException {
        var result = execute(1, path);
        var list = new ArrayList<FileItem>();
        var strings = result.split(" ");
        int n = Integer.parseInt(strings[0]);
        for (int i = 1; i < 2 * n + 1; i += 2) {
            var name = strings[i];
            var isDirectory = Integer.parseInt(strings[i + 1]);
            list.add(new FileItem(name, isDirectory == 1));
        }
        return list;
    }

    /**
     * Downloads file content from server.
     * Returns "-1" if there is no such directory
     */
    public String executeGet(String path) throws IOException {
        return execute(2, path);
    }

    public static void main(String[] args) throws IOException {
        var client = new SimpleFtpClient("localhost", 9999);

        System.out.println(client.executeList("./src/main/java/ru/hse/team"));
        System.out.println(client.executeGet("./src/main/java/ru/hse/team/SimpleFtpClient.java"));
    }
}
