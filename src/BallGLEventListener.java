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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BallGLEventListener implements GLEventListener, KeyListener, MouseListener {
    /**** get texture ****/
    File file = new File("src/PNG");
    String[] textureNames = file.list();
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    /**********************/


    int[] pageNumbers = new int[14];
    File users = new File("src/UsersHighScore");
    List<User> usersList = new ArrayList<>();

    PrintWriter inputFile = new PrintWriter(users) ;
    Scanner input = new Scanner(System.in) ;
    Scanner output = new Scanner(users) ;
    User[] players = new User[2] ;


    /*
     5 means gun in array pos
     x and y coordinate for gun
     */
    double width = 700, hight = 500;
    GL gl;
    double x;
    double x_ball = 0, y_ball = -400;
    double dx = 0;
    double dy = 5;
    boolean startBall = false;
    double x_meteor = -750, y_meteor = Math.random()*1000-500 ;
    boolean nameIsExit ;

    BallGLEventListener() throws FileNotFoundException {
    }

    public static void main(String[] args) throws FileNotFoundException {
        new BounceBall();
        BounceBall.animator.start();
    }

    public static void playSound() {
        try {
            File wavFile = new File("src/solid.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(wavFile));
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        GLU glu = new GLU();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(file + "\\" + textureNames[i], true);
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

        /** add old users **/
        for (int i = 0; i < usersList.size(); i++) {
            usersList.get(i).name = output.next() ;
            usersList.get(i).highestScore = output.nextInt() ;
        }
    }

    public void display(GLAutoDrawable gld) {
        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        background();

        /** Bar **/
        gl.glPushMatrix();
        gl.glTranslated(x, -450, 0);
        DrawSprite(0, -0, 50, 100, 15);
        gl.glPopMatrix();
        /** Draw ball and ball collision **/
        ball();
        highScore();
    }

    public void typingName(){
        String name = input.next() ;
        User user = new User(name) ;
        User newUser = new User(name) ;
        newUser.name = name ;

        for (int i = 0; i < usersList.size()-1; i++) {
           if (usersList.get(i).name.equals(name)) {
               usersList.remove(usersList.size()-1) ;
               nameIsExit = true ;
               System.out.println("this name is exist");
           }
        }

        if (!nameIsExit) {
            inputFile.println(name + " " + user.highestScore);

        }
    }
    public void highScore(){
        DrawSprite(0 , 0 , 70 , 300,300);


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
            dx = 0;
            dy = 3;
        }
        double barWidth = 200;
        double barHeight = 15;
        double barY = -450;

        if (y_ball - r <= barY + barHeight / 2 && y_ball + r >= barY - barHeight / 2) {
            if (x_ball >= x - barWidth / 2 && x_ball <= x + barWidth / 2) {
                dy = -dy;


                dx = (x_ball - x) / (100/3);

                playSound();
            }
        }
    }

    public void background() {
        DrawBackground(68);
        DrawSprite(x_meteor - 50, y_meteor, 62, 30, 30);
        DrawSprite(x_meteor -100, y_meteor - 1000, 62, 30, 30);
        x_meteor += 5;
        y_meteor += 5;
        if (x_meteor >= 700) {
            x_meteor = (int)(Math.random()*700-1400);
            y_meteor = (int)(Math.random()*500-1000);
        }
        DrawSprite(-650, 450, 0, 30, 30);
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
        System.out.println("key pressed");
//        System.out.println(keyCode);
        int keycode = event.getKeyCode();
        System.out.println(x);

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
        // don't care
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
        System.out.println("presed");

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

    private double convertX(double x, double width) {

        return x - this.width;
    }

    private double convertY(double y, double height) {
        return hight - y;
    }

}