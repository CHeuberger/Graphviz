/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Carlos F. Heuberger, 2023-10-05
 *
 * A custom Graphviz language describing ellipses, polygons, polylines, beziers, text, images, colors, gradients, fonts, and styles. </P>
 * <table>
 * <tr><td><code>E x y w h</code></td><td>Filled ellipse</td></tr>
 * <tr><td><code>e x y w h</code></td><td>Unfilled ellipse</td></tr>
 * <tr><td><code>P n x₁ y₁ ... xₙ yₙ</code></td><td>Filled polygon</td></tr>
 * <tr><td><code>p n x₁ y₁ ... xₙ yₙ</code></td><td>Unfilled polygon</td></tr>
 * <tr><td><code>L n x₁ y₁ ... xₙ yₙ</code></td><td>Polyline</td></tr>
 * <tr><td><code>B n x₁ y₁ ... xₙ yₙ</code></td><td>B-spline</td></tr>
 * <tr><td><code>b n x₁ y₁ ... xₙ yₙ</code></td><td>Filled B-spline</td></tr>
 * <tr><td><code>T&nbsp;x&nbsp;y&nbsp;j&nbsp;w&nbsp;n&nbsp;-b₁b₂...bₙ</td><td>Text at <code>x,y</code>; <code>n</code> bytes; left-, centerd, right-aligned if<code>j=-1, 0, 1</code>; <code>w</code> computed width</td></tr>
 * <tr><td><code>t f</td><td>Font characteristics (1:bold, 2:italic, 4:underline, 8:superscript, 16:subscript, 32:striked, 64:overline</td></tr>
 * <tr><td><code>C n -b₁b₂...bₙ</td><td>Fill color <code>n</code> bytes</td></tr>
 * <tr><td><code>c s n -b₁b₂...bₙ</td><td>Pen color <code>n</code> bytes</td></tr>
 * <tr><td><code>F s n -b₁b₂...bₙ</td>Font: size <code>s</code> bytes, name <code>n</code> bytes<td></td></tr>
 * <tr><td><code>S n -b₁b₂...bₙ</td><td>Style as in {@link Style}</td></tr>
 * <tr><td><code>I&nbsp;x&nbsp;y&nbsp;w&nbsp;h&nbsp;n&nbsp;-b₁b₂...bₙ</td><td>External image, name <code>n</code> bytes</td></tr>
 * </table>
 */
public sealed interface XDot {
    //
}

/**
 * @author Carlos F. Heuberger, 2023-10-05
 *
 */
final class XDotImpl implements XDot, Valuable {
    
    private final List<String> components; // TODO change to List<Component>?

    XDotImpl(String... components) {
        this.components = Collections.unmodifiableList( new ArrayList<>( Arrays.asList(components) ) );
    }
    
    @Override
    public String value() {
        return '"' + String.join(" ", components) + '"';
    }
}
