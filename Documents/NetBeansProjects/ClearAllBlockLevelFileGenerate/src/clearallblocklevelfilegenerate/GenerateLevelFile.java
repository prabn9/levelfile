package clearallblocklevelfilegenerate;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author prabin
 */
public final class GenerateLevelFile {

    int row, col;
    HashMap<Integer, String> levelFile;
    ArrayList<Point> pointsVisited;
    String currentLevelFile;
    ArrayList<Integer> randNos;
    String[][] arrStrLevel;

    public GenerateLevelFile() {

        this.initComponents();
    }

    public void initComponents() {

        levelFile = new HashMap<Integer, String>();
        currentLevelFile = new String();
        pointsVisited = new ArrayList<Point>();
        randNos = new ArrayList<Integer>();


        for (int i = 0; i < 8; i++) {
            randNos.add(i);
        }


    }

    public void reset() {


        currentLevelFile = "";
        pointsVisited.clear();


    }

    public void writeToFile(int pack) {
        BufferedWriter writer;
        try {

            writer = new BufferedWriter(new FileWriter("d:\\clearallblock levelfile\\pack" + pack + ".txt", true));

            System.out.println(currentLevelFile);
            writer.append(currentLevelFile + "\n");

            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void createStringFile() {

        currentLevelFile = "";
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                if (j < col - 1) {
                    currentLevelFile += arrStrLevel[i][j] + ",";
                } else {
                    currentLevelFile += arrStrLevel[i][j] + ";";
                }

            }
        }


    }

    public void generate() {

        for (int pack = 8; pack <= 9; pack++) {

            levelFile.clear();
            String grid = Grids.getGrids(pack - 1);

            row = grid.charAt(0) - 48;
            col = grid.charAt(2) - 48;

            arrStrLevel = new String[row][col];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    arrStrLevel[i][j] = new String();
                }
            }


            for (int level = 0; level < 150; level++) {

                reset();
                createLevelFile();
                writeToString();

                createStringFile();

                if (inspectMatch(level)) {
                    level--;
                } else {

                    System.out.println(pack + "::" + level + "::" + pointsVisited.toString());
                    System.out.println(row + "::" + col);

                    setHints();

                    writeToFile(pack);
                    System.out.println("***********************************************");
                }


            }

