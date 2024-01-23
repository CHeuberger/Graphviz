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
import cfh.jgraphviz.PackMode;
import cfh.jgraphviz.PackMode.Align;

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
        var max = scrollpane.getMaximumSize();
        max.width = Math.min(max.width, 1900);
        max.height = Math.min(max.height, 980);
        scrollpane.setPreferredSize(max);
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
                graph("11")
                .directed()
                .add(node("A").to(node("B")))
                ,
                // un-directed graph
                graph("12")
                .directed(false)
                .add(node("A").to(node("B")))
                ,
                // strict graph
                graph("13")
                .strict()
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("A")))
                ,
                // un-strict graph
                graph("14")
                .strict(false)
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("A")))
                ,
                // named graph
                graph("25 the \"name\"")
                .add(node("A"))
                ,
                // node without attributes
                graph("16")
                .add(node("A"))
                ,
                // with attributes
                graph("17")
                .add(node("B").with(label("label")))
                ,
                // edge without attributes
                graph("18")
                .add(node("A").to(node("B")))
                ,
                // edge with attributes
                graph("19")
                .add(node("A").to(node("B")).with(label("test")))
                ,
                // alternative edge
                graph("20")
                .add(edge(node("A"), (node("B"))))
                )
            ,
            // attr_stmt
            List.of(
                // graph defaults
                graph("21")
                .graphdefs()
                .graphdefs(label("Dummy"))
                .graphdefs(label("Default"), fontsize(8))
                .add(node("A"))
                ,
                // node defaults
                graph("22")
                .nodedefs()
                .nodedefs(fontsize(22))
                .add(node("A"))
                .add(node("B"))
                ,
                // edge defaults
                graph("23")
                .edgedefs()
                .edgedefs(label("Edge"))
                .add(edge(node("A"), node("B")))
                ,
                // attribute statement
                graph("24")
                .with()
                .with(label("Label"))
                .add(node("A"))
                ,
                // subgraph
                graph("25")
                .add(subgraph())
                .add(subgraph().add(node("A")).add(node("B")).to(node("S")))
                .add(subgraph(node("D"), node("E")).to(subgraph().add(node("F")).add(node("G"))))
                ,
                // subgraph with ID
                graph("26")
                .add(subgraph("ID").add(node("A")).add(node("B")))
                .add(node("S").from(subgraph("ID")))
                )
            ,
            // attributes
            List.of(
                graph("31")
                .with(_background(xdot("c 7 -#000000 S 7 -#ff0000 E 26 95 20 10")))
                .with(bgcolor(rgb(200, 140, 200, 140)))
                .with(label("Attributes"))
                .add(edge(node("A"), node("B")))
                ,
                graph("32")
                .with(center())
                .with(attribute("layout", Engine.PATCHWORK.value()))
                .with(bgcolor(RED))
                .with(AQUA.bgcolor())
                .nodedefs(attribute("style", "filled"))
                .add(node("8").with(area(8)))
                .add(node("4").with(area(4)))
                .add(node("2").with(area(2)))
                .add(node("2+").with(area(2)))
                ,
                digraph("33")
                .with(bgcolor(hsv(0.0, 0.8, 1.0)))
                .edgedefs(arrowhead(arrowtype("odot", "lbox")))
                .add(edge(node("A"), node("B")).with(arrowsize(1.5)))
                .add(edge(node("B"), node("S")).with(attribute("dir", "both"), arrowtail(arrowtype("tee"))))
                ,
                graph("34")
                .with(
                    attribute("layout", "sfdp"),
                    beautify()
                    )
                .nodedefs(label("\\N"))
                .add(edge(node("A"), subgraph(node("b"), node("c"), node("d"), node("e"))))
                ,
                digraph("35")
                .with(attribute("style", "radial"), RED.to(BLUE).bgcolor())
                .with(classname("test"))
                .add(edge(node("A"), node("B")).with(arrowsize(3.1)))
                )
            ,
            // subgraph
            List.of(
                digraph("41")
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
                digraph("42")
                .with(comment("Just a comment"))
                .nodedefs(attribute("style", "filled"), colorscheme("oranges9"), color(color("2")))
                .add(node("A"))
                .add(node("B").with(color(AQUAMARINE)))
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("C")))
                ,
                digraph("43")
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
                digraph("51")
                .with(concentrate())
                .add(edge(node("A"), node("D")).with(label("1")))
                .add(edge(node("B"), node("D")))
                .add(edge(node("C"), node("D")))
                ,
                digraph("52")
                .add(edge(node("A"), node("C")).with(RED))
                .add(edge(node("A"), node("B")))
                .add(edge(node("B"), node("C")).with(unconstraint()))
                ,
                digraph("53")
                .add(edge(node("A"), node("B")).with(GREEN.to(RED).to(BLUE).to(BEIGE)))  // green:red:blue
                .add(edge(node("B"), node("C")).with(GREEN.split(0.3, RED).split(0.4, BLUE)))  // green;0.3:red;0.4:blue
                .add(edge(node("C"), node("D")).with(GREEN.split(0.3, RED).split(BLUE, 0.3)))  // green;0.3:red:blue;0.3
                .add(edge(node("D"), node("E")).with(GREEN.split(RED, 0.4).split(BLUE, 0.3)))  // green:red;0.4:blue;0.3
                ,
                digraph("54")
                .with(dim(5))
                .add(edge(node("A"), node("B")).with(label("first")))
                .add(edge(node("B"), node("C")).with(label("second"), decorate()))
                .add(edge(node("D"), node("E")).with(label("third"), decorate(true)))
                .add(edge(node("E"), node("F")).with(label("fourth"), decorate(false)))
                ,
                digraph("55")
                .add(edge(node("A"), node("B")).with(dir(DirType.FORWARD)))
                .add(edge(node("B"), node("C")).with(dir(DirType.BACK)))
                .add(edge(node("D"), node("E")).with(dir(DirType.BOTH)))
                .add(edge(node("E"), node("F")).with(dir(DirType.NONE)))
                .add(node("B").with(attribute("xlabel", "Additional label")))
                ,
                digraph("56")
                .with(diredgeconstraints(), attribute("mode", "ipsep"), forcelabels(false))
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")).with(attribute("xlabel", "some more text")))
                .add(edge(node("B"), node("C")))
                .add(edge(node("A"), node("D")))
                .add(edge(node("D"), node("C")))
                )
            ,
            List.of(
                graph("61")
                .with(dpi(96))
                .with(fontpath(Path.of("/tmp")))
                .nodedefs(fontsize(8))
                .nodedefs(GREEN.to(AQUAMARINE4).fill(), attribute("style", "filled"))
                .add(node("Bottom").with(distortion(-0.5), attribute("shape", "polygon"), attribute("sides", 4)))
                .add(node("Top").with(distortion(+0.5), attribute("shape", "polygon"), attribute("sides", 4)))
                .add(node("A").with(attribute("style", "filled"), fillcolor(BLUE)))
                ,
                digraph("62")
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
                digraph("63")
                .with(imagepath("build"))
                .add(edge(node("A1"), node("A2")))
                .add(edge(node("A1"), node("A3")).with(headport(Compass.E)))
                .add(edge(node("A1"), node("A4")).with(headport(port("b"))))
                .add(edge(node("A1"), node("A5")).with(headport(port("c", Compass.W))))
                .add(node("C").with(image("icon.png"), imagepos(ImagePos.TOP_CENTER), height(1), imagescale(ImageScale.WIDTH)))
                ,
                digraph("64")
                .with(label(html("test <b>bold</b>")))
                .nodedefs(attribute("shape","record"))
                .add(node("A").with(label("<a>a|<b>"), height(1)))
                .add(node("B").with(label("{<c>c|<d>}")))
                .add(edge(node("A", port("a")), node("B", port("c", Compass.W))))
                )
            ,
            List.of(
                graph("71")
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
                digraph("82")
                .with(layout(Engine.NEATO))
                .with(levelsgap(-1.0), attribute("mode", "hier"))
                .add(edge("A", "B").with(len(1.5)))
                .add(edge("B", "C"))
                .add(edge("A", "C"))
                .add(edge("A", "D"))
                ,
                digraph("83")
                .with(layout(Engine.NEATO))
                .with(levelsgap(1.0), attribute("mode", "hier"))
                .add(edge("A", "B").with(len(1.5)))
                .add(edge("B", "C"))
                .add(edge("A", "C"))
                .add(edge("A", "D"))
                ,
                digraph("84")
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
                digraph("91")
                .with(mclimit(1))
                .add(edge("A", "B"))
                .add(edge("A", "C"))
                .add(edge("A", "D"))
                .add(edge("A", "E"))
                .add(edge("B", "C"))
                .add(edge("B", "D"))
                .add(edge("B", "E"))
                ,
                digraph("92")
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
                tmp = graph("93+94")
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
                tmp.copy()
                .with(newrank())
                ,
                graph("95")
                .with(compound())
                .with(nodesep(0.1))
                .add(subgraph("cluster_a")
                    .with(label("a"))
                    .add(edge("A1", "A2"), edge("A2", "A3"))
                    )
                .add(subgraph("cluster_b")
                    .with(label("b"))
                    .add(edge("B1", "B2"), edge("B1", "B3"))
                    )
                .add(edge("A2", "B2"))
                )
            ,
            List.of(
                graph("101")
                .nodedefs(attribute("shape", "box"))
                .nodedefs(attribute("width", "3"))
                .add(node("default").with(label("long line followed by\\nsmall line\\l")))
                .add(node("nojustify").with(nojustify(),label("long line followed by\\nsmall line\\l")))
                ,
                graph("102")
                .with(nslimit(0.02))
                .with(nslimit1(0.03))
                .add(edge("A", "B"))
                .add(edge("A", "C"))
                .add(edge("A", "D"))
                .add(edge("A", "E"))
                .add(edge("B", "C"))
                .add(edge("B", "D"))
                .add(edge("B", "E"))
                ,
                tmp = digraph("103+104")
                .with(label("no ordering"))
                .add(node("A"), node("B"), node("C"))
                .add(node("X"))
                .add(node("E"), node("E"), node("F"))
                .add(edge("B", "X").with(label("1")))
                .add(edge("A", "X").with(label("2")))
                .add(edge("C", "X").with(label("3")))
                .add(edge("X", "D").with(label("4")))
                .add(edge("X", "F").with(label("5")))
                .add(edge("X", "E").with(label("6")))
                ,
                tmp.copy()
                .with(label("ordering: IN"))
                .with(ordering(Ordering.IN))
                ,
                tmp.copy()
                .with(label("ordering: OUT"))
                .with(ordering(Ordering.OUT))
                )
            ,
            List.of(
                graph("111")
                .with(orientation())
                .add(node("A"), node("B"), node("C"))
                ,
                graph("112")
                .with(orientation(false))
                .add(node("A"), node("B"), node("C"))
                ,
                graph("113")
                .with(Engine.DOT)
                .with(orientation(false))
                .with(CharSet.UTF_8)
                .with(charset(CharSet.UTF_8))
                .nodedefs(attribute("shape", "house"))
                .nodedefs(orientation(30))
                .add(
                    node("A"), 
                    node("B").with(orientation(45)), 
                    node("C").with(orientation(90))
                    )
                ,
                tmp = graph("114+...")
                .add(subgraph()
                    .add(edge("A", "B"))
                    .add(edge("B", "C"))
                    )
                .add(subgraph()
                    .add(edge("D", "E"))
                    .add(edge("E", "F"))
                    )
                ,
                tmp.copy()
                .with(pack(true))
                ,
                tmp.copy()
                .with(pack(20))
                ,
                tmp.copy()
                .with(packmode(PackMode.ARRAY_C.count(3)))
                .with(packmode(PackMode.ARRAY.count(1)))
                .with(packmode(PackMode.ARRAY.align(Align.BOTTOM)))
                .with(packmode(PackMode.ARRAY_C.align(Align.TOP).align(Align.RIGHT).count(3)))
                .with(packmode(PackMode.ARRAY.count(1).align(Align.LEFT)))
                )
            ,
            List.of(
                tmp = graph("121-124")
                .add(edge("A", "B"), edge("B", "C"))
                ,
                tmp.copy()
                .with(pad(-0.2))
                ,
                tmp.copy()
                .with(pad(0.2))
                ,
                tmp.copy()
                .with(page(4.0))   // only Postscript
                .with(PageDir.RT)  // only Postscript
                ,
                graph("125")
                .add(subgraph()
                    .with(cluster())
                    .add(edge("A", "B"))
                    .add(edge("B", "C"))
                    )
                .add(subgraph()
                    .with(cluster())
                    .with(DARKORANGE.pen())
                    .with(penwidth(5.0))
                    .add(edge("D", "E"))
                    .add(edge("E", "F"))
                    )
                ,
                graph("126")
                .with(Engine.NEATO)
                .add(node("A").with(pos(point(1, 1))))
                .add(node("B").with(pos(point(2, 1))))
                .add(node("C").with(pos(point(3, 1))))
                ,
                graph("126")
                .with(Engine.NEATO)
                .nodedefs(pin(true))
                .add(node("A").with(pos(point(1, 1))))
                .add(node("B").with(pos(point(2, 1))))
                .add(node("C").with(pos(point(4, 1))))
                ,
                digraph("127")
                .with(Engine.NEATO)
                .nodedefs(pin(true))
                .add(node("A").with(pos(point(1, 1))))
                .add(node("B").with(pos(point(2, 1))))
                .add(node("C").with(pos(point(4, 1))))
                .add(edge("A", "B"))
                .add(edge("B", "C"))
                )
            // TODO ports
            );
    }
}
