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

    File file = new File("D:\\CS304\\src\\PNG\\");
    String[] textureNames = file.list();
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];

    public static void main(String[] args) {
        new BounceBall();
    }

    double width = 700, hight = 500;
    GL gl;

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        GLU glu = new GLU();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture("D:\\CS304\\src\\PNG\\" + textureNames[i], true);
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
        DrawSprite(-390, 430, 60, 50, 50);
        DrawSprite(-510, 430, 60, 50, 50);
        DrawSprite(-630, 430, 60, 50, 50);

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
        }

        elapsedTime = System.nanoTime() - startTime;
        long timeLeft = 6_000_000_000L - elapsedTime;

//        displayTime(gl, timeLeft);
    }

    // دالة لتشغيل الصوت
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

    // دالة رسم الصورة
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
        System.out.println("you click");
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
    public void displayTime(GL gl, long timeleft) {
        int secondleft = (int) (timeleft / 1_000_000_000L)+1;
        String timeString = String.valueOf(secondleft);

        // عرض كل رقم من الوقت المتبقي
        for (int i = 0; i < timeString.length(); i++) {
            char digit = timeString.charAt(i); // الحصول على الرقم كحرف
            int digitIndex = digit - '0' + 73; // حساب المؤشر المناسب للرقم

            // التأكد من أن digitIndex ضمن النطاق المسموح به
            if (digitIndex >= textures.length+2) {
                digitIndex = 73; // إعادة التعيين إلى أول رقم
            }

            // عرض الرقم في المكان المناسب
            DrawSprite(-100 + (i * 30), 400, digitIndex, 20, 20);

        }
    }
}
