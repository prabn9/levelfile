package moveit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author prabin
 */
public class LevelFile {

    HashMap<Integer, String> levelFiles;
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

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LevelFile() {

        init();
    }

    public void init() {

        levelFiles = new HashMap<Integer, String>();

        balls = new ArrayList<Ball>();
        blocks = new ArrayList<Block>();
        reservedPoint = new ArrayList<Point>();
        reservedColor = new ArrayList<Integer>();

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
                System.out.println("level"+level);
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        System.out.print("\t" + boardStatus[i][j]);
                    }

                    System.out.println("\n");

                }
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
        int color = 1;

        for (int p = 0; p < noOfBalls; p++) {
            row = rand(0, this.row - 1);
            col = rand(0, this.col - 1);

            while (reservedPoint.contains(new Point(row, col))) {
                row = rand(0, this.row - 1);
                col = rand(0, this.col - 1);
            }

            reservedPoint.add(new Point(row, col));
            boardStatus[row][col] = color;

            if (colors == 1) {
                color = 1;
            } else {
                while (reservedColor.contains(color = rand(1, colors))) {
                }

                reservedColor.add(color);

            }

            balls.add(new Ball(row, col, color));

        }
    }

    public int rand(int min, int max) {

        Random random = new Random();

        return (random.nextInt(max - min + 1) + min);
    }
}
