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

public class MainWindow extends Application {

    public Canvas boardCanvas;
    public Parent root;
    public Group boardPane;
    public ListView recruitList;

    private GameEngineCallbacks callbacks;
    private GuiBoard board;
    private RecruitMenu recruitMenu;

    private void initGame() {
        callbacks = new GameEngineCallbacks(node -> {
            boardPane.getChildren().add(node);
        });
        callbacks.setup(Color.WHITE, AIDifficulty.EXPERT);
        board = new GuiBoard(callbacks);
        recruitMenu = new RecruitMenu(callbacks);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        primaryStage.setTitle("Welcome to BattleChess");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();

        boardCanvas = (Canvas) root.lookup("#boardCanvas");
        boardPane = (Group) root.lookup("#boardPane");
        initGame();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
