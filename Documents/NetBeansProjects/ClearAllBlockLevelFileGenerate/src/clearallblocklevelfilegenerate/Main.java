package clearallblocklevelfilegenerate;

/**
 *
 * @author prabin
 */
public class Main {

    public void load() {

        new GenerateLevelFile().generate();
    }

    public static void main(String[] args) {

        Main mainObj = new Main();
        mainObj.load();
    }
}
