/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;

import cfh.jgraphviz.Attr.GNES;

/**
 * @author Carlos F. Heuberger, 2023-10-16
 *
 */
public sealed interface ColorList permits Color, ColorListImpl {

    /** Add another color to the actual color (list). */
    public ColorList and(Color second);
    
    /** Add another color to the actual color and given fraction (0.0-1.0). */
    public ColorList split(Color second, double fraction);
    
    /** Add another color to the actual color, setting the fraction (0.0-1.0) for the previous last color. */
    public ColorList split(double fraction, Color second);

    /** Canvas background color. */
    public Attr.GS bgcolor();
    
    /**  Basic drawing color for graphics, not text. */
    public Attr.GNES color();
}

final class ColorListImpl implements ColorList {

    private static final double NO_FRACTION = -1;
    
    private final ColorImpl color;
    private double fraction;
    private ColorListImpl next;
    
    private ColorListImpl(ColorImpl color, double fraction) {
        this.color = requireNonNull(color, "null color");
        this.fraction = fraction;
        this.next = null;
    }
    
    ColorListImpl(ColorImpl first, ColorImpl second) {
        this.color = requireNonNull(first, "null first color");
        this.fraction = NO_FRACTION;
        this.next = new ColorListImpl(second, NO_FRACTION);
    }
    
    ColorListImpl(ColorImpl first, ColorImpl second, double fraction) {
        if (fraction < 0.0 || fraction > 1.0)
            throw new IllegalArgumentException("fraction must be between 0.0 and 1.0: " + fraction);
        this.color = requireNonNull(first, "null first color");
        this.fraction = NO_FRACTION;
        this.next = new ColorListImpl(second, fraction);
    }
    
    ColorListImpl(ColorImpl first, double fraction, ColorImpl second) {
        if (fraction < 0.0 || fraction > 1.0)
            throw new IllegalArgumentException("fraction must be between 0.0 and 1.0: " + fraction);
        this.color = requireNonNull(first, "null first color");
        this.fraction = fraction;
        this.next = new ColorListImpl(second, NO_FRACTION);
    }
    
    @Override
    public ColorList and(Color color) {
        append((ColorImpl) color, NO_FRACTION);
        return this;
    }
    
    @Override
    public ColorList split(Color color, double fraction) {
        if (fraction < 0.0 || fraction > 1.0)
            throw new IllegalArgumentException("fraction must be between 0.0 and 1.0: " + fraction);
        append((ColorImpl) color, fraction);
        return this;
    }
    
    @Override
    public ColorList split(double fraction, Color color) {
        if (fraction < 0.0 || fraction > 1.0)
            throw new IllegalArgumentException("fraction must be between 0.0 and 1.0: " + fraction);
        var prev = append((ColorImpl) color, NO_FRACTION);
        if (prev.fraction != NO_FRACTION)
            throw new IllegalArgumentException("fraction already set before: " + prev.fraction);
        prev.fraction = fraction;
        return this;
    }
    
    private ColorListImpl append(ColorImpl newColor, double newFraction) {
        var c = this;
        while (c.next != null) {
            c = c.next;
        }
        c.next = new ColorListImpl(newColor, newFraction);
        return c;
    }
    
    @Override
    public Attr.GS bgcolor() {
        return new Attribute("bgcolor", this);
    }
    
    @Override
    public GNES color() {
        return new Attribute("color", this);
    }
    
    @Override
    public String toString() {
        return "\"" + value() + "\"";
    }
    
    private String value() {
        var f = (fraction == NO_FRACTION) ? "" : ";%.2f".formatted(fraction);
        if (next == null)
            return color.value() + f;
        else
            return color.value() + f + ":" + next.value();
    }
}