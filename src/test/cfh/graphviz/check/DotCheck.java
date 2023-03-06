/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graphviz.check;

import static cfh.graphviz.Dot.*;

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

import cfh.graphviz.Graph;

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
        List<List<BufferedImage>> list;
        try {
            list = createGraphs();
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        var all = new JPanel();
        all.setLayout(new GridLayout(0, 1));

        for (var images : list) {
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

    private List<List<BufferedImage>> createGraphs() throws IOException, InterruptedException {
        Function<Graph, BufferedImage> dotJpg = g -> g.visit(System.out::println).image(JPG);

        return List.of(
            List.of(
                // old interface
                dotToImage(JPG, """
                    digraph {
                      label="test"
                      { A ->  { B C} } -> D
                    }
                    """)
                )
            ,
            List.of(
                // default unnamed undirected graph
                graph()
                .add(node("A"))
                ,
                // default undirected named
                graph("name")
                .add(node("B"))
                ,
                // strict
                graph()
                .strict()
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("A")))
                ,
                // un-strict
                graph()
                .strict(false)
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("A")))
                ,
                //directed
                graph()
                .directed()
                .add(node("A").to(node("B")))
                ).stream().map(dotJpg).toList()
//            ,
//            List.of(
//                // subgraph directly in graph
//                graph()
//                .add(subgraph(node("A")).to(node("B")))
//                ,
//                // subgraph source
//                graph()
//                .add(edge(
//                    subgraph().add(node("A")).add(node("B")),
//                    node("C")
//                    ))
//                ,
//                // subgraph target
//                graph()
//                .add(edge(
//                    node("A"),
//                    subgraph().add(node("B")).add(node("C"))
//                    ))
//                ).stream().map(dotJpg).toList()
            );
    }

}
