package colorswaplevelfilegenerate;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author prabin
 */
public class GenerateLevel {

    ArrayList<Point> validPoints;
    int[][] piecesIdColorCode;
    Point startPoint;
    Grid grid;
    int rowSize, colSize;
    HashMap<Integer, String> levelFileColorSwap;
    String currentLevelFile;
    int level;

    public GenerateLevel() {

        grid = new Grid();
        grid.setGrids();
        validPoints = new ArrayList<Point>();
        levelFileColorSwap = new HashMap<Integer, String>();
        currentLevelFile = new String();
    }

    public void generateLevelFile() {


        for (int p = 0; p < grid.grids.length; p++) {


            /**
             * ********************************************
             */
            String currentGrid = grid.getGridSize(p);
            int mul = 1;
            int k = 0;

            rowSize = 0;
            colSize = 0;
            while (!Character.toString(currentGrid.charAt(k)).equalsIgnoreCase("x")) {

                rowSize = rowSize * mul + (currentGrid.charAt(k) - 48);
                mul *= 10;
                k++;
            }
            k++;

            mul = 1;
            while (k < currentGrid.length()) {
                colSize = colSize * mul + (currentGrid.charAt(k) - 48);
                mul *= 10;
                k++;
            }

            /**
             * *****************************************************************
             */
            /**
             * *****************************************************************
             */
            piecesIdColorCode = new int[rowSize][colSize];

            for (int l = 1; l <= 150; l++) {

                level = l;



                reset();

                createLevelFile(l);

                for (int i = 0; i < rowSize; i++) {
                    for (int j = 0; j < colSize; j++) {
                        System.out.print("\t" + piecesIdColorCode[i][j]);
                    }

                    System.out.println();
                }
                System.out.println(l);

            }


            writeToFile(currentGrid);
            levelFileColorSwap.clear();
//            break;

        }
    }

    public void reset() {

        levelFileColorSwap.put(level, new String());

        startPoint = new Point();
        validPoints.clear();
        currentLevelFile = "";
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                piecesIdColorCode[i][j] = -2;
            }
        }


    }

    public void createLevelFile(int level) {

        int row = rand(0, rowSize - 1);
        int col = rand(0, colSize - 1);

        System.out.println(row + "::" + col);
        startPoint = new Point(row, col);

        piecesIdColorCode[row][col] = rand(0, 4);


        System.out.println(piecesIdColorCode[row][col]);
        validPoints.add(new Point(row, col));
        fillGridWithColor();

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {

                if (i == startPoint.x && j == startPoint.y) {
                    if (j < colSize - 1) {
                        currentLevelFile += piecesIdColorCode[i][j] + "s,";
                    } else {
                        currentLevelFile += piecesIdColorCode[i][j] + "s;";
                    }
                } else{
                    if (j < colSize - 1) {
                    currentLevelFile += piecesIdColorCode[i][j] + ",";
                } else {
                    currentLevelFile += piecesIdColorCode[i][j] + ";";
                }
                }
                

            }
        }

        if (level == 1) {
            levelFileColorSwap.put(level, currentLevelFile);
        } else {

            boolean flag = true;

            for (int p = 1; p <= levelFileColorSwap.size(); p++) {

                if (p != level) {

                    if (levelFileColorSwap.get(p).equals(currentLevelFile)) {

                        reset();
                        createLevelFile(level);
                        return;
                    }
                }
            }

            levelFileColorSwap.put(level, currentLevelFile);
        }


    }

    public void fillGridWithColor() {

        boolean firstLoop = true;

        for (int p = 0; p < validPoints.size(); p++) {

            int row = validPoints.get(p).x;
            int col = validPoints.get(p).y;


            if (row - 1 >= 0) {

                if (!validPoints.contains(new Point(row - 1, col))) {

                    validPoints.add(new Point(row - 1, col));

                    piecesIdColorCode[row - 1][col] = rand(-1, 4);

                    if (firstLoop) {
                        while (piecesIdColorCode[startPoint.x][startPoint.y] == piecesIdColorCode[row - 1][col]) {
                            piecesIdColorCode[row - 1][col] = rand(-1, 4);
                        }
                    }
                }
            }

            if (col - 1 >= 0) {

                if (!validPoints.contains(new Point(row, col - 1))) {
                    validPoints.add(new Point(row, col - 1));

                    piecesIdColorCode[row][col - 1] = rand(-1, 4);

                    if (firstLoop) {
                        while (piecesIdColorCode[startPoint.x][startPoint.y] == piecesIdColorCode[row][col - 1]) {
                            piecesIdColorCode[row][col - 1] = rand(-1, 4);
                        }
                    }


                }
            }

            if (row + 1 < rowSize) {

                if (!validPoints.contains(new Point(row + 1, col))) {
                    validPoints.add(new Point(row + 1, col));

                    piecesIdColorCode[row + 1][col] = rand(-1, 4);

                    if (firstLoop) {
                        while (piecesIdColorCode[startPoint.x][startPoint.y] == piecesIdColorCode[row + 1][col]) {
                            piecesIdColorCode[row + 1][col] = rand(-1, 4);
                        }
                    }
                }
            }

            if (col + 1 < colSize) {

                if (!validPoints.contains(new Point(row, col + 1))) {
                    validPoints.add(new Point(row, col + 1));

                    piecesIdColorCode[row][col + 1] = rand(-1, 4);

                    if (firstLoop) {
                        while (piecesIdColorCode[startPoint.x][startPoint.y] == piecesIdColorCode[row][col + 1]) {
                            piecesIdColorCode[row][col + 1] = rand(-1, 4);
                        }

                        firstLoop = false;
                    }
                }
            }
        }

    }

    public void writeToFile(String grid) {

        PrintWriter writer;

        try {

            File fOut = new File("d:\\levelfile colorswap");
            if (!fOut.exists()) {
                fOut.mkdir();
            }

            writer = new PrintWriter(new File("d:\\levelfile colorswap\\" + grid + ".txt"));

            for (int p = 1; p <= levelFileColorSwap.size(); p++) {

                writer.println(levelFileColorSwap.get(p));
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int rand(int min, int max) {

        Random random = new Random();

        return (random.nextInt(max - min + 1) + min);
    }
}
