/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static cfh.graph.Dot.*;
import static javax.swing.JOptionPane.*;

import java.io.IOException;

import javax.swing.ImageIcon;

import cfh.graph.attr.Color;

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
//            .with(Color.RED.background())
            .add( node("A") )
            .add( nodeB )
            .add( node("C").with(Color.RED) )
            .add( edge(node("A"), nodeB).with("taillabel", "tail") )
            .with("label", "red")
            .add( nodeB.to(node("C")).with("penwidth", 5).with(color("yellow")) )
            .visit(System.out::println)
//            .save(JPEG, new java.io.File("test.jpg"))
            .toImage(JPEG);
        showMessageDialog(null, new ImageIcon(img));
    }
}
