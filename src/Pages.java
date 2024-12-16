import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

class Pages implements GLEventListener, KeyListener, MouseListener {
    JFrame frame = new JFrame("Button Example");
    private String username = "";
    private String SecondNamee= "";


    File file = new File("C:\\BounceBall\\src\\PNG");
    String[] textureNames = file.list();
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];



    /*
     5 means gun in array pos
     x and y coordinate for gun
     */
    double width = 700, hight = 500;
    GL gl;

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        GLU glu = new GLU();

        gl.glClearColor(0f, 00f, 0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

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



        // Set up orthographic projection
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-width, width, -hight, hight);
        gl.glMatrixMode(GL.GL_MODELVIEW);

    }

    double x;
    double x_ball = 0, y_ball = -100;
    double dx = -5;
    double dy = 5;
    boolean startBall = false;

    public void display(GLAutoDrawable gld) {

        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        HighscoreList();
//        Entername();
//        Name2Player();
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


    public void Start() {
        DrawBackground(73); // رسم الخلفية
        DrawSprite(0,0,74,300,100);
//        Entername();
//        if(!username.isEmpty()) {
//            Home();
//        }

    }

    public void Entername() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Enter Username");
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Enter your username: ");
        JTextField textField = new JTextField();
        JButton submitButton = new JButton("Enter");

        submitButton.addActionListener(e -> {
            username = textField.getText();
            System.out.println("Username entered: " + username);
            dialog.dispose();
            Home();
        });

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);

        dialog.add(panel);

        dialog.setModal(true);
        dialog.setVisible(true);



    }

    public void Home() {
        DrawBackground(75);

    }
    public void HighscoreList(){

        DrawSprite(-550,300,78,30,30);



    }
    public void instruction() {
        DrawBackground(76);
        DrawSprite(550,-400,77,100,30);


    }


    public void Name2Player() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Enter SecondNamee");
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Enter SecondNamee: ");
        JTextField textField = new JTextField();
        JButton submitButton = new JButton("Enter");

        submitButton.addActionListener(e -> {
            SecondNamee = textField.getText();
            System.out.println("SecondNamee entered: " + SecondNamee);
            dialog.dispose();
            Home();
        });

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);

        dialog.add(panel);

        dialog.setModal(true);
        dialog.setVisible(true);


    }




    @Override
    public void keyPressed(final KeyEvent event) {

    }

    @Override
    public void keyReleased(final KeyEvent event) {

    }

    @Override

    public void keyTyped(final KeyEvent event) {

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
        System.out.println(x);

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