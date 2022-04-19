package se.cha;

import lombok.Data;

@Data
public class PixelData {
    String imageGroup;
    String imageName;
    int imageWidth;
    int imageHeight;
    double progress;
    int x;
    int y;
    int pixelWidth;
    int pixelHeight;
    int[] color;
}
