module vmo.p2c.p2ctool {
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires java.datatransfer;
    requires java.desktop;
    requires JavaFXSmartGraph;

//    exports com.brunomnsilva.smartgraph.graph;

    opens vmo.p2c.p2ctool to javafx.fxml;
    exports vmo.p2c.p2ctool;
}