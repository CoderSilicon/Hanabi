package src.com.hanabi.main;

import src.com.hanabi.entities.Particle;
import src.com.hanabi.entities.Player;
import src.com.hanabi.entities.Projectile;
import src.com.hanabi.input.KeyHandler;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends Canvas implements Runnable {
    private boolean running = false;
    private Thread gameThread;

    private final int WIDTH = 1280;
    private final int HEIGHT = 800;

    private KeyHandler keyHandler = new KeyHandler();
    private Player player;
    private List<Projectile> projectiles = new ArrayList<>();
    // Top of your class
    private java.util.List<Particle> particles = new java.util.ArrayList<>();


    public GamePanel() {
        setSize(WIDTH, HEIGHT);
        setBackground(Color.BLACK);
        addKeyListener(keyHandler);

        // Initialize Player in the center
        player = new Player(WIDTH / 2, HEIGHT / 2, keyHandler, WIDTH, HEIGHT);
    }

    public synchronized void start() {
        if (running)
            return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Fixed timestep game loop setup
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                update(); // Calculate physics
                delta--;
            }
            render(); // Draw to screen
        }
        stop();
    }

    private void update() {
        player.update();
        if (keyHandler.spacePressed) {
            // You might want a "cooldown" timer here so it doesn't shoot 60 bullets a
            // second
            projectiles.add(new Projectile(player.getX(), player.getY(), player.getAngle()));
        }

        // Update and clean up dead projectiles
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            p.update();
            if (!p.isAlive()) {
                projectiles.remove(i);
                i--;
            }
        }

        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.update();
            if (p.isDead()) {
                particles.remove(i);
                i--;
            }
        }
        // Later add: bulletManager.update(), particleManager.update()
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clear screen
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw Player
        player.render(g2d);

        player.render(g2d);

        for (Projectile p : projectiles) {
            p.render(g2d);
        }

        g2d.dispose();
        bs.show();
    }
}
