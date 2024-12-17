public class Brick {
    double x, y; // إحداثيات الطوب
    boolean visible = true;
    int health;
    int index;
    // حالة ظهور الطوب

    Brick( double x, double y, int index){
        this.x = x;
        this.y = y;
        this.health = 1;
        this.index = index;// الطوب مرئي عند إنشائه
    }


}