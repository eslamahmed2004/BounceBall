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

class BallGLEventListener implements GLEventListener, KeyListener, MouseListener {

    File file = new File("C:\\CS304\\src\\PNG\\");
    String[] textureNames = file.list();
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];

    public static void main(String[] args) {
        new BounceBall();
    }

    double width = 700, hight = 500;
    GL gl;
    //    boolean h1=true;
//    boolean h2=true;
//    boolean h3=true;
    int count=0;

    long previousTime = System.nanoTime();  // حفظ الوقت الحالي عند بداية اللعبة
    int count2 = 0;
    int count3 = 0;
    final long oneSecondInNano = 1_000_000_000L; // ثابت يمثل ثانية واحدة بالنانوثانية


    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        GLU glu = new GLU();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture("C:\\CS304\\src\\PNG\\" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                glu.gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Set up orthographic projection
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-width, width, -hight, hight);
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }

    double x;
    double x_ball = 0, y_ball = -400;
    double dx = -5;
    double dy = 5;
    boolean startBall = false;

    long startTime = System.nanoTime();
    long elapsedTime;


    public void display(GLAutoDrawable gld) {
        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        // رسم الخلفية
        DrawBackground(70);

        // رسم الكائنات الأخرى
        for (int i = -400; i < 400; i += 45) {
            DrawSprite(i, 0, 3, 20, 10);
        }

        // رسم القلوب
//        if (h1){
//        DrawSprite(-390, 430, 60, 20, 20);}
//        if (h2){
//        DrawSprite(-510, 430, 60, 20, 20);}
//        if (h3){
//        DrawSprite(-630, 430, 60, 20, 20);}
//
//        if (count>=1){
//            h1=false;
//        }
//        if (count>=2){
//            h2=false;
//        }
//        if (count>=3){
//            h3=false;
//        }

        // رسم الكائنات المتبقية
        gl.glPushMatrix();
        gl.glTranslated(x, -450, 0);
        DrawSprite(0, -0, 50, 100, 15);
        gl.glPopMatrix();

        // رسم الكرة المتحركة
        gl.glPushMatrix();
        gl.glTranslated(x_ball, y_ball, 0);
        DrawSprite(0, 0, 58, 15, 15);
        gl.glPopMatrix();

        if (startBall) {
            x_ball += dx;
            y_ball += dy;
        }

        if (-hight >= y_ball) {
            startBall = false;
            x_ball = 0;
            y_ball = -400;

        }

        // إدارة الاصطدامات
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
            y_ball = -400;
            x = 0;
            startBall = false;
            dx = 3;
            dy = 3;
            count ++;
            System.out.println(count);
        }

        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - previousTime;

        // إذا مرَّت ثانية (1 ثانية = 1,000,000,000 نانوثانية)
        if (elapsedTime >= oneSecondInNano) {
            if (count2 == 10) {
                count2 = 0;
                count3++;
            }
            count2++; // زيادة المتغير count2
            previousTime = currentTime; // تحديث الوقت السابق

            // إذا وصل count2 إلى 9، نعيده إلى 0


            // رسم الصورة بناءً على count2
        }

        // رسم التايم
        DrawSprite(-350, 350, 76 + count2, 20, 20);// الاحاد
        DrawSprite(-400, 350, 77 + count3, 20, 20);// العشرات
        DrawSprite(-620, 350, 106, 20, 20); //T
        DrawSprite(-590, 350, 95, 5, 20);// ال i ال بايظة
        DrawSprite(-550, 350, 99, 20, 20);//M
        DrawSprite(-497, 350, 91, 20, 20);//E

        //رسم ال score
        DrawSprite(-620, 280, 105, 20, 20);
        DrawSprite(-570, 280, 89, 20, 20);
        DrawSprite(-520, 280, 101, 20, 20);
        DrawSprite(-470, 280, 104, 20, 20);
        DrawSprite(-420, 280, 91, 20, 20);




