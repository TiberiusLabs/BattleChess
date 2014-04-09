package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.GameEngine.GameEngineCallbacks;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class MainWindow extends Application {

    public Canvas boardCanvas;
    public Parent root;
    public Group boardPane;
    public Stage primaryStage;

    private void initGame() {
        GameEngineCallbacks callbacks = new GameEngineCallbacks(node -> {
            boardPane.getChildren().add(node);
        });
        callbacks.setup(Color.WHITE, AIDifficulty.EXPERT);
        new GuiBoard(callbacks);
        new RecruitMenu(callbacks);
        callbacks.setWinningEvent(playerWins -> {
            String title = playerWins ? "You are victorious!" : "You have been defeated!";
            String mesg = playerWins ?
                    "You have beaten the computer. Would you like to play again?" :
                    "The computer has beaten you. Would you like to play again?";
            Action response = Dialogs.create()
                    .owner(primaryStage)
                    .title(title)
                    .masthead(null)
                    .message(mesg)
                    .nativeTitleBar()
                    .showConfirm();

            if (response == Dialog.Actions.YES) {
                try {
                    stop();
                    start(primaryStage);
                } catch (Exception e) {
                    System.exit(-1);
                }
            } else {
                System.exit(0);
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        this.primaryStage.setTitle("Welcome to BattleChess");
        this.primaryStage.setScene(new Scene(root, 800, 600));
        this.primaryStage.sizeToScene();
        this.primaryStage.setResizable(false);
        this.primaryStage.show();

        boardCanvas = (Canvas) root.lookup("#boardCanvas");
        boardPane = (Group) root.lookup("#boardPane");
        initGame();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
