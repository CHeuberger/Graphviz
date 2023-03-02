/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.check;

import static cfh.graph.Dot.*;

import java.awt.GridLayout;
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
        var list = createGraphs();

        var all = new JPanel();
        all.setLayout(new GridLayout(0, 1));
        
        for (List<BufferedImage> images : list) {
            var panel = new JPanel();
            images.stream().map(ImageIcon::new).map(JLabel::new).forEach(panel::add);
            all.add(panel);
        }
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        
        frame.add(new JScrollPane(all));
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private List<List<BufferedImage>> createGraphs() {
        var list = new ArrayList<List<BufferedImage>>();
        
        var images = new ArrayList<BufferedImage>();
        
        images.add(
            graph("directed")
            .directed()
            .with(label("directed"))
            .add(node("A").to(node("B")))
            .visit(System.out::println)
            .toImage(JPEG) );
        
        images.add(
            graph("undirected")
            .undirected()
            .with(label("undirected"))
            .add(node("A").to(node("B")))
            .add(node("B").to(node("A")))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        images.add(
            graph("strict")
            .strict()
            .with(label("strict"))
            .add(node("A").to(node("B")))
            .add(node("B").to(node("A")))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        images.add(
            graph()
            .strict()
            .with(label("nodes"))
            .add(node("A"), node("B"))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        // TODO default graph
        
        images.add(
            graph()
            .with(label("default node"))
            .nodes(attr("style", "filled"), Color.LIGHTBLUE.fill())
            .add(node("A"), node("B"))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        images.add(
            graph()
            .with(label("default edges"))
            .edges(attr("penwidth", 4))
            .add(edge(node("A"), node("B")))
            .add(edge(node("A"), node("C")))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        images.add(
            graph()
            .with(label("graph attribute"))
            .add(node("A").to(node("B")))
            .add(attr("orientation", "landscape"))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        list.add(images);
        images = new ArrayList<>();
        
        images.add(
            graph()
            .with(label("cluster attribute"))
            .add(
                cluster()
                .nodes(attr("penwidth", 5))
                .add(node("A"), node("C"))
                )
            .add(node("A").to(node("B")))
            .add(node("B").to(node("C")))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        images.add(
            graph()
            .with(label("subgraph attribute"))
            .add(
                subgraph()
                .nodes(attr("penwidth", 5))
                .add(node("A"), node("C"))
                )
            .add(node("A").to(node("B")))
            .add(node("B").to(node("C")))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        images.add(
            graph()
            .with(label("cluster target"))
            .add(node("A").to(
                cluster().add(node("B"), node("C"))
                ))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        images.add(
            graph()
            .with(label("cluster source"))
            .add( cluster().add(node("A"), node("B"))
                .to(node("C"))
                )
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        images.add(
            graph()
            .with(label("ports"))
            .add(node("A", Port.E).to(node("B", Port.W)))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        images.add(
            graph()
            .directed()
            .with(label("record"))
            .nodes(attr("shape", "record"))
            .add(node("struct1").with(label("<1> A|<2> B|<3> C")))
            .add(node("struct2").with(label("{<1> a|<2> b|<3> c}")))
            .add(node("struct1", "1").to(node("struct2", "2", Port.W)))
            .visit(System.out::println)
            .toImage(JPEG)
            );
        
        list.add(images);
        
        return list;
    }
}
