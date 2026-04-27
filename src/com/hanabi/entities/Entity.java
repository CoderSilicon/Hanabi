package src.com.hanabi.entities;

import java.awt.Graphics2D;

public abstract class Entity {
    protected double x, y, velX, velY;
    protected int screenWidth, screenHeight;

    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public abstract void update();
    public abstract void render(Graphics2D g2d);

    public double getX() { return x; }
    public double getY() { return y; }
}