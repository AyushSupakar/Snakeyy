package snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
class Gamepanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH=1080;
    static final int SCREEN_HEIGHT = 720;
    static final int UNIT_SIZE = 40 ;
    static  final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/(UNIT_SIZE * UNIT_SIZE);
    static  int DELAY = 120;
    final  int x[] = new int[GAME_UNITS];
    final  int y[] = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    ImageIcon img;
    Image appl1 = Toolkit.getDefaultToolkit().createImage("./worm.png");
    Image appl2 =
            Toolkit.getDefaultToolkit().createImage(getClass().getResource("./ladybug.png"));
    ImageIcon snk;
    Image bgi =
            Toolkit.getDefaultToolkit().createImage(getClass().getResource("./backg.png"));
    //JButton butt;

    Gamepanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timerr();
        timer.start();

    }
    public void timerr(){
        DELAY =  160 - (applesEaten*20);
        timer = new Timer(DELAY,this);
    }
    public  void paintComponent(Graphics g){
        super.paintComponent(g);
        //img = new ImageIcon("backg.png");
        //appl = Toolkit.getDefaultToolkit().createImage("btlf.png");
        g.drawImage(bgi,0,0,null );
        draw(g);

    }

    public void draw(Graphics g){
        if (running){
            for(int i = 0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                // g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE, SCREEN_HEIGHT);
                //  g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH, i*UNIT_SIZE);
            }
            g.drawImage(appl2,appleX,appleY, null );
            //g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
            // g.drawImage(appl, appleX, appleY, null);
            imageUpdate(appl2, 0, appleX,appleY, UNIT_SIZE, UNIT_SIZE);


            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {

                    imageUpdate(appl2, 0, x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    g.setColor(Color.orange);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(Color.yellow);
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }
    public void move(){
        for (int i = bodyParts;i>0;i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction){
            case 'U' :
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D' :
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'R' :
                x[0] = x[0] + UNIT_SIZE;
                break;

            case 'L' :
                x[0] = x[0] - UNIT_SIZE;
                break;

        }

    }
    public void checkApple(){
        if ((x[0] == appleX) && (y[0]==appleY)) {
            bodyParts++;
            applesEaten++;
            timerr();
            newApple();
        }

    }
    public void checkCollisions(){
        for (int i = bodyParts; i>0;i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }

        }
        if (x[0] < 0) {
            running = false;
        }
        if (y[0] < 0) {
            running = false;
        }
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running){
            timer.stop();
        }



    }
    public void gameOver(Graphics g){
        g.setColor(Color.blue);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        //PlayAgain
        g.setColor(Color.black);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Reload to play again...", (SCREEN_WIDTH - metrics3.stringWidth("Reload to play again...")) / 2, 4*(SCREEN_HEIGHT / 5));


    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
        /*if(e.getSource()==butt){
            startGame();

        }*/

    }
    class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }

        }
    }
}



class Gameframe extends JFrame {
    Gameframe(){
        this.add(new Gamepanel());
        this.setTitle("Snakey");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}

public class Snakegame {
    public static void main(String[]args){
        new Gameframe();
    }

}
