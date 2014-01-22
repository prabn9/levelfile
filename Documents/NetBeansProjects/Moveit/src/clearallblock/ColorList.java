/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clearallblock;

import java.awt.Color;
import javax.swing.JFrame;

/**
 *
 * @author prabin
 */
public class ColorList extends JFrame {

    public static Color getColor(int id) {

        Color[] colorList = new Color[]{
            Color.blue, // blue
            Color.red, // red
            Color.green, // green
            Color.yellow, // yellow
            Color.orange, // orange
            
        };

        return colorList[id];
    }
}
