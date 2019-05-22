package ru.hse.team;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class SimpleFtpClient {
    private SocketChannel socketChannel;

    public void connect() throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", 9999);
        socketChannel = SocketChannel.open(address);
        socketChannel.configureBlocking(false);
    }

    public void disconnect() throws IOException {
        socketChannel.close();
    }

    public String executeList(String path) throws IOException {
        byte[] message = ("1 " + path).getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(message);
        socketChannel.write(buffer);
        buffer.clear();
        socketChannel.read(buffer);
        String response = new String(buffer.array(), StandardCharsets.UTF_8);
        buffer.clear();
        return response;
    }

    public String executeGet(String path) throws IOException {
        byte[] message = ("2 " + path).getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(message);
        socketChannel.write(buffer);
        buffer.clear();
        socketChannel.read(buffer);
        String response = new String(buffer.array(), StandardCharsets.UTF_8);
        buffer.clear();
        return response;
    }

    void createFile(String name, byte[] content) {
        //File file = Files.createFile(name);
    }

    public static void main(String[] args) throws IOException {
        var client = new SimpleFtpClient();
        client.connect();
        System.out.println(client.executeGet("docs/"));
        client.disconnect();
    }
}
