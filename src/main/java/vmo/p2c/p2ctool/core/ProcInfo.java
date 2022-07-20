package vmo.p2c.p2ctool.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.io.File;

public class ProcInfo {
    private File originalFile;
    private String file;
    private String path;
    private StringProperty procName;

    public ProcInfo(File originalFile, String file, String path,
                    String procName) {
        this.originalFile = originalFile;
        this.file = file;
        this.path = path;
        this.procName = new SimpleStringProperty(procName);
    }

    public File getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(File originalFile) {
        this.originalFile = originalFile;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public StringProperty getProcName() {
        return procName;
    }

    public void setProcName(StringProperty procName) {
        this.procName = procName;
    }
}
