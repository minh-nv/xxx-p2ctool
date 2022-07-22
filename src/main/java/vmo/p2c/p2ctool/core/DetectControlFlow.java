package vmo.p2c.p2ctool.core;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class DetectControlFlow {
    String rootPath;
    private Map<String, Integer> fileCallMap = new HashMap<>();
    private HashMap<String, Set<String>> flowNodeMap = new HashMap<>();

    public DetectControlFlow(String rootPath) {
        this.rootPath= rootPath;
    }

    public void showResult() {
        System.out.println("---------HOW MANY CALL?--------------");
        for (Map.Entry<String, Integer> fileCallEntry : fileCallMap.entrySet()) {
            if (!fileCallEntry.getValue().equals(0)) {
                System.out.printf("%s has %s call\n", fileCallEntry.getKey(), fileCallEntry.getValue());
            }
        }
        System.out.println("-----------------------");
    }

    public void buildFlowUsingFileWalkAndVisitor() throws IOException {
        Files.walkFileTree(Paths.get(rootPath), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                String fileName = path.getFileName().toString();
                fileCallMap.put(fileName, 0);
                flowNodeMap.put(fileName, new HashSet<>());

                System.out.println("fileName: " + fileName);
                try (Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1)) {
                    lines.forEach(line -> {
                        if ((StringUtils.containsIgnoreCase(line, "WS-PROGRAM")
                                || StringUtils.containsIgnoreCase(line, "WS-PROGRAM-NAME"))
                                && StringUtils.containsIgnoreCase(line, "MOVE")
                                && StringUtils.containsIgnoreCase(line, "TO")
                                && lineIsNotComment(line)) {
                            String subFile = StringUtils.substringBetween(line, "'");
                            if (!StringUtils.equalsIgnoreCase(fileName, subFile) && StringUtils.isNotBlank(subFile)) {
                                Set<String> nextSubroutineFile = flowNodeMap.get(fileName);
                                nextSubroutineFile.add(subFile);
                            }
//                            String[] words = line.trim().split("\\s+");
                            fileCallMap.put(fileName, fileCallMap.get(fileName) + 1);
                        }
                    });
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void findChildNode(DefaultMutableTreeNode parent, Set<String> childSet) {
        for (String branch : childSet) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(branch);
            parent.add(child);
            if (flowNodeMap.containsKey(branch)) {
                Set<String> nestChildSet = flowNodeMap.get(branch);
                findChildNode(child, nestChildSet);
            }
        }
    }

    public void buildJTree() {
        System.out.println("---------FLOW MAP--------------");
        JFrame frame = new JFrame();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("MSOS");
        for (Map.Entry<String, Set<String>> flowNodeEntry : flowNodeMap.entrySet()) {
            if (!fileCallMap.get(flowNodeEntry.getKey()).equals(0)) {
                DefaultMutableTreeNode parent = new DefaultMutableTreeNode(flowNodeEntry.getKey());
                root.add(parent);
                if (!flowNodeEntry.getValue().isEmpty()) {
                    System.out.printf("%s -> %s\n", flowNodeEntry.getKey(), flowNodeEntry.getValue());
                    findChildNode(parent, flowNodeEntry.getValue());
                }
            }
        }
        JTree jt = new JTree(root);
        for (int i = 0; i < jt.getRowCount(); i++) {
            jt.expandRow(i);
        }
        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        JScrollPane jScrollPane = new JScrollPane(jt, v, h);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(jScrollPane);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public boolean lineIsNotComment(String line) {
        return line.charAt(6) != '*';
    }

    public Map<String, Integer> getFileCallMap() {
        return fileCallMap;
    }

    public void setFileCallMap(Map<String, Integer> fileCallMap) {
        this.fileCallMap = fileCallMap;
    }

    public HashMap<String, Set<String>> getFlowNodeMap() {
        return flowNodeMap;
    }

    public void setFlowNodeMap(HashMap<String, Set<String>> flowNodeMap) {
        this.flowNodeMap = flowNodeMap;
    }
}
