import javax.swing.*;
import java.awt.*;

public class Bird {
    private int x, y;
    private int velocityY = 0;
    private Image birdImage;
    //Vị trí ban đầu
    public Bird(int startX, int startY, String imagePath) {
        this.x = startX;
        this.y = startY;
        this.birdImage = new ImageIcon(imagePath).getImage();
    }
    public void update() {
        velocityY += Constants.GRAVITY;  // Áp dụng trọng lực
        y += velocityY;  // Cập nhật vị trí Bird
    }
    public void flap() {
        velocityY = Constants.JUMP_STRENGTH; // Nhảy lên
    }
    public void draw(Graphics g) {
        g.drawImage(birdImage, x, y, Constants.BIRD_WIDTH, Constants.BIRD_HEIGHT, null);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, Constants.BIRD_WIDTH, Constants.BIRD_HEIGHT);
    }
}
