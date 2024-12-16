import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BallGLEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

    // ملف يحتوي على جميع الصور المستخدمة كخلفيات وقوام
    File file = new File("D:\\CS304\\src\\PNG\\");
    String[] textureNames = file.list();
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];

    // نقطة البداية لتشغيل اللعبة
    public static void main(String[] args) {
        new BounceBall(); // إنشاء نافذة اللعبة
        BounceBall.animator.start(); // بدء عملية التحديث المستمر للشاشة
    }

    double width = 700, hight = 500; // أبعاد شاشة اللعبة
    GL gl;
    List<Brick> bricks = new ArrayList<>(); // قائمة تحتوي على جميع الطوب
    double brickWidth = 200, brickHeight = 80; // أبعاد الطوب
    int lives = 0;
    boolean heart1 = true;
    boolean heart2 = true;
    boolean heart3 = true;
    List<Brick> level1 = new ArrayList<>();
    List<Brick> level2 = new ArrayList<>();
    List<Brick> level3 = new ArrayList<>();
    List<Brick> level4 = new ArrayList<>();
    List<Brick> level5 = new ArrayList<>();
    List<Brick> level6 = new ArrayList<>();

    // تعريف الطوب ككائن مستقل
    public class Brick {
        double x, y; // إحداثيات الطوب
        boolean visible = true;
        int health;
        int index;
        // حالة ظهور الطوب

        Brick(double x, double y) {
            this.x = x;
            this.y = y;
            this.index = index;
            this.health = 1;
        }
    }

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        GLU glu = new GLU();
        // إعدادات أولية للألوان والنسيج
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // لون الخلفية أبيض
        gl.glEnable(GL.GL_TEXTURE_2D); // تفعيل استخدام القوام
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA); // تفعيل الشفافية
        // تحميل القوام
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture("D:\\CS304\\src\\PNG\\" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, texture[i].getWidth(), texture[i].getHeight(), GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture[i].getPixels());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // إعدادات العرض ثنائية الأبعاد
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-width, width, -hight, hight);
        gl.glMatrixMode(GL.GL_MODELVIEW);

        initLevels(-300,350,30, level1);
        initLevels(-350,400,40,level2);
        initLevels(-400,400,50,level3);
        initLevels(-450,400,60, level4);
        initLevels(-500,400,70, level5);
        initLevels(-600,400,80, level6);
    }

    double x = 0, y = -450; // موقع المضرب
    double x_ball = 0, y_ball = -400; // موقع الكرة
    double dx = -3, dy = 3; // اتجاه وسرعة الكرة
    boolean startBall = false; // حالة بدء تحرك الكرة
    double barWidth = 260; // عرض المضرب
    double barHeight = 15; // عرض المضرب
    int countBricks = 24;
    boolean isclicked = false;
    boolean next = false;
    boolean restart = false;
    boolean exit = false;
    boolean choose = false;
    @Override
    public void display(GLAutoDrawable gld) {
        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // مسح الشاشة
        gl.glLoadIdentity(); // إعادة تعيين المصفوفة
        DrawBackground(68); // رسم الخلفية
        int brokenBricks = 0;
        for (Brick brick : bricks) {
            if (!brick.visible) {
                brokenBricks++;
            }
        }

// زيادة السرعة بعد تدمير كل 5 طوبات
        if (brokenBricks % 6 == 0 && brokenBricks > 0) {
            x_ball += 0.3 * dx; // زيادة السرعة الأفقية بمعدل أقل
            y_ball += 0.3 * dy; // زيادة السرعة الرأسية بمعدل أقل
        }
        // رسم الطوب
        for (Brick brick : level1) {
            if (brick.visible) { // يتم رسم الطوب فقط إذا كان مرئيًا
                DrawSprite(brick.x, brick.y, 1, 30, 15);
            }
        }

        // رسم المضرب
        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        DrawSprite(0, 0, 50, 110, 15);
        gl.glPopMatrix();
        ball();
//        // رسم الكرة
//        gl.glPushMatrix();
//        gl.glTranslated(x_ball, y_ball, 0);
//        DrawSprite(0, 0, 58, 15, 15);
//        gl.glPopMatrix();

        // تحديث موقع الكرة إذا بدأت الحركة
        if (startBall) {
            x_ball += dx;
            y_ball += dy;
        }

        // التحقق من التصادم مع حدود الشاشة
        if (x_ball >= width - 15 || x_ball <= -width + 15) {
            dx = -dx; // عكس اتجاه الكرة أفقيًا
            playSound();
        }
        if (y_ball >= hight - 15) {
            dy = -dy; // عكس اتجاه الكرة رأسيًا
            playSound();
        }

//        if (y_ball <= -hight + 15) {
//            System.out.println( "before"+count);
//            startBall=false;
//            count++;
////            resetBall(); // إعادة تعيين الكرة عند سقوطها
////            startBall=false;
////            System.out.println( "before"+count);
////            count++;
//            System.out.println(count);
//        }
        checkGameStatus();

        // التحقق من التصادم مع الطوب
        for (Brick brick : bricks) {
            if (brick.visible) {
                double left = brick.x - brickWidth / 5;
                double right = brick.x + brickWidth / 5;
                double top = brick.y + brickHeight / 5;
                double bottom = brick.y - brickHeight / 5;

                if (x_ball + 15 > left && x_ball - 15 < right && y_ball + 15 > bottom && y_ball - 15 < top) {
                    // التحقق من موقع التصادم لتحديد اتجاه الكرة
                    if (x_ball + 10 >= left && x_ball - 10 <= right) {
                        dy = -dy;
                    } else {
                        dx = -dx;
                    }
                    brick.visible = false; // إخفاء الطوب
                    playSound();
                    break;
                }
            }
        }
        if (heart1) {
            DrawSprite(150, 450, 60, 20, 20);

        }
        if (heart2) {
            DrawSprite(200, 450, 60, 20, 20);
        }
        if (heart3) {
            DrawSprite(250, 450, 60, 20, 20);

        }

        if (lives >= 1) {
            heart1 = false;
        }
        if (lives >= 2) {
            heart2 = false;

        }
        if (lives >= 3) {
            heart3 = false;
            DrawSprite(0, 0, 103, 400, 400);
        }
        if(isclicked) {
            isclicked = false;
            if (mouseX >= -279 && mouseX <= -23 && mouseY <= -218 && mouseY >= -459) {
                choose = true;
                next = true;
            }
            if (mouseX >= -4 && mouseX <= 272 && mouseY <= -218 && mouseY >= -459) {
                choose = true;
                restart = true;
            }
            if (mouseX >= -163 && mouseX <= -15 && mouseY <= -185 && mouseY >= -376) {
                choose = true;
                restart = true;
            }
            if (mouseX >= 9 && mouseX <= 152 && mouseY <= -185 && mouseY >= -376) {
                choose = true;
                exit = true;
            }
        }
        if (choose){
            if (exit){
                System.exit(0);
            }
            if (restart){
                for (double i = -600; i <= 600; i += brickWidth + 80) {
                    for (double j = 100; j <= 100; j += brickHeight + 10) {
//                bricks.add(new Brick(i, j));
                        bricks.add(new Brick(i, j));
                    }

                }
            }
            if (next){
                BounceBall.animator.stop();
                BounceBall.animator.start();
            }
        }
    }

    void initLevels(int startX, int startY, int n, List<Brick> level) {
        double x = startX;
        double y = startY;
        for (int i = 0; i < n; i++) {
            level.add(new Brick(x, y));
            x += brickWidth + 10; // المسافة الأفقية بين الطوب
            if (x >= -startX) {  // الانتقال لسطر جديد عندما تصل إلى الحافة
                x = startX;
                y -= brickHeight + 10; // الانتقال رأسيًا لصف جديد
            }
        }
    }



    public void checkGameStatus() {
        // التحقق من إذا كانت كل الطوب قد تم تدميرها
        boolean allBricks = true;
        for (Brick brick : bricks) {
            if (brick.visible) {
                allBricks = false;
                break;
            }
        }

//        if (allBricks) {
//            BounceBall.animator.stop();
//            DrawSprite(0, 0, 102, 400, 400);
//
//        }
    }

    public void ball() {
        gl.glPushMatrix();
        gl.glTranslated(x_ball, y_ball, 0);
        DrawSprite(0, 0, 58, 15, 15);
        gl.glPopMatrix();

        if (startBall) {
            x_ball += dx;
            y_ball += dy;
        }
//        if (-hight >= y_ball) {
//            startBall = false;
//            x_ball = 0;
//            y_ball = -100;
//        }

        double r = 15;

        if (x_ball >= width - r || x_ball <= -width + r) {
            dx = -dx;

            playSound();

        }
        if (y_ball >= hight - r) {
            dy = -dy;

            playSound();

        }
        if (y_ball <= -hight + r) {
            // إعادة تعيين موقع الكرة
            x_ball = 0;
            y_ball = -100;
            x = 0; // إعادة تعيين موقع المضرب إذا لزم الأمر
            startBall = false; // إبقاء الكرة ثابتة حتى يبدأ المستخدم
            lives++; // زيادة عدد الأرواح المستخدمة
            System.out.println("عدد الأرواح: " + lives);
        }


        if (y_ball - r <= y + barHeight / 2 && y_ball + r >= y - barHeight / 2) {
            if (x_ball >= x - barWidth / 2 && x_ball <= x + barWidth / 2) {
                dy = -dy;


                dx = (x_ball - x) / (barWidth / 2) * 3;

                playSound();
            }
        }
    }


    public static void playSound() {
        try {
            File wavFile = new File("D:\\CS304\\src\\solid.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(wavFile));
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void DrawSprite(double x, double y, int index, float scale_x, float scale_y) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glScaled(scale_x, scale_y, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-(float) width - 20, -(float) hight - 20, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f((float) width + 20, -(float) hight - 20, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f((float) width + 20, (float) hight + 20, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-(float) width - 20, (float) hight + 20, -1.0f);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        int keycode = event.getKeyCode();
        if (x >= -width + 60 && keycode == KeyEvent.VK_LEFT) x -= 40;
        if (x <= width - 60 && keycode == KeyEvent.VK_RIGHT) x += 40;
        if (keycode == KeyEvent.VK_SPACE) startBall = true;
    }

    @Override
    public void keyReleased(final KeyEvent event) {
    }

    @Override
    public void keyTyped(final KeyEvent event) {
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("you click");

    }


    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(mouseX + " " + mouseY);
        isclicked = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("released");


    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("exited");
    }

    private double convertX(double x, double screenWidth) {
        return (x / screenWidth) * 1000 - 500;
    }

    private double convertY(double y, double screenHeight) {
        return 700 - (y / screenHeight) * 1400;
    }

    double mouseX = 0, mouseY = 0;

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

        mouseX = convertX(e.getX(), 2 * width);
        mouseY = convertY(e.getY(), 2 * hight);
    }
}
