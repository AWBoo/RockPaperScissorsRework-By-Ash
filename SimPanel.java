import java.awt.*;
import java.util.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class SimPanel extends JPanel implements Runnable{

    static final int GAME_WIDTH = (int) (MainFrame.WindowWidth * 0.75);

    static final int GAME_HEIGHT = MainFrame.WindowHeight;
    static int radius;
    static int TryCounter;

    JPanel SimPanel = new JPanel();

    Thread gameThread;
    Image image;
    Graphics graphics;

    static List<Ball> BallArray = new ArrayList<>();

    public SimPanel() {
        SimPanel.setFocusable(true);
        setBackground(Color.WHITE);
        gameThread = new Thread(this);
        gameThread.start();
    }

    public static void changeRadius(int R){
        radius = R;
    }

    public static boolean distanceBetweenBalls(Ball ball1, Ball ball2, int radius) {
        int diameter = radius* 2;
        int x1 = ball1.getX();
        int y1 = ball1.getY();
        int x2 = ball2.getX();
        int y2 = ball2.getY();

        int xDist = x2 - x1;
        int yDist = y2 - y1;
        return (int) Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2)) < diameter;
    }

    public static void SpawnBallForType(int SpawnAmount, String type) {
        Random random = new Random();

        for (int i = 0; i < SpawnAmount; i++) {
            System.out.println("Try Counter: " + TryCounter);

            if (TryCounter == 300) {
                TryCounter = 0;
                showErrorDialog("The attempts to spawn the ball exceed the limit.\n" +
                        "Spawning current balls");
                break;
            }

            int x;
            int y;
            int XVelocity;
            int YVelocity;
            boolean collision;

            do {
                x = random.nextInt(GAME_WIDTH - radius * 2);
                y = random.nextInt(GAME_HEIGHT - radius * 2);
                XVelocity = random.nextInt(11) - 5;
                YVelocity = random.nextInt(11) - 5;
                if(XVelocity == 0){
                    XVelocity = random.nextInt(11) - 5;
                }

                Ball ball = new Ball(x, y, XVelocity, YVelocity, radius, 1, type);

                collision = false;
                for (Ball existingBall : BallArray) {
                    if (distanceBetweenBalls(ball, existingBall, radius)) {
                        collision = true;
                        System.out.println("New ball collided with an existing ball. Finding a new position...");
                        TryCounter += 1;
                        break;
                    }
                }
            } while (collision);

            Ball ball = new Ball(x, y, XVelocity, YVelocity, radius, 1, type);
            BallArray.add(ball);
            System.out.println(ball.getX() + " | " + ball.getY() + " spawned." +
                    " Current Velocity: " + ball.getYVelocity() + " | " + ball.getYVelocity() +
                    "   Ball type: " + ball.getType());
        }
    }

    void CheckCollisions() {
        int ballCount = BallArray.size();
        for (int i = 0; i < ballCount; i++) {
            Ball ball1 = BallArray.get(i);

            if (ball1.getY() <= 0) {
                ball1.setYVelocity(-ball1.getYVelocity());
            }
            if (ball1.getY() >= GAME_HEIGHT - radius * 2) {
                ball1.setYVelocity(-ball1.getYVelocity());
            }

            if (ball1.getX() <= 0 || ball1.getX() >= GAME_WIDTH - radius * 2) {
                ball1.setXVelocity(-ball1.getXVelocity());
            }

            for (int j = i + 1; j < ballCount; j++) {
                Ball ball2 = BallArray.get(j);
                if (distanceBetweenBalls(ball1, ball2, radius)) {
                    checkTypeCollision(ball1, ball2);
                }
            }
        }
    }

    public static void ResolveCollide(Ball ball1, Ball ball2) {
        // Calculate the initial velocities
        double v1xInitial = ball1.getXVelocity();
        double v1yInitial = ball1.getYVelocity();
        double v2xInitial = ball2.getXVelocity();
        double v2yInitial = ball2.getYVelocity();

        // Calculate the masses
        double m1 = ball1.getMass();
        double m2 = ball2.getMass();

        // Check if the balls are overlapping
        double distance = Math.sqrt(Math.pow(ball2.getX() - ball1.getX(), 2) + Math.pow(ball2.getY() - ball1.getY(), 2));
        double radiiSum = ball1.getRadius() + ball2.getRadius();

        if (distance <= radiiSum) {
            // Adjust the positions to separate the balls
            double overlap = radiiSum - distance;
            double dx = (ball2.getX() - ball1.getX()) / distance;
            double dy = (ball2.getY() - ball1.getY()) / distance;

            ball1.setX((int) (ball1.getX() - overlap * dx));
            ball1.setY((int) (ball1.getY() - overlap * dy));
            ball2.setX((int) (ball2.getX() + overlap * dx));
            ball2.setY((int) (ball2.getY() + overlap * dy));

            // Calculate the final velocities using the conservation of momentum and kinetic energy equations
            double v1xFinal = ((m1 - m2) * v1xInitial + 2 * m2 * v2xInitial) / (m1 + m2);
            double v1yFinal = ((m1 - m2) * v1yInitial + 2 * m2 * v2yInitial) / (m1 + m2);
            double v2xFinal = ((m2 - m1) * v2xInitial + 2 * m1 * v1xInitial) / (m1 + m2);
            double v2yFinal = ((m2 - m1) * v2yInitial + 2 * m1 * v1yInitial) / (m1 + m2);

            // Update the velocities of the balls
            ball1.setXVelocity((int) v1xFinal);
            ball1.setYVelocity((int) v1yFinal);
            ball2.setXVelocity((int) v2xFinal);
            ball2.setYVelocity((int) v2yFinal);
        }
    }

    public void checkTypeCollision(Ball ball1, Ball ball2) {
        if (Objects.equals(ball1.type, "rock") && Objects.equals(ball2.type, "scissors")) {
            checkTypeCollisionBulkMaintain(ball1,ball2);

        }
        else if (Objects.equals(ball1.type, "scissors") && Objects.equals(ball2.type, "rock")){
            checkTypeCollisionBulkMaintain(ball2,ball1);
        }

        else if (Objects.equals(ball1.type, "paper") && Objects.equals(ball2.type, "rock")) {
            checkTypeCollisionBulkMaintain(ball1,ball2);
        }
        else if (Objects.equals(ball1.type, "rock") && Objects.equals(ball2.type, "paper")){
            checkTypeCollisionBulkMaintain(ball2,ball1);
        }

        else if (Objects.equals(ball1.type, "scissors") && Objects.equals(ball2.type, "paper")) {
            checkTypeCollisionBulkMaintain(ball1,ball2);
        }
        else if (Objects.equals(ball1.type, "paper") && Objects.equals(ball2.type, "scissors")){
            checkTypeCollisionBulkMaintain(ball2,ball1);
        }

        else{
            ResolveCollide(ball1, ball2);
        }
    }

    public void checkTypeCollisionBulkMaintain(Ball ball1 , Ball ball2){
        ResolveCollide(ball1, ball2);
//        System.out.println("Ball1 X/Y: " + ball1.getX() + "/" + ball1.getY() + " | Ball2 X/Y: " + ball2.getX() + "/" + ball2.getY()
//                + "Ball 1 and 2 Type: " + ball1.type + "|" + ball2.type);

        ball2.setType(ball1.type);
        EditPanel.UpdateNumberOfBalls();
//        System.out.println("Ball2 change: " + ball2.type);

    }

    static void cleanBallArray() {
        int ballCount = BallArray.size();
        if (ballCount > 0) {
            BallArray.subList(0, ballCount).clear();
        }
    }

    public void move(){
        try{
            for (Ball ball : BallArray) {
                ball.move();
                CheckCollisions();
            }
        }catch (ConcurrentModificationException e){
            showErrorDialog("System Crash.\n Unintended Behavior might occur \n Feel Free to try again, try using a smaller amount");
        }
    }

    private static void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void paint(Graphics g) {
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }

    public void draw(Graphics g) {
        for (Ball ball : BallArray) {
            ball.draw(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public void run() {
        // Game loop
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        long lastTime = System.nanoTime();
        long accumulatedTime = 0;

        while(true) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;
            lastTime = now;
            accumulatedTime += elapsedTime;

            // Cap the accumulated time to prevent spiral of death
            if (accumulatedTime > 1000000000)
                accumulatedTime = 1000000000;

            // Update the game state multiple times if needed to catch up
            while (accumulatedTime >= OPTIMAL_TIME) {
                move();
                repaint();
                accumulatedTime -= OPTIMAL_TIME;
            }

        }
    }
}
