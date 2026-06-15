package pingPong;

/*
 * Ping Pong Game
 * Created during Java Workshop Learning
 * Customized by Ishant Neekhra
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main extends JFrame implements KeyListener {

    private int ballX = 100;
    private int ballY = 100;
    private int ballDX = 3;
    private int ballDY = 3;

    private int paddleX = 180;
    private int score = 0;
    private int highScore = 0;

    private Timer timer;
    private JPanel gamePanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton restartButton;

    private void configureWindow() {
        setTitle("Ping Pong Game by Ishant Neekhra");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw ball
            g.setColor(Color.ORANGE);
            g.fillOval(ballX, ballY, 20, 20);

            // Draw paddle
            g.setColor(new Color(30, 144, 255));
            g.fillRect(paddleX, 320, 100, 10);

            // Draw score
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score : " + score, 20, 30);
            g.drawString("High Score : " + highScore, 20, 60);
        }
    }

    private void setupGameCanvas() {
        gamePanel = new GamePanel();
        gamePanel.setBackground(Color.WHITE);
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);
    }

    private void setupButtons() {
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        restartButton = new JButton("Restart");

        startButton.addActionListener(e -> {
            timer.start();
            gamePanel.requestFocusInWindow();
        });

        stopButton.addActionListener(e -> {
            timer.stop();
            gamePanel.requestFocusInWindow();
        });

        restartButton.addActionListener(e -> {
            resetGame();
            gamePanel.repaint();
            gamePanel.requestFocusInWindow();
        });
    }

    private void setupTimer() {
        timer = new Timer(10, e -> processGamePhysics());
    }

    private void processGamePhysics() {
        ballX += ballDX;
        ballY += ballDY;

        // Left and right wall collision
        if (ballX <= 0 || ballX >= gamePanel.getWidth() - 20) {
            ballDX = -ballDX;
        }

        // Top wall collision
        if (ballY <= 0) {
            ballDY = -ballDY;
        }

        // Paddle collision
        if (ballY + 20 >= 320 &&
            ballY + 20 <= 330 &&
            ballX + 20 >= paddleX &&
            ballX <= paddleX + 100) {

            ballDY = -ballDY;
            score++;

            if (score > highScore) {
                highScore = score;
            }
        }

        // Game over
        if (ballY > gamePanel.getHeight()) {
            timer.stop();

            JOptionPane.showMessageDialog(
                this,
                "Game Over!\n\nScore : " + score + "\nHigh Score : " + highScore,
                "Result",
                JOptionPane.INFORMATION_MESSAGE
            );

            resetGame();
        }

        gamePanel.repaint();
    }

    private void resetGame() {
        timer.stop();
        ballX = 100;
        ballY = 100;
        ballDX = 3;
        ballDY = 3;
        paddleX = 180;
        score = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddleX -= 20;

            if (paddleX < 0) {
                paddleX = 0;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddleX += 20;

            if (paddleX > gamePanel.getWidth() - 100) {
                paddleX = gamePanel.getWidth() - 100;
            }
        }

        gamePanel.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    private void showWindow() {
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(restartButton);

        add(gamePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);

        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    }

    public Main() {
        configureWindow();
        setupGameCanvas();
        setupButtons();
        setupTimer();
        showWindow();
    }

    public static void main(String[] args) {
        new Main();
    }
}
