import java.awt.*;
import java.util.Objects;
public class Ball {

    int x;
    int y;
    int XVelocity;
    int YVelocity;
    int radius;
    int mass;
    String type;

    Color ballColor;

    Ball(int x, int y, int XVelocity, int YVelocity,int radius, int mass, String type){
        this.x = x;
        this.y = y;
        this.XVelocity = XVelocity;
        this.YVelocity = YVelocity;

        this.mass = mass;
        this.radius = radius;

        this.type = type;

        if(Objects.equals(type, "scissors")){
            ballColor = Color.BLUE;
        }

        if(Objects.equals(type, "paper")){
            ballColor = Color.GREEN;
        }
        if(Objects.equals(type, "rock")){
            ballColor = Color.RED;
        }
    }

    public void move(){
        x += getXVelocity();
        y += getYVelocity();
    }

    public void setType(String NewType){
        type = NewType;

        if(Objects.equals(type, "scissors")){
            ballColor = Color.BLUE;
        }

        if(Objects.equals(type, "paper")){
            ballColor = Color.GREEN;
        }
        if(Objects.equals(type, "rock")){
            ballColor = Color.RED;
        }
    }

    public void draw(Graphics g) {
        g.setColor(ballColor);
        g.fillOval(x, y, radius*2, radius*2);
    }

    public int getMass() {
        return mass;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public void setXVelocity(int XVelocity) {
        this.XVelocity = XVelocity;
    }

    public void setYVelocity(int YVelocity) {
        this.YVelocity = YVelocity;
    }

    public int getXVelocity() {
        return XVelocity;
    }

    public int getYVelocity() {
        return YVelocity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public String getType() {
        return type;
    }
}
