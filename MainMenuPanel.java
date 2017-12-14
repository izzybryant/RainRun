/**
 * FILENAME: MainMenuPanel
 * DESCRIPTION: CS230 Final Project - Rain Run Game
 * @author Isabel Bryant
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenuPanel extends TextPanel {
    
    private static final Color PLAY_COLOR = RRConstants.CHAR_DEFAULT_COLOR;
    private static final Color RULES_COLOR = RRConstants.RULE_BUTTON_COLOR;
    private static final Color SCORES_COLOR = new Color(255, 0, 255);
    
    private JLabel gameName;
    private JButton playButton;
    private JButton rulesButton;
    private JButton scoresButton;
    
    public MainMenuPanel() {
        super(RRConstants.BORDER_COLOR);
        
        //setting up "Rain Run" Label
        gameName = new JLabel("RAIN RUN");
        gameName.setFont(this.nameFont);
        gameName.setForeground(Color.white);

        // setting up the buttons
        ButtonListener b = new ButtonListener();
        playButton = getButton("PLAY", PLAY_COLOR, b);
        rulesButton = getButton("RULES", RULES_COLOR, b);
        scoresButton = getButton("SCORES", SCORES_COLOR, b);

        addComponent(gameName, RRConstants.HEIGHT/4);
        addComponent(playButton, RRConstants.HEIGHT/2);
        addComponent(rulesButton, RRConstants.HEIGHT/2 + RRConstants.HEIGHT/8);
        addComponent(scoresButton, RRConstants.HEIGHT/2 + RRConstants.HEIGHT/4);
    }
    
    private class ButtonListener implements ActionListener {

        public void actionPerformed (ActionEvent event) {
            if (event.getSource() == playButton) {
                RainRunGUI.c1.show(RainRunGUI.cards, RainRunGUI.PLAYPANEL);
                RainRunGUI.gamePanel.startGame();
                RainRunGUI.gamePanel.requestFocusInWindow();
            }
            else if (event.getSource() == rulesButton){
                RainRunGUI.c1.show(RainRunGUI.cards, RainRunGUI.RULESPANEL);
            }
            else if (event.getSource() == scoresButton) {
                RainRunGUI.c1.show(RainRunGUI.cards, RainRunGUI.SCORESPANEL);
            }
        }
    }
}