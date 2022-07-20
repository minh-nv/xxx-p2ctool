package vmo.p2c.p2ctool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.util.Callback;
import vmo.p2c.p2ctool.core.DetectControlFlow;
import vmo.p2c.p2ctool.core.FileInfo;
import vmo.p2c.p2ctool.core.ProcInfo;

import java.awt.*;
import java.io.IOException;

public class ControlFlowCobolController {
    private String pathToResources;

    @FXML
    private TreeTableView<ProcInfo> ttvControlFlow;

    @FXML
    private Label lbSummary;

    void initData(String pathToResources) throws IOException {
        this.pathToResources= pathToResources;

        TreeTableColumn<ProcInfo, String> fileName_ProcNameCol = new TreeTableColumn<ProcInfo, String>("File Name/Program Call");
        fileName_ProcNameCol.setCellValueFactory(cd -> cd.getValue().getValue().getProcName());
        ttvControlFlow.getColumns().add(fileName_ProcNameCol);

        TreeTableColumn<ProcInfo, String> pathCol = new TreeTableColumn<ProcInfo, String>("Path");
        pathCol.setCellValueFactory(cd -> cd.getValue().getValue().getProcName());
        ttvControlFlow.getColumns().add(pathCol);

//        //Button - Open File
//        TreeTableColumn<ProcInfo, Void> colButton = new TreeTableColumn<>("");
//        Callback<TreeTableColumn<ProcInfo, Void>, TreeTableCell<ProcInfo, Void>> cellFactory = new Callback<TreeTableColumn<ProcInfo, Void>, TreeTableCell<ProcInfo, Void>>() {
//            @Override
//            public TreeTableCell<ProcInfo, Void> call(final TreeTableColumn<ProcInfo, Void> param) {
//                final TreeTableCell<ProcInfo, Void> cell = new TreeTableCell<ProcInfo, Void>() {
//                    private final Button btn = new Button("Open File");
//                    {
//                        btn.setOnAction((ActionEvent event) -> {
//                            ProcInfo data = ttvControlFlow.getTreeItem(getIndex()).getValue();
//
//                            if(data!= null && data.getOriginalFile().exists() ) {
//                                try {
//                                    Desktop.getDesktop().open(data.getOriginalFile());
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                        } else {
//                            setGraphic(btn);
//                        }
//                    }
//                };
//                return cell;
//            }
//        };
//        colButton.setCellFactory(cellFactory);
//        ttvControlFlow.getColumns().add(colButton);


        DetectControlFlow detectControlFlow= new DetectControlFlow(this.pathToResources);
//        DetectControlFlow detectControlFlow= new DetectControlFlow("D:\\Developer\\Vmo-HKTelecom\\SourceCode\\COBOL-SRC");
        detectControlFlow.buildFlowUsingFileWalkAndVisitor();
        detectControlFlow.showResult();

        ProcInfo empBoss1 = new ProcInfo(null, "1", "4", "abc1");
        ProcInfo empBoss2 = new ProcInfo(null, "2", "5", "abc2");
        ProcInfo empBoss3 = new ProcInfo(null, "3", "6", "abc3");

        TreeItem<ProcInfo> item1 = new TreeItem<ProcInfo>(empBoss1);
        TreeItem<ProcInfo> item2 = new TreeItem<ProcInfo>(empBoss2);
        TreeItem<ProcInfo> item3= new TreeItem<ProcInfo>(empBoss3);

        item1.getChildren().addAll(item2, item3);
        item1.setExpanded(true);
        ttvControlFlow.setRoot(item1);
        ttvControlFlow.refresh();

        lbSummary.setText("1.");
    }

    @FXML
    public void initialize() {
    }
}
