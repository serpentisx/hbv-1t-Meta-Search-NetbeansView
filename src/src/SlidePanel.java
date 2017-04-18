package src;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Huy Van Nguyen
 */
public class SlidePanel {
    Timer timer;
    
    public SlidePanel() {
        timer = new Timer();
    }

    public void slideRight(final int stop, int delay, final int increment, final JPanel jPanel) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (jPanel.getX() < stop) {
                    jPanel.setLocation(jPanel.getX() + increment, jPanel.getY());
                } else {
                    jPanel.setLocation(stop, jPanel.getY());
                    this.cancel();
                }
            }
        }, 0, delay);
    }
    
    public void slideLeft(final int stop, int delay, final int increment, final JPanel jPanel) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (jPanel.getX() > stop) {
                    jPanel.setLocation(jPanel.getX() - increment, jPanel.getY());
                } else {
                    jPanel.setLocation(stop, jPanel.getY());
                    this.cancel();
                }
            }
        }, 0, delay);
    }
}
