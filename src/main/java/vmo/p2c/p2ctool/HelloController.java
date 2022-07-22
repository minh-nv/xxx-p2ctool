package vmo.p2c.p2ctool;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.dlsc.formsfx.model.event.FormEvent;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import vmo.p2c.p2ctool.core.DetectControlFlow;
import vmo.p2c.p2ctool.core.DetectFileType;
import vmo.p2c.p2ctool.core.FileInfo;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class HelloController {
    private final String PathToResources= "D:\\Developer\\Vmo-HKTelecom\\Tools\\P2CTool\\P2CTool\\src\\main\\resources";
    private final String Space= "    ";

    @FXML
    private Label lbSummary;

    @FXML
    private TextField txtFolderPath;

    @FXML
    private TableView<FileInfo> tvFileStructure;

    @FXML
    protected void btnControlFlowCobol_onActionClick() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("control-flow-cobol.fxml"));
//        Parent root1 = (Parent) fxmlLoader.load();
//        ControlFlowCobolController controller = fxmlLoader.getController();
//        controller.initData(txtFolderPath.getText());
//
//        Stage stage = new Stage();
//        stage.setTitle("P2C Tool - Control Flow Cobol");
//
//        stage.setScene(new Scene(root1, 920, 740));
//        stage.show();

        Graph<String, String> g = new GraphEdgeList<>();
        DetectControlFlow detectControlFlow= new DetectControlFlow(txtFolderPath.getText());
        detectControlFlow.buildFlowUsingFileWalkAndVisitor();
        detectControlFlow.showResult();

        //buildChart
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        for (Map.Entry<String, Set<String>> flowNodeEntry : detectControlFlow.getFlowNodeMap().entrySet()) {
            if (!detectControlFlow.getFileCallMap().get(flowNodeEntry.getKey()).equals(0)) {
                if (!flowNodeEntry.getValue().isEmpty()) {
                    g.insertVertex(flowNodeEntry.getKey());
                    g.insertVertex(flowNodeEntry.getValue().toString());
                    g.insertEdge(flowNodeEntry.getKey(), flowNodeEntry.getValue().toString(), flowNodeEntry.getKey());

                    System.out.printf("%s -> %s\n", flowNodeEntry.getKey(), flowNodeEntry.getValue());
                }
            }
        }

        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);

        for (Map.Entry<String, Set<String>> flowNodeEntry : detectControlFlow.getFlowNodeMap().entrySet()) {
            if (!detectControlFlow.getFileCallMap().get(flowNodeEntry.getKey()).equals(0)) {
                if (!flowNodeEntry.getValue().isEmpty()) {
                    graphView.getStylableVertex(flowNodeEntry.getKey()).setStyleClass("myVertex");
                }
            }
        }

        Scene scene = new Scene(graphView, 1024, 768);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFXGraph Visualization");
        stage.setScene(scene);
        stage.show();

        graphView.init();
    }

    @FXML
    protected void btnExportResult_onActionClick() throws IOException {

    }

    @FXML
    protected void btnScanningFolder_onActionClick() throws IOException {
        File f = new File(txtFolderPath.getText());
        if (!f.exists()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("");
            alert.setContentText("folder doesn't exist");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                }
            });
        }
        else if (!f.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("");
            alert.setContentText("folder doesn't exist");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                }
            });
        }
        else {
            ScanFolder(txtFolderPath.getText());
        }
    }

    @FXML
    protected void btnLoadFolder_onActionClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory == null) {
            //No Directory selected
        } else {
            txtFolderPath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void WalkFolder(int level, ObservableList<FileInfo> csvData,
                           String rootPath, String path ) throws IOException {
        File root = new File( path );
        File[] list = root.listFiles();
        long seq= 0;

        level+=1;

        if (list == null) return;
        Arrays.sort(list);
        for ( File f : list ) {
            seq++;

            if ( f.isDirectory() ) {
                String totalSpace= "";
                for(int i=0; i< level; i++ )
                    totalSpace+= Space;

                FileInfo f1 = new FileInfo(f, seq, new Image(new FileInputStream(PathToResources + "/assets/folder.png")),
                                        totalSpace + f.getAbsolutePath().substring(rootPath.length() + 1),
                                       "",
                                        "", "lastMofified", "",
                                       false, false,false, false,
                                        false, false,false,
                                        false, false,false,
                                        "metadata");

                csvData.add(f1);

                //System.out.println( "Dir: " + f.getAbsolutePath().substring(rootPath.length() + 1));
                WalkFolder(level, csvData, rootPath, f.getAbsolutePath());
            }
            else {
                String totalSpace= "";
                for(int i=0; i< level; i++ )
                    totalSpace+= Space;

                FileInfo f1 = new FileInfo(f, seq, new Image(new FileInputStream(PathToResources + "/assets/file-1.png")),
                        totalSpace + f.getAbsolutePath().substring(path.length() + 1),
                        "",
                        String.format("%,d b", f.length()),
                        "lastMofified",
                        "",
                        false, false,false, false,
                        false, false,false,
                        false, false,false,
                        "metadata");

//                CheckLineOfCode(f, f1);
                (new DetectFileType()).DetectProgram_Fes(f, f1);
                (new DetectFileType()).DetectProgram_Buc(f, f1);
                (new DetectFileType()).DetectProgram_Stars(f, f1);
                (new DetectFileType()).DetectProgram_Msos(f, f1);
                (new DetectFileType()).DetectProgram_Lnas(f, f1);
                (new DetectFileType()).DetectProgram_Cpas(f, f1);

                (new DetectFileType()).DetectFile_Clist(f, f1);
                if(!f1.getClist().getValue())
                    (new DetectFileType()).DetectFile_Rexx(f, f1);

                csvData.add(f1);

                System.out.println( "File:" + f.getAbsolutePath().substring(rootPath.length() + 1));
            }
        }
    }

    private void CheckLineOfCode(File f, FileInfo f1) {
        String lineCount= "";
        try {
            try (Stream<String> stream = Files.lines(f.getAbsoluteFile().toPath(), StandardCharsets.UTF_8)) {
                lineCount= String.format("%,d", stream.count());
            }

        }
        catch (Exception ex)
        {
            lineCount= "ERR";
        }

        f1.setLineOfCode(new SimpleStringProperty(lineCount));
    }

    private void AddColumnInTableView(List<String> columns ) {
        if(tvFileStructure.getColumns().size()>0 )
            return;;

        tvFileStructure.getColumns().clear();

        //Image
        TableColumn<FileInfo, Image> column1 = new TableColumn<>("");
        column1.setCellValueFactory(new PropertyValueFactory<FileInfo, Image>("Icon"));
        column1.setCellFactory(col -> new TableCell<FileInfo, Image>() {
            private final ImageView imageView = new ImageView();
            {
                // set size of ImageView
                imageView.setFitHeight(13);
                imageView.setFitWidth(13);
                imageView.setPreserveRatio(true);

                // display ImageView in cell
                setGraphic(imageView);
            }

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                imageView.setImage(item);
            }
        });
        tvFileStructure.getColumns().add(column1);

        for (int ii = 0; ii < columns.size(); ii++) {
            int finalIdx = ii;

            if(finalIdx== FileInfo.idx_Seq)
            {
                TableColumn<FileInfo, String> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getSeq().asString());
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_File )
            {
                TableColumn<FileInfo, String> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getFile());
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Path )
            {
                TableColumn<FileInfo, String> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getPath());
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Size) {
                TableColumn<FileInfo, String> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getSize());
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_LoC) {
                TableColumn<FileInfo, String> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getLineOfCode());
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Cobol) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getCobol());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Jcl) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getJcl());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Rexx) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getRexx());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Clist) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getClist());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Msos) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getMsos());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Lnas) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getLnas());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Cpas) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getCpas());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Stars) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getStars());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Buc) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getBuc());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }

            if(finalIdx== FileInfo.idx_Fes) {
                TableColumn<FileInfo, Boolean> column = new TableColumn<>(columns.get(ii));

                column.setCellValueFactory(cd -> cd.getValue().getFes());
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                tvFileStructure.getColumns().add(column);
            }
        }

        //Button - Open File
        TableColumn<FileInfo, Void> colButton = new TableColumn<>("");
        Callback<TableColumn<FileInfo, Void>, TableCell<FileInfo, Void>> cellFactory = new Callback<TableColumn<FileInfo, Void>, TableCell<FileInfo, Void>>() {
            @Override
            public TableCell<FileInfo, Void> call(final TableColumn<FileInfo, Void> param) {
                final TableCell<FileInfo, Void> cell = new TableCell<FileInfo, Void>() {
                    private final Button btn = new Button("Open File");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            FileInfo data = getTableView().getItems().get(getIndex());

                            if(data.getOriginalFile().exists() ) {
                                try {
                                    Desktop.getDesktop().open(data.getOriginalFile());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colButton.setCellFactory(cellFactory);
        tvFileStructure.getColumns().add(colButton);
    }

    public void ScanFolder(String folderPath) throws IOException {
        List<String> columns = new ArrayList<String>();

        String items[] = null;
        ObservableList<FileInfo> csvData = FXCollections.observableArrayList();

        WalkFolder(0, csvData, folderPath, folderPath);

        String headers[] = new String[16];
        headers[FileInfo.idx_Seq]= "Seq";
        headers[FileInfo.idx_File]= "File";
        headers[FileInfo.idx_Path]= "Path";
        headers[FileInfo.idx_Size]= "Size";
        headers[FileInfo.idx_LoC]= "LoC";
        headers[FileInfo.idx_Cobol]= "Cobol";
        headers[FileInfo.idx_Jcl]= "Jcl";
        headers[FileInfo.idx_Rexx]= "Rexx";
        headers[FileInfo.idx_Clist]= "Clist";
        headers[FileInfo.idx_Msos]= "Msos";
        headers[FileInfo.idx_Lnas]= "Lnas";
        headers[FileInfo.idx_Cpas]= "Cpas";
        headers[FileInfo.idx_Stars]= "Stars";
        headers[FileInfo.idx_Buc]= "Buc";
        headers[FileInfo.idx_Fes]= "Fes";

        for (String w : headers) {
            columns.add(w);
        }

        AddColumnInTableView(columns);

        tvFileStructure.getItems().clear();
        tvFileStructure.setItems(csvData);

        if(csvData!= null )
            for(FileInfo fileInfo : csvData )
            {

            }

        lbSummary.setText("Summary: ");

        SetStyleColumn_TableView();

        tvFileStructure.refresh();
    }

    private void SetStyleColumn_TableView() {
        for(int i=0; i< tvFileStructure.getColumns().size(); i++ )
        {
            tvFileStructure.getColumns().get(i).setSortable(false);
            tvFileStructure.getColumns().get(i).setResizable(true);
        }

        //Icon
        tvFileStructure.getColumns().get(FileInfo.idx_Icon).setStyle( "-fx-alignment: CENTER;");
        tvFileStructure.getColumns().get(FileInfo.idx_Icon).setPrefWidth(20);
        tvFileStructure.getColumns().get(FileInfo.idx_Icon).setMinWidth(20);
        tvFileStructure.getColumns().get(FileInfo.idx_Icon).setMaxWidth(20);

        //Seq
        tvFileStructure.getColumns().get(FileInfo.idx_Seq).setStyle( "-fx-alignment: CENTER-RIGHT;");
        tvFileStructure.getColumns().get(FileInfo.idx_Seq).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Seq).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Seq).setMaxWidth(40);

        //File
//        tvFileStructure.getColumns().get(FileInfo.idx_File).setPrefWidth(450);
//        tvFileStructure.getColumns().get(FileInfo.idx_File).setMinWidth(450);
//        tvFileStructure.getColumns().get(FileInfo.idx_File).setMaxWidth(450);

//        Path
        tvFileStructure.getColumns().get(FileInfo.idx_Path).setPrefWidth(0);
        tvFileStructure.getColumns().get(FileInfo.idx_Path).setMinWidth(0);
        tvFileStructure.getColumns().get(FileInfo.idx_Path).setMaxWidth(0);

        //Size
        tvFileStructure.getColumns().get(FileInfo.idx_Size).setStyle( "-fx-alignment: CENTER-RIGHT;");
        tvFileStructure.getColumns().get(FileInfo.idx_Size).setPrefWidth(60);
        tvFileStructure.getColumns().get(FileInfo.idx_Size).setMinWidth(60);
        tvFileStructure.getColumns().get(FileInfo.idx_Size).setMaxWidth(60);

        //LoC
        tvFileStructure.getColumns().get(FileInfo.idx_LoC).setStyle( "-fx-alignment: CENTER-RIGHT;");
        tvFileStructure.getColumns().get(FileInfo.idx_LoC).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_LoC).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_LoC).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Cobol).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Cobol).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Cobol).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Jcl).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Jcl).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Jcl).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Rexx).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Rexx).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Rexx).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Clist).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Clist).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Clist).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Msos).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Msos).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Msos).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Lnas).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Lnas).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Lnas).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Cpas).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Cpas).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Cpas).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Stars).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Stars).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Stars).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Buc).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Buc).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Buc).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_Fes).setPrefWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Fes).setMinWidth(40);
        tvFileStructure.getColumns().get(FileInfo.idx_Fes).setMaxWidth(40);

        tvFileStructure.getColumns().get(FileInfo.idx_ActionOpenFile).setPrefWidth(80);
        tvFileStructure.getColumns().get(FileInfo.idx_ActionOpenFile).setMinWidth(80);
        tvFileStructure.getColumns().get(FileInfo.idx_ActionOpenFile).setMaxWidth(80);
    }

}