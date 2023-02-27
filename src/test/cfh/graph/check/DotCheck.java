/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.check;

import static cfh.graph.Dot.*;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
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
        var panel = new JPanel();
        panel.add(createDirectedGraph());
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        
        frame.add(new JScrollPane(panel));
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private JComponent createDirectedGraph() {
        var image = graph("demo")
            .directed()
            .strict()
            .with( Color.ORANGE.gradient(Color.LIGHTBLUE3).background() )
            .add( comment("====================") )
            .add( edge(node("A"), node("B")).with(attr("taillabel", "TAIL"), attr("label", "LABEL")) )
            .add( node("B").to(node("C")) )
            .visit( System.out::println )
            .toImage(Format.JPG);
        return new JScrollPane(new JLabel(new ImageIcon(image)));
    }
}
