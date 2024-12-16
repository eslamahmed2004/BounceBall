import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

public class BounceBall extends JFrame {
    static FPSAnimator animator  ;
    GLCanvas glcanvas  = new GLCanvas();
    final private Pages listener = new Pages();
//    private GLCanvas glcanvas = new GLCanvas();
//    private final BallGLEventListener listener = new BallGLEventListener();
    private int timeLeft = 60;
    private JLabel timerLabel = new JLabel("Time left: 60", SwingConstants.CENTER);
    private JButton startStopButton = new JButton("Start Game");
    private Timer countdownTimer;




    public BounceBall() {
        super("Bounce Ball");
        animator = new FPSAnimator(glcanvas , 120) ;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // إعداد الغcanvas ومراقبي الأحداث
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        glcanvas.addMouseListener(listener);

        // إعداد المؤقت النصي
//        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        timerLabel.setForeground(Color.RED);

        // إعداد زر التحكم في اللعبة
//        startStopButton.addActionListener(this::toggleGame);

        // إعداد المؤقت
//        setupCountdownTimer();

        // ترتيب المكونات
//        JPanel controlPanel = new JPanel();
//        controlPanel.add(startStopButton);
//
        add(glcanvas, BorderLayout.CENTER);
//        add(timerLabel, BorderLayout.NORTH);
//        add(controlPanel, BorderLayout.SOUTH);

        setSize(1400, 1000);
        centerWindow();
        setVisible(true);
        glcanvas.setFocusable(true);
        glcanvas.requestFocus();
    }

    private void toggleGame(ActionEvent e) {
        if (!animator.isAnimating()) {
            animator.start();
            countdownTimer.start();
            startStopButton.setText("Stop Game");
        } else {
            animator.stop();
            countdownTimer.stop();
            startStopButton.setText("Start Game");
        }
    }

    private void setupCountdownTimer() {
        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft);

            if (timeLeft <= 0) {
                countdownTimer.stop();
                animator.stop();
                JOptionPane.showMessageDialog(this, "Time's up!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                startStopButton.setText("Start Game");
            }
        });
    }

    public void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();

        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;

        this.setLocation(
                (screenSize.width - frameSize.width) >> 1,
                (screenSize.height - frameSize.height) >> 1
        );
    }
}
