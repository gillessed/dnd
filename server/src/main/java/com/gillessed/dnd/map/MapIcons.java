package com.gillessed.dnd.map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class generates a png with some icons for the map. Otherwise, it's useless.
 */
public class MapIcons extends JFrame {
    private final MyPanel myPanel;

    public MapIcons() throws IOException {
        setTitle("Test");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        myPanel = new MyPanel();
        getContentPane().add(myPanel);

        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        myPanel.paintComponent(image.getGraphics());
        ImageIO.write(image, "png", new File("/Users/gcole/sandbox/icons.png"));
    }

    public static void main(String args[]) throws IOException {
        MapIcons mapIcons = new MapIcons();

        /*
         * Uncomment these to actually see the rendered image in a jframe.
         */
//        mapIcons.pack();
//        mapIcons.setVisible(true);
    }
}

class MyPanel extends JPanel {
    public MyPanel() {
        setPreferredSize(new Dimension(300, 300));
    }

    @Override
    protected void paintComponent(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());

        //City
        g.setColor(Color.white);
        g.fillOval(48, 48, 14, 14);
        g.setColor(Color.black);
        g.fillOval(50, 50, 10, 10);

        //Town
        g.setColor(Color.white);
        g.fillRect(100, 50, 10, 10);
        g.setColor(Color.black);
        g.fillRect(101, 51, 8, 8);

        //Point of Interest
        g.setColor(Color.white);
        g.fillPolygon(
                new int[] {44, 56, 50},
                new int[] {106, 106, 92},
                3);
        g.setColor(Color.black);
        g.fillPolygon(
                new int[] {46, 54, 50},
                new int[] {104, 104, 95},
                3);

        //Ruins
        g.setColor(Color.white);
        g.fillRect(99, 99, 12, 12);
        g.setColor(Color.black);
        g.fillRect(100, 100, 10, 10);
        g.setColor(Color.white);
        g.fillRect(101, 101, 8, 8);
        g.setColor(Color.black);
        g.drawLine(100, 100, 109, 109);
        g.drawLine(100, 109, 109, 100);
    }
}
