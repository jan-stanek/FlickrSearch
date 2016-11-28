package cz.cvut.fit.vmm.FlickrSearch.entity;

import cz.cvut.fit.vmm.FlickrSearch.business.Metric;

import java.io.Serializable;

/**
 * Created by jan on 02.11.2016.
 */
public class Color implements Serializable {

    private int red, green, blue;
    private double L, c, h, a, b;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;

        countLch();
    }

    public Color(String hexColor) {
        red = Integer.parseInt(hexColor.substring(1, 3), 16);
        green = Integer.parseInt(hexColor.substring(3, 5), 16);
        blue = Integer.parseInt(hexColor.substring(5, 7), 16);

        countLch();
    }

    public double distanceTo(Color color, Metric type) {
        if (type == Metric.CIE2000) {
            double kL = 1;
            double kC = 1;
            double kH = 1;

            double Cavg = (c + color.c) / 2;
            double Cavg_7 = Math.pow(Cavg, 7);

            double pow25_7 = Math.pow(25, 7);

            double G = 0.5 * (1 - Math.sqrt(Cavg_7 / (Cavg_7 + pow25_7)));
            double a1_ = (1 + G) * a;
            double a2_ = (1 + G) * color.a;

            double C1_ = Math.sqrt(a1_ * a1_ + b * b);
            double C2_ = Math.sqrt(a2_ * a2_ + color.b * color.b);

            double h1_ = (Math.atan2(b, a1_) * 180 / Math.PI + 360) % 360.0;
            double h2_ = (Math.atan2(color.b, a2_) * 180 / Math.PI + 360) % 360.0;

            double dL_ = color.L - L;
            double dC_ = C2_ - C1_;

            double dh_abs = Math.abs(h1_ - h2_);
            double dh_;

            if (C1_ == 0 || C2_ == 0)
                dh_ = 0;
            else {
                if (dh_abs <= 180)
                    dh_ = h2_ - h1_;
                else if (dh_abs > 180 && h2_ <= h1_)
                    dh_ = h2_ - h1_ + 360.0;
                else
                    dh_ = h2_ - h1_ - 360.0;
            }

            double dH_ = 2 * Math.sqrt(C1_ * C2_) * Math.sin(dh_ * Math.PI / 360);

            double Lavg = (L + color.L) / 2;
            double C_avg = (C1_ + C2_) / 2;

            double H_avg;
            if (C1_ == 0 || C2_ == 0)
                H_avg = 0;
            else {
                if (dh_abs <= 180d)
                    H_avg = (h1_ + h2_) / 2;
                else if (dh_abs > 180d && h1_ + h2_ < 360)
                    H_avg = (h1_ + h2_ + 360) / 2;
                else
                    H_avg = (h1_ + h2_ - 360) / 2;
            }

            double T = 1
                    - 0.17 * Math.cos((H_avg - 30) / (180 / Math.PI))
                    + 0.24 * Math.cos((H_avg * 2) / (180 / Math.PI))
                    + 0.32 * Math.cos((H_avg * 3 + 6) / (180 / Math.PI))
                    - 0.2 * Math.cos((H_avg * 4 - 63) / (180 / Math.PI));

            double SL = 1 + ((0.015 * Math.pow(Lavg - 50, 2)) / Math.sqrt(20 + Math.pow(Lavg - 50, 2)));
            double SC = 1 + 0.045 * C_avg;
            double SH = 1 + 0.015 * T * C_avg;

            double C_avg_7 = Math.pow(C_avg, 7);

            double RT = -2 * Math.sqrt(C_avg_7 / (C_avg_7 + pow25_7)) * Math.sin((60 * Math.exp(-Math.pow((H_avg - 275) / 25, 2))) / (180 / Math.PI));

            return Math.sqrt(
                    Math.pow(dL_ / (kL * SL), 2) +
                            Math.pow(dC_ / (kC * SC), 2) +
                            Math.pow(dH_ / (kH * SH), 2) +
                            RT * (dC_ / (kC * SC)) * (dH_ / (kH * SH))
            );
        }
        else if (type == Metric.CIE76) {
            return Math.sqrt(
                    Math.pow(color.L - L, 2) +
                            Math.pow(color.a - a, 2) +
                            Math.pow(color.b - b, 2));
        }
        else if (type == Metric.RGB) {
            return Math.sqrt(
                    Math.pow(color.red - red, 2) +
                            Math.pow(color.green - green, 2) +
                            Math.pow(color.blue - blue, 2));
        }
        return 0;
    }

    private void countLch() {
        double red = this.red / 255.0;
        double green = this.green / 255.0;
        double blue = this.blue / 255.0;

        if (red > 0.04045)
            red = Math.pow((red + 0.055) / 1.055, 2.4);
        else
            red = red / 12.92;

        if (green > 0.04045)
            green = Math.pow((green + 0.055 ) / 1.055, 2.4);
        else
            green = green / 12.92;

        if (blue > 0.04045)
            blue = Math.pow((blue + 0.055) / 1.055, 2.4);
        else
            blue = blue / 12.92;

        red *= 100;
        green *= 100;
        blue *= 100;

        double x = red * 0.4124 + green * 0.3576 + blue * 0.1805;
        double y = red * 0.2126 + green * 0.7152 + blue * 0.0722;
        double z = red * 0.0193 + green * 0.1192 + blue * 0.9505;

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

        a = 500 * (x - y);
        b = 200 * (y - z);

        this.c = Math.sqrt(a*a + b*b);

        this.h = (Math.atan2(b, a) * (180 / Math.PI) + 360) % 360.0;
    }
}
