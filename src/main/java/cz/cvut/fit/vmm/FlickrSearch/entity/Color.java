package cz.cvut.fit.vmm.FlickrSearch.entity;

import java.io.Serializable;

/**
 * Created by jan on 02.11.2016.
 */
public class Color implements Serializable {

    private int red, green, blue;
    private double L, a, b;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;

        countLab();
    }

    public Color(String hexColor) {
        red = Integer.parseInt(hexColor.substring(1, 3), 16);
        green = Integer.parseInt(hexColor.substring(3, 5), 16);
        blue = Integer.parseInt(hexColor.substring(5, 7), 16);

        countLab();
    }

    public double distanceTo(Color color) {
        double L2 = Math.pow(L - color.L, 2);
        double a2 = Math.pow(a - color.a, 2);
        double b2 = Math.pow(b - color.b, 2);

        return Math.sqrt(L2 + a2 + b2);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public double getL() {
        return L;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    private void countLab() {
        double r = red / 255.0;
        double g = green / 255.0;
        double b = blue / 255.0;

        if (r > 0.04045)
            r = Math.pow((r + 0.055) / 1.055, 2.4);
        else
            r = r / 12.92;

        if (g > 0.04045)
            g = Math.pow((g + 0.055 ) / 1.055, 2.4);
        else
            g = g / 12.92;

        if (b > 0.04045)
            b = Math.pow((b + 0.055) / 1.055, 2.4);
        else
            b = b / 12.92;

        r *= 100;
        g *= 100;
        b *= 100;

        double x = r * 0.4124 + g * 0.3576 + b * 0.1805;
        double y = r * 0.2126 + g * 0.7152 + b * 0.0722;
        double z = r * 0.0193 + g * 0.1192 + b * 0.9505;

        x /= 95.047;
        y /= 100.000;
        z /= 108.883;

        if (x > 0.008856)
            x = Math.pow(x, 1.0 / 3.0);
        else
            x = (7.787 * x) + (16.0 / 116.0);

        if (y > 0.008856 )
            y = Math.pow(y, 1.0 / 3.0);
        else
            y = (7.787 * y) + (16.0 / 116.0);

        if (z > 0.008856)
            z = Math.pow(z, 1.0 / 3.0);
        else
            z = (7.787 * z) + (16.0 / 116.0);

        this.L = (116 * y) - 16;
        this.a = 500 * (x - y);
        this.b = 200 * (y - z);
    }
}
