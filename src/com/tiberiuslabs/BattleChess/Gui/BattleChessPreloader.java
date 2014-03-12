package com.tiberiuslabs.BattleChess.Gui;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Amandeep Gill
 */
public class BattleChessPreloader extends Preloader {
    ProgressBar progressBar;
    Stage stage;

    private Scene createPreloaderScene() {
        progressBar = new ProgressBar();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(progressBar);
        return new Scene(borderPane, 800, 800);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(createPreloaderScene());
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification progressNotification) {
        progressBar.setProgress(progressNotification.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }
}
