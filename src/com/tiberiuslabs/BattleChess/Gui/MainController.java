package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.GameEngine.GameEngineCallbacks;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class MainController extends Application{
    public Canvas boardCanvas;
    private GuiBoard board;
    private GameEngineCallbacks callbacks;

;

    public MainController() {
        callbacks = new GameEngineCallbacks();
        callbacks.setup(Color.BLACK, AIDifficulty.EXPERT);
    }

    @Override
    public void start(Stage stage) throws Exception {

        board = new GuiBoard(callbacks, boardCanvas);
    }
}
