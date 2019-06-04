package ru.hse.team;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SimpleFtpClient {
    private SocketChannel socketChannel;

    public void connect() throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", 9999);
        socketChannel = SocketChannel.open(address);
    }

    public void disconnect() throws IOException {
        socketChannel.close();
    }

    public String execute(int id, String path) throws IOException {
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
        return response;
    }

    public String executeList(String path) throws IOException {
        return execute(1, path);
    }

    public String executeGet(String path) throws IOException {
        return execute(2, path);
    }

    public static void main(String[] args) throws IOException {
        var client = new SimpleFtpClient();
        client.connect();
        // Только один запрос за раз!
        System.out.println(client.executeList("./src/main/java/ru/hse/team"));
        //System.out.println(client.executeGet("./src/main/java/ru/hse/team/SimpleFtpClient.java"));
        client.disconnect();
    }
}
