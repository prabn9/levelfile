
package colorswaplevelfilegenerate;

/**
 *
 * @author prabin
 */
public class ColorSwapLevelFileGenerate {
    
    GenerateLevel generateLevel;
    public void load(){
        generateLevel =  new GenerateLevel();
        
        generateLevel.generateLevelFile();
    }

    public static void main(String[] args) {
        ColorSwapLevelFileGenerate cslfg = new ColorSwapLevelFileGenerate();
        
       cslfg.load();
       
    }
}
