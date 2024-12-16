import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BallGLEventListener implements GLEventListener, KeyListener, MouseListener {

    // ملف يحتوي على جميع الصور المستخدمة كخلفيات وقوام
    File file = new File("C:\\BounceBall\\src\\PNG\\");
    String[] textureNames = file.list();
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];

    // نقطة البداية لتشغيل اللعبة
    public static void main(String[] args) {
        new BounceBall(); // إنشاء نافذة اللعبة
        BounceBall.animator.start(); // بدء عملية التحديث المستمر للشاشة
    }

    double width = 700, hight = 900; // أبعاد شاشة اللعبة
    GL gl;
    List<Brick> bricks = new ArrayList<>(); // قائمة تحتوي على جميع الطوب
    double brickWidth = 200, brickHeight = 80; // أبعاد الطوب
    int count = 0;
    boolean h1 = true;
    boolean h2 = true;
    boolean h3 = true;

    // تعريف الطوب ككائن مستقل
    class Brick {
        double x, y; // إحداثيات الطوب
        boolean visible; // حالة ظهور الطوب

        Brick(double x, double y) {
            this.x = x;
            this.y = y;
            this.visible = true; // الطوب مرئي عند إنشائه
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
                texture[i] = TextureReader.readTexture("C:\\BounceBall\\src\\PNG\\" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, texture[i].getWidth(), texture[i].getHeight(), GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture[i].getPixels());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // إنشاء شبكة الطوب
        for (double i = -600; i <= 600; i += brickWidth + 20) {
            for (double j = 300; j <= 400; j += brickHeight + 10) {
                bricks.add(new Brick(i, j));
            }
        }

        // إعدادات العرض ثنائية الأبعاد
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-width, width, -hight, hight);
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }

    double x = 0, y = -700; // موقع المضرب
    double x_ball = 0, y_ball = -400; // موقع الكرة
    double dx = -7, dy = 7; // اتجاه وسرعة الكرة
    boolean startBall = false; // حالة بدء تحرك الكرة
    double barWidth = 260; // عرض المضرب
    double barHeight = 15; // عرض المضرب

    @Override
    public void display(GLAutoDrawable gld) {
        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // مسح الشاشة
        gl.glLoadIdentity(); // إعادة تعيين المصفوفة

        DrawBackground(68); // رسم الخلفية

        // رسم الطوب
        for (Brick brick : bricks) {
            if (brick.visible) { // يتم رسم الطوب فقط إذا كان مرئيًا
                gl.glPushMatrix();
                gl.glTranslated(brick.x, brick.y, 0);
                DrawSprite(0, 0, 1, (float) (brickWidth / 5), (float) (brickHeight / 5));
                gl.glPopMatrix();
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
        if (h1) {
            DrawSprite(150, 850, 60, 20, 20);
        }
        if (h2) {
            DrawSprite(200, 850, 60, 20, 20);
        }
        if (h3) {
            DrawSprite(250, 850, 60, 20, 20);
        }

        if (count >= 1) {
            h1 = false;
        }
        if (count >= 2) {
            h2 = false;

        }
        if (count >= 3) {
            h3 = false;
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

        if (allBricks) {
            // إذا تم تدمير كل الطوب، يعتبر اللاعب فائزًا
            System.out.println("فزت! لقد دمرت كل الطوب.");
        }
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
            x_ball = 0;
            y_ball = -100;
            x = 0;
            startBall = false;
            dx = 3;
            dy = 3;
                        System.out.println( "before"+count);

            count++;
            System.out.println(count);
        }


        if (y_ball - r <= y + barHeight / 2 && y_ball + r >= y - barHeight / 2) {
            if (x_ball >= x - barWidth / 2 && x_ball <= x + barWidth / 2) {
                dy = -dy;


                dx = (x_ball - x) / (barWidth / 2) * 3;

                playSound();
            }
        }
    }

    private void resetBall() {
        // إعادة تعيين الكرة إلى وضع البداية
        x_ball = 0;
        y_ball = -400;
        dx = (Math.random() > 0.5 ? 5 : -5); // اتجاه عشوائي أفقي
        dy = 5; // اتجاه ثابت رأسي
        startBall = true;
    }

    public static void playSound() {
        try {
            File wavFile = new File( "C:\\BounceBall\\src\\\\solid.wav");
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
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
