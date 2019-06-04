package ru.hse.team.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import ru.hse.team.SimpleFtpClient;

import java.io.IOException;

public class FileTreeItem extends TreeItem<FileItem> {

    private final SimpleFtpClient client;
    private boolean childrenConstructed;

    public FileTreeItem(FileItem item, SimpleFtpClient client) {
        super(item);
        this.client = client;
    }

    @Override
    public boolean isLeaf() {
        return !getValue().isDirectory();
    }

    @Override
    public ObservableList<TreeItem<FileItem>> getChildren() {
        if (!childrenConstructed) {
            childrenConstructed = true;
            super.getChildren().addAll(constructChildren());
        }
        return super.getChildren();
    }

    private ObservableList<FileTreeItem> constructChildren() {
        String message = "";
        try {
            message = client.executeList(getValue().getName());
        } catch (IOException e) {
            e.printStackTrace();
            //AAAA
        }
        ObservableList<FileTreeItem> children = FXCollections.observableArrayList();
        children.add(new FileTreeItem(new FileItem(message, false), client));

        return children;
    }
}
