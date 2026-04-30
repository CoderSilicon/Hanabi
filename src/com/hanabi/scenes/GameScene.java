package src.com.hanabi.scenes;

import src.com.hanabi.entities.Enemy;
import src.com.hanabi.entities.Particle;
import src.com.hanabi.entities.Player;
import src.com.hanabi.entities.Projectile;
import src.com.hanabi.input.KeyHandler;
import src.com.hanabi.main.GamePanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScene implements Scene {
    private final GamePanel panel;
    private final KeyHandler keyHandler;
    private final Player player;
    private final List<Projectile> projectiles = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    private int spawnTimer = 0;
    private int score = 0;
    private int difficultyTimer = 0;

    public GameScene(GamePanel panel, KeyHandler keyHandler) {
        this.panel = panel;
        this.keyHandler = keyHandler;
        this.player = new Player(panel.getWidth() / 2, panel.getHeight() / 2, keyHandler, panel.getWidth(), panel.getHeight());
        resetWaveTimer();
    }

    @Override
    public void update() {
        player.update();

        if (keyHandler.spacePressed) {
            projectiles.add(new Projectile(player.getX(), player.getY(), player.getAngle(), panel.getWidth(), panel.getHeight()));
        }

        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            p.update();
            if (!p.isAlive()) {
                projectiles.remove(i);
                i--;
                continue;
            }

            for (int j = 0; j < enemies.size(); j++) {
                Enemy e = enemies.get(j);
                if (!e.isAlive()) {
                    continue;
                }

                double dx = p.getX() - e.getX();
                double dy = p.getY() - e.getY();
                double dist = Math.hypot(dx, dy);
                if (dist < p.getRadius() + e.getRadius()) {
                    p.kill();
                    e.kill();
                    score += 10;
                    spawnHitEffect(p.getX(), p.getY(), Color.WHITE);
                    break;
                }
            }
        }

        spawnTimer--;
        difficultyTimer++;
        if (spawnTimer <= 0) {
            int baseCount = 3 + Math.min(5, difficultyTimer / (60 * 20));
            int count = baseCount + random.nextInt(3);
            spawnEnemyWave(count);
            resetWaveTimer();
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (!e.isAlive()) {
                enemies.remove(i);
                i--;
                continue;
            }
            e.setTarget(player.getX(), player.getY());
            e.update();

            double dx = player.getX() - e.getX();
            double dy = player.getY() - e.getY();
            double dist = Math.hypot(dx, dy);
            if (dist < player.getRadius() + e.getRadius()) {
                panel.setScene(new GameOverScene(panel, keyHandler, score));
                return;
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
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        player.render(g2d);

        for (Enemy e : enemies) {
            if (e.isAlive()) {
                e.render(g2d);
            }
        }

        for (Projectile p : projectiles) {
            p.render(g2d);
        }

        for (Particle p : particles) {
            p.render(g2d);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        g2d.drawString("Score: " + score, 16, 24);
        g2d.drawString("Enemies: " + enemies.size(), 16, 44);
    }

    private void spawnEnemyWave(int count) {
        for (int i = 0; i < count; i++) {
            double x = random.nextDouble() * panel.getWidth();
            double y = random.nextDouble() * panel.getHeight();
            Enemy e = new Enemy(x, y, panel.getWidth(), panel.getHeight());
            e.setTarget(player.getX(), player.getY());
            enemies.add(e);
        }
    }

    private void resetWaveTimer() {
        spawnTimer = 120 + random.nextInt(120);
    }

    private void spawnHitEffect(double x, double y, Color color) {
        for (int i = 0; i < 12; i++) {
            particles.add(new Particle(x, y, color));
        }
    }
}
