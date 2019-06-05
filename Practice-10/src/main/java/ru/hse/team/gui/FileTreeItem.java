package ru.hse.team.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import ru.hse.team.SimpleFtpClient;

import java.io.IOException;
import java.util.List;

/**
 * Tree of files on server.
 * Every directory will be downloaded once only on first demand
 */
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
        List<FileItem> files;
        try {
            files = client.executeList(getValue().getName());
        } catch (IOException e) {
            e.printStackTrace();
            return FXCollections.emptyObservableList();
        }
        ObservableList<FileTreeItem> children = FXCollections.observableArrayList();
        for (FileItem file : files) {
            if (!file.equals(getValue())) {
                children.add(new FileTreeItem(file, client));
            }
        }

        return children;
    }
}
