package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.GameEngine.GameEngineCallbacks;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Canvas boardCanvas;
    private GuiBoard board;
    private GameEngineCallbacks callbacks;

    public MainController() {

        callbacks = new GameEngineCallbacks();
        callbacks.setup(Color.BLACK, AIDifficulty.EXPERT);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(url);
        Pane root;
        try {
            root = loader.load();
            boardCanvas = (Canvas) root.lookup("#boardCanvas");
            board = new GuiBoard(callbacks, boardCanvas);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
