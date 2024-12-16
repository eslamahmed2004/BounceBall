public class Brick {
         double x, y; // إحداثيات الطوب
        boolean visible = true;
        int health;
        int index;
        // حالة ظهور الطوب

        Brick(double x, double y, int health, int index) {
            this.x = x;
            this.y = y;
            this.health = health;
            this.index = index;// الطوب مرئي عند إنشائه
        }
    }
