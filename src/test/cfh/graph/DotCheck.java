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
            .with( Color.RED.and(.3f, Color.LIGHTBLUE).background() )
            .nodes( font("Corrier"), font(8), Color.CORAL3.font() )
            .nodes( attr("style", "filled"), Color.YELLOW.fill() )
            .edges( attr("decorate", true) )
            .add( node("A") )
            .add( nodeB )
            .add( node("C").with(Color.RED) )
            .add( comment("==========") )
            .add( edge(node("A"), nodeB).with("taillabel", "tail").with("label", " LABEL") )
            .add( nodeB.to(node("C")).with("penwidth", 5).with(color("yellow")) )
            .visit(System.out::println)
//            .save(JPEG, new java.io.File("test.jpg"))
            .toImage(JPEG);
        showMessageDialog(null, new ImageIcon(img));
    }
}
