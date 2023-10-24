/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz.check;

import static cfh.jgraphviz.Dot.*;
import static cfh.jgraphviz.Color.X11.*;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import cfh.jgraphviz.Graph;
import cfh.jgraphviz.LayerRange;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public class DotCheck {

    private static final Engine ENGINE = Engine.DOT;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DotCheck::new);
    }

    //==============================================================================================

    private final JFrame frame;

    private DotCheck() {
        List<List<Graph>> list = createDotGraphs();

        var allPanel = Box.createVerticalBox();

        try {
            var dot = """
                digraph {
                  label="old interface"
                  { A ->  { B C} } -> D
                }
                """;
            System.out.println(dot);
            allPanel.add(new JLabel(new ImageIcon(dotToImage(ENGINE, JPG, dot))));
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        Function<Graph, BufferedImage> dotToJpg = g -> g.visit(System.out::println).image(ENGINE, JPG);
        for (var graph : list) {
            System.out.println("======================================");
            var panel = new JPanel();
            graph.stream().map(dotToJpg).map(ImageIcon::new).map(JLabel::new).forEach(panel::add);
            allPanel.add(panel);
        }

        var scrollpane = new JScrollPane(allPanel);
        SwingUtilities.invokeLater( () -> scrollpane.getVerticalScrollBar().setValue(Integer.MAX_VALUE) );
        
        var close = new JButton("close");
        
        frame = new JFrame(ENGINE.toString());
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

        frame.add(scrollpane);
        frame.add(close, BorderLayout.PAGE_END);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        close.addActionListener(ev -> frame.dispose());
    }

    private List<List<Graph>> createDotGraphs() {
        Graph tmp;

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
                .with(attribute("style", "radial"), RED.to(BLUE).bgcolor())
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
                .add(node("G").with(ORANGE))
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
                .add(edge(node("A"), node("C")).with(RED))
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("C")).with(unconstraint()))
                ,
                digraph()
                .add(edge(node("A"), node("B")).with(GREEN.to(RED).to(BLUE).to(BEIGE)))  // green:red:blue
                .add(edge(node("B"), node("C")).with(GREEN.split(0.3, RED).split(0.4, BLUE)))  // green;0.3:red;0.4:blue
                .add(edge(node("C"), node("D")).with(GREEN.split(0.3, RED).split(BLUE, 0.3)))  // green;0.3:red:blue;0.3
                .add(edge(node("D"), node("E")).with(GREEN.split(RED, 0.4).split(BLUE, 0.3)))  // green:red;0.4:blue;0.3
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
                .add(node("B").with(attribute("xlabel", "Additional label")))
                ,
                digraph()
                .with(diredgeconstraints(), attribute("mode", "ipsep"), forcelabels(false))
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")).with(attribute("xlabel", "some more text")))
                .add(edge(node("B"), node("C")))
                .add(edge(node("A"), node("D")))
                .add(edge(node("D"), node("C")))
                )
            ,
            List.of(
                graph()
                .with(dpi(96))
                .with(fontpath(Path.of("/tmp")))
                .nodedefs(fontsize(8))
                .nodedefs(GREEN.to(AQUAMARINE4).fill(), attribute("style", "filled"))
                .add(node("Bottom").with(distortion(-0.5), attribute("shape", "polygon"), attribute("sides", 4)))
                .add(node("Top").with(distortion(+0.5), attribute("shape", "polygon"), attribute("sides", 4)))
                .add(node("A").with(attribute("style", "filled"), fillcolor(BLUE)))
                ,
                digraph()
                .add(node("A1").with(group("A")))
                .add(node("A2").with(group("A")))
                .add(node("A3").with(group("A")))
                .add(node("A4").with(group("A")))
                .add(edge(node("A1"), node("A2")).with(attribute("headlabel", new StringBuilder("<head<br/>line>"))))
                .add(edge(node("A1"), node("A3")))
                .add(edge(node("A1"), node("A4")))
                .add(edge(node("A2"), node("A3")))
                .add(edge(node("A2"), node("A4")))
                .add(edge(node("A3"), node("A4")))
                ,
                digraph()
                .with(imagepath("build"))
                .add(edge(node("A1"), node("A2")))
                .add(edge(node("A1"), node("A3")).with(headport(Compass.E)))
                .add(edge(node("A1"), node("A4")).with(headport(port("b"))))
                .add(edge(node("A1"), node("A5")).with(headport(port("c", Compass.W))))
                .add(node("C").with(image("icon.png"), imagepos(ImagePos.TopCenter), height(1), imagescale(ImageScale.WIDTH)))
                ,
                digraph()
                .with(label(html("test <b>bold</b>")))
                .nodedefs(attribute("shape","record"))
                .add(node("A").with(label("<a>a|<b>"), height(1)))
                .add(node("B").with(label("{<c>c|<d>}")))
                .add(edge(node("A", port("a")), node("B", port("c", Compass.W))))
                )
            ,
            List.of(
                graph()
                .with(landscape(false))
                .with(labelloc(LabelLoc.TOP), labeljust(LabelJust.RIGHT), label("X"))
                .add(edge(node("A1"), node("B1")).with(label("plain"), headlabel("head"), labelfontsize(8.0), attribute("taillabel", "tail")))
                .add(edge(node("A2"), node("B2")).with(label("angle 0"), labelangle(0), headlabel("head"), attribute("taillabel", "tail")))
                .add(edge(node("A3"), node("B3")).with(label("angle 45"), labelangle(45), headlabel("head"), attribute("taillabel", "tail")))
                .add(edge(node("A4"), node("B4")).with(label("angle 90"), labelangle(90), headlabel("head"), attribute("taillabel", "tail")))
                .add(edge(node("A5"), node("B5")).with(label("distance 0.0"), labeldistance(0.0), headlabel("head"), attribute("taillabel", "tail")))
                .add(edge(node("A6"), node("B6")).with(label("distance 1.0"), labeldistance(1.0), headlabel("head"), attribute("taillabel", "tail")))
                .add(edge(node("A7"), node("B7")).with(label("float") ,labelfloat(), labelfontcolor(RED), headlabel("head"), attribute("taillabel", "tail")))
                )
            ,
            List.of(
                graph("Layers")
                .with(layerlistsep(Character.toString(0x1F600)))
                .with(layersep(Character.toString(0x1F601)))
                .with(layers("a", "b", "c", "d:e:f:g"))
                .with(layerselect(range("a"), range("c")))
                .add(node("A").with(layer(range("a", "b").include("d").include("f", LayerRange.ALL))))
                .add(node("B").with(layer(range("a", "b"), range("d"), range("f", LayerRange.ALL))))
                ,
                digraph()
                .with(layout(Engine.NEATO))
                .with(levelsgap(-1.0), attribute("mode", "hier"))
                .add(edge("A", "B").with(len(1.5)))
                .add(edge("B", "C"))
                .add(edge("A", "C"))
                .add(edge("A", "D"))
                ,
                digraph()
                .with(layout(Engine.NEATO))
                .with(levelsgap(1.0), attribute("mode", "hier"))
                .add(edge("A", "B").with(len(1.5)))
                .add(edge("B", "C"))
                .add(edge("A", "C"))
                .add(edge("A", "D"))
                ,
                digraph()
                .with(layout(Engine.DOT), compound())
                .with(margin(point(0.3, 0.1)))
                .add(subgraph("cluster_a").add(node("A1"), node("A2"), node("A3")).with(margin(16)))
                .add(subgraph("cluster_b").add(node("B1"), node("B2"), node("B3")))
                .add(edge("A1", "B1"))
                .add(edge("A2", "B2").with(lhead("cluster_b")))
                .add(edge("A3", "B3").with(ltail("cluster_a")))
                .add(node("A1").with(margin(point(0.3, 0.1))))
                )
            ,
            List.of(
                digraph()
                .with(mclimit(1))
                .add(edge("A", "B"))
                .add(edge("A", "C"))
                .add(edge("A", "D"))
                .add(edge("A", "E"))
                .add(edge("B", "C"))
                .add(edge("B", "D"))
                .add(edge("B", "E"))
                ,
                digraph()
                .with(mclimit(2.0))
                .edgedefs(minlen(0))
                .add(edge("A", "B"))
                .add(edge("A", "C"))
                .add(edge("A", "D"))
                .add(edge("A", "E"))
                .add(edge("B", "C"))
                .add(edge("B", "D"))
                .add(edge("B", "E"))
                ,
                tmp = graph()
                .with(compound())
                .add(subgraph("cluster_a")
                    .with(label("a"))
                    .add(edge("A1", "A2"), edge("A2", "A3"))
                    )
                .add(subgraph("cluster_b")
                    .with(label("b"))
                    .add(edge("B1", "B2"), edge("B1", "B3"))
                    )
                .add(edge("A2", "B2"))
                ,
                tmp.with(newrank())
                )
            // TODO ports
            );
    }
}
