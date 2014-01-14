package colorswapgenbestmove;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author prabin
 */
public class Main {

    ArrayList<Integer> validColors;
    HashMap<Integer, Integer> blocks;
    HashMap<Integer, Integer> bestCoveredArea;
    HashMap<Integer, ArrayList<Point>> blockPoints;
    int selectedColor;
    int[][] gridElements;
    ArrayList<Point> edgePoints;
    ArrayList<Point> currentGrids;
    int currentColor;
    int grids;
    int pack;
    ArrayList<String> levelFiles;
    int currentLevel = 1;
    Point startPoint;
    boolean gameEnd;
    int moves;
    int level;

    public void initComponents() {

        validColors = new ArrayList<Integer>();
        blockPoints = new HashMap<Integer, ArrayList<Point>>();
        blocks = new LinkedHashMap<Integer, Integer>();
        bestCoveredArea = new HashMap<Integer, Integer>();
        edgePoints = new ArrayList<Point>();
        currentGrids = new ArrayList<Point>();
        levelFiles = new ArrayList<String>();

    }

    public void resetAll() {
        resetList();
        resetBlockMap();
        moves = 0;


    }

    public void resetList() {
        edgePoints.clear();
        currentGrids.clear();


    }

    public void resetBlockMap() {

        for (int p = 0; p < 5; p++) {

            blocks.put(p, 0);
            bestCoveredArea.put(p, 0);
            blockPoints.put(p, new ArrayList<Point>());

        }
    }

