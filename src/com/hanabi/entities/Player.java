package src.com.hanabi.entities;

import src.com.hanabi.input.KeyHandler;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class Player {
    private double x, y, velX, velY, angle;
    private double thrustPower = 0.15;
    private final double ROTATION_SPEED = 0.07;
    private final double DRAG = 0.98;
    private final double bounceRestitution = 0.7;

    private KeyHandler keyHandler;
    private int screenWidth, screenHeight;

    public Player(int startX, int startY, KeyHandler keyHandler, int width, int height) {
        this.x = startX;
        this.y = startY;
        this.angle = -Math.PI / 2;
        this.keyHandler = keyHandler;
        this.screenWidth = width;
        this.screenHeight = height;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public void update() {
        if (keyHandler.leftPressed)
            angle -= ROTATION_SPEED;
        if (keyHandler.rightPressed)
            angle += ROTATION_SPEED;

        if (keyHandler.upPressed) {
            thrustPower += 0.005;
            velX += Math.cos(angle) * thrustPower;
            velY += Math.sin(angle) * thrustPower;
        } else {
            thrustPower = 0.15;
        }

        if(keyHandler.downPressed) {
            velX *= 0.95; // Apply braking force
            velY *= 0.95;
            thrustPower = 0.15; // Reset thrust power when braking
            thrustPower -= 0.005; // Allow thrust to build up again after braking
        }

        velX *= DRAG;
        velY *= DRAG;
        x += velX;
        y += velY;

        // Check Left/Right walls
        if (x < 0) {
            x = 0; // Keep it inside the screen
            velX *= -bounceRestitution; // Flip horizontal direction
        } else if (x > screenWidth) {
            x = screenWidth;
            velX *= -bounceRestitution;
        }

        // Check Top/Bottom walls
        if (y < 0) {
            y = 0;
            velY *= -bounceRestitution; // Flip vertical direction
        } else if (y > screenHeight) {
            y = screenHeight;
            velY *= -bounceRestitution;
        }
    }

    public void render(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(angle);

        // (Insert your drawing code for the Rocket and Flame here)
        GeneralPath rocketShape = new GeneralPath();
        rocketShape.moveTo(14, 0);
        rocketShape.curveTo(8, -4, 4, -8, -8, -10);
        rocketShape.curveTo(-12, -8, -14, -2, -12, 0);
        rocketShape.curveTo(-14, 2, -12, 8, -8, 10);
        rocketShape.curveTo(4, 8, 8, 4, 14, 0);
        rocketShape.closePath();

        if (keyHandler.upPressed) {
            g2d.setColor(Color.CYAN);
            GeneralPath flame = new GeneralPath();
            flame.moveTo(-12, 0);
            flame.lineTo(-18, -3);
            flame.lineTo(-22, 0);
            flame.lineTo(-18, 3);
            flame.closePath();
            g2d.fill(flame);
        }

        if(keyHandler.downPressed) {
            // Left brake flame
            g2d.setColor(Color.ORANGE);
            GeneralPath brakeFlameLeft = new GeneralPath();
            brakeFlameLeft.moveTo(0, -8);
            brakeFlameLeft.lineTo(-5, -12);
            brakeFlameLeft.lineTo(-2, -15);
            brakeFlameLeft.lineTo(2, -10);
            brakeFlameLeft.closePath();
            g2d.fill(brakeFlameLeft);
            
            // Right brake flame
            g2d.setColor(Color.RED);
            GeneralPath brakeFlameRight = new GeneralPath();
            brakeFlameRight.moveTo(0, 8);
            brakeFlameRight.lineTo(-5, 12);
            brakeFlameRight.lineTo(-2, 15);
            brakeFlameRight.lineTo(2, 10);
            brakeFlameRight.closePath();
            g2d.fill(brakeFlameRight);
        }

        g2d.setColor(Color.WHITE);
        g2d.draw(rocketShape);

        g2d.setTransform(oldTransform);
    }
}