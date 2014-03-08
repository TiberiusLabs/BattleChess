package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.GameEngine.GameEngineCallbacks;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.util.List;

public class MainWindow extends Application {

    public Canvas boardCanvas;
    public Parent root;
    private GameEngineCallbacks callbacks;
    private GuiBoard board;

    private void initGame() {
        callbacks = new GameEngineCallbacks();
        callbacks.setup(Color.BLACK, AIDifficulty.EXPERT);
        board = new GuiBoard(callbacks, boardCanvas);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        primaryStage.setTitle("Welcome to BattleChess");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMaxWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setMaxHeight(600);
        primaryStage.show();

        boardCanvas = (Canvas) root.lookup("#boardCanvas");
        initGame();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