    public void readLevel() {

        for (pack = 1; pack <= 5; pack++) {

            levelFiles.clear();
            BufferedReader reader;

            try {

                reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/colorswap/pack (" + pack + ").txt")));

                String str;
                while ((str = reader.readLine()) != null) {
                    levelFiles.add(str);

                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            for (level = 0; level < levelFiles.size(); level++) {
                String currentLevelFile = levelFiles.get(level);

                String[] tmpFile = currentLevelFile.split(";");

                grids = tmpFile.length;

                gridElements = new int[grids][grids];

                for (int p = 0; p < tmpFile.length; p++) {

                    String[] lf = tmpFile[p].split(",");

                    for (int r = 0; r < lf.length; r++) {


                        if (lf[r].contains("s")) {
                            startPoint = new Point(p, r);

                            edgePoints.add(startPoint);
                            currentGrids.add(startPoint);

                            currentColor = gridElements[p][r] = lf[r].charAt(0) - 48;

                        } else {

                            gridElements[p][r] = Integer.parseInt(lf[r]);
                        }



                    }
                }

                moves = 0;
                start();
                resetList();
            }

//            System.out.println(levelFiles.size());
            saveToFile();
            resetAll();

        }
    }

    public void start() {

        setValidColors();



        if (validColors.size() == 1) {

            selectedColor = validColors.get(0);
            computeGameProgress();

        } else {// greater than one

            resetBlockMap();
            countBlocks();
            selectTheBestBlockAndRun();

        }
    }

    public void countBlocks() {

        ArrayList<Point> visitedPoints = new ArrayList<Point>();

        for (int p = 0; p < edgePoints.size(); p++) {

            int i = edgePoints.get(p).x;
            int j = edgePoints.get(p).y;

            if (i + 1 < grids) {
                if (!visitedPoints.contains(new Point(i + 1, j))
                        && currentColor != gridElements[i + 1][j]
                        && gridElements[i + 1][j] != -1) {

                    visitedPoints.add(new Point(i + 1, j));

                    blockPoints.get(gridElements[i + 1][j]).add(
                            new Point(i + 1, j));

                    bestCoveredArea
                            .put(gridElements[i + 1][j], bestCoveredArea
                            .get(gridElements[i + 1][j]) + 1);

                    blocks.put(gridElements[i + 1][j],
                            blocks.get(gridElements[i + 1][j]) + 1);
                    parseBlocks(i + 1, j, visitedPoints,
                            gridElements[i + 1][j]);

                }
            }

            if (j + 1 < grids) {

                if (!visitedPoints.contains(new Point(i, j + 1))
                        && currentColor != gridElements[i][j + 1]
                        && gridElements[i][j + 1] != -1) {

                    visitedPoints.add(new Point(i, j + 1));

                    blockPoints.get(gridElements[i][j + 1]).add(
                            new Point(i, j + 1));

                    bestCoveredArea
                            .put(gridElements[i][j + 1], bestCoveredArea
                            .get(gridElements[i][j + 1]) + 1);

                    blocks.put(gridElements[i][j + 1],
                            blocks.get(gridElements[i][j + 1]) + 1);
                    parseBlocks(i, j + 1, visitedPoints,
                            gridElements[i][j + 1]);

                }
            }

            if (i - 1 >= 0) {

                if (!visitedPoints.contains(new Point(i - 1, j))
                        && currentColor != gridElements[i - 1][j]
                        && gridElements[i - 1][j] != -1) {

                    visitedPoints.add(new Point(i - 1, j));

                    blockPoints.get(gridElements[i - 1][j]).add(
                            new Point(i - 1, j));

                    bestCoveredArea.put(gridElements[i - 1][j], bestCoveredArea.get(gridElements[i - 1][j]) + 1);
                    blocks.put(gridElements[i - 1][j],
                            blocks.get(gridElements[i - 1][j]) + 1);
                    parseBlocks(i - 1, j, visitedPoints,
                            gridElements[i - 1][j]);

                }

            }

            if (j - 1 >= 0) {

                if (!visitedPoints.contains(new Point(i, j - 1)) && currentColor != gridElements[i][j - 1] && gridElements[i][j - 1] != -1) {

                    visitedPoints.add(new Point(i, j - 1));

                    blockPoints.get(gridElements[i][j - 1]).add(new Point(i, j - 1));

                    bestCoveredArea.put(gridElements[i][j - 1], bestCoveredArea.get(gridElements[i][j - 1]) + 1);
                    blocks.put(gridElements[i][j - 1], blocks.get(gridElements[i][j - 1]) + 1);
                    parseBlocks(i, j - 1, visitedPoints, gridElements[i][j - 1]);
                }

            }

        }


    }

    public boolean hasColouredGridNextToIt() {

        boolean flag = false;

        for (int p = 0; p < currentGrids.size(); p++) {

            int i = currentGrids.get(p).x;
            int j = currentGrids.get(p).y;

            if (i - 1 >= 0) {

                if (!currentGrids.contains(new Point(i - 1, j))
                        && selectedColor == gridElements[i - 1][j]) {

                    currentGrids.add(new Point(i - 1, j));
                    flag = true;
                }
            }

            if (i + 1 < grids) {
                if (!currentGrids.contains(new Point(i + 1, j))
                        && selectedColor == gridElements[i + 1][j]) {

                    currentGrids.add(new Point(i + 1, j));
                    flag = true;
                }
            }

            if (j - 1 >= 0) {
                if (!currentGrids.contains(new Point(i, j - 1))
                        && selectedColor == gridElements[i][j - 1]) {

                    currentGrids.add(new Point(i, j - 1));
                    flag = true;
                }

            }

            if (j + 1 < grids) {

                if (!currentGrids.contains(new Point(i, j + 1))
                        && selectedColor == gridElements[i][j + 1]) {

                    currentGrids.add(new Point(i, j + 1));

                    flag = true;

                }

            }

        }

        return flag;

    }

    public void parseBlocks(int i, int j, ArrayList<Point> visitedPoints, int currentBlockColor) {

        if (i + 1 < grids) {

            if (!visitedPoints.contains(new Point(i + 1, j)) && currentBlockColor == gridElements[i + 1][j] && gridElements[i + 1][j] != -1) {

                visitedPoints.add(new Point(i + 1, j));
                blockPoints.get(gridElements[i + 1][j]).add(new Point(i + 1, j));
                bestCoveredArea.put(gridElements[i + 1][j], bestCoveredArea.get(gridElements[i + 1][j]) + 1);
                parseBlocks(i + 1, j, visitedPoints, currentBlockColor);

            }
        }

        if (j + 1 < grids) {

            if (!visitedPoints.contains(new Point(i, j + 1)) && currentBlockColor == gridElements[i][j + 1] && gridElements[i][j + 1] != -1) {

                visitedPoints.add(new Point(i, j + 1));

                blockPoints.get(gridElements[i][j + 1]).add(new Point(i, j + 1));
                bestCoveredArea.put(gridElements[i][j + 1], bestCoveredArea.get(gridElements[i][j + 1]) + 1);
                parseBlocks(i, j + 1, visitedPoints, currentBlockColor);

            }
        }

        if (i - 1 >= 0) {

            if (!visitedPoints.contains(new Point(i - 1, j)) && currentBlockColor == gridElements[i - 1][j] && gridElements[i - 1][j] != -1) {

                visitedPoints.add(new Point(i - 1, j));
                blockPoints.get(gridElements[i - 1][j]).add(new Point(i - 1, j));
                bestCoveredArea.put(gridElements[i - 1][j], bestCoveredArea.get(gridElements[i - 1][j]) + 1);
                parseBlocks(i - 1, j, visitedPoints, currentBlockColor);

            }

        }

        if (j - 1 >= 0) {

            if (!visitedPoints.contains(new Point(i, j - 1)) && currentBlockColor == gridElements[i][j - 1] && gridElements[i][j - 1] != -1) {

                visitedPoints.add(new Point(i, j - 1));

                blockPoints.get(gridElements[i][j - 1]).add(new Point(i, j - 1));
                bestCoveredArea.put(gridElements[i][j - 1], bestCoveredArea.get(gridElements[i][j - 1]) + 1);
                parseBlocks(i, j - 1, visitedPoints, currentBlockColor);
            }

        }

    }

    public void selectTheBestBlockAndRun() {
        int rank = 0; // represents the color number of block that are currently
        // in touch with selected block
        int color = -1;
        int bestCoverdArea = 0;
        ArrayList<Integer> sameRank = new ArrayList<Integer>();

        for (int p = 0; p < blocks.size(); p++) {
            // p represents block color

            if (isBlockSingle(p) && blocks.get(p) != 0) {
                selectedColor = p;
                computeGameProgress();

                return;
            }

            if (rank < blocks.get(p)) {
                rank = blocks.get(p);
                color = p;
                // currentSelectedBlock = p;
                sameRank.clear();
            }

            if (rank == 1) {

                if (bestCoverdArea < this.bestCoveredArea.get(p)) {
                    bestCoverdArea = this.bestCoveredArea.get(p);
                    color = p;

                }

            }

            if (rank == blocks.get(p) && rank > 1) {

                sameRank.add(p);
            }

        }

        if (sameRank.size() > 1) {

            bestCoverdArea = 0;

            for (int p = 0; p < sameRank.size(); p++) {

                if (bestCoverdArea < this.bestCoveredArea.get(p)) {
                    bestCoverdArea = this.bestCoveredArea.get(p);
                    color = p;
                }
            }
        }

        selectedColor = color;
        computeGameProgress();
    }

    public boolean isBlockSingle(int colorIndex) {

        for (int i = 0; i < grids; i++) {

            for (int j = 0; j < grids; j++) {

                if (gridElements[i][j] == colorIndex) {

                    if (!blockPoints.get(colorIndex).contains(new Point(i, j))) {

                        return false;

                    }
                }
            }
        }

        return true;

    }

    public void computeGameProgress() {

        for (int r = 0; r < edgePoints.size(); r++) {

            if (hasColouredGridNextToIt()) {

                moves++;

                currentColor = selectedColor;

                computeNewEdgePointsAndChangeColor();

                if (!gameEnd()) {
                    start();
                }

                break;

            }

        }

    }

    public void computeNewEdgePointsAndChangeColor() {

        edgePoints.clear();

        for (int p = 0; p < currentGrids.size(); p++) {

            int i = currentGrids.get(p).x;
            int j = currentGrids.get(p).y;

            // color change
            gridElements[i][j] = selectedColor;

            // add edge points
            if (i - 1 >= 0) {

                if (gridElements[i - 1][j] != currentColor) {

                    if (!edgePoints.contains(new Point(i, j))) {
                        edgePoints.add(new Point(i, j));
                    }
                }
            }

            if (i + 1 < grids) {
                if (gridElements[i + 1][j] != currentColor) {

                    if (!edgePoints.contains(new Point(i, j))) {
                        edgePoints.add(new Point(i, j));
                    }

                }
            }

            if (j - 1 >= 0) {
                if (gridElements[i][j - 1] != currentColor) {

                    if (!edgePoints.contains(new Point(i, j))) {
                        edgePoints.add(new Point(i, j));
                    }
                }

            }

            if (j + 1 < grids) {

                if (gridElements[i][j + 1] != currentColor) {

                    if (!edgePoints.contains(new Point(i, j))) {
                        edgePoints.add(new Point(i, j));
                    }
                }

            }

        }
    }

    public void setValidColors() {

        validColors.clear();

        for (int p = 0; p < edgePoints.size(); p++) {

            int i = edgePoints.get(p).x;
            int j = edgePoints.get(p).y;

            if (i + 1 < grids) {

                if (gridElements[i + 1][j] != currentColor
                        && !validColors.contains(gridElements[i + 1][j])
                        && gridElements[i + 1][j] != -1) {

                    validColors.add(gridElements[i + 1][j]);

                }
            }

            if (j + 1 < grids) {

                if (gridElements[i][j + 1] != currentColor
                        && !validColors.contains(gridElements[i][j + 1])
                        && gridElements[i][j + 1] != -1) {

                    validColors.add(gridElements[i][j + 1]);
                }
            }

            if (i - 1 >= 0) {
                if (gridElements[i - 1][j] != currentColor
                        && !validColors.contains(gridElements[i - 1][j])
                        && gridElements[i - 1][j] != -1) {

                    validColors.add(gridElements[i - 1][j]);

                }
            }

            if (j - 1 >= 0) {

                if (gridElements[i][j - 1] != currentColor
                        && !validColors.contains(gridElements[i][j - 1])
                        && gridElements[i][j - 1] != -1) {

                    validColors.add(gridElements[i][j - 1]);
                }
            }
        }





    }

    public void saveToFile() {



        PrintWriter out;

        try {

            File file = new File("d:/colorswap");

            if (!file.exists()) {
                file.mkdir();
            }

            out = new PrintWriter(file+"/pack (" + pack + ").txt");

            System.out.println(levelFiles.size());

            int p = 0;

            while (p < levelFiles.size()) {
                out.println(levelFiles.get(p));

                p++;
            }


            out.close();
        } catch (IOException e) {
        }


        //   System.out.println(moves);
    }

    public void rewriteLevelFile() {

        levelFiles.set(level, levelFiles.get(level) + moves);
//        System.out.println(levelFiles.get(levelFiles.size() - 1));
    }

    public boolean gameEnd() {

        for (int i = 0; i < grids; i++) {
            for (int j = 0; j < grids; j++) {
                if (gridElements[i][j] != currentColor) {

                    if (gridElements[i][j] != -1) {


                        return false;
                    }

                }
            }
        }

        rewriteLevelFile();
        return true;


    }

    public static void main(String[] args) {

        Main obj = new Main();

        obj.initComponents();
        obj.readLevel();


    }
}
