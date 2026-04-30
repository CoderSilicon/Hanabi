package src.com.hanabi.scenes;

import src.com.hanabi.main.GamePanel;
import src.com.hanabi.input.KeyHandler;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GameOverScene implements Scene {
    private final GamePanel panel;
    private final KeyHandler keyHandler;
    private final int score;

    public GameOverScene(GamePanel panel, KeyHandler keyHandler, int score) {
        this.panel = panel;
        this.keyHandler = keyHandler;
        this.score = score;
    }

    @Override
    public void update() {
        if (keyHandler.spacePressed) {
            panel.setScene(new GameScene(panel, keyHandler));
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        int width = panel.getWidth();
        int height = panel.getHeight();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 60));
        String title = "GAME OVER";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, height / 2 - 60);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 24));
        String message = "Press SPACE to try again";
        int messageWidth = g2d.getFontMetrics().stringWidth(message);
        g2d.drawString(message, (width - messageWidth) / 2, height / 2);

        String scoreText = "Score: " + score;
        int scoreWidth = g2d.getFontMetrics().stringWidth(scoreText);
        g2d.drawString(scoreText, (width - scoreWidth) / 2, height / 2 + 40);
    }
}
