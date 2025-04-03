import javax.swing.*;
import java.awt.*;

public class Pipe {
    private int x, y;
    private int height;
    private Image pipeImage;
    private boolean isPassed = false;

    public Pipe(int startX, int startY, int height, String imagePath) {
        this.x = startX;
        this.y = startY;
        this.pipeImage = new ImageIcon(imagePath).getImage();

        this.height = height;
    }
    public void update() {
        x -= Constants.PIPE_SPEED; // Di chuyển ống từ phải sang trái
    }
    public void draw(Graphics g) {
        g.drawImage(pipeImage, x, y, Constants.PIPE_WIDTH, height, null);
    }

    public int getX() {
        return x;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, Constants.PIPE_WIDTH, height);
    }
    
    public boolean isPassed(){
        return isPassed;
    }

    public void setPassed(boolean value){
        isPassed = value;
    }
}
