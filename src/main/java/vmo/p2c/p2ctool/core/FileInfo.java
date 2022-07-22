package vmo.p2c.p2ctool.core;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.io.File;

public class FileInfo {
    public static final int idx_Icon= 0;
    public static final int idx_Seq= 1;
    public static final int idx_File= 2;
    public static final int  idx_Path= 3;
    public static final int  idx_Size= 4;
    public static final int idx_LoC= 5;
    public static final int  idx_Cobol= 6;
    public static final int idx_Jcl= 7;
    public static final int idx_Rexx= 8;
    public static final int idx_Clist= 9;
    public static final int idx_Msos= 10;
    public static final int idx_Lnas= 11;
    public static final int idx_Cpas= 12;
    public static final int idx_Stars= 13;
    public static final int idx_Buc= 14;
    public static final int idx_Fes= 15;
    public static final int idx_ActionOpenFile= 16;

    private Boolean isFile;
    private File originalFile;
    private LongProperty seq;
    private Image icon;
    private StringProperty file;
    private StringProperty path;
    private StringProperty size;
    private StringProperty lastMofified;
    private StringProperty lineOfCode;
    private BooleanProperty cobol;
    private BooleanProperty jcl;
    private BooleanProperty rexx;

    private BooleanProperty clist;

    private BooleanProperty msos;
    private BooleanProperty lnas;
    private BooleanProperty cpas;
    private BooleanProperty stars;
    private BooleanProperty buc;
    private BooleanProperty fes;
    private StringProperty metadata;

    public FileInfo(File originalFile, Long seq, Image icon, String file, String path,
                     String size, String lastMofified, String lineOfCode,
                     Boolean cobol, Boolean jcl, Boolean rexx, Boolean clist,
                     Boolean msos, Boolean lnas, Boolean cpas,
                     Boolean stars, Boolean buc, Boolean fes,
                    String metadata) {
        if(originalFile.exists() && originalFile.isFile() )
            this.isFile= true;
        else
            this.isFile= false;

        this.originalFile = originalFile;
        this.seq = new SimpleLongProperty(seq);
        this.icon = icon;
        this.file = new SimpleStringProperty(file);
        this.path = new SimpleStringProperty(path);
        this.size = new SimpleStringProperty(size);
        this.lastMofified = new SimpleStringProperty(lastMofified);

        this.lineOfCode = new SimpleStringProperty(lineOfCode);

        this.cobol = new SimpleBooleanProperty(cobol);
        this.jcl = new SimpleBooleanProperty(jcl);
        this.setRexx(new SimpleBooleanProperty(rexx));
        this.clist = new SimpleBooleanProperty(clist);

        this.msos = new SimpleBooleanProperty(msos);
        this.lnas = new SimpleBooleanProperty(lnas);
        this.cpas = new SimpleBooleanProperty(cpas);
        this.stars = new SimpleBooleanProperty(stars);
        this.buc = new SimpleBooleanProperty(buc);
        this.fes = new SimpleBooleanProperty(fes);

        this.metadata = new SimpleStringProperty(metadata);
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public StringProperty getFile() {
        return file;
    }

    public void setFile(StringProperty file) {
        this.file = file;
    }

    public StringProperty getPath() {
        return path;
    }

    public void setPath(StringProperty path) {
        this.path = path;
    }

    public StringProperty getSize() {
        return size;
    }

    public void setSize(StringProperty size) {
        this.size = size;
    }

    public StringProperty getLastMofified() {
        return lastMofified;
    }

    public void setLastMofified(StringProperty lastMofified) {
        this.lastMofified = lastMofified;
    }

    public StringProperty getLineOfCode() {
        return lineOfCode;
    }

    public void setLineOfCode(StringProperty lineOfCode) {
        this.lineOfCode = lineOfCode;
    }

    public BooleanProperty getCobol() {
        return cobol;
    }

    public void setCobol(BooleanProperty cobol) {
        this.cobol = cobol;
    }

    public BooleanProperty getJcl() {
        return jcl;
    }

    public void setJcl(BooleanProperty jcl) {
        this.jcl = jcl;
    }

    public BooleanProperty getRexx() {
        return rexx;
    }

    public void setRexx(BooleanProperty rexx) {
        this.rexx = rexx;
    }

    public StringProperty getMetadata() {
        return metadata;
    }

    public void setMetadata(StringProperty metadata) {
        this.metadata = metadata;
    }

    public LongProperty getSeq() {
        return seq;
    }

    public void setSeq(LongProperty seq) {
        this.seq = seq;
    }

    public File getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(File originalFile) {
        this.originalFile = originalFile;
    }

    public BooleanProperty getMsos() {
        return msos;
    }

    public void setMsos(BooleanProperty msos) {
        this.msos = msos;
    }

    public BooleanProperty getLnas() {
        return lnas;
    }

    public void setLnas(BooleanProperty lnas) {
        this.lnas = lnas;
    }

    public BooleanProperty getCpas() {
        return cpas;
    }

    public void setCpas(BooleanProperty cpas) {
        this.cpas = cpas;
    }

    public BooleanProperty getStars() {
        return stars;
    }

    public void setStars(BooleanProperty stars) {
        this.stars = stars;
    }

    public BooleanProperty getBuc() {
        return buc;
    }

    public void setBuc(BooleanProperty buc) {
        this.buc = buc;
    }

    public BooleanProperty getFes() {
        return fes;
    }

    public void setFes(BooleanProperty fes) {
        this.fes = fes;
    }

    public Boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(Boolean isFile) {
        this.isFile = isFile;
    }

    public BooleanProperty getClist() {
        return clist;
    }

    public void setClist(BooleanProperty clist) {
        this.clist = clist;
    }
}
