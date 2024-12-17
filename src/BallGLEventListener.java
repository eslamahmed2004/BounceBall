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

class
BallGLEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

    File file = new File("C:\\Users\\trrom\\IdeaProjects\\CS304\\Bounce-Ball\\src\\PNG\\");
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
    List<List> levels = new ArrayList<>();

    //*new* initialize the lists of the multiPLayer levels
    List<Brick> l1MultiPlayer1 = new ArrayList<>();
    List<Brick> l1MultiPlayer2 = new ArrayList<>();


    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        GLU glu = new GLU();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture("C:\\Users\\trrom\\IdeaProjects\\CS304\\Bounce-Ball\\src\\PNG\\" + textureNames[i], true);
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

        levels.add(level1);
        levels.add(level2);
        levels.add(level3);
        levels.add(level4);
        levels.add(level5);
        //*new*  adding the multi 1,2 level lists into the level list
        levels.add(l1MultiPlayer1); //5
        levels.add(l1MultiPlayer2); //6


        initLevels(-600, 600,350 , 32, level1, 5);
        initLevels(-650, 650, 350, 45, level2, 11);
        initLevels(-650, 650, 380, 54, level3, 17);
        initLevels(-650, 650, 380, 63, level4, 15);
        initLevels(-650, 650, 380, 72, level5, 7);
        // initialize the levels by calling the method which fill the multi lists
        for (double i = -600; i <= 600; i += brickWidth + 20) {
            for (double j = 300; j <= 400; j += brickHeight + 10) {
                bricks.add(new Brick(i, j));
            }
        }
        initLevels(-650,-50,380, 16, l1MultiPlayer1,3);
        initLevels(50, 650,380, 16, l1MultiPlayer2,3);
        countBricks = level1.size();

    }

    double x;
    double y = -450;
    double x_ball = 0, y_ball = -400;
    double dx = -5;
    double dy = 5;
    boolean startBall = false;

    long startTime = System.nanoTime();
    long elapsedTime;
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
    int level = 4;
    boolean heart1 = true;
    boolean heart2 = true;
    boolean heart3 = true;
    boolean next = false;
    boolean restart = false;
    boolean exit = false;


    public void display(GLAutoDrawable gld) {
        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        if (!choose) {
            DrawBackground(72);
            gl.glColor3f(1, 1, 1);
            if (isclicked) {
                isclicked = false;
                if (mouseX >= -200 && mouseX <= 200 && mouseY >= 133 && mouseY <= 285) {
                    choose = true;
                    oneplayer = true;
                }
                if (mouseX >= -200 && mouseX <= 200 && mouseY >= -50 && mouseY <= 85) {
                    choose = true;
                    multiplayer = true;

                }
                if (mouseX >= -200 && mouseX <= 200 && mouseY >= -240 && mouseY <= -100) {
                    choose = true;
                    ai = true;
                }


            }


        }
        if (choose) {


            DrawBackground(68);
            if (oneplayer) {
                drawOnePlayer(gl);
            }


            if (ai) {
                drawAI(gl);
            }


            if (multiplayer) {
                drawMultiplayer(gl);
            }

        }
    }
    public void drawOnePlayer(GL gl) {
        if (!isPaused) {

            // عرض المستوى المناسب بناءً على قيمة level
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
            }

            // رسم المضرب (paddle)
            drawPaddle(gl, x_single, y);

            // التعامل مع الكرة
            ball();

            if (startBall) {
                x_ball += dx;
                y_ball += dy;
            }

            // التحقق من تصادم الكرة
            checkBallCollision(x_ball, y_ball, width, hight);

            // التحقق من تصادم الكرة مع الطوب (bricks)
            checkBrickCollision(level - 1, x_ball, y_ball, brickWidth, brickHeight);

            // التحقق من حالة اللعبة وعدد الأرواح المتبقية
            checkGameStatusAndLives(lives);

            // التعامل مع شاشة نهاية اللعبة
            handleEndGameScreen(mouseX, mouseY);
        } else {
            // في حالة التوقف (إيقاف مؤقت)
            DrawSprite(0, 0, 73, 400, 400);
        }

        // التعامل مع النقرات لإلغاء الإيقاف المؤقت أو تغيير إعدادات اللعبة
        if (isclicked) {
            isclicked = false;
            if (mouseX >= -295 && mouseX <= -85 && mouseY >= -310 && mouseY <= 270) {
                isPaused = false; // إلغاء التوقف المؤقت
            } else if (mouseX >= 115 && mouseX <= 250 && mouseY >= -360 && mouseY <= 380) {
                // العودة إلى الشاشة الرئيسية
                isPaused = false;
                oneplayer = false;
                ai = false;
                choose = false;
            }
        }
    }


    public void drawMultiplayer(GL gld) {
        for (Brick brick3 : bricks) {
            if (brick3.visible) { // يتم رسم الطوب فقط إذا كان مرئيًا
                gl.glPushMatrix();
                gl.glTranslated(brick3.x, brick3.y, 0);
                DrawSprite(0, 0, 1, (float) (brickWidth / 5), (float) (brickHeight / 5));
                gl.glPopMatrix();
            }
        }

        gl = gld; // تم تصحيح السطر هنا
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

            // Handle the bottom of the screen
            if (-hight >= y_multi_ball1 || -hight >= y_multi_ball2) {
                startBall = false;
                x_multi_ball1 = 250;
                x_multi_ball2 = -250;
                y_multi_ball1 = -100;
                y_multi_ball2 = -100;
            }

            // Handle the walls (left-right)
            double r = 15;
            if (x_multi_ball1 >= width - r || x_multi_ball1 <= 0 + r) {
                dx_multi_ball1 = -dx_multi_ball1;
                playSound();
            }
            if (x_multi_ball2 >= 0 - r || x_multi_ball2 <= -width + r) {
                dx_multi_ball2 = -dx_multi_ball2;
                playSound();
            }

            // Handle the ceiling
            if (y_multi_ball1 >= hight - r) {
                dy_multi_ball1 = -dy_multi_ball1;
                playSound();
            }
            if (y_multi_ball2 >= hight - r) {
                dy_multi_ball2 = -dy_multi_ball2;
                playSound();
            }

            // Handle the bottom (reset if lost)
            if (y_multi_ball1 <= -hight + r || y_multi_ball2 <= -hight + r) {
                x_multi_ball1 = 250;
                x_multi_ball2 = -250;
                y_multi_ball1 = -100;
                y_multi_ball2 = -100;
                x_multi_1 = 250;
                startBall = false;
            }

            // Check for brick collision
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

            // Check if all bricks are broken (end game condition)
            boolean allBricksBroken = true;
            for (Brick brick3 : bricks) {
                if (brick3.visible) {
                    allBricksBroken = false;
                    break;
                }
            }

            // If all bricks are broken, show winner image
            if (allBricksBroken) {
                DrawSprite(0, 0, 200, 200, 200);  // عرض صورة الفائز هنا
                // Add a message or sound to notify the winner
                // PlayWinnerSound();
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

        // Handle mouse click for pausing/unpausing
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



    public void drawAI(GL gl) {
        // رسم الطوب
        for (Brick brick2 : bricks) {
            if (brick2.visible) { // يتم رسم الطوب فقط إذا كان مرئيًا
                gl.glPushMatrix();
                gl.glTranslated(brick2.x, brick2.y, 0);
                DrawSprite(0, 0, 1, (float) (brickWidth / 5), (float) (brickHeight / 5));
                gl.glPopMatrix();
            }
        }

        gl.glBegin(GL.GL_LINES);
        gl.glVertex2d(0, hight);
        gl.glVertex2d(0, -hight);
        gl.glEnd();

        // رسم اللاعب
        gl.glPushMatrix();
        gl.glTranslated(x_ai_1, -450, 0);
        DrawSprite(0, 0, 50, 100, 15);
        gl.glPopMatrix();

        // رسم الكرة
        gl.glPushMatrix();
        gl.glTranslated(x_ai_ball1, y_ai_ball1, 0);
        DrawSprite(0, 0, 58, 15, 15);
        gl.glPopMatrix();

        // رسم الـ AI
        gl.glPushMatrix();
        gl.glTranslated(x_ai_2 = x_ai_ball2 + 70, -450, 0);
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

            // القاع
            if (-hight >= y_ai_ball1 || -hight >= y_ai_ball2) {
                startBall = false;
                x_ai_ball1 = 250;
                x_ai_ball2 = -250;
                y_ai_ball1 = -100;
                y_ai_ball2 = -100;
            }

            // الحيطان
            double r = 15;
            if (x_ai_ball1 >= width - r || x_ai_ball1 <= 0 + r) {
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
            if (y_ai_ball1 <= -hight + r || y_ai_ball2 <= -hight + r) {
                x_ai_ball1 = 250;
                x_ai_ball2 = -250;
                y_ai_ball1 = -100;
                y_ai_ball2 = -100;
                x_ai_1 = 250;
                startBall = false;
            }

            // رسم الـ Paddle
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

            // التحقق من التصادم مع الـ Paddle
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
        }
    }



    void initLevels(int startX,int endX, int startY, int n, List<Brick> level, int index) {
        int x = startX;
        for (int i = 0; i < n; i++) {
            if (startX >= endX) {
                startX = x;
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


    public void checkBallCollision(double x_ball, double y_ball, double width, double height) {
        if (x_ball >= width - 15 || x_ball <= -width + 15) {
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
            dx += 0.3 * dx;
            dy += 0.3 * dy;
        }
    }


    public void drawHearts() {
        if (heart1) {
            DrawSprite(150, 450, 60, 20, 20);
        }
        if (heart2) {
            DrawSprite(200, 450, 60, 20, 20);
        }
        if (heart3) {
            DrawSprite(250, 450, 60, 20, 20);
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
    }

    public void handleEndGameScreen(double mouseX, double mouseY) {
        if (isclicked) {
            isclicked = false;


            if (isGameWon) {
                if (mouseX >= -279 && mouseX <= -23 && mouseY <= -218 && mouseY >= -459) {
                    choose = true;
                    next = true;
                }
            }


            if (isGameLost) {
                if (mouseX >= -279 && mouseX <= -23 && mouseY <= -184 && mouseY >= -376) {
                    choose = true;
                    restart = true;
                }


                if (mouseX >= 9 && mouseX <= 151 && mouseY <= -184 && mouseY >= -376) {
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
        levels.get(level - 1).clear();
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
            initLevels(-600, 600, 32, level1.size(), level1, 5);
        } else if (level == 2) {
            initLevels(-650, 650, 45, level2.size(), level2, 11);

        } else if (level == 3) {
            initLevels(-650, 650, 54, level3.size(), level3, 17);
        } else if (level == 4) {
            initLevels(-650, 650, 63, level4.size(), level4, 15);
        } else if (level == 5) {
            initLevels(-650, 650, 72, level5.size(), level5, 7);
        }

        // تحديث العدد الإجمالي للطوب
        countBricks = levels.get(level - 1).size();

        // إعادة تعيين حالة الفوز والخسارة
        isGameLost = false;
        isGameWon = false;
    }

    public static void playSound() {
        try {
            File wavFile = new File("C:\\Users\\trrom\\IdeaProjects\\CS304\\Bounce-Ball\\src\\solid.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(wavFile));
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void DrawSprite(double x, double y, int index, double scale_x, double scale_y) {
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
    public void mouseDragged(MouseEvent e) {

    }

    /*
     * KeyListener 8432677
     */


    @Override
    public void keyPressed ( final KeyEvent event){
        System.out.println("key pressed");
//        System.out.println(keyCode);
        int keycode = event.getKeyCode();
        System.out.println(x_single);
        if (oneplayer) {
            if (!isPaused) {
                if (x_single >= -width + 60 && keycode == KeyEvent.VK_LEFT) x_single -= 10;
                if (x_single <= width - 60 && keycode == KeyEvent.VK_RIGHT) x_single += 10;
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
    public void keyReleased ( final KeyEvent event){
        System.out.println("key released");
    }

    @Override

    public void keyTyped ( final KeyEvent event){
        System.out.println("key typed ");
        // don't care
    }


    public void reshape (GLAutoDrawable drawable,int x, int y, int width, int height){
    }

    public void displayChanged (GLAutoDrawable drawable,boolean modeChanged, boolean deviceChanged){
    }

    @Override
    public void mouseClicked (MouseEvent e){
        System.out.println("you click");

    }

    @Override
    public void mousePressed (MouseEvent e){
        System.out.println(mouseX + " " + mouseY);
        isclicked = true;
    }

    @Override
    public void mouseReleased (MouseEvent e){
        System.out.println("released");


    }

    @Override
    public void mouseEntered (MouseEvent e){
        System.out.println("entered");
    }

    @Override
    public void mouseExited (MouseEvent e){
        System.out.println("exited");
    }

    private double convertX ( double x, double screenWidth){
        return (x / screenWidth) * 1000 - 500;
    }

    private double convertY ( double y, double screenHeight){
        return 700 - (y / screenHeight) * 1400;
    }

    public void mouseMoved (MouseEvent e){

        mouseX = convertX(e.getX(), 2 * width);
        mouseY = convertY(e.getY(), 2 * hight);
    }

}