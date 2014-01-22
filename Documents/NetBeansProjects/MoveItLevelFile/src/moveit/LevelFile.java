package moveit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author prabin
 */
public class LevelFile {

    HashMap<Integer, String> levelFile;
    String currentLevelFile;
    int[][] boardStatus;
    int row, col;
    int var; //number of variations for each grid size :: equals to the number of rows or column
    int noOfBlocks;
    int totalColor;
    int noOfBalls; //total number of balls and colored grid to be generated equals to var
    ArrayList<Ball> balls;
    ArrayList<Block> blocks;
    ArrayList<Point> reservedPoint;
    ArrayList<Integer> reservedColor;
    ArrayList<Character> direction;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LevelFile() {

        init();
    }

    public void init() {

        levelFile = new HashMap<Integer, String>();

        balls = new ArrayList<Ball>();
        blocks = new ArrayList<Block>();
        reservedPoint = new ArrayList<Point>();
        reservedColor = new ArrayList<Integer>();
        direction = new ArrayList<Character>();

        direction.add('u');
        direction.add('d');
        direction.add('l');
        direction.add('r');
        direction.add('u');
        direction.add('d');
        direction.add('l');
        direction.add('r');


        load();

    }

    private void resetAll() {

        balls.clear();
        blocks.clear();
        reservedColor.clear();
        reservedPoint.clear();
    }

    public void setRowCol(int id) {

        row = col = Grids.grids[id];
    }

    public void setVar() {
        var = row;
    }

    public void setBoard() {

        boardStatus = new int[row][col];

    }

    public void setNoOfBlocks() {

        noOfBlocks = rand(var - 2, var);

    }

    public void setBlocks() {

        int row, col;
        for (int p = 0; p < noOfBlocks; p++) {

            row = rand(0, this.row - 1);
            col = rand(0, this.col - 1);

            while (reservedPoint.contains(new Point(row, col))) {
                row = rand(0, this.row - 1);
                col = rand(0, this.col - 1);
            }

            reservedPoint.add(new Point(row, col));
            boardStatus[row][col] = -1;

        }
    }

    /*
     * chooses the random position for balls and colored grid
     * here both the position of ball and colored grids are same
     */
    public void selectRandomPosition() {
    }

    public void setNoOfBalls() {

        noOfBalls = var;
    }

    private void load() {

        for (int grids = 0; grids < Grids.grids.length; grids++) {

            setRowCol(grids);
            setBoard();
            setVar();
            setNoOfBalls();
            setNoOfBlocks();
            generate();
        }
    }

    public void generate() {

        for (int pack = 1; pack <= var; pack++) {


            totalColor = pack;

            for (int level = 0; level < 150; level++) {
                resetAll();

                setRandomObject(totalColor);
                setBlocks();
                setRestOfGrid();

                System.out.println("--------------------------");
                System.out.println("level" + level);
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        System.out.print("\t" + boardStatus[i][j]);
                    }

                    System.out.println("\n");


                }

                setToString(totalColor);
            }


        }



    }

    public void setRestOfGrid() {

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                if (!reservedPoint.contains(new Point(i, j))) {
                    boardStatus[i][j] = 0;
                }
            }
        }
    }

    public void setRandomObject(int colors) {
        @SuppressWarnings("LocalVariableHidesMemberVariable")
        int row, col;

        for (int i = 1; i <= colors; i++) {
            reservedColor.add(i);
        }

        Collections.shuffle(reservedColor);
        int index = 0;

        for (int p = 0; p < noOfBalls; p++) {
            row = rand(0, this.row - 1);
            col = rand(0, this.col - 1);

            while (reservedPoint.contains(new Point(row, col))) {
                row = rand(0, this.row - 1);
                col = rand(0, this.col - 1);
            }

            reservedPoint.add(new Point(row, col));

            if (index >= colors) {
                index = 0;
            }
            int color = reservedColor.get(index++);


            balls.add(new Ball(row, col, color));

            boardStatus[row][col] = color;
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
                        System.err.println("str-----" + strLvl);
                        System.err.println("clf-----" + currentLevelFile);
                        System.err.println("---------------------Match Found---" + lvl + "--" + level + "------------");
                        return true;
                    }
                }

            }
        }

        levelFile.put(level, currentLevelFile);
        return false;
    }

    public void shuffleBoard() {

        Collections.shuffle(this.direction);

        char direction;
        for (int p = 0; p < this.direction.size(); p++) {

            for (Ball ball : balls) {
                
                
                direction = this.direction.get(p);
                switch (direction) {

                    case 'u':

                        ball.row(ball.row() - 1);
                        break;

                    case 'd':
                        
                        ball.row(ball.row() + 1);
                        break;

                    case 'l':
                        ball.col(ball.col() - 1);
                        break;

                    case 'r':
                        
                        ball.col(ball.row() + 1);
                        break;

                }
            }

        }

    }

public void move(int row, int col) {
    }

    public String setToString(int colors) {


        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                boolean flag = true;
                for (Ball ball : balls) {

                    if (ball.get(i, j) > 0) {

                        sb.append((char) (ball.get(i, j) + 48));
                        flag = false;
                        break;
                    }
                }

                if (boardStatus[i][j] == -1) {
                    sb.append('-');
                    sb.append('1');
                } else {

                    if (flag) {
                        sb.append('0');
                    }

                    sb.append((char) (boardStatus[i][j] + 48));
                }

                if (j < col - 1) {
                    sb.append(",");
                }

            }
            sb.append(";");
        }

        System.out.println(sb.toString());

        return sb.toString();
    }

    public int rand(int min, int max) {

        Random random = new Random();

        return (random.nextInt(max - min + 1) + min);
    }
}
