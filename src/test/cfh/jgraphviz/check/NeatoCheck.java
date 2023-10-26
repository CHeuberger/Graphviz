/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz.check;

import static cfh.jgraphviz.Dot.*;
import static cfh.jgraphviz.Color.*;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
import cfh.jgraphviz.Dot.Engine;

/**
 * @author Carlos F. Heuberger, 2023-10-18
 *
 */
public class NeatoCheck {

    private static final Engine ENGINE = Engine.NEATO;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NeatoCheck::new);
    }

    //==============================================================================================

    private final JFrame frame;

    private NeatoCheck() {
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
                graph("10")
                .add(node("B").with(X11.GREEN))
                .add(node("A").to(node("B")))
                ,
                // directed graph
                graph()
                .directed()
                .add(node("A").to(node("B")))
                .add(node("A").to(node("B")))
                ,
                digraph()
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")))
                .add(edge(node("A"), node("D")))
                .add(edge(node("A"), node("E")))
                .add(edge(node("E"), node("F")))
                ,
                digraph()
                .with(epsilon(0.2))
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")))
                .add(edge(node("A"), node("D")))
                .add(edge(node("A"), node("E")))
                .add(edge(node("E"), node("F")))
                ,
                digraph()
                .add(node("A").with(attribute("shape", "polygon"), attribute("sides", 4)))
                .add(node("B").with(attribute("shape", "polygon"), attribute("sides", 5)))
                .add(edge(node("A"), node("B")))
                ,
                digraph()
                .with(esep(add(2, 5)))
                .add(node("A").with(attribute("shape", "polygon"), attribute("sides", 4)))
                .add(node("B").with(attribute("shape", "polygon"), attribute("sides", 5)))
                .add(edge(node("A"), node("B")))
                ,
                graph()
                .nodedefs(fixedsize(FixedSize.FALSE), attribute("width", 1.5))
                .add(node("A").with(label("false: A wide label to test fixedsize")))
                .add(node("B").with(label("true:  A wide label to test fixedsize"), fixedsize(true)))
                .add(node("C").with(label("shape: A wide label to test fixedsize"), fixedsize(FixedSize.SHAPE)))
                )
            ,
            List.of(
                digraph("20")
                .with(fontname("Symbol"), label("Font Symbol"), fontnames(FontNames.HD))
                .nodedefs(X11.RED.font())
                .edgedefs(X11.BLUE.font())
                .add(node("A").to(node("B")).with(label(" test")))
                ,
                graph()
                .nodedefs(attribute("style", "filled"), X11.RED.split(0.3, X11.BLUE))
                .add(node("A").with(gradientangle(0)))
                .add(node("B").with(gradientangle(135)))
                ,
                graph()
                .add(node("A").with(attribute("pos", point(2, 1))))
                .add(node("B").with(attribute("pos", point(1, 2))))
                .add(edge(node("A"), node("B")))
                ,
                graph()
                .add(node("A").with(attribute("pos", point(1, 1))))
                .add(node("B").with(attribute("pos", point(2, 2))))
                .add(edge(node("A"), node("B")))
                ,
                graph()
                .with(K(2))
                .with(maxiter(0))
                .add(subgraph().add(edge(node("A"), node("B"))))
                .add(subgraph().add(edge(node("C"), node("D"))))
                ,
                graph()
                .with(layout(Engine.CIRCO), mindist(2))
                .add(subgraph().add(edge(node("A"), node("B"))))
                .add(subgraph().add(edge(node("C"), node("D"))))
                ,
                graph()
                .with(layout(Engine.SFDP), mode(Mode.MAXENT))
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")))
                .add(edge(node("A"), node("D")))
                .add(edge(node("A"), node("E")).with(len(2)))
                .add(edge(node("C"), node("F")))
                .add(edge(node("E"), node("F")))
                ),
            List.of(
                graph("30")
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")))
                .add(edge(node("A"), node("D")))
                .add(edge(node("A"), node("E")).with(len(2)))
                .add(edge(node("C"), node("F")))
                .add(edge(node("E"), node("F")))
                ,
                graph("31")
                .with(model(Model.MDS))
                .edgedefs(len(0.8))
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")))
                .add(edge(node("A"), node("D")))
                .add(edge(node("A"), node("E")))
                .add(edge(node("C"), node("F")).with(len(1.5)))
                .add(edge(node("E"), node("F")))
                ,
                graph("32")
                .with(layout(Engine.CIRCO))
                .with(normalize(30.0))
                .add(edge("A", "B"))
                .add(edge("A", "C"))
                .add(edge("A", "D"))
                .add(edge("A", "E"))
                )
            ,
            List.of( 
                tmp = digraph("33+34")
                .with(layout(Engine.CIRCO))
                .add(edge("A1", "B1"))
                .add(edge("B1", "C1"))
                .add(edge("C1", "A1"))
                .add(edge("C1", "A2"))
                .add(edge("A2", "B2"))
                .add(edge("B2", "C2"))
                .add(edge("C2", "A2"))
                ,
                tmp.copy()
                .with(oneblock())
                ,
                tmp = digraph("35+36")
                .with(epsilon(0.8))
                .nodedefs(attribute("style", "filled"))
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")))
                .add(edge(node("A"), node("D")))
                .add(edge(node("A"), node("E")))
                .add(edge(node("E"), node("F")))
                ,
                tmp.copy().with(outputorder(OutputMode.EDGES_FIRST))
                )
            ,
            List.of( 
                tmp = graph("41")
                .with(epsilon(0.2))
                .add(edge(node("A"), node("B")))
                .add(edge(node("A"), node("C")))
                .add(edge(node("A"), node("D")))
                .add(edge(node("A"), node("E")))
                .add(edge(node("E"), node("F")))
                ,
                tmp.copy()
                .with(overlap(false))
                ,
                tmp.copy()
                .with(overlap(Overlap.SCALEXY))
                )
            );
    }
}
