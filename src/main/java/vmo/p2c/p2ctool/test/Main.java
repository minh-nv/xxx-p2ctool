package vmo.p2c.p2ctool.test;

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

public class Main {
    private static final String ROOT_PATH = "D:\\Developer\\Vmo-HKTelecom\\SourceCode\\COBOL-SRC";
    private static final Map<String, Integer> fileCallMap = new HashMap<>();
    private static final HashMap<String, Set<String>> flowNodeMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        buildFlowUsingFileWalkAndVisitor();
        buildJTree();
        showResult();
    }

    private static void showResult() {
        System.out.println("---------HOW MANY CALL?--------------");
        for (Map.Entry<String, Integer> fileCallEntry : fileCallMap.entrySet()) {
            if (!fileCallEntry.getValue().equals(0)) {
                System.out.printf("%s has %s call\n", fileCallEntry.getKey(), fileCallEntry.getValue());
            }
        }
        System.out.println("-----------------------");

        for (Map.Entry<String, Set<String>> flowNodeEntry : flowNodeMap.entrySet()) {
            if (!fileCallMap.get(flowNodeEntry.getKey()).equals(0)) {
                DefaultMutableTreeNode parent = new DefaultMutableTreeNode(flowNodeEntry.getKey());
                if (!flowNodeEntry.getValue().isEmpty()) {
                    System.out.printf("%s -> %s\n", flowNodeEntry.getKey(), flowNodeEntry.getValue());
                    findChildNode(parent, flowNodeEntry.getValue());
                }
            }
        }
    }

    private static void buildFlowUsingFileWalkAndVisitor() throws IOException {
        Files.walkFileTree(Paths.get(ROOT_PATH), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                String fileName = path.getFileName().toString();
                fileCallMap.put(fileName, 0);
                flowNodeMap.put(fileName, new HashSet<>());

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

    private static void findChildNode(DefaultMutableTreeNode parent, Set<String> childSet) {
        for (String branch : childSet) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(branch);
            parent.add(child);
            if (flowNodeMap.containsKey(branch)) {
                Set<String> nestChildSet = flowNodeMap.get(branch);
                findChildNode(child, nestChildSet);
            }
        }
    }

    private static void buildJTree() {
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

    private static boolean lineIsNotComment(String line) {
        return line.charAt(6) != '*';
    }
}