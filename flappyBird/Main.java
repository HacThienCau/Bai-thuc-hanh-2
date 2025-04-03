import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Main extends JFrame{
    class BackgroundPanel extends JPanel implements ActionListener {
        private Image backgroundImage;
        private Bird bird;
        private ArrayList<Pipe> pipes;
        private Timer timer;    
        private Timer pipeTimer;  
        private double score;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon("./public/flappybirdbg.png").getImage();
            bird = new Bird(Constants.BIRD_START_X,Constants.BIRD_START_Y,"./public/flappybird.png");
            pipes = new ArrayList<>();
            score = 0;
            setFocusable(true);
            setFocusTraversalKeysEnabled(false);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                        bird.flap(); 
                    }
                }
            });
        }

        public void startGame() {
            score = 0;
            timer = new Timer(Constants.TIME, this);
            timer.start();
            if (pipeTimer != null) {
                pipeTimer.stop();
            }    
            pipeTimer = new Timer(Constants.PIPETIME, e -> addPipe());
            pipeTimer.start();
        }

        private void addPipe() {
            int gap = (int) (Math.random() * (Constants.MAX_GAP-Constants.MIN_GAP)) + Constants.MIN_GAP;
            int pipeHeight = (int) (Math.random() * (Constants.MAX_PIPE_HEIGHT-Constants.MIN_PIPE_HEIGHT)) + Constants.MIN_PIPE_HEIGHT;
            // Ống nước trên
            pipes.add(new Pipe(Constants.SCREEN_WIDTH, 0, pipeHeight, "./public/toppipe.png"));
            // Ống nước dưới
            pipes.add(new Pipe(Constants.SCREEN_WIDTH, pipeHeight + gap, Constants.SCREEN_HEIGHT-pipeHeight-gap, "./public/bottompipe.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            bird.update();
            //Kiểm tra va chạm với viền màn hình
            if(bird.getBounds().y > Constants.SCREEN_HEIGHT || bird.getBounds().y < 0){
                gameOver();
            }
            // Cập nhật vị trí các ống nước
            Iterator<Pipe> iterator = pipes.iterator();
            while (iterator.hasNext()) {
                Pipe pipe = iterator.next();
                pipe.update();
                // Xóa ống nước khi ra khỏi màn hình
                if (pipe.getX() < -Constants.PIPE_WIDTH) {
                    iterator.remove();
                }
                // Kiểm tra va chạm với ống
                if (bird.getBounds().intersects(pipe.getBounds())) {
                    gameOver();
                }
                // Tăng điểm nếu con chim vượt qua ống
                if (!pipe.isPassed() && pipe.getX() + Constants.PIPE_WIDTH < bird.getX()) {
                    pipe.setPassed(true);
                    score += 0.5; // Mỗi lần đi qua 1 cặp ống đưọc 1 điểm
                }
            }
            repaint();
        }
        private void showGameOverPopup() {
            JDialog gameOverDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
            gameOverDialog.setSize(Constants.GAMEOVER_DIALOG_WIDTH, Constants.GAMEOVER_DIALOG_HEIGHT);
            gameOverDialog.setLayout(new BorderLayout());
            gameOverDialog.setLocationRelativeTo(this);     
            gameOverDialog.setUndecorated(true);  

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(Color.RED); 
            // Title
            JLabel titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
            titleLabel.setForeground(Color.YELLOW);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            // Hiển thị điểm số
            JLabel messageLabel = new JLabel("Điểm của bạn: " + Math.round(score), SwingConstants.CENTER);
            messageLabel.setForeground(Color.YELLOW);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 18));     
            // Panel chứa nút
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.RED);

            JButton restartButton = new JButton("Chơi lại");
            restartButton.setBackground(new Color(0, 150, 0)); // Màu xanh lá đậm
            restartButton.setForeground(Color.WHITE);
            restartButton.setFocusPainted(false);

            JButton exitButton = new JButton("Thoát");   
            exitButton.setBackground(Color.GRAY); 
            exitButton.setForeground(Color.WHITE); 
            exitButton.setFocusPainted(false);     

            restartButton.addActionListener(e -> {
                gameOverDialog.dispose();
                resetGame();
            });        
            exitButton.addActionListener(e -> System.exit(0));  

            buttonPanel.add(exitButton);            
            buttonPanel.add(restartButton);   
            mainPanel.add(titleLabel, BorderLayout.NORTH);
            mainPanel.add(messageLabel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);       
            gameOverDialog.add(mainPanel);
            gameOverDialog.setVisible(true);
        }
        private void gameOver() {
            timer.stop();
            pipeTimer.stop();
            showGameOverPopup();
        }
        private void resetGame() {
            if (pipeTimer != null) pipeTimer.stop();
            // Trì hoãn việc xóa pipes để tránh lỗi
            SwingUtilities.invokeLater(() -> {
                pipes.clear(); 
                bird = new Bird(Constants.BIRD_START_X,Constants.BIRD_START_Y,"./public/flappybird.png");
                startGame();
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            bird.draw(g);
            for (Pipe pipe : pipes) {
                pipe.draw(g);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Score: " + Math.round(score), 20, 40);
        }
    }
    public Main() {
        setTitle("Flappy Bird");
        setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BackgroundPanel panel = new BackgroundPanel();
        setContentPane(panel);
        setVisible(true);
        panel.startGame();
    }
    public static void main(String[] args) {
        new Main();
    }
}