/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static cfh.graph.Dot.*;
import static javax.swing.JOptionPane.*;

import java.io.IOException;

import javax.swing.ImageIcon;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public class DotCheck {

    public static void main(String[] args) throws IOException, InterruptedException {
        var nodeB = node("B \"1\"");
        var img = 
            graph("Test 1")
            .directed(true)
            .add( node("A") )
            .add( nodeB )
            .add( node("C").with("color", "red") )
            .add( link(node("A"), nodeB) )
            .add( nodeB.to(node("C")).with("penwidth", 5).with("color", "yellow") )
            .visit(System.out::println)
            .toImage(JPEG);
        showMessageDialog(null, new ImageIcon(img));
    }
}
