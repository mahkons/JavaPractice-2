package ru.hse.team;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.team.gui.FileItem;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleFtpClientTest {

    private static String localHost = "localhost";
    private static int port = 9999;

    private SimpleFtpServer localServer;

    @BeforeEach
    void startServer() throws IOException {
        localServer = new SimpleFtpServer(localHost, port);
        localServer.start();
    }

    private void sortFileItemsList(List<FileItem> list) {
        list.sort((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getName(), b.getName()));
    }

    private void compareFileLists(List<FileItem> expected, List<FileItem> fileItems) {
        sortFileItemsList(expected);
        sortFileItemsList(fileItems);
        assertEquals(expected, fileItems);
    }

    @Test
    void listFiles() throws IOException {
        var client = new SimpleFtpClient(localHost, port);
        List<FileItem> fileItems = client.executeList("src/test/resources/dir1/dir2");
        List<FileItem> expectedItems = Arrays.asList(
                new FileItem("src/test/resources/dir1/dir2/file1", false),
                new FileItem("src/test/resources/dir1/dir2/dir3", true),
                new FileItem("src/test/resources/dir1/dir2", true));

        compareFileLists(expectedItems, fileItems);
    }

    @Test
    void listComplexDir() throws IOException {
        var client = new SimpleFtpClient(localHost, port);
        List<FileItem> fileItems = client.executeList("src/test/resources/dir1/dir2/dir3");
        List<FileItem> expectedItems = Arrays.asList(
                new FileItem("src/test/resources/dir1/dir2/dir3/dir4", true),
                new FileItem("src/test/resources/dir1/dir2/dir3/file2", false),
                new FileItem("src/test/resources/dir1/dir2/dir3/file3", false),
                new FileItem("src/test/resources/dir1/dir2/dir3", true));

        compareFileLists(expectedItems, fileItems);
    }

    @Test
    void listAlmostEmptyDir() throws IOException {
        var client = new SimpleFtpClient(localHost, port);
        List<FileItem> fileItems = client.executeList("src/test/resources/dir1");
        List<FileItem> expectedItems = Arrays.asList(
                new FileItem("src/test/resources/dir1", true),
                new FileItem("src/test/resources/dir1/dir2", true));

        compareFileLists(expectedItems, fileItems);
    }

    @Test
    void getFile() throws IOException {
        var client = new SimpleFtpClient(localHost, port);
        String message = client.executeGet("src/test/resources/dir1/dir2/file1");
        String expected = "10 file1 text";
        assertEquals(expected, message);
    }

    @AfterEach
    void stopServer() throws IOException {
        localServer.stop();
    }
}