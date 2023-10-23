/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz.check;

import static cfh.jgraphviz.Dot.*;
import static cfh.jgraphviz.Color.X11.*;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author Carlos F. Heuberger, 2023-10-18
 *
 */
public class SVGCheck {

    private static final Engine ENGINE = Engine.DOT;

    public static void main(String[] args) {
        new SVGCheck();
    }

    //==============================================================================================

    private SVGCheck() {

        try {
            var file = File.createTempFile("test", ".svg");

            var graph =
                graph()
                .with(fontnames(FontNames.HD), fontname("Times-Roman"))
                .nodedefs(fontname("Times-Roman"))
                .add(node("A").to(node("B")).with(
                    headlabel("test"), 
                    headURL("http://example.com/"), 
                    headtarget("main"),
                    headtooltip("The tooltip here")))
                ;
            try (var out = new FileOutputStream(file)) {
                Consumer<String> dotToSVG = dot ->  {
                    try {
                        dot(ENGINE, Format.SVG, dot, out);
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                };
                graph.visit(System.out::println).visit(dotToSVG);
            }
            Desktop.getDesktop().browse(file.toURI());
            try (var in = new BufferedReader(new FileReader(file))) {
                in.lines().forEach(System.out::println);
            }
            Thread.sleep(1000);
            file.delete();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }
}
