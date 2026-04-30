package src.com.hanabi.main;

import src.com.hanabi.input.KeyHandler;
import src.com.hanabi.scenes.GameStartScene;
import src.com.hanabi.scenes.Scene;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GamePanel extends Canvas implements Runnable {
    private boolean running = false;
    private Thread gameThread;

    private final int WIDTH = 1280;
    private final int HEIGHT = 800;

    private KeyHandler keyHandler = new KeyHandler();
    private Scene startScene;
    private Scene activeScene;

    public GamePanel() {
        setSize(WIDTH, HEIGHT);
        setBackground(Color.BLACK);
        addKeyListener(keyHandler);
        startScene = new GameStartScene(this, keyHandler);
        activeScene = startScene;
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

    public void setScene(Scene scene) {
        this.activeScene = scene;
    }

    public Scene getActiveScene() {
        return activeScene;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                update();
                delta--;
            }
            render();
        }
        stop();
    }

    private void update() {
        if (activeScene != null) {
            activeScene.update();
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (activeScene != null) {
            activeScene.render(g2d);
        }

        g2d.dispose();
        bs.show();
    }
}
