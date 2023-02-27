/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.check;

import static cfh.graph.Dot.*;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import cfh.graph.attr.Color;

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
        frame = new JFrame();
        
        try {
            frame.add(createGraph());
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return;
        }
        frame.pack();
        
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private JComponent createGraph() throws IOException, InterruptedException {
        var image = graph("demo")
            .directed(true)
            .strict(false)
            .with( Color.DARKBLUE.gradient(Color.LIGHTBLUE).background() )
            .nodes( font("Corrier"), font(14) )
            .nodes( attr("style", "filled"), Color.YELLOW.fill() )
            .edges( attr("decorate", true) )
            .add( node("C").with(Color.RED) )
            .add( comment("====================") )
            .add( edge(node("A"), node("B")).with("taillabel", "TAIL").with("label", "LABEL") )
            .add( node("B").to(node("C")) )
            .visit( System.out::println )
            .toImage(Format.JPG);
        return new JScrollPane(new JLabel(new ImageIcon(image)));
    }
}
