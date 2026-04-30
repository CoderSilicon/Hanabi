package src.com.hanabi.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Enemy extends Entity {
    private static final double MAX_SPEED = 2.5;
    private static final double ATTRACTION_STRENGTH = 0.08;
    private static final double DRAG = 0.95;

    private boolean alive = true;
    private double targetX;
    private double targetY;

    public Enemy(double x, double y, int screenWidth, int screenHeight) {
        super(x, y, screenWidth, screenHeight);
        this.velX = (Math.random() - 0.5) * 3.0;
        this.velY = (Math.random() - 0.5) * 3.0;
    }

    public void setTarget(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public int getRadius() {
        return 12; // For collision detection
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    public void resetPosition() {
        this.x = Math.random() * screenWidth;
        this.y = Math.random() * screenHeight;
        this.velX = (Math.random() - 0.5) * 3.0;
        this.velY = (Math.random() - 0.5) * 3.0;
    }

    @Override
    public void update() {
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.hypot(dx, dy);

        if (distance > 0.1) {
            velX += (dx / distance) * ATTRACTION_STRENGTH;
            velY += (dy / distance) * ATTRACTION_STRENGTH;
        }

        double speed = Math.hypot(velX, velY);
        if (speed > MAX_SPEED) {
            velX = (velX / speed) * MAX_SPEED;
            velY = (velY / speed) * MAX_SPEED;
        }

        velX *= DRAG;
        velY *= DRAG;

        x += velX;
        y += velY;

        if (x < 0) {
            x = 0;
            velX *= -0.7;
        } else if (x > screenWidth) {
            x = screenWidth;
            velX *= -0.7;
        }

        if (y < 0) {
            y = 0;
            velY *= -0.7;
        } else if (y > screenHeight) {
            y = screenHeight;
            velY *= -0.7;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        int size = 20;
        int drawX = (int) x - size / 2;
        int drawY = (int) y - size / 2;

        g2d.setColor(Color.BLACK);
        g2d.fillOval(drawX, drawY, size, size);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new java.awt.BasicStroke(2f));
        g2d.drawOval(drawX, drawY, size, size);

        Font oldFont = g2d.getFont();
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Ö", drawX + 5, drawY + 16);
        g2d.setFont(oldFont);
    }
}
