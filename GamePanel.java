import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.logging.XMLFormatter;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY=75;
    final int x[]=new int[GAME_UNITS];
    final int y[]=new int[GAME_UNITS];
    int BodyParts = 6;
    int applesEaten;
    int AppleX;
    int AppleY;
    char Direction = 'R';
    boolean Running =false;
    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }
    public void startGame(){
        newApple();
        Running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Draw(g);
    }
    public void Draw(Graphics g){
        if(Running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(AppleX, AppleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < BodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("ink free",Font.BOLD,40));
            FontMetrics metrics =  getFontMetrics(g.getFont());
            g.drawString("Score: "+ applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: "+ applesEaten))/2,SCREEN_HEIGHT/2);

        }
        else {
            gameOver(g);
        }

    }
    public void newApple(){
        AppleX=random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        AppleY=random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void move(){
        for (int i =BodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (Direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
            break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
            break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
            break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
            break;
        }

    }
    public void checkApples(){
        if ((x[0]==AppleX) && (y[0]==AppleY)){
            BodyParts++;
            applesEaten++;
            newApple();
        }

    }
    public void checkCollisions(){
        //check if head collides with body
        for (int i =BodyParts;i>0;i--){
            if ((x[0] == x[i])&& y[0] == y[i]){
                Running= false;
            }
        }
        //check if head touches left border
        if (x[0]<0){
            Running=false;
        }
        //check if head touches right border
        if(x[0]>SCREEN_WIDTH){
            Running=false;
        }
        //check if head touches top border
        if (y[0]<0){
            Running=false;
        }
        //check if head touches bottom
        if (y[0]>SCREEN_HEIGHT){
            Running=false;
        }
        if (!Running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        //game over text
        g.setColor(Color.RED);
        g.setFont(new Font("ink free",Font.BOLD,75));
        FontMetrics metrics =  getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Running){
            move();
            checkApples();
            checkCollisions();
        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter{
        public void KeyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (Direction!='R'){
                        Direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (Direction!='L'){
                        Direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (Direction!='D'){
                        Direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (Direction!='U'){
                        Direction='D';
                    }
                    break;
            }
        }
    }
}
