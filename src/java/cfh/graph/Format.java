/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public enum Format {
    // TODO enable all formats
    BMP,
    // CGImage,
    // CANON, DOT, XDOT, XDOT1$2, XDOT1$4
    EPS,
    // EXR,
    // FIG,
    // GD, GD2,
    GIF,
    // GTK,
    ICO,
    // IMAP, IMAP_NP, ISMAP, CMAP, CMAPX, CMAPX_NP,
    JPG, JPEG,
    // JP2,
    // JSON, JSON0, DOT_JSON, XDOT_JSON,
    // PDF,
    // PIC,
    // PCT, PICT,
    // PLAIN, PLAIN-EXT,
    PNG,
    // POV,
    // PS,
    // PS2,
    // PSD,
    // SGI,
    SVG, SVGZ,
    // TGA,
    TIF, TIFF,
    // TK,
    // VML, VMLZ,
    // VRML,
    // WBMP,
    // WEBP,
    // XLIB, X11,
    ;
    public String asParameter() { return name().toLowerCase().replace("$", "."); }
}
