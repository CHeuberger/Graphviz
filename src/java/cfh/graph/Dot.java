package cfh.graph;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;

public final class Dot {
    
    public static final String VERSION = "1.1";
    
    public static enum Format {
        BMP,
        GIF,
        ICO,
        JPEG,
        PNG,
        SVG,
        TIFF;
        public String asParameter() { return name().toLowerCase(); }
    }

    static final String CMD;
    static {
        String cmd = System.getProperty("Graph");
        if (cmd == null) {
            String path = System.getenv("GRAPHVIZ_HOME");
            if (path == null) {
                cmd = "/usr/local/bin/dot";
                System.err.println("neither \"Graph\" nor \"GRAPHVIZ_HOME\" environmrnt variables set, using \"" + cmd + "\"");
            } else {
                cmd = path + "/bin/dot";
            }
        }
        CMD = cmd;
    }
    
    public static void dot(Format format, InputStream dotInput, OutputStream output) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(new String[] { CMD, "-T" + format.asParameter() });

        try (OutputStream processIn = process.getOutputStream()) {
            writeAll(dotInput, processIn);
        }

        try (InputStream processOut = process.getInputStream()) {
            writeAll(processOut, output);
        }

        int ret = process.waitFor();
        if (ret != 0) {
            writeAll(process.getErrorStream(), System.err);
            throw new RuntimeException("dot to " + format + " conversion failed, returned: " + ret);
        }
    }

    public static void dot(Format format, String dotInput, OutputStream output) throws IOException, InterruptedException {
        dot(format, new ByteArrayInputStream(dotInput.getBytes(StandardCharsets.UTF_8)), output);
    }
    
    public static void dotToSvg(InputStream dotInput, OutputStream svgOutput) throws IOException, InterruptedException {
        dot(Format.SVG, dotInput, svgOutput);
    }
    
    public static void dotToPng(InputStream dotInput, OutputStream pngOutput) throws IOException, InterruptedException {
        dot(Format.PNG, dotInput, pngOutput);
    }
    
    public static BufferedImage dotToImage(Format format, String dotInput) throws IOException, InterruptedException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        dot(format, dotInput, output);
        return ImageIO.read(new ByteArrayInputStream(output.toByteArray()));
    }
    
    private static void writeAll(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];

        int count;
        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }
    } 
}
