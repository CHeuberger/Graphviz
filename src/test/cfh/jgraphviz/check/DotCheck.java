/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz.check;

import static cfh.jgraphviz.Dot.*;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import cfh.jgraphviz.Graph;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public class DotCheck {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DotCheck::new);
    }

    //==============================================================================================

    private final JFrame frame;

    private DotCheck() {
        var allPanel = new JPanel();
        allPanel.setLayout(new GridLayout(0, 1));
        
        try {
            var dot = """
                digraph {
                  label="old interface"
                  { A ->  { B C} } -> D
                }
                """;
            System.out.println(dot);
            allPanel.add(new JLabel(new ImageIcon(dotToImage(JPG, dot))));
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        
        List<List<Graph>> list = createGraphs();

        Function<Graph, BufferedImage> dotToJpg = g -> g.visit(System.out::println).image(JPG);
        for (var images : list) {
            System.out.println("======================================");
            var panel = new JPanel();
            images.stream().map(dotToJpg).map(ImageIcon::new).map(JLabel::new).forEach(panel::add);
            allPanel.add(panel);
        }

        frame = new JFrame();
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

        frame.add(new JScrollPane(allPanel));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private List<List<Graph>> createGraphs() {

        return List.of(
            List.of(
                // default graph
                graph()
                .add(node("A").to(node("B")))
                ,
                // directed graph
                graph()
                .directed()
                .add(node("A").to(node("B")))
                ,
                // un-directed graph
                graph()
                .directed(false)
                .add(node("A").to(node("B")))
                ,
                // strict graph
                graph()
                .strict()
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("A")))
                ,
                // un-strict graph
                graph()
                .strict(false)
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("A")))
                ,
                // named graph
                graph("the \"name\"")
                .add(node("A"))
                ,
                // node without attributes
                graph()
                .add(node("A"))
                ,
                // with attributes
                graph()
                .add(node("B").with(label("label")))
                ,
                // edge without attributes
                graph()
                .add(node("A").to(node("B")))
                ,
                // edge with attributes
                graph()
                .add(node("A").to(node("B")).with(label("test")))
                ,
                // alternative edge
                graph()
                .add(edge(node("A"), (node("B"))))
                )
            ,
            // attr_stmt
            List.of(
                // graph defaults
                graph()
                .graphdefs()
                .graphdefs(label("Dummy"))
                .graphdefs(label("Default"), fontsize(8))
                .add(node("A"))
                ,
                // node defaults
                graph()
                .nodedefs()
                .nodedefs(fontsize(22))
                .add(node("A"))
                .add(node("B"))
                ,
                // edge defaults
                graph()
                .edgedefs()
                .edgedefs(label("Edge"))
                .add(edge(node("A"), node("B")))
                ,
                // attribute statement
                graph()
                .with()
                .with(label("Label"))
                .add(node("A"))
                ,
                // subgraph
                graph()
                .add(subgraph())
                .add(subgraph().add(node("A")).add(node("B")).to(node("C")))
                .add(subgraph(node("D"), node("E")).to(subgraph().add(node("F")).add(node("G"))))
                ,
                // subgraph with ID
                graph()
                .add(subgraph("ID").add(node("A")).add(node("B")))
                .add(node("C").from(subgraph("ID")))
                )
            ,
            // attributes
            List.of(
                graph()
//                .with(attribute("_background", "c 7 -#000000 C 7 -#ff0000 E 26 80 20 10"))
                .with(_background(xdot("c 7 -#000000 C 7 -#ff0000 E 26 80 20 10")))
                .with(label("Attributes"))
                .add(edge(node("A"), node("B")))
                )

            // TODO ports
            );
    }

}
