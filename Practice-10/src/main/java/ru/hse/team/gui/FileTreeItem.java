package ru.hse.team.gui;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import ru.hse.team.SimpleFtpClient;

public class FileTreeItem extends TreeItem<FileItem> {

    private final SimpleFtpClient client;
    private boolean childrensConstructed;

    public FileTreeItem(SimpleFtpClient client) {
        this.client = client;
    }

    @Override
    public boolean isLeaf() {
        return !getValue().isDirectory();
    }

    @Override
    public ObservableList<TreeItem<FileItem>> getChildren() {
        if (!childrensConstructed) {
            childrensConstructed = true;
            getChildren().addAll(constructChildren());
        }
        return getChildren();
    }

    private ObservableList<FileTreeItem> constructChildren() {
        client.executeList()
    }
}
