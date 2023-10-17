/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz.check;

import static cfh.jgraphviz.Dot.*;
import static cfh.jgraphviz.Color.X11.*;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
        List<List<Graph>> list = createDotGraphs();
        var tabbed = new JTabbedPane();

        {
            var allPanel = Box.createVerticalBox();

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

            Function<Graph, BufferedImage> dotToJpg = g -> g.visit(System.out::println).image(Engine.DOT, JPG);
            for (var images : list) {
                System.out.println("======================================");
                var panel = new JPanel();
                images.stream().map(dotToJpg).map(ImageIcon::new).map(JLabel::new).forEach(panel::add);
                allPanel.add(panel);
            }

            var scroll = new JScrollPane(allPanel);
            SwingUtilities.invokeLater( () -> scroll.getVerticalScrollBar().setValue(Integer.MAX_VALUE) );
            tabbed.add("DOT", scroll);
        }
        
//        if (false)
        {
            var allPanel = Box.createVerticalBox();

            Function<Graph, BufferedImage> neatoToJpg = g -> g.visit(System.out::println).image(Engine.NEATO, JPG);
            for (var images : list) {
                System.out.println("======================================");
                var panel = new JPanel();
                images.stream().map(neatoToJpg).map(ImageIcon::new).map(JLabel::new).forEach(panel::add);
                allPanel.add(panel);
            }

            var scroll = new JScrollPane(allPanel);
            SwingUtilities.invokeLater( () -> scroll.getVerticalScrollBar().setValue(Integer.MAX_VALUE) );

            tabbed.add("NEATO", scroll);
        }
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

        frame.add(tabbed);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private List<List<Graph>> createDotGraphs() {

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
                .add(subgraph().add(node("A")).add(node("B")).to(node("S")))
                .add(subgraph(node("D"), node("E")).to(subgraph().add(node("F")).add(node("G"))))
                ,
                // subgraph with ID
                graph()
                .add(subgraph("ID").add(node("A")).add(node("B")))
                .add(node("S").from(subgraph("ID")))
                )
            ,
            // attributes
            List.of(
                graph()
                .with(_background(xdot("c 7 -#000000 S 7 -#ff0000 E 26 95 20 10")))
                .with(bgcolor(rgb(200, 140, 200, 140)))
                .with(label("Attributes"))
                .add(edge(node("A"), node("B")))
                ,
                graph()
                .with(center())
                .with(attribute("layout", Engine.PATCHWORK.toString().toLowerCase()))
                .with(bgcolor(RED))
                .with(AQUA.bgcolor())
                .nodedefs(attribute("style", "filled"))
                .add(node("8").with(area(8)))
                .add(node("4").with(area(4)))
                .add(node("2").with(area(2)))
                .add(node("2+").with(area(2)))
                ,
                digraph()
                .with(bgcolor(hsv(0.0, 0.8, 1.0)))
                .edgedefs(arrowhead(arrowtype("odot", "lbox")))
                .add(edge(node("A"), node("B")).with(arrowsize(1.5)))
                .add(edge(node("B"), node("S")).with(attribute("dir", "both"), arrowtail(arrowtype("tee"))))
                ,
                graph()
                .with(
                    attribute("layout", "sfdp"),
                    beautify()
                    )
                .nodedefs(label("\\N"))
                .add(edge(node("A"), subgraph(node("b"), node("c"), node("d"), node("e"))))
                ,
                digraph()
                .with(attribute("style", "radial"), RED.and(BLUE).bgcolor())
                .with(classname("test"))
                .add(edge(node("A"), node("B")).with(arrowsize(3.1)))
                )
            ,
            // subgraph
            List.of(
                digraph()
                .with(clusterrank(true))
                .add(subgraph(node("A").with(color(ORANGE)), node("B")).with(cluster()))
                .add(subgraph(node("C"), node("D")).with(cluster(false)))
                .add(
                    subgraph("cluster_test3")
                    .with(label("test3"))
                    .add(node("E").to(node("F")).with(color(AQUAMARINE)))
                    )
                .add(node("G").with(ORANGE.color()))
                ,
                digraph()
                .with(comment("Just a comment"))
                .nodedefs(attribute("style", "filled"), colorscheme("oranges9"), color(color("2")))
                .add(node("A"))
                .add(node("B").with(color(AQUAMARINE)))
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("C")))
                ,
                digraph()
                .with(compound())
                .add(subgraph("A")
                    .with(label("A"), cluster())
                    .add(node("N1"), node("N3"), node("N5"), node("N7"))
                    )
                .add(subgraph("B")
                    .with(label("B"), cluster())
                    .add(node("N2"), node("N4"), node("N6"), node("N8"))
                    )
                .add(edge(node("N1"), node("N2")).with(label("1")))
                .add(edge(node("N3"), node("N4")).with(label("2"), attribute("ltail", "A")))
                .add(edge(node("N5"), node("N6")).with(label("3"), attribute("lhead", "B")))
                .add(edge(node("N7"), node("N8")).with(label("4"), attribute("ltail", "A"), attribute("lhead", "B")))
                )
            ,
            List.of(
                digraph()
                .with(concentrate())
                .add(edge(node("A"), node("D")).with(label("1")))
                .add(edge(node("B"), node("D")))
                .add(edge(node("C"), node("D")))
                ,
                digraph()
                .add(edge(node("A"), node("C")))
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("C")).with(unconstraint()))
                ,
                digraph()
                .add(edge(node("A"), node("B")).with(GREEN.and(RED).and(BLUE).and(BEIGE).color()))  // green:red:blue
                .add(edge(node("B"), node("C")).with(GREEN.split(0.3, RED).split(0.4, BLUE).color()))  // green;0.3:red;0.4:blue
                .add(edge(node("C"), node("D")).with(GREEN.split(0.3, RED).split(BLUE, 0.3).color()))  // green;0.3:red:blue;0.3
                .add(edge(node("D"), node("E")).with(GREEN.split(RED, 0.4).split(BLUE, 0.3).color()))  // green:red;0.4:blue;0.3
                ,
                digraph()
                .with(dim(5))
                .add(edge(node("A"), node("B")).with(label("first")))
                .add(edge(node("B"), node("C")).with(label("second"), decorate()))
                .add(edge(node("D"), node("E")).with(label("third"), decorate(true)))
                .add(edge(node("E"), node("F")).with(label("fourth"), decorate(false)))
                ,
                digraph()
                .add(edge(node("A"), node("B")).with(dir(DirType.FORWARD)))
                .add(edge(node("B"), node("C")).with(dir(DirType.BACK)))
                .add(edge(node("D"), node("E")).with(dir(DirType.BOTH)))
                .add(edge(node("E"), node("F")).with(dir(DirType.NONE)))
                ,
                digraph()
                .with(diredgeconstraints(), attribute("mode", "ipsep"))
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")))
                .add(edge(node("B"), node("C")))
                )
            ,
            List.of(
                graph()
                .with(dpi(96))
                .add(node("Bottom").with(distortion(-0.5), attribute("shape", "polygon"), attribute("sides", 4)))
                .add(node("Top").with(distortion(+0.5), attribute("shape", "polygon"), attribute("sides", 4)))
                )
            // TODO ports
            );
    }

}
