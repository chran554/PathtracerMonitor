package se.cha.frame;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Color {

    public static final Color BLACK = new Color(0.0, 0.0, 0.0);
    public static final Color WHITE = new Color(1.0, 1.0, 1.0);

    public static final Color GRAY_DARK = new Color(0.25, 0.25, 0.25);
    public static final Color GRAY = new Color(0.5, 0.5, 0.5);
    public static final Color GRAY_LIGHT = new Color(0.75, 0.75, 0.75);

    public static final Color RED = new Color(1.0, 0.0, 0.0);
    public static final Color GREEN = new Color(0.0, 1.0, 0.0);
    public static final Color BLUE = new Color(0.0, 0.0, 1.0);

    public static final Color YELLOW = new Color(1.0, 1.0, 0.0);
    public static final Color CYAN = new Color(0.0, 1.0, 1.0);
    public static final Color PURPLE = new Color(1.0, 0.0, 1.0);

    public double red;
    public double green;
    public double blue;

    public Color(int[] rgb) {
        this(rgb[0], rgb[1], rgb[2]);
    }

    public Color(int intColor) {
        red = ((intColor & 0x00FF0000) >>> 16) / 255.0;
        green = ((intColor & 0x0000FF00) >>> 8) / 255.0;
        blue = ((intColor & 0x000000FF) >>> 0) / 255.0;
    }

    public Color(int r, int g, int b) {
        red = r / 255.0;
        green = g / 255.0;
        blue = b / 255.0;
    }

    public Color(double intensity) {
        red = intensity;
        green = intensity;
        blue = intensity;
    }

    public Color(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color() {
    }


    private static double clamp(final double n, final double min, final double max) {
        return (n < min) ? min : Math.min(n, max);
    }

    public static int getIntColor(final double red, final double green, final double blue) {
        return new java.awt.Color(
                (float) clamp(red, 0.0, 1.0),
                (float) clamp(green, 0.0, 1.0),
                (float) clamp(blue, 0.0, 1.0)
        ).getRGB();
    }

    public static int getIntColor(final double intensity) {
        return getIntColor(intensity, intensity, intensity);
    }

    public int getIntColor() {
        return getIntColor(red, green, blue);
    }

    public int getIntRed() {
        return (getIntColor() & 0x00FF0000) >> 16;
    }

    public int getIntGreen() {
        return (getIntColor() & 0x0000FF00) >> 8;
    }

    public int getIntBlue() {
        return (getIntColor() & 0x000000FF) >> 0;
    }

    public Color fraction(double fraction) {
        return new Color(red * fraction, green * fraction, blue * fraction);
    }

    public Color copy() {
        return new Color(red, green, blue);
    }

    public Color getMonochromeColor() {
        final double averageIntensity = (red + green + blue) / 3.0;
        return new Color(averageIntensity);
    }

    public static Color add(Color color1, Color color2) {
        return new Color(color1.red + color2.red, color1.green + color2.green, color1.blue + color2.blue);
    }

    public void add(Color addition) {
        red += addition.red;
        green += addition.green;
        blue += addition.blue;
    }

    public boolean isBlack() {
        return (red <= 0.0) && (green <= 0.0) && (blue <= 0.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Color color = (Color) o;

        if (Double.compare(color.blue, blue) != 0) return false;
        if (Double.compare(color.green, green) != 0) return false;
        if (Double.compare(color.red, red) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = red != +0.0d ? Double.doubleToLongBits(red) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = green != +0.0d ? Double.doubleToLongBits(green) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = blue != +0.0d ? Double.doubleToLongBits(blue) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final DecimalFormat format = new DecimalFormat("#0.000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        return "(rgb " + format.format(red) + ", " + format.format(green) + ", " + format.format(blue) + ")";
    }

    public Color fadedTo(Color color, double factor) {
        final double r = red * (1.0 - factor) + color.red * (factor);
        final double g = green * (1.0 - factor) + color.green * (factor);
        final double b = blue * (1.0 - factor) + color.blue * (factor);
        return new Color(r, g, b);
    }
}
