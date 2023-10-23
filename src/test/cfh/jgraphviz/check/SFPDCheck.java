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
 * @author Carlos F. Heuberger, 2023-10-23
 *
 */
public class SFPDCheck {

    private static final Engine ENGINE = Engine.SFDP;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SFPDCheck::new);
    }

    //==============================================================================================

    private final JFrame frame;

    private SFPDCheck() {
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

        return List.of(
            List.of(
                // default graph
                graph()
                .with(label_scheme(LabelScheme.NO_EFFECT), label_scheme(LabelScheme.NEIGHBOR))
                .add(node("B").with(GREEN))
                .add(node("A").to(node("B")))
                )
            );
    }
}
