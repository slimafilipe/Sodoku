package dev.filipe.ui.custom.screen;

import dev.filipe.service.BoardService;
import dev.filipe.ui.custom.button.FinishGameButton;
import dev.filipe.ui.custom.button.ResetButton;
import dev.filipe.ui.custom.frame.MainFrame;
import dev.filipe.ui.custom.panel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static javax.swing.JOptionPane.*;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600,600);

    private final BoardService boardService;

    private JButton checkGameStatusButton;

    private JButton finishGameButton;

    private JButton resetButton;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if (boardService.gameIsFinished()){
                JOptionPane.showMessageDialog(null, "Paranbéns você concluiu o jogo!");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            }else {
                var message = "Seu jogo tem alguma inconsistencia, ajuste e tent novamente";
                showMessageDialog(null, message);
            }
           });
        mainPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new FinishGameButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(MainScreen.this.checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e -> {
           var dialogResult = JOptionPane.showConfirmDialog(
                   null,
                   "Deseja realmente o jogo?",
                   "Limpar o jogo",
                   YES_NO_OPTION,
                   QUESTION_MESSAGE
           );
           if (dialogResult == 0){
               boardService.reset();
           }
        });
        mainPanel.add(resetButton);
    }
}
