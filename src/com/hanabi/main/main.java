package src.com.hanabi.main;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.MediaTracker;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hanabi :: 花火");
        GamePanel game = new GamePanel();

        ImageIcon windowIcon = new ImageIcon("assets/icon.png");
        if (windowIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
            System.out.println("Could not find the icon at: assets/icon.png");
        } else {
            frame.setIconImage(windowIcon.getImage());
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false); // Often safer for physics limits
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }
}