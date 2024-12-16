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
        for (double i = -600; i <= 600; i += brickWidth + 20) {
            for (double j = 300; j <= 400; j += brickHeight + 10) {
//                bricks.add(new Brick(i, j)
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
    boolean oneplayer = false;
    List<Brick> bricks = new ArrayList<>(); // قائمة تحتوي على جميع الطوب
    double brickWidth = 200, brickHeight = 80; // أبعاد الطوب
    double x_single = 0;
    double x_single_ball1 = 0, y_single_ball1 = -100;
    double dx_single = -5;
    double dy_single = 5;

    boolean isPaused = false;
    double mouseX;
    double mouseY;
    boolean isclicked = false;
    boolean ai = false;

    boolean h1 = true;
    boolean h2 = true;
    boolean h3 = true;

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


    public void display(GLAutoDrawable gld) {
        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        if (!choose) {
            DrawBackground(75);
            gl.glColor3f(1, 1, 1);
//                gl.glBegin(GL.GL_POLYGON);
//                gl.glVertex2d(-250, 190);
//                gl.glVertex2d(265, 190);
//                gl.glVertex2d(265, 80);
//                gl.glVertex2d(-250, 80);
//                gl.glEnd();
//            gl.glBegin(GL.GL_POLYGON);
//            gl.glVertex2d(-250, 190-130);
//            gl.glVertex2d(265, 190-130);
//            gl.glVertex2d(265, 80-130);
//            gl.glVertex2d(-250, 80-130);
//            gl.glEnd();
//            gl.glBegin(GL.GL_POLYGON);
//            gl.glVertex2d(-250, 190-265);
//            gl.glVertex2d(265, 190-265);
//            gl.glVertex2d(265, 80-265);
//            gl.glVertex2d(-250, 80-265);
//            gl.glEnd();
            if (isclicked) {
                isclicked = false;
                if (mouseX >= -200 && mouseX <= 200 && mouseY >= 133 && mouseY <= 285) {
                    choose=true;
                     oneplayer=true;
                }
                if (mouseX >= -200 && mouseX <= 200 && mouseY >= -50 && mouseY <= 85) {
                    choose=true;
                    multiplayer=true;

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

//    public double sqrdDistance(double x, double y, double x1, double y1) {
//
//        return Math.pow(x - x1, 2) + Math.pow(y - y1, 2);
//                }
                if (mouseX >= -200 && mouseX <= 200 && mouseY >= -240 && mouseY <= -100) {
                    choose=true;
                    ai=true;
                }


                }


        }
        if (choose) {


            DrawBackground(70);

            if (oneplayer) {
                for (Brick brick : bricks) {
                    if (brick.visible) { // يتم رسم الطوب فقط إذا كان مرئيًا
                        gl.glPushMatrix();
                        gl.glTranslated(brick.x, brick.y, 0);
                        DrawSprite(0, 0, 1, (float) (brickWidth / 5), (float) (brickHeight / 5));
                        gl.glPopMatrix();
                    }
                }
                gl.glPushMatrix();
                gl.glTranslated(x_single, -450, 0);
                DrawSprite(0, -0, 50, 100, 15);
                gl.glPopMatrix();

                gl.glPushMatrix();
                gl.glTranslated(x_single_ball1, y_single_ball1, 0);
                DrawSprite(0, 0, 58, 15, 15);
                gl.glPopMatrix();
                if (!isPaused) {
                    if (startBall) {
                        x_single_ball1 += dx_single;
                        y_single_ball1 += dy_single;
                    }
                    if (-hight >= y_single_ball1) {
                        startBall = false;
                        x_single_ball1 = 0;
                        y_single_ball1 = -100;
                    }
                    double r = 15;
                    if (x_single_ball1 >= width - r || x_single_ball1 <= -width + r) {
                        dx_single = -dx_single;

                        playSound();

                    }
                    if (y_single_ball1 >= hight - r) {
                        dy_single = -dy_single;

                        playSound();

                    }
                    if (y_single_ball1 <= -hight + r) {
                        x_single_ball1 = 0;
                        y_single_ball1 = -100;
                        x_single = 0;
                        startBall = false;
                        dx_single = 3;
                        dy_single = 3;
                    }
                    // Bar dimensions and position
                    double barWidth = 200;
                    double barHeight = 15;
                    double barY = -450; // Bar's fixed Y position

                    for (Brick brick : bricks) {
                        if (brick.visible) {
                            double left = brick.x - brickWidth / 5;
                            double right = brick.x + brickWidth / 5;
                            double top = brick.y + brickHeight / 5;
                            double bottom = brick.y - brickHeight / 5;

                            if (x_single_ball1 + 15 > left && x_single_ball1 - 15 < right && y_single_ball1+ 15 > bottom && y_single_ball1 - 15 < top) {
                                // التحقق من موقع التصادم لتحديد اتجاه الكرة
                                if (x_single_ball1+ 10 >= left && x_single_ball1 - 10 <= right) {
                                    dy_single = -dy_single;
                                } else {
                                    dx_single = -dx_single;
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



// Check collision with the bar
                    if (y_single_ball1 - r <= barY + barHeight / 2 && y_single_ball1 + r >= barY - barHeight / 2) {
                        if (x_single_ball1 >= x_single - barWidth / 2 && x_single_ball1 <= x_single + barWidth / 2) {
                            dy_single = -dy_single; // Reverse vertical direction
                            double barWight = 200;

                            dx_single = (x_single_ball1 - x_single) / (100 / 3);

                            playSound();
                        }
                    }
                } else {

                    DrawSprite(0, 0, 105, 400, 400);
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
                gl.glTranslated(x_ai_2 , -450, 0);
                DrawSprite(0, -0, 50, 100, 15);
                gl.glPopMatrix();

                gl.glPushMatrix();
                gl.glTranslated(x_ai_ball2, y_ai_ball2, 0);
                DrawSprite(0, 0, 58, 15, 15);
                x_ai_2 =  x_ai_ball2+50;
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
                    if (y_ai_ball1 <= -hight + r || y_ai_ball2 <= -hight + r) {
                        x_ai_ball1 = 250;
                        x_ai_ball2 = -250;
                        y_ai_ball1 = -100;
                        y_ai_ball2 = -100;
                        x_ai_1 = 250;

                        startBall = false;
//                        dx_ai_ball1 = 3;
//                        dx_ai_ball2 = 3;
//                        dy_ai_ball2 = 3;
//                        dy_ai_ball1 = 3;
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

                            if (x_ai_ball1 + 15 > left && x_ai_ball1 - 15 < right && y_ai_ball1+ 15 > bottom && y_ai_ball1 - 15 < top) {
                                // التحقق من موقع التصادم لتحديد اتجاه الكرة
                                if (x_ai_ball1+ 10 >= left && x_ai_ball1 - 10 <= right) {
                                    dy_ai_ball1 = -dy_ai_ball1;
                                } else {
                                    dx_ai_ball1 = -dx_ai_ball1;
                                }
                                brick2.visible = false; // إخفاء الطوب
                                playSound();
                                break;
                            }
                            if (x_ai_ball2 + 15 > left && x_ai_ball2 - 15 < right && y_ai_ball2+ 15 > bottom && y_ai_ball2 - 15 < top) {
                                // التحقق من موقع التصادم لتحديد اتجاه الكرة
                                if (x_ai_ball2+ 10 >= left && x_ai_ball2 - 10 <= right) {
                                    dy_ai_ball2 = -dy_ai_ball2;
                                } else {
                                    dx_ai_ball2= -dx_ai_ball2;
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

                    DrawSprite(0, 0, 105, 400, 400);
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
                gl.glVertex2d(0,hight);
                gl.glVertex2d(0,-hight);
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
                gl.glTranslated(x_multi_2 , -450, 0);
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
                    //القاع
                    if (-hight >= y_multi_ball1 || -hight >= y_multi_ball2) {
                        startBall = false;
                        x_multi_ball1 = 250;
                        x_multi_ball2 = -250;
                        y_multi_ball1 = -100;
                        y_multi_ball2 = -100;
                    }
                    //الحيطان
                    double r = 15;
                    if  (x_multi_ball1 >= width - r || x_multi_ball1 <= 0 + r) {
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
//                        dx_multi_ball1 = 3;
//                        dx_multi_ball2 = 3;
//                        dy_multi_ball2 = 3;
//                        dy_multi_ball1 = 3;
                    }
                    // Bar dimensions and position
                    double barWidth = 200;
                    double barHeight = 15;
                    double barY = -450; // Bar's fixed Y position
                    for (Brick brick3 : bricks) {
                        if (brick3.visible) {
                            double left = brick3.x - brickWidth / 5;
                            double right = brick3.x + brickWidth / 5;
                            double top = brick3.y + brickHeight / 5;
                            double bottom = brick3.y - brickHeight / 5;

                            if (x_multi_ball1 + 15 > left && x_multi_ball1 - 15 < right && y_multi_ball1+ 15 > bottom && y_multi_ball1 - 15 < top) {
                                // التحقق من موقع التصادم لتحديد اتجاه الكرة
                                if (x_multi_ball1+ 10 >= left && x_multi_ball1 - 10 <= right) {
                                    dy_multi_ball1 = -dy_multi_ball1;
                                } else {
                                    dx_multi_ball1 = -dx_multi_ball1;
                                }
                                brick3.visible = false; // إخفاء الطوب
                                playSound();
                                break;
                            }
                            if (x_multi_ball2 + 15 > left && x_multi_ball2 - 15 < right && y_multi_ball2+ 15 > bottom && y_multi_ball2 - 15 < top) {
                                // التحقق من موقع التصادم لتحديد اتجاه الكرة
                                if (x_multi_ball2+ 10 >= left && x_multi_ball2 - 10 <= right) {
                                    dy_multi_ball2 = -dy_multi_ball2;
                                } else {
                                    dx_multi_ball2= -dx_multi_ball2;
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

                    DrawSprite(0, 0, 105, 400, 400);
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
                        multiplayer = false;
                        choose = false;
                    }
                }
            }



        }
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


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /*
     * KeyListener 8432677
     */


    @Override
    public void keyPressed(final KeyEvent event) {
        System.out.println("key pressed");
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

        if (x >= -width + 60 && keycode == KeyEvent.VK_LEFT) x -= 40;
        if (x <= width - 60 && keycode == KeyEvent.VK_RIGHT) x += 40;
        if (x >= -width + 60 && keycode == KeyEvent.VK_LEFT) x -= 10;
        if (x <= width - 60 && keycode == KeyEvent.VK_RIGHT) x += 10;
        if (keycode == KeyEvent.VK_SPACE) startBall = true;
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

}
