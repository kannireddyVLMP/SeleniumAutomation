package com.Pearson.Screenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScreenshotCombiner {
    /**
     * Combines multiple PNG images vertically into a single PNG file.
     * @param imagePaths List of PNG file paths to combine (in order).
     * @param outputPath Path to save the combined PNG.
     * @throws IOException if reading or writing fails.
     */
    public static void combineImagesVertically(List<String> imagePaths, String outputPath) throws IOException {
        if (imagePaths == null || imagePaths.isEmpty()) return;
        BufferedImage[] images = new BufferedImage[imagePaths.size()];
        int totalWidth = 0;
        int totalHeight = 0;
        // Load images and calculate total dimensions
        for (int i = 0; i < imagePaths.size(); i++) {
            images[i] = ImageIO.read(new File(imagePaths.get(i)));
            totalWidth = Math.max(totalWidth, images[i].getWidth());
            totalHeight += images[i].getHeight();
        }
        // Create combined image
        BufferedImage combined = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        int y = 0;
        for (BufferedImage img : images) {
            g.drawImage(img, 0, y, null);
            y += img.getHeight();
        }
        g.dispose();
        // Save combined image
        ImageIO.write(combined, "PNG", new File(outputPath));
    }
}

