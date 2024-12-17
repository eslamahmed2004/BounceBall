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

import static java.awt.SystemColor.info;

class
BallGLEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

    File file = new File("C:\\Bounce-Ball\\src\\PNG\\");
    String[] textureNames = file.list();
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];

    public static void main(String[] args) {
        new BounceBall();
        BounceBall.animator.start();
    }

    /*
     5 means gun in array pos
     x_singleand y coordinate for gun
     */
    double width = 700, hight = 500;
    GL gl;
    List<Brick> level1 = new ArrayList<>();
    List<Brick> level2 = new ArrayList<>();
    List<Brick> level3 = new ArrayList<>();
    List<Brick> level4 = new ArrayList<>();
    List<Brick> level5 = new ArrayList<>();
    List<Brick> level6 = new ArrayList<>();
    List<List> levels = new ArrayList<>();

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        GLU glu = new GLU();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture("C:\\Bounce-Ball\\src\\PNG\\" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, texture[i].getWidth(), texture[i].getHeight(), GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture[i].getPixels());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-width, width, -hight, hight);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        for (double i = -600; i <= 600; i += brickWidth + 20) {
            for (double j = 300; j <= 400; j += brickHeight + 10) {
                bricks.add(new Brick(i, j));
            }
        }

        levels.add(level1);
        levels.add(level2);
        levels.add(level3);
        levels.add(level4);
        levels.add(level5);
        levels.add(level6);


        initLevels(-600, 350, 32, level1, 5);
        initLevels(-650, 380, 45, level2, 11);
        initLevels(-650, 380, 54, level3, 17);
        initLevels(-650, 380, 63, level4, 15);
        initLevels(-650, 380, 72, level5, 7);
        countBricks = level1.size();

    }
    long previousTime = System.nanoTime();  // حفظ الوقت الحالي عند بداية اللعبة
    int count2Time = 0;
    int count3Time = 0;
    int count3Score = 0;
    int count4Score =0;
    int count4Time=0;

    int saveTime1=0;
    int saveTime2=0;



    boolean ClearOld = false;
    boolean ClearNew = false;

    final long oneSecondInNano = 1_000_000_000L; // ثابت يمثل ثانية واحدة بالنانوثانية

    long currentTime = System.nanoTime();
    long elapsedTime = currentTime - previousTime;

    double x;
    double y =-450;
    double x_ball = 0, y_ball = -400;
    double dx = -5;
    double dy = 5;
    boolean startBall = false;
    boolean start = false;
    long startTime = System.nanoTime();
    boolean oneplayer = false;
    List<Brick> bricks = new ArrayList<>(); // قائمة تحتوي على جميع الطوب
    double brickWidth = 200, brickHeight = 80; // أبعاد الطوب
    double x_single = 0;
    double x_single_ball1 = 0, y_single_ball1 = -100;
    double dx_single = -5;
    double dy_single = 5;
    boolean isGameWon = false;
    boolean isGameLost = false;
    boolean isPaused = false;
    double mouseX;
    double mouseY;
    boolean isclicked = false;
    boolean ai = false;

    boolean h1 = true;
    boolean h2 = true;
    boolean h3 = true;
    double barWidth = 200;
    double barHeight = 15;
    double barY = -450;
    double x_ai_1 = 250;
    double x_ai_2 = -250;
    double x_ai_ball1 = 250, y_ai_ball1 = -100;
    double x_ai_ball2 = -250, y_ai_ball2 = -100;
    double dx_ai_ball1 = -5;
    double dy_ai_ball1 = 5;
    double dx_ai_ball2 = -5;
    double dy_ai_ball2 = 5;
    boolean choose = false;
    boolean multiplayer = false;
    double x_multi_1 = 250;
    double x_multi_2 = -250;
    double x_multi_ball1 = 250, y_multi_ball1 = -100;
    double x_multi_ball2 = -250, y_multi_ball2 = -100;
    double dx_multi_ball1 = -5;
    double dy_multi_ball1 = 5;
    double dx_multi_ball2 = -5;
    double dy_multi_ball2 = 5;
    int count = 0;
    int lives = 0;
    int brokenBricks = 0;
    int countBricks;
    int level = 1;
    boolean heart1 = true;
    boolean heart2 = true;
    boolean heart3 = true;
    boolean next = false;
    boolean restart = false;
    boolean exit = false;
    boolean info = true;

    public void display(GLAutoDrawable gld) {
        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        start();
    }
    void initLevels(int startX, int startY, int n, List<Brick> level, int index) {
        int endX = -startX;
        for (int i = 0; i < n; i++) {
            if (startX >= endX) {
                startX = -endX;
                startY -= 75;
            }
            level.add(new Brick(startX + 50, startY, index));
            startX += 150;
        }
    }


    void drawLevels(List<Brick> level) {
        for (Brick brick : level) {
            if (brick.visible) {
                DrawSprite(brick.x, brick.y, brick.index, 30, 15);
            }
        }
    }


    public void drawPaddle(GL gl, double x, double y) {
        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        DrawSprite(0, 0, 50, 110, 15);
        gl.glPopMatrix();
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


        if (y_ball <= -hight + r) {
            x_ball = 0;
            y_ball = -400;

            x_single = 0;
            startBall = false;
            lives++;
            System.out.println("عدد الأرواح: " + lives);
        }


        if (y_ball - r <= y + barHeight / 2 && y_ball + r >= y - barHeight / 2) {
            if (x_ball >= x_single - barWidth / 2 && x_ball <= x_single + barWidth / 2) {
                dy = -dy;


                dx = (x_ball - x_single) / (barWidth / 2) * 3;

                playSound();
            }
        }
    }


    public void checkBallCollision(double x_ball, double y_ball, double width_right, double width_left, double height) {
        if (x_ball >= width_right - 15 || x_ball <= width_left + 15) {
            dx = -dx;
            playSound();
        }
        if (y_ball >= height - 15) {
            dy = -dy;
            playSound();
        }
    }

    public void checkBrickCollision(int levelIndex, double x_ball, double y_ball, double brickWidth, double brickHeight) {
        List<Brick> level = levels.get(levelIndex);
        for (Brick brick : level) {
            if (brick.visible) {
                double left = brick.x - brickWidth / 5;
                double right = brick.x + brickWidth / 5;
                double top = brick.y + brickHeight / 5;
                double bottom = brick.y - brickHeight / 5;

                if (x_ball + 15 > left && x_ball - 15 < right && y_ball + 15 > bottom && y_ball - 15 < top) {

                    if (x_ball + 10 >= left && x_ball - 10 <= right) {
                        dy = -dy;
                    } else {
                        dx = -dx;
                    }
                    brick.visible = false;
                    brokenBricks++;
                    playSound();
                    increaseSpeed();
                    break;
                }
            }
        }
    }

    public void increaseSpeed() {
        if (brokenBricks % 5 == 0 && brokenBricks > 0) {

            dx += 0.01 * dx;
            dy += 0.01 * dy;
        }
    }



    public void drawHearts() {
        if (heart1) {
            DrawSprite(200, 450, 60, 20, 20);
        }
        if (heart2) {
            DrawSprite(250, 450, 60, 20, 20);
        }
        if (heart3) {
            DrawSprite(300, 450, 60, 20, 20);
        }
    }

    public void checkGameStatusAndLives(int lives) {
        if (lives >= 1) {
            heart1 = false;
        }
        if (lives >= 2) {
            heart2 = false;
        }
        if (lives >= 3) {
            heart3 = false;
            isGameLost = true;
            DrawSprite(0, 0, 75, 400, 400);
        }


        boolean allBricksDestroyed = true;
        for (Brick brick : level1) {
            if (brick.visible) {
                allBricksDestroyed = false;
                break;
            }
        }


        if (allBricksDestroyed) {
            isGameWon = true;
            BounceBall.animator.stop();
            DrawSprite(0, 0, 74, 400, 400);
        }

        drawHearts();
        Time2();
        Score();


    }
    public void Score(){


        if (restart == true) {
            count3Score = 0;
            count4Score = 0;
            restart = false;
        }
        for (Brick brick : level1) {
            if (!brick.visible) {
                count3Score++;
                break;
            }
        }

        if (count3Score > 9) {
            count3Score = 0;
            count4Score++;
        }

        if (count4Score > 9) {
            count4Score = 0;
        }

        // رسم القيم
        DrawSprite(130, 450, 76 + count3Score, 20, 20);
        DrawSprite(80, 450, 76 + count4Score, 20, 20);
    }


    public void handleEndGameScreen(double mouseX, double mouseY) {
        if (isclicked) {
            isclicked = false;


            if (isGameWon) {
                if (mouseX >= -279 && mouseX <= -23 && mouseY <= -218 && mouseY >= -459) {
                    choose = true;
                    next = true;
                    level++;
                }
            }


            if (isGameLost) {
                if (mouseX >= -279 && mouseX <= -23 && mouseY <= -184 && mouseY >= -376) {
                    choose = true;
                    restart = true;
                }


                if (mouseX >=9 && mouseX <= 151 && mouseY <= -184 && mouseY >= -376) {
                    choose = true;
                    exit = true;

                }
            }


            if (choose) {
                if (exit) {
                    System.exit(0);
                } else if (restart) {
                    resetLevel();
                } else if (next) {
                    level++;
                    if (level > 6) {
                        level = 1;
                    }
                    resetLevel();
                }
            }
        }
    }


    public void resetLevel() {
        levels.get(level -1).clear();
        // إعادة تعيين الكرة والمضرب
        x_ball = 0;
        y_ball = -400;
        dx = -4;
        dy = 4;
        startBall = false;
        lives = 0;
        heart1 = true;
        heart2 = true;
        heart3 = true;
        brokenBricks = 0;

        // إعادة تعيين المستويات
        if (level == 1) {
            initLevels(-600, 350, 32, level1, 5);
        } else if (level == 2) {
            initLevels(-650, 380, 45, level2, 11);
        } else if (level == 3) {
            initLevels(-650, 380, 54, level3, 17);
        } else if (level == 4) {
            initLevels(-650, 380, 63, level4, 15);
        } else if (level == 5) {
            initLevels(-650, 380, 72, level5, 7);
        } else if (level == 6) {
            initLevels(-650, 380, 72, level6, 11);
        }

        // تحديث العدد الإجمالي للطوب
        countBricks = levels.get(level - 1).size();

        // إعادة تعيين حالة الفوز والخسارة
        isGameLost = false;
        isGameWon = false;
    }

    public static void playSound() {
        try {
            File wavFile = new File("C:\\Bounce-Ball\\src\\solid.wav");
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


    public void Time2() {
        if (restart) {
            count2Time = 0;
            count3Time = 0;
            count3Score=0;
            count4Score=0;
            count4Time=0;
            previousTime = System.nanoTime();
            restart = false;
        }

        long currentTime = System.nanoTime(); // الحصول على الوقت الحالي

        if (currentTime - previousTime >= oneSecondInNano) {
            previousTime = currentTime;

            if (!isGameLost) {
                count2Time++;
            }

            if (count2Time >= 10) {
                count2Time = 0;
                count3Time++;
            }


            if (count3Time >= 10) {
                count3Time = 0;
                count4Time++;
            }

            System.out.println("Count2: " + count2Time + ", Count3: " + count3Time);
        }

        DrawSprite(-330, 450, 76 + count2Time, 20, 20);
        DrawSprite(-380, 450, 76 + count3Time, 20, 20);
        DrawSprite(-430, 450, 76 + count4Time, 20, 20);


        DrawSprite(-620, 450, 105, 20, 20); // T
        DrawSprite(-590, 450, 94, 5, 20);   // i
        DrawSprite(-550, 450, 98, 20, 20); // M
        DrawSprite(-497, 450, 90, 20, 20); // E

        DrawSprite(-200, 450, 104, 20, 20); // S
        DrawSprite(-150, 450, 88, 20, 20);  // C
        DrawSprite(-100, 450, 100, 20, 20); // O
        DrawSprite(-50, 450, 103, 20, 20); // R
        DrawSprite(0, 450, 90, 20, 20);   // E
    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }



    @Override
    public void keyPressed(final KeyEvent event) {
        System.out.println("key pressed");
//        System.out.println(keyCode);
        int keycode = event.getKeyCode();
        System.out.println(x_single);
        if (oneplayer) {
            if (!isPaused) {
                if (x_single >= -width + 60 && keycode == KeyEvent.VK_LEFT) x_single -= 50;
                if (x_single <= width - 60 && keycode == KeyEvent.VK_RIGHT) x_single += 50;
            }
        }
        if (keycode == KeyEvent.VK_SPACE) startBall = true;
        if (keycode == KeyEvent.VK_ESCAPE) isPaused = !isPaused;
        if (ai) {
            if (!isPaused) {
                if (x_ai_1 >= 0 + 60 && keycode == KeyEvent.VK_LEFT) x_ai_1 -= 10;
                if (x_ai_1 <= width - 60 && keycode == KeyEvent.VK_RIGHT) x_ai_1 += 10;

            }
        }
        if (multiplayer) {
            if (!isPaused) {
                if (x_multi_1 >= 0 + 60 && keycode == KeyEvent.VK_LEFT) x_multi_1 -= 10;
                if (x_multi_1 <= width - 60 && keycode == KeyEvent.VK_RIGHT) x_multi_1 += 10;
                if (x_multi_2 >= -width + 60 && keycode == KeyEvent.VK_A) x_multi_2 -= 10;
                if (x_multi_2 <= 0 - 60 && keycode == KeyEvent.VK_D) x_multi_2 += 10;
            }
        }
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

    public void mouseMoved(MouseEvent e) {

        mouseX = convertX(e.getX(), 2 * width);
        mouseY = convertY(e.getY(), 2 * hight);
    }
    public void start(){
        DrawBackground(114);
        if(isclicked&&mouseX<=135&&mouseX>=-151&&mouseY<=370&&mouseY>=231) {
            isclicked=false;
            start=true;
        }
        if (!info)
            start=false;

        if(start){
            if (!choose) {
                DrawBackground(72);
                gl.glColor3f(1, 1, 1);

                if (isclicked) {
                    isclicked = false;
                    if (mouseX >= -200 && mouseX <= 200 && mouseY >= 133 && mouseY <= 285) {
                        choose=true;
                        oneplayer=true;
                    }
                    if (mouseX >= -200 && mouseX <= 200 && mouseY >= -50 && mouseY <= 85) {
                        choose=true;
                        multiplayer = true;

                    }
                    if (mouseX >= -200 && mouseX <= 200 && mouseY >= -240 && mouseY <= -100) {
                        choose=true;
                        ai=true;
                    }


                }


            }
            if (choose) {


                DrawBackground(68);
                if (oneplayer) {
                    if(!isPaused) {

                        if (level == 1) {
                            drawLevels(level1); // عرض المستوى الأول
                        } else if (level == 2) {
                            drawLevels(level2); // عرض المستوى الثاني
                        } else if (level == 3) {
                            drawLevels(level3); // عرض المستوى الثالث
                        } else if (level == 4) {
                            drawLevels(level4); // عرض المستوى الرابع
                        } else if (level == 5) {
                            drawLevels(level5); // عرض المستوى الخامس
                        } else if (level == 6) {
                            drawLevels(level6); // عرض المستوى السادس
                        }

                        drawPaddle(gl, x_single, y);

                        ball();

                        if (startBall) {
                            x_ball += dx;
                            y_ball += dy;
                        }

                        checkBallCollision(x_ball, y_ball, width,-width, hight);

                        checkBrickCollision(level - 1, x_ball, y_ball, brickWidth, brickHeight);

                        checkGameStatusAndLives(lives);


                        handleEndGameScreen(mouseX, mouseY);
                    }
                    else{

                        DrawSprite(0, 0, 73, 400, 400);
                    }
                    if (isclicked) {
                        isclicked = false;
                        if (mouseX >= -295 && mouseX <= -85 && mouseY >= -310 && mouseY <= 270) {
                            isPaused = false;
                        } else if (mouseX >= 115 && mouseX <= 250 && mouseY >= -360 && mouseY <= 380) {
                            isPaused = false;
                            oneplayer = false;
                            ai = false;
                            choose = false;
                        }
                    }
                }



                if (ai) {
                    for (Brick brick2 : bricks) {
                        if (brick2.visible) { // يتم رسم الطوب فقط إذا كان مرئيًا
                            gl.glPushMatrix();
                            gl.glTranslated(brick2.x, brick2.y, 0);
                            DrawSprite(0, 0, 1, (float) (brickWidth / 5), (float) (brickHeight / 5));
                            gl.glPopMatrix();
                        }
                    }

                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex2d(0,hight);
                    gl.glVertex2d(0,-hight);
                    gl.glEnd();

                    //player
                    gl.glPushMatrix();
                    gl.glTranslated(x_ai_1, -450, 0);
                    DrawSprite(0, 0, 50, 100, 15);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(x_ai_ball1, y_ai_ball1, 0);
                    DrawSprite(0, 0, 58, 15, 15);
                    gl.glPopMatrix();
                    //ai
                    gl.glPushMatrix();
                    gl.glTranslated(x_ai_2 = x_ai_ball2+70, -450, 0);
                    DrawSprite(0, -0, 50, 100, 15);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(x_ai_ball2, y_ai_ball2, 0);
                    DrawSprite(0, 0, 58, 15, 15);

                    gl.glPopMatrix();
                    if (!isPaused) {
                        x_ai_2 = x_ai_ball2;
                        if (startBall) {
                            x_ai_ball1 += dx_ai_ball1;
                            y_ai_ball1 += dy_ai_ball1;
                            x_ai_ball2 += dx_ai_ball2;
                            y_ai_ball2 += dy_ai_ball2;
                        }
                        //القاع
                        if (-hight >= y_ai_ball1 || -hight >= y_ai_ball2) {
                            startBall = false;
                            x_ai_ball1 = 250;
                            x_ai_ball2 = -250;
                            y_ai_ball1 = -100;
                            y_ai_ball2 = -100;
                        }
                        //الحيطان
                        double r = 15;
                        if  (x_ai_ball1 >= width - r || x_ai_ball1 <= 0 + r) {
                            dx_ai_ball1 = -dx_ai_ball1;

                            playSound();

                        }
                        if (x_ai_ball2 >= 0 - r || x_ai_ball2 <= -width + r) {
                            dx_ai_ball2 = -dx_ai_ball2;

                            playSound();

                        }
                        if (y_ai_ball1 >= hight - r) {
                            dy_ai_ball1 = -dy_ai_ball1;

                            playSound();

                        }
                        if (y_ai_ball2 >= hight - r) {
                            dy_ai_ball2 = -dy_ai_ball2;

                            playSound();

                        }
                        if (y_ai_ball1 <= -hight + r ) {
                            DrawSprite(0, 0, 74, 400, 400);

                        }
                        // Bar dimensions and position
                        double barWidth = 200;
                        double barHeight = 15;
                        double barY = -450; // Bar's fixed Y position
                        for (Brick brick2 : bricks) {
                            if (brick2.visible) {
                                double left = brick2.x - brickWidth / 5;
                                double right = brick2.x + brickWidth / 5;
                                double top = brick2.y + brickHeight / 5;
                                double bottom = brick2.y - brickHeight / 5;

                                if (x_ai_ball1 + 15 > left && x_ai_ball1 - 15 < right && y_ai_ball1 + 15 > bottom && y_ai_ball1 - 15 < top) {
                                    // التحقق من موقع التصادم لتحديد اتجاه الكرة
                                    if (x_ai_ball1 + 10 >= left && x_ai_ball1 - 10 <= right) {
                                        dy_ai_ball1 = -dy_ai_ball1;
                                    } else {
                                        dx_ai_ball1 = -dx_ai_ball1;
                                    }
                                    brick2.visible = false; // إخفاء الطوب
                                    playSound();
                                    break;
                                }
                                if (x_ai_ball2 + 15 > left && x_ai_ball2 - 15 < right && y_ai_ball2 + 15 > bottom && y_ai_ball2 - 15 < top) {
                                    // التحقق من موقع التصادم لتحديد اتجاه الكرة
                                    if (x_ai_ball2 + 10 >= left && x_ai_ball2 - 10 <= right) {
                                        dy_ai_ball2 = -dy_ai_ball2;
                                    } else {
                                        dx_ai_ball2 = -dx_ai_ball2;
                                    }
                                    brick2.visible = false; // إخفاء الطوب
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


// Check collision with the bar
                        if (y_ai_ball1 - r <= barY + barHeight / 2 && y_ai_ball1 + r >= barY - barHeight / 2) {
                            if (x_ai_ball1 >= x_ai_1 - barWidth / 2 && x_ai_ball1 <= x_ai_1 + barWidth / 2) {
                                dy_ai_ball1 = -dy_ai_ball1; // Reverse vertical direction
                                double barWight = 200;

                                dx_ai_ball1 = (x_ai_ball1 - x_ai_1) / (100 / 3);

                                playSound();
                            }
                        }
                        if (y_ai_ball2 - r <= barY + barHeight / 2 && y_ai_ball2 + r >= barY - barHeight / 2) {
                            if (x_ai_ball2 >= x_ai_2 - barWidth / 2 && x_ai_ball2 <= x_ai_2 + barWidth / 2) {
                                dy_ai_ball2 = -dy_ai_ball2; // Reverse vertical direction
                                double barWight = 200;

                                dx_ai_ball2 = (x_ai_ball2 - x_ai_2) / (100 / 3);

                                playSound();
                            }
                        }

                    } else {

                        DrawSprite(0, 0, 72, 400, 400);
//                gl.glColor3f(1, 1, 1);
//                gl.glBegin(GL.GL_POLYGON);
//                gl.glVertex2d(-65, 270);
//                gl.glVertex2d(145, 270);
//                gl.glVertex2d(145, -310);
//                gl.glVertex2d(-65, -310);
//                gl.glEnd();
//
//                gl.glBegin(GL.GL_POLYGON);
//                gl.glVertex2d(165, 270);
//                gl.glVertex2d(375, 270);
//                gl.glVertex2d(375, -310);
//                gl.glVertex2d(165, -310);
//                gl.glEnd();
//                gl.glBegin(GL.GL_POLYGON);
//                gl.glVertex2d(-295, 270);
//                gl.glVertex2d(-85, 270);
//                gl.glVertex2d(-85, -310);
//                gl.glVertex2d(-295, -310);
//                gl.glEnd();
                    }

                    if (isclicked) {
                        isclicked = false;
                        if (mouseX >= -295 && mouseX <= -85 && mouseY >= -310 && mouseY <= 270) {
                            isPaused = false;
                        } else if (mouseX >= 115 && mouseX <= 250 && mouseY >= -360 && mouseY <= 380){
                            isPaused = false;
                            oneplayer = false;
                            ai = false;
                            choose = false;
                        }
                    }
                }
                if (multiplayer) {
                    for (Brick brick3 : bricks) {
                        if (brick3.visible) { // يتم رسم الطوب فقط إذا كان مرئيًا
                            gl.glPushMatrix();
                            gl.glTranslated(brick3.x, brick3.y, 0);
                            DrawSprite(0, 0, 1, (float) (brickWidth / 5), (float) (brickHeight / 5));
                            gl.glPopMatrix();
                        }
                    }

                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex2d(0, hight);
                    gl.glVertex2d(0, -hight);
                    gl.glEnd();

                    //multi 1
                    gl.glPushMatrix();
                    gl.glTranslated(x_multi_1, -450, 0);
                    DrawSprite(0, 0, 50, 100, 15);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(x_multi_ball1, y_multi_ball1, 0);
                    DrawSprite(0, 0, 58, 15, 15);
                    gl.glPopMatrix();
                    //multi 2
                    gl.glPushMatrix();
                    gl.glTranslated(x_multi_2, -450, 0);
                    DrawSprite(0, -0, 50, 100, 15);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(x_multi_ball2, y_multi_ball2, 0);
                    DrawSprite(0, 0, 58, 15, 15);

                    gl.glPopMatrix();
                    if (!isPaused) {

                        if (startBall) {
                            x_multi_ball1 += dx_multi_ball1;
                            y_multi_ball1 += dy_multi_ball1;
                            x_multi_ball2 += dx_multi_ball2;
                            y_multi_ball2 += dy_multi_ball2;
                        }
                        if (-hight >= y_multi_ball1 || -hight >= y_multi_ball2) {
                            startBall = false;
                            x_multi_ball1 = 250;
                            x_multi_ball2 = -250;
                            y_multi_ball1 = -100;
                            y_multi_ball2 = -100;
                        }
                        double r = 15;
                        if (x_multi_ball1 >= width - r || x_multi_ball1 <= 0 + r) {
                            dx_multi_ball1 = -dx_multi_ball1;

                            playSound();

                        }
                        if (x_multi_ball2 >= 0 - r || x_multi_ball2 <= -width + r) {
                            dx_multi_ball2 = -dx_multi_ball2;

                            playSound();

                        }
                        if (y_multi_ball1 >= hight - r) {
                            dy_multi_ball1 = -dy_multi_ball1;

                            playSound();

                        }
                        if (y_multi_ball2 >= hight - r) {
                            dy_multi_ball2 = -dy_multi_ball2;

                            playSound();

                        }
                        if (y_multi_ball1 <= -hight + r || y_multi_ball2 <= -hight + r) {
                            x_multi_ball1 = 250;
                            x_multi_ball2 = -250;
                            y_multi_ball1 = -100;
                            y_multi_ball2 = -100;
                            x_multi_1 = 250;

                            startBall = false;
                        }
                        double barWidth = 200;
                        double barHeight = 15;
                        double barY = -450;
                        for (Brick brick3 : bricks) {
                            if (brick3.visible) {
                                double left = brick3.x - brickWidth / 5;
                                double right = brick3.x + brickWidth / 5;
                                double top = brick3.y + brickHeight / 5;
                                double bottom = brick3.y - brickHeight / 5;

                                if (x_multi_ball1 + 15 > left && x_multi_ball1 - 15 < right && y_multi_ball1 + 15 > bottom && y_multi_ball1 - 15 < top) {
                                    // التحقق من موقع التصادم لتحديد اتجاه الكرة
                                    if (x_multi_ball1 + 10 >= left && x_multi_ball1 - 10 <= right) {
                                        dy_multi_ball1 = -dy_multi_ball1;
                                    } else {
                                        dx_multi_ball1 = -dx_multi_ball1;
                                    }
                                    brick3.visible = false; // إخفاء الطوب
                                    playSound();
                                    break;
                                }
                                if (x_multi_ball2 + 15 > left && x_multi_ball2 - 15 < right && y_multi_ball2 + 15 > bottom && y_multi_ball2 - 15 < top) {
                                    // التحقق من موقع التصادم لتحديد اتجاه الكرة
                                    if (x_multi_ball2 + 10 >= left && x_multi_ball2 - 10 <= right) {
                                        dy_multi_ball2 = -dy_multi_ball2;
                                    } else {
                                        dx_multi_ball2 = -dx_multi_ball2;
                                    }
                                    brick3.visible = false; // إخفاء الطوب
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

                        if (y_multi_ball1 - r <= barY + barHeight / 2 && y_multi_ball1 + r >= barY - barHeight / 2) {
                            if (x_multi_ball1 >= x_multi_1 - barWidth / 2 && x_multi_ball1 <= x_multi_1 + barWidth / 2) {
                                dy_multi_ball1 = -dy_multi_ball1; // Reverse vertical direction
                                double barWight = 200;

                                dx_multi_ball1 = (x_multi_ball1 - x_multi_1) / (100 / 3);

                                playSound();
                            }
                        }
                        if (y_multi_ball2 - r <= barY + barHeight / 2 && y_multi_ball2 + r >= barY - barHeight / 2) {
                            if (x_multi_ball2 >= x_multi_2 - barWidth / 2 && x_multi_ball2 <= x_multi_2 + barWidth / 2) {
                                dy_multi_ball2 = -dy_multi_ball2; // Reverse vertical direction
                                double barWight = 200;

                                dx_multi_ball2 = (x_multi_ball2 - x_multi_2) / (100 / 3);

                                playSound();
                            }
                        }

                    } else {

                        DrawSprite(0, 0, 73, 400, 400);

                    }

                    if (isclicked) {
                        isclicked = false;
                        if (mouseX >= -295 && mouseX <= -85 && mouseY >= -310 && mouseY <= 270) {
                            isPaused = false;
                        } else if (mouseX >= 115 && mouseX <= 250 && mouseY >= -360 && mouseY <= 380) {
                            isPaused = false;
                            oneplayer = false;
                            multiplayer = false;
                            choose = false;
                        }
                    }
                }
            }
        }


        if (isclicked && mouseX >= -480 && mouseX <= -390 && mouseY <= 690 && mouseY >= 560) {
            isclicked = false;
            info = false;
            System.out.println("info");
            System.out.println("mouseX: " + mouseX + " mouseY: " + mouseY);
        }

        if (!info) {
            DrawSprite(0, 0, 112, 300, 300);
        }
        if(isclicked&&mouseX<=200&&mouseX>=130&&mouseY<=-275&&mouseY>=-320){
            isclicked=false;
            info=true;
            System.out.println("info no info");
        }
        if (mouseX <= 635 && mouseX >= 520 && mouseY <= 487 && mouseY >= 405) {
            DrawSprite(0, 0, 70, 300, 300);
        }
    }
}