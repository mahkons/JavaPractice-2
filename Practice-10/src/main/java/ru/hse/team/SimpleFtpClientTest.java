package ru.hse.team;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SimpleFtpClientTest {
    private SimpleFtpClient client;

    @BeforeEach
    void init() {
        client = new SimpleFtpClient();
    }

    @Test
    void simpleTest() throws IOException {
        client.connect();
        String response = client.executeList("docs/");
        assertEquals("docs/ 1", response);
        client.disconnect();
    }

    @Test
    void noSuchFileTest() throws IOException {
        client.connect();
        String response = client.executeList("docs/lalla");
        assertEquals("-1", response);
        client.disconnect();
    }
}