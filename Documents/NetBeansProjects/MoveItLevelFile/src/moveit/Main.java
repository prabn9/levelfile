
package moveit;

/**
 *
 * @author prabin
 */
public class Main {
    
    public void load(){
    
        new LevelFile().generate();
    }

    public static void main(String[] args) {
        
        new Main().load();
        
      
    }
}
