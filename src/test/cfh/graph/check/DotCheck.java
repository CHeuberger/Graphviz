/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.check;

import static cfh.graph.Dot.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import cfh.graph.Color;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public class DotCheck {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DotCheck::new);
    }
    
    private final JFrame frame;
    
    private DotCheck() {
        var images = createGraphs();
        
        var panel = new JPanel();
        images.stream().map(ImageIcon::new).map(JLabel::new).forEach(panel::add);
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        
        frame.add(new JScrollPane(panel));
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private List<BufferedImage> createGraphs() {
        var list = new ArrayList<BufferedImage>();
        
        list.add(
            graph("directed")
            .directed()
            .with(label("directed"))
            .add(node("A").to(node("B")))
            .visit(System.out::println)
            .toImage(JPEG) );
        
        list.add(
            graph("undirected")
            .undirected()
            .with(label("undirected"))
            .add(node("A").to(node("B")))
            .add(node("B").to(node("A")))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        list.add(
            graph("strict")
            .strict()
            .with(label("strict"))
            .add(node("A").to(node("B")))
            .add(node("B").to(node("A")))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        list.add(
            graph()
            .strict()
            .with(label("nodes"))
            .add(node("A"), node("B"))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        // TODO default graph
        
        list.add(
            graph()
            .with(label("default node"))
            .nodes(attr("style", "filled"), Color.LIGHTBLUE.fill())
            .add(node("A"), node("B"))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        list.add(
            graph()
            .with(label("default edges"))
            .edges(attr("penwidth", 4))
            .add(edge(node("A"), node("B")))
            .add(edge(node("A"), node("C")))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        list.add(
            graph()
            .with(label("graph attribute"))
            .add(node("A").to(node("B")))
            .add(attr("orientation", "landscape"))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        return list;
    }
}