            System.gc();

        
        }
    }

    public void writeToString() {

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                arrStrLevel[i][j] = setFakePath("00000000", i, j);

            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                rewritePaths(i, j);

            }
        }

        for (int p = 0; p < pointsVisited.size() - 1; p++) {

            setPaths(pointsVisited.get(p).x, pointsVisited.get(p).y, pointsVisited.get(p + 1).x, pointsVisited.get(p + 1).y);

        }
    }

    public void setHints() {
        
        for (int p = 0; p < pointsVisited.size(); p++) {
            
            if(p < pointsVisited.size() - 1){
                currentLevelFile+=pointsVisited.get(p).x+""+pointsVisited.get(p).y+"|";
            } else {
                currentLevelFile+=pointsVisited.get(p).x+""+pointsVisited.get(p).y;
            }
            
            
        }
    }

    public boolean inspectMatch(int level) {

        if (level == 0) {

            levelFile.put(level, currentLevelFile);
        } else {

            for (Map.Entry<Integer, String> entry : levelFile.entrySet()) {
                Integer lvl = entry.getKey();
                String strLvl = entry.getValue();


                if (lvl != level) {
                    if (currentLevelFile.equalsIgnoreCase(strLvl)) {

                        System.err.println("---------------------Match Found---" + lvl + "--" + level + "------------");
                        System.err.println("str-----"+strLvl);
                        System.err.println("clf-----"+currentLevelFile);
                        System.err.println("---------------------Match Found---" + lvl + "--" + level + "------------");
                        return true;
                    }
                }

            }
        }

        levelFile.put(level, currentLevelFile);
        return false;
    }

    public String setCharacter(String copy, int index) {

        StringBuilder cc = new StringBuilder(copy);

        cc.setCharAt(index, '1');

        return cc.toString();
    }

    public void rewritePaths(int i, int j) {

        if (arrStrLevel[i][j].charAt(0) == '1') {
            arrStrLevel[i][j - 1] = setCharacter(arrStrLevel[i][j - 1], 4);
        }

        if (arrStrLevel[i][j].charAt(1) == '1') {
            arrStrLevel[i - 1][j - 1] = setCharacter(arrStrLevel[i - 1][j - 1], 5);

        }

        if (arrStrLevel[i][j].charAt(2) == '1') {
            arrStrLevel[i - 1][j] = setCharacter(arrStrLevel[i - 1][j], 6);
        }

        if (arrStrLevel[i][j].charAt(3) == '1') {
            arrStrLevel[i - 1][j + 1] = setCharacter(arrStrLevel[i - 1][j + 1], 7);
        }

        if (arrStrLevel[i][j].charAt(4) == '1') {
            arrStrLevel[i][j + 1] = setCharacter(arrStrLevel[i][j + 1], 0);
        }

        if (arrStrLevel[i][j].charAt(5) == '1') {
            arrStrLevel[i + 1][j + 1] = setCharacter(arrStrLevel[i + 1][j + 1], 1);
        }

        if (arrStrLevel[i][j].charAt(6) == '1') {
            arrStrLevel[i + 1][j] = setCharacter(arrStrLevel[i + 1][j], 2);
        }

        if (arrStrLevel[i][j].charAt(7) == '1') {
            arrStrLevel[i + 1][j - 1] = setCharacter(arrStrLevel[i + 1][j - 1], 3);
        }
    }

    public void createLevelFile() {

        int lastRow = rand(0, row - 1);
        int lastCol = rand(0, col - 1);

        pointsVisited.add(new Point(lastRow, lastCol));

        for (int p = 0; p < pointsVisited.size(); p++) {

            Collections.shuffle(randNos);


            generateRandomPath(lastRow, lastCol);

            lastRow = pointsVisited.get(p + 1).x;
            lastCol = pointsVisited.get(p + 1).y;



            if (pointsVisited.size() == row * col) {

                return;
            }

        }
    }

    public void generateRandomPath(int i, int j) {

        for (int p = 0; p < randNos.size(); p++) {

            int rns = randNos.get(p);

            switch (rns) {

                case 0:

                    if (i + 1 < row && !pointsVisited.contains(new Point(i + 1, j))) {

                        pointsVisited.add(new Point(i + 1, j));
                        return;
                    }
                    break;
                case 1:

                    if (j + 1 < col && !pointsVisited.contains(new Point(i, j + 1))) {


                        pointsVisited.add(new Point(i, j + 1));
                        return;
                    }
                    break;

                case 2:

                    if (i - 1 >= 0 && !pointsVisited.contains(new Point(i - 1, j))) {


                        pointsVisited.add(new Point(i - 1, j));
                        return;
                    }
                    break;
                case 3:

                    if (j - 1 >= 0 && !pointsVisited.contains(new Point(i, j - 1))) {


                        pointsVisited.add(new Point(i, j - 1));
                        return;
                    }
                    break;

                case 4:

                    if ((i + 1 < row && j + 1 < col) && !pointsVisited.contains(new Point(i + 1, j + 1))) {


                        pointsVisited.add(new Point(i + 1, j + 1));
                        return;
                    }
                    break;
                case 5:

                    if ((i + 1 < row && j - 1 >= 0) && !pointsVisited.contains(new Point(i + 1, j - 1))) {


                        pointsVisited.add(new Point(i + 1, j - 1));
                        return;
                    }
                    break;

                case 6:

                    if ((i - 1 >= 0 && j + 1 < col) && !pointsVisited.contains(new Point(i - 1, j + 1))) {


                        pointsVisited.add(new Point(i - 1, j + 1));
                        return;
                    }
                    break;
                case 7:

                    if ((i - 1 >= 0 && j - 1 >= 0) && !pointsVisited.contains(new Point(i - 1, j - 1))) {


                        pointsVisited.add(new Point(i - 1, j - 1));
                        return;
                    }
                    break;

                default:

                    break;
            }

        }



        reset();
        createLevelFile();
    }

    public void setPaths(int currentRow, int currentCol, int nextRow, int nextCol) {

        int dx = currentRow - nextRow;
        int dy = currentCol - nextCol;

        //left
        if (dx == 0 & dy == 1) {

            if (arrStrLevel[currentRow][currentCol].equals("null")) {
                arrStrLevel[currentRow][currentCol] = getPath(0);
            } else {


                arrStrLevel[currentRow][currentCol] = remakePath(arrStrLevel[currentRow][currentCol], getPath(0));

            }

            if (arrStrLevel[nextRow][nextCol].equals("null")) {
                arrStrLevel[nextRow][nextCol] = getPath(4);
            } else {
                arrStrLevel[nextRow][nextCol] = remakePath(arrStrLevel[nextRow][nextCol], getPath(4));
            }

        }

        //left-top
        if (dx == 1 & dy == 1) {

            if (arrStrLevel[currentRow][currentCol].equals("null")) {
                arrStrLevel[currentRow][currentCol] = getPath(1);
            } else {

                arrStrLevel[currentRow][currentCol] = remakePath(arrStrLevel[currentRow][currentCol], getPath(1));
            }

            if (arrStrLevel[nextRow][nextCol].equals("null")) {
                arrStrLevel[nextRow][nextCol] = getPath(5);
            } else {
                arrStrLevel[nextRow][nextCol] = remakePath(arrStrLevel[nextRow][nextCol], getPath(5));
            }
        }

        //top
        if (dx == 1 & dy == 0) {


            if (arrStrLevel[currentRow][currentCol].equals("null")) {
                arrStrLevel[currentRow][currentCol] = getPath(2);
            } else {

                arrStrLevel[currentRow][currentCol] = remakePath(arrStrLevel[currentRow][currentCol], getPath(2));
            }

            if (arrStrLevel[nextRow][nextCol].equals("null")) {
                arrStrLevel[nextRow][nextCol] = getPath(6);
            } else {
                arrStrLevel[nextRow][nextCol] = remakePath(arrStrLevel[nextRow][nextCol], getPath(6));
            }
        }

        //right-top
        if (dx == 1 & dy == -1) {


            if (arrStrLevel[currentRow][currentCol].equals("null")) {
                arrStrLevel[currentRow][currentCol] = getPath(3);
            } else {

                arrStrLevel[currentRow][currentCol] = remakePath(arrStrLevel[currentRow][currentCol], getPath(3));
            }

            if (arrStrLevel[nextRow][nextCol].equals("null")) {
                arrStrLevel[nextRow][nextCol] = getPath(7);
            } else {
                arrStrLevel[nextRow][nextCol] = remakePath(arrStrLevel[nextRow][nextCol], getPath(7));
            }
        }

        //right
        if (dx == 0 & dy == -1) {


            if (arrStrLevel[currentRow][currentCol].equals("null")) {
                arrStrLevel[currentRow][currentCol] = getPath(4);
            } else {

                arrStrLevel[currentRow][currentCol] = remakePath(arrStrLevel[currentRow][currentCol], getPath(4));
            }

            if (arrStrLevel[nextRow][nextCol].equals("null")) {
                arrStrLevel[nextRow][nextCol] = getPath(0);
            } else {
                arrStrLevel[nextRow][nextCol] = remakePath(arrStrLevel[nextRow][nextCol], getPath(0));
            }
        }

        //right-bottom
        if (dx == -1 & dy == -1) {

            if (arrStrLevel[currentRow][currentCol].equals("null")) {
                arrStrLevel[currentRow][currentCol] = getPath(5);
            } else {

                arrStrLevel[currentRow][currentCol] = remakePath(arrStrLevel[currentRow][currentCol], getPath(5));
            }

            if (arrStrLevel[nextRow][nextCol].equals("null")) {
                arrStrLevel[nextRow][nextCol] = getPath(1);
            } else {
                arrStrLevel[nextRow][nextCol] = remakePath(arrStrLevel[nextRow][nextCol], getPath(1));
            }
        }

        //bottom
        if (dx == -1 & dy == 0) {
            if (arrStrLevel[currentRow][currentCol].equals("null")) {
                arrStrLevel[currentRow][currentCol] = getPath(6);
            } else {

                arrStrLevel[currentRow][currentCol] = remakePath(arrStrLevel[currentRow][currentCol], getPath(6));
            }

            if (arrStrLevel[nextRow][nextCol].equals("null")) {
                arrStrLevel[nextRow][nextCol] = getPath(2);
            } else {
                arrStrLevel[nextRow][nextCol] = remakePath(arrStrLevel[nextRow][nextCol], getPath(2));
            }
        }

        //left-bottom
        if (dx == -1 & dy == 1) {
            if (arrStrLevel[currentRow][currentCol].equals("null")) {
                arrStrLevel[currentRow][currentCol] = getPath(7);
            } else {

                arrStrLevel[currentRow][currentCol] = remakePath(arrStrLevel[currentRow][currentCol], getPath(7));
            }

            if (arrStrLevel[nextRow][nextCol].equals("null")) {
                arrStrLevel[nextRow][nextCol] = getPath(3);
            } else {
                arrStrLevel[nextRow][nextCol] = remakePath(arrStrLevel[nextRow][nextCol], getPath(3));
            }
        }

    }

    public String getPath(int path) {

        switch (path) {

            case 0://left
                return "10000000";
            case 1://top-left
                return "01000000";
            case 2://top
                return "00100000";
            case 3://top-right
                return "00010000";
            case 4://right
                return "00001000";
            case 5://bottom-right
                return "00000100";
            case 6://bottom
                return "00000010";
            case 7://bottom-left
                return "00000001";
        }

        return "";

    }

    public String remakePath(String currPath, String newPath) {

        StringBuilder newPathBuilder = new StringBuilder(currPath);

        for (int i = 0; i < newPath.length(); i++) {

            if (newPath.charAt(i) == '1') {
                newPathBuilder.setCharAt(i, '1');
            }
        }

        return newPathBuilder.toString();
    }

    public String setFakePath(String path, int i, int j) {

        ArrayList<Integer> invalidNos = (ArrayList<Integer>) randNos.clone();

        StringBuilder fakeStr = new StringBuilder(path);

        if (j - 1 < 0) {

            invalidNos.remove(Integer.valueOf(0));
            invalidNos.remove(Integer.valueOf(1));
            invalidNos.remove(Integer.valueOf(7));
        }


        if (i - 1 < 0) {
            invalidNos.remove(Integer.valueOf(1));
            invalidNos.remove(Integer.valueOf(2));
            invalidNos.remove(Integer.valueOf(3));

        }


        if (j + 1 >= col) {
            invalidNos.remove(Integer.valueOf(3));
            invalidNos.remove(Integer.valueOf(4));
            invalidNos.remove(Integer.valueOf(5));
        }

        if (i + 1 >= row) {

            invalidNos.remove(Integer.valueOf(5));
            invalidNos.remove(Integer.valueOf(6));
            invalidNos.remove(Integer.valueOf(7));

        }

        int ran = rand(0, 2);

        Collections.shuffle(invalidNos);

        if (invalidNos.size() < ran) {
            ran = invalidNos.size();
        }

        for (int p = 0; p < ran; p++) {

            fakeStr.setCharAt(invalidNos.get(p), '1');

        }

        return fakeStr.toString();
    }

    public int rand(int min, int max) {
        Random rand = new Random();

        int randomNum = rand.nextInt(Math.abs((max - min) + 1)) + min;

        return randomNum;
    }
}
