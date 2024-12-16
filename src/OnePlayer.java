import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;

public class OnePlayer {
    boolean oneplayer = false;
    double x;
    double x_ball = 0, y_ball = -100;
    double dx = -5;
    double dy = 5;
    boolean startBall = false;
    boolean isPaused = false;
    double mouseX;
    double mouseY;

    boolean isclicked = false;
    int ai_x;
    boolean ai =false;
    double width = 700, hight = 500;
    GL gl;
    GLU glu = new GLU();
    File file = new File("C:\\BounceBall\\src\\PNG");
    String[] textureNames = file.list();
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
     OnePlayer(){
         for (int i = 0; i < textureNames.length; i++) {
                 try {
                     texture[i] = TextureReader.readTexture("C:\\BounceBall\\src\\PNG\\" + textureNames[i], true);
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
             gl.glPushMatrix();
             gl.glTranslated(  x, -450, 0);
             DrawSprite(0, -0, 50, 100, 15);
             gl.glPopMatrix();

             gl.glPushMatrix();
             gl.glTranslated(x_ball, y_ball, 0);
             DrawSprite(0, 0, 58, 15, 15);
             gl.glPopMatrix();
             if (!isPaused) {
                 if (startBall) {
                     x_ball += dx;
                     y_ball += dy;
                 }
                 if (-hight >= y_ball) {
                     startBall = false;
                     x_ball = 0;
                     y_ball = -100;
                 }
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
                 }
                 // Bar dimensions and position
                 double barWidth = 200;
                 double barHeight = 15;
                 double barY = -450; // Bar's fixed Y position

// Check collision with the bar
                 if (y_ball - r <= barY + barHeight / 2 && y_ball + r >= barY - barHeight / 2) {
                     if (x_ball >= x - barWidth / 2 && x_ball <= x + barWidth / 2) {
                         dy = -dy; // Reverse vertical direction
                         double barWight = 200;

                         dx = (x_ball - x) / (100 / 3);

                         playSound();
                     }
                 }
             } else {

                 DrawSprite(0, 0, 72, 400, 400);
//gl.glColor3f(1,1,1);
//gl.glBegin(GL.GL_POLYGON);
//    gl.glVertex2d(-65,270);
//    gl.glVertex2d(145,270);
//    gl.glVertex2d(145,-310);
//    gl.glVertex2d(-65,-310);
//gl.glEnd();
//
//        gl.glBegin(GL.GL_POLYGON);
//        gl.glVertex2d(165,270);
//        gl.glVertex2d(375,270);
//        gl.glVertex2d(375,-310);
//        gl.glVertex2d(165,-310);
//        gl.glEnd();
//    gl.glBegin(GL.GL_POLYGON);
//    gl.glVertex2d(-295,270);
//    gl.glVertex2d(-85,270);
//    gl.glVertex2d(-85,-310);
//    gl.glVertex2d(-295,-310);
//    gl.glEnd();
             }
             if (isclicked) {
                 isclicked = false;
                 if (mouseX >= -295 && mouseX <= -85 && mouseY >= -310 && mouseY <= 270) {
                     isPaused = false;
                 }
                 DrawSprite(-650, 450, 71, 30, 30);
             }
         }

    public static void playSound() {
        try {
            File wavFile = new File("C:\\BounceBall\\src\\solid.wav");
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
        // Adjust translation to fit the new orthographic projection
        gl.glTranslated(x, y, 0);
        gl.glScaled(scale_x, scale_y, 1); // Adjusted scaling factor
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
}

