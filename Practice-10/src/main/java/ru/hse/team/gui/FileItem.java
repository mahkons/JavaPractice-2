package ru.hse.team.gui;

import java.nio.file.Path;
import java.util.Objects;

/**
 * File item which keeps
 *  path to file and whether a file is directory.
 */
public class FileItem {

    private final String name;
    private final boolean isDirectory;

    public FileItem(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileItem fileItem = (FileItem) o;
        return isDirectory() == fileItem.isDirectory() &&
                getName().equals(fileItem.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), isDirectory());
    }

    /**
     * Returns only Name of file without absolute path.
     */
    @Override
    public String toString() {
        return Path.of(name).getFileName().toString();
    }
}
