package paymentGateway;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class TwoNeutronsGame {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Two Neutrons — 2 Player Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            GamePanel panel = new GamePanel(800, 600);
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.start(); 
        });
    }
}
class GamePanel extends JPanel implements ActionListener, KeyListener {
    
    private final int WIDTH;
    private final int HEIGHT;
    private final Timer timer;
    private final Player p1;
    private final Player p2;
    private int score1 = 0;
    private int score2 = 0;
    private final int WIN_SCORE = 10;
    private boolean paused = false;
    private boolean gameOver = false;

    private final Random rand = new Random();
    private final Font hudFont = new Font("SansSerif", Font.BOLD, 18);

    public GamePanel(int w, int h) {
        this.WIDTH = w;
        this.HEIGHT = h;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        p1 = new Player(100, HEIGHT/2, 28, Color.CYAN);
        p2 = new Player(WIDTH-100, HEIGHT/2, 28, Color.ORANGE);
        timer = new Timer(16, this); 
    }

    public void start() {
        timer.start();
        requestFocusInWindow();
    }
   
    private void resetPositions() {
        p1.setPosition(100, HEIGHT/2);
        p2.setPosition(WIDTH-100, HEIGHT/2);
        p1.stopMovement();
        p2.stopMovement();
    }

    private void restartGame() {
        score1 = 0;
        score2 = 0;
        paused = false;
        gameOver = false;
        resetPositions();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(new Color(255,255,255,40));
        Stroke old = g2.getStroke();
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                                     new float[] {8f, 8f}, 0));
        g2.drawLine(WIDTH/2, 0, WIDTH/2, HEIGHT);
        g2.setStroke(old);        
        p1.draw(g2);
        p2.draw(g2);        
        g2.setFont(hudFont);
        String left = "Player 1 (Blue): " + score1;
        String right = "Player 2 (Red): " + score2;
        g2.setColor(Color.CYAN);
        g2.drawString(left, 12, 22);
        g2.setColor(Color.ORANGE);
        int rw = g2.getFontMetrics().stringWidth(right);
        g2.drawString(right, WIDTH - rw - 12, 22);       
        g2.setFont(g2.getFont().deriveFont(12f));
        g2.setColor(Color.WHITE);
        g2.drawString("P: Pause/Resume  |  R: Restart", WIDTH/2 - 110, HEIGHT - 10);
        
        if (gameOver) {
            String winner = (score1 >= WIN_SCORE) ? "Player 1 (Blue) WINS!" : "Player 2 (Red) WINS!";
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            int sw = g2.getFontMetrics().stringWidth(winner);
            g2.setColor(Color.YELLOW);
            g2.drawString(winner, (WIDTH - sw) / 2, HEIGHT / 2);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
            String again = "Press R to play again";
            int aw = g2.getFontMetrics().stringWidth(again);
            g2.drawString(again, (WIDTH - aw) / 2, HEIGHT/2 + 32);
        	} 
        	else if (paused) {
            String pausedText = "PAUSED";
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            int sw = g2.getFontMetrics().stringWidth(pausedText);
            g2.setColor(Color.WHITE);
            g2.drawString(pausedText, (WIDTH - sw) / 2, HEIGHT / 2);
        }
        g2.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (paused || gameOver) {
            repaint();
            return;
        }        
        p1.update(WIDTH, HEIGHT);
        p2.update(WIDTH, HEIGHT);
        
        if (p1.collidesWith(p2)) {

            boolean p1Moving = p1.isMoving();
            boolean p2Moving = p2.isMoving();
            boolean p1Hit = false;
            boolean p2Hit = false;
            if (p1Moving) p1Hit = true;
            if (p2Moving) p2Hit = true;
            if (p1Hit) score1++;
            if (p2Hit) score2++;
  
            p1.bounceFrom(p2);
            p2.bounceFrom(p1);

            Timer t = new Timer(400, ev -> {
                resetPositions();
                ((Timer) ev.getSource()).stop();
            });
            t.setRepeats(false);
            t.start();
        }
        if (score1 >= WIN_SCORE || score2 >= WIN_SCORE) {
            gameOver = true;
        }
        repaint();
    }   
    @Override
    public void keyPressed(KeyEvent e) {
        
    	int k = e.getKeyCode();        
        if (k == KeyEvent.VK_W) p1.setUp(true);
        if (k == KeyEvent.VK_S) p1.setDown(true);
        if (k == KeyEvent.VK_A) p1.setLeft(true);
        if (k == KeyEvent.VK_D) p1.setRight(true);
        
        if (k == KeyEvent.VK_UP) p2.setUp(true);
        if (k == KeyEvent.VK_DOWN) p2.setDown(true);
        if (k == KeyEvent.VK_LEFT) p2.setLeft(true);
        if (k == KeyEvent.VK_RIGHT) p2.setRight(true);
        
        if (k == KeyEvent.VK_P) paused = !paused;
        if (k == KeyEvent.VK_R) restartGame();
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_W) p1.setUp(false);
        if (k == KeyEvent.VK_S) p1.setDown(false);
        if (k == KeyEvent.VK_A) p1.setLeft(false);
        if (k == KeyEvent.VK_D) p1.setRight(false);

        if (k == KeyEvent.VK_UP) p2.setUp(false);
        if (k == KeyEvent.VK_DOWN) p2.setDown(false);
        if (k == KeyEvent.VK_LEFT) p2.setLeft(false);
        if (k == KeyEvent.VK_RIGHT) p2.setRight(false);
    }
    @Override
    public void keyTyped(KeyEvent e) {}   
    static class Player {
        private double x, y;
        private final int size;
        private final Color color;
        private boolean up, down, left, right;
        private double vx = 0, vy = 0;
        private final double SPEED = 4.2; 
        private final double FRICTION = 0.88;

        public Player(double x, double y, int size, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
        }
        public void setPosition(double nx, double ny) {
            this.x = nx;
            this.y = ny;
        }
        public void stopMovement() {
            this.vx = 0;
            this.vy = 0;
            this.up = this.down = this.left = this.right = false;
        }
        public void update(int boundsW, int boundsH) {            
            double ax = 0, ay = 0;
            if (up) ay -= SPEED;
            if (down) ay += SPEED;
            if (left) ax -= SPEED;
            if (right) ax += SPEED;

            vx += ax * 0.12;
            vy += ay * 0.12;            
            vx *= FRICTION;
            vy *= FRICTION;           
            x += vx;
            y += vy;            
            double r = size / 2.0;
            if (x - r < 0) { x = r; vx = -vx * 0.5; }
            if (x + r > boundsW) { x = boundsW - r; vx = -vx * 0.5; }
            if (y - r < 0) { y = r; vy = -vy * 0.5; }
            if (y + r > boundsH) { y = boundsH - r; vy = -vy * 0.5; }
        }
        public void draw(Graphics2D g2) {
            int ix = (int) Math.round(x);
            int iy = (int) Math.round(y);
            int s = size;            
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 90));
            g2.fillOval(ix - s/2 - 6, iy - s/2 - 6, s + 12, s + 12);           
            g2.setColor(color);
            g2.fillOval(ix - s/2, iy - s/2, s, s);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(ix - s/2, iy - s/2, s, s);
        }
        public boolean collidesWith(Player other) {
            double dx = x - other.x;
            double dy = y - other.y;
            double dist2 = dx*dx + dy*dy;
            double r = (size/2.0) + (other.size/2.0);
            return dist2 <= r*r;
        }
        public boolean isMoving() {          
            return Math.hypot(vx, vy) > 0.8;
        }

        public void bounceFrom(Player other) {
            
            double nx = x - other.x;
            double ny = y - other.y;
            double len = Math.hypot(nx, ny);
            if (len == 0) {
                nx = 1; ny = 0; len = 1;
            }
            nx /= len; ny /= len;
            
            vx = Math.max(-6, vx + nx * 4);
            vy = Math.max(-6, vy + ny * 4);
            other.vx = Math.min(6, other.vx - nx * 4);
            other.vy = Math.min(6, other.vy - ny * 4);
        }       
        public void setUp(boolean b) { up = b; }
        public void setDown(boolean b) { down = b; }
        public void setLeft(boolean b) { left = b; }
        public void setRight(boolean b) { right = b; }
    }
}