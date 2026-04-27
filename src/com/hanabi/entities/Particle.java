package src.com.hanabi.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Particle extends Entity {
    private float alpha = 1.0f; // Transparency (1.0 = solid, 0.0 = invisible)
    private Color color;
    private double fadeSpeed;

    public Particle(double x, double y, Color color) {
        super(x, y, 1280, 800); // Pass screen bounds
        this.color = color;
        
        Random rand = new Random();
        // Give it a random burst direction
        double angle = rand.nextDouble() * Math.PI * 2;
        double speed = rand.nextDouble() * 4 + 1;
        this.velX = Math.cos(angle) * speed;
        this.velY = Math.sin(angle) * speed;
        this.fadeSpeed = rand.nextDouble() * 0.02 + 0.01;
    }

    @Override
    public void update() {
        x += velX;
        y += velY;
        velY += 0.05; // Gravity pull
        
        alpha -= fadeSpeed; // Fade out over time
        if (alpha < 0) alpha = 0;
    }

    @Override
    public void render(Graphics2D g2d) {
        // Set transparency based on alpha
        g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(color);
        g2d.fillOval((int)x, (int)y, 3, 3);
        // Reset composite so other things aren't transparent
        g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
    }

    public boolean isDead() { return alpha <= 0; }
}
