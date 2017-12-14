/**
 * FILENAME: RainRunPanel
 * DESCRIPTION: CS230 Final Project - Rain Run Game
 * @author Angelina Li
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.*;

import java.awt.image.ImageObserver;
import javax.imageio.*;
import java.io.*;

public class RainRunPanel extends JPanel {
    
    private static final Color PLAY_COLOR = RRConstants.CHAR_DEFAULT_COLOR;
    private static final Font SCORE_FONT = RRConstants.getFont(16); // new Font(RRConstants.FONT_NAME, Font.BOLD, 16);
    protected static final int DELAY = 25;
    
    private RainRun game;
    private Timer timer;
    private TimerListener tListener;
    private MoveListener kListener;
    private boolean running;
    private JButton pause;
    private PauseListener pListener;
    private ImageIcon pauseIcon, playIcon;
    
    public RainRunPanel() {
        this.game = new RainRun();
        tListener = new TimerListener();
        kListener = new MoveListener();
        pListener = new PauseListener();
        
        timer = new Timer(DELAY, tListener); // 1000ms = 1 second
        
        setBorder(BorderFactory.createLineBorder(RRConstants.BORDER_COLOR, RRConstants.BORDER));
        setBackground(RRConstants.BACKGROUND_COLOR);
        setFocusable(true);
        requestFocus();
        
        addKeyListener(kListener);
        running = false;
        timer.start();
        
        //setting up pauseButton with image
        pauseIcon = new ImageIcon(new ImageIcon("images/pause.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        playIcon = new ImageIcon(new ImageIcon("images/play.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));

        pause = new JButton();
        pause.setIcon(pauseIcon);
        
        pause.setBackground(PLAY_COLOR);
        pause.setOpaque(true);
        pause.setBorderPainted(false);
        
        pause.addActionListener (pListener);
        add(pause);
    }
    
    public RainRunPanel(int charSize, Color charColor) {
        this();
        this.game = new RainRun(charSize, charColor);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        game.getCharacter().drawCharacter(g);
        
        for(FallingObject obj : game.getFallingObjects()) {
            obj.drawCharacter(g);
        }
        
        // score and health appear on top of objects
        makeScore(g);
        makeHealth(g);
        
        if (game.getCharacter().getDied()) {
            pauseGame();
            RainRunGUI.c1.show(RainRunGUI.cards, RainRunGUI.DEADPANEL);
            RainRunGUI.newGame();
        }
        
        repaint();
    }
    
    private void makeScore(Graphics g) {
        g.setColor(RRConstants.BORDER_COLOR);
        g.setFont(SCORE_FONT);
        g.drawString("Score: " + game.getScore(), RRConstants.BORDER + 8, RRConstants.BORDER + 20);
    }
    
    private void makeHealth(Graphics g) {
        try { // the health hearts are each 20 px wide
            Image img = ImageIO.read(new File("images/life.png"));
            for (int i = 1; i <= game.getCharacter().getHealth(); i++) {
                int xCoord = RRConstants.WIDTH - (RRConstants.BORDER + 3) - i*(20 + 5);
                g.drawImage(img, xCoord, RRConstants.BORDER + 8, new PaneObserver());
            }
        } catch (IOException e) {
            System.out.println("Couldn't open image images/life.png");
        }
    }
    
    // LISTENERS //
    
    // required to add an image
    public class PaneObserver implements ImageObserver {
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return false;
        }
    }
    
    public class MoveListener implements KeyListener {
        
        public void keyPressed(KeyEvent e) {
            int keyPressed = e.getKeyCode();
            if (running && (keyPressed == KeyEvent.VK_LEFT || keyPressed == KeyEvent.VK_A)) {
                game.moveLeft();
                game.checkHit();
            }
            if (running && (keyPressed == KeyEvent.VK_RIGHT || keyPressed == KeyEvent.VK_D)) {
                game.moveRight();
                game.checkHit();
            }
            if (keyPressed == KeyEvent.VK_SPACE) {
                if (isRunning()) {
                    pauseGame();
                    pause.setIcon(playIcon);
                } else {
                    startGame();
                    pause.setIcon(pauseIcon);
                }
            }
        }
        // required by the KeyListener interface
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
    }
    
    public class TimerListener implements ActionListener {
        
        public void actionPerformed(ActionEvent event) {
            
            if (event.getSource() == timer && running) {
                
                if (game.getCharacter().getDied()) {
                    timer.stop();
                    removeKeyListener(kListener);
                    timer.removeActionListener(tListener);
                    running = false;
                }
                
                game.checkTime();
            }
        }
    }
    
    private class PauseListener implements ActionListener {
        public void actionPerformed (ActionEvent event) {
            if (isRunning()) {
                pauseGame();
                pause.setIcon(playIcon);
            } else {
                startGame();
                pause.setIcon(pauseIcon);
                RainRunGUI.gamePanel.requestFocusInWindow();
            }
        }
    }
       
// GETTERS AND SETTERS //
    
    public void startGame() {
        this.running = true;
    }
    
    public void pauseGame() {
        this.running = false;
    }
    
    public boolean isRunning() {
        return running;
    }
    
}