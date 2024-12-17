public class Brick {
    double x, y;
    boolean visible = true;
    int health;
    int index;


    Brick( double x, double y, int index){
        this.x = x;
        this.y = y;
        this.health = 1;
        this.index = index;
    }

    public Brick(double x, double y) {
        this.x = x;
        this.y = y;
    }

}