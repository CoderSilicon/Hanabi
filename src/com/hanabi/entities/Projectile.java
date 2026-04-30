package src.com.hanabi.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Projectile extends Entity {
    private float hue = 0; // For the rainbow effect
    private boolean alive = true;
    private int lifeSpan = 200; // Frames until it disappears
    private double angle;
    private double trailX[], trailY[];
    private int trailIndex = 0;
    private double bounceFactor = 0.9;
    private int radius = 4;

    public Projectile(double x, double y, double angle, int screenWidth, int screenHeight) {
        super(x, y, screenWidth, screenHeight);
        this.angle = angle;

        // Projectile speed
        double speed = 3.0;
        this.velX = Math.cos(angle) * speed;
        this.velY = Math.sin(angle) * speed;

        // Initialize trail
        this.trailX = new double[15];
        this.trailY = new double[15];
    }

    public void update() {
        x += velX;
        y += velY;

        // Update trail
        trailX[trailIndex] = x;
        trailY[trailIndex] = y;
        trailIndex = (trailIndex + 1) % trailX.length;

        if (x < 0) {
            x = 0;
            velX *= -bounceFactor;
        } else if (x > screenWidth) {
            x = screenWidth;
            velX *= -bounceFactor;
        }

        // Check Top/Bottom walls
        if (y < 0) {
            y = 0;
            velY *= -bounceFactor;
        } else if (y > screenHeight) {
            y = screenHeight;
            velY *= -bounceFactor;
        }

        // Cycle the rainbow hue (0.0 to 1.0)
        hue += 0.01f;
        if (hue > 1.0f)
            hue = 0;

        lifeSpan--;
        if (lifeSpan <= 0)
            alive = false;
    }

    public void render(Graphics2D g2d) {
        // Draw trail effect
        for (int i = 0; i < trailX.length; i++) {
            int idx = (trailIndex + i) % trailX.length;
            if (trailX[idx] == 0 && trailY[idx] == 0)
                continue;

            float trailAlpha = (float) i / trailX.length;
            float trailHue = (hue + trailAlpha * 0.3f) % 1.0f;
            Color trailColor = Color.getHSBColor(trailHue, 0.8f, 0.6f);
            int alpha = (int) (200 * trailAlpha);

            g2d.setColor(new Color(trailColor.getRed(), trailColor.getGreen(),
                    trailColor.getBlue(), alpha));
            g2d.fillOval((int) trailX[idx] - 2, (int) trailY[idx] - 2, 4, 4);
        }

        // Draw main projectile body
        Color mainColor = Color.getHSBColor(hue, 1.0f, 1.0f);
        g2d.setColor(mainColor);
        g2d.fillOval((int) x - 4, (int) y - 4, 8, 8);

        // Draw bright glow/ring
        g2d.setColor(new Color(mainColor.getRed(), mainColor.getGreen(),
                mainColor.getBlue(), 100));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval((int) x - 6, (int) y - 6, 12, 12);

        // Draw direction indicator (spike)
        g2d.setColor(new Color(255, 255, 255, 150));
        int spikeX = (int) (x + Math.cos(angle) * 6);
        int spikeY = (int) (y + Math.sin(angle) * 6);
        g2d.drawLine((int) x, (int) y, spikeX, spikeY);
    }

    public boolean isAlive() {
        return alive;
    }

    public int getRadius() {
        return radius;
    }

    public void kill() {
        alive = false;
    }
}