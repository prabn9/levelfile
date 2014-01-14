package colorswaplevelfilegenerate;

/**
 *
 * @author prabin
 */
public final class Grid {

    String[] grids;

    public Grid() {
        grids = new String[16];
    }

    public void setGrids() {

        grids = new String[]{"6x6","7x7","8x8","9x9","10x10"};
    }

    public String getGridSize(int id) {

        return grids[id];
    }
}
