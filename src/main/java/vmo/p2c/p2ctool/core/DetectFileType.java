package vmo.p2c.p2ctool.core;

import javafx.beans.property.SimpleBooleanProperty;

import java.io.File;

public class DetectFileType {
    public void DetectProgram_Msos(File f, FileInfo f1) {
        if(f.getName().contains("XXM") || f.getName().contains("XMS") || f.getName().contains("PMS") ||
                f.getName().contains("PXM") || f.getName().contains("PCXS") || f.getName().contains("DMS") )
        {
            f1.setMsos(new SimpleBooleanProperty(true));
        }
    }

    public void DetectProgram_Lnas(File f, FileInfo f1) {
        if(f.getName().contains("XXL") || f.getName().contains("XLN") || f.getName().contains("PXL") ||
                f.getName().contains("PLN") || f.getName().contains("DLN") )
        {
            f1.setLnas(new SimpleBooleanProperty(true));
        }
    }

    public void DetectProgram_Cpas(File f, FileInfo f1) {
        if(f.getName().contains("XXC") || f.getName().contains("XCP") || f.getName().contains("PXC") ||
                f.getName().contains("PCP") || f.getName().contains("DCP") )
        {
            f1.setCpas(new SimpleBooleanProperty(true));
        }
    }

    public void DetectProgram_Stars(File f, FileInfo f1) {
        if(f.getName().contains("XST") || f.getName().contains("XXW") || f.getName().contains("PST") ||
                f.getName().contains("DST") )
        {
            f1.setStars(new SimpleBooleanProperty(true));
        }
    }

    public void DetectProgram_Buc(File f, FileInfo f1) {
        if(f.getName().contains("PBC") || f.getName().contains("DBC") )
        {
            f1.setBuc(new SimpleBooleanProperty(true));
        }
    }

    public void DetectProgram_Fes(File f, FileInfo f1) {
        if(f.getName().contains("PFES") )
        {
            f1.setFes(new SimpleBooleanProperty(true));
        }
    }

    public boolean DetectFile_Clist(File f, FileInfo f1) {
        if(f.getName().contains("&DATATYPE") || f.getName().contains("&EVAL") || f.getName().contains("&LENGTH") ||
           f.getName().contains("&NSTR") || f.getName().contains("&STR") || f.getName().contains("&SUBSTR") )
        {
            f1.setClist(new SimpleBooleanProperty(true));
            return true;
        }

        return  false;
    }

    public boolean DetectFile_Rexx(File f, FileInfo f1) {
        if(f.getName().contains("DATATYPE") || f.getName().contains("EVAL") || f.getName().contains("LENGTH") ||
                f.getName().contains("NSTR") || f.getName().contains("STR") || f.getName().contains("SUBSTR") )
        {
            f1.setRexx(new SimpleBooleanProperty(true));
            return true;
        }

        return  false;
    }

}