//        displayTime(gl, timeLeft);
//        for (int i = -100; i < 100; i += 45) {
//            DrawSprite(i, 0, 3, 20, 10);
//        }
//        gl.glPushMatrix();
//        gl.glTranslated(x, -450, 0);
//        DrawSprite(0, -0, 50, 100, 15);
//        gl.glPopMatrix();
//
//        gl.glPushMatrix();
//        gl.glTranslated(x_ball, y_ball, 0);
//        DrawSprite(0, 0, 58, 15, 15);
//        gl.glPopMatrix();
//
//        if (startBall) {
//            x_ball += dx;
//            y_ball += dy;
//        }
//        if (-hight >= y_ball) {
//            startBall = false;
//            x_ball = 0;
//            y_ball = -100;
//        }
//
//        double r = 15;
//
//        if (x_ball >= width - r || x_ball <= -width + r) {
//            dx = -dx;
//
//            playSound();
//
//        }
//        if (y_ball >= hight - r) {
//            dy = -dy;
//
//            playSound();
//
//        }
//        if (y_ball <= -hight + r) {
//            x_ball = 0;
//            y_ball = -100;
//            x = 0;
//            startBall = false;
//            dx = 3;
//            dy = 3;
//        }
//        // Bar dimensions and position
//        double barWidth = 200;
//        double barHeight = 15;
//        double barY = -450; // Bar's fixed Y position
//
//// Check collision with the bar
//        if (y_ball - r <= barY + barHeight / 2 && y_ball + r >= barY - barHeight / 2) {
//            if (x_ball >= x - barWidth / 2 && x_ball <= x + barWidth / 2) {
//                dy = -dy; // Reverse vertical direction
//                double barWight = 200;
//
//                dx = (x_ball - x) / (100 / 3);
//
//                playSound();
//            }
//        }
//
//
//        DrawSprite(-650, 450, 71, 30, 30);
    }

    public double sqrdDistance(double x, double y, double x1, double y1) {
        return Math.pow(x - x1, 2) + Math.pow(y - y1, 2);
    }

    // دالة لتشغيل الصوت
    public static void playSound() {
        try {
            File wavFile = new File("C:\\CS304\\src\\solid.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(wavFile));
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();
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

    // رسم الخلفية
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

    // KeyListener
    @Override
    public void keyPressed(final KeyEvent event) {
        System.out.println("key pressed");
        int keycode = event.getKeyCode();
        System.out.println(x);

        if (x >= -width + 60 && keycode == KeyEvent.VK_LEFT) x -= 40;
        if (x <= width - 60 && keycode == KeyEvent.VK_RIGHT) x += 40;
        if (x >= -width + 60 && keycode == KeyEvent.VK_LEFT) x -= 10;
        if (x <= width - 60 && keycode == KeyEvent.VK_RIGHT) x += 10;
        if (keycode == KeyEvent.VK_SPACE) startBall = true;
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        System.out.println("key released");
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        System.out.println("key typed ");
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // طباعة الإحداثيات
        System.out.println("Mouse clicked at: (" + x + ", " + y + ")");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("pressed");
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

    // دالة لعرض الوقت المتبقي
//    public void displayTime(GL gl, long timeleft) {
//        int secondleft = (int) (timeleft / 1_000_000_000L)+1;
//        String timeString = String.valueOf(secondleft);
//
//        // عرض كل رقم من الوقت المتبقي
//        for (int i = 0; i < timeString.length(); i++) {
//            char digit = timeString.charAt(i); // الحصول على الرقم كحرف
//            int digitIndex = digit - '0' + 73; // حساب المؤشر المناسب للرقم
//
//            // التأكد من أن digitIndex ضمن النطاق المسموح به
//            if (digitIndex >= textures.length+2) {
//                digitIndex = 73; // إعادة التعيين إلى أول رقم
//            }
//
//            // عرض الرقم في المكان المناسب
//            DrawSprite(-100 + (i * 30), 400, digitIndex, 20, 20);
//
//        }
//    }
}
