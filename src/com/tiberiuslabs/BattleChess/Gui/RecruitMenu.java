package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls the recruit menu interface
 *
 * @author Amandeep Gill
 */
public class RecruitMenu {
    private static ImageView background = new ImageView(new Image(
            MainWindow.class.getResourceAsStream("/art/board/graveyard.png")));

    private RecruitCallback recruitCallback;
    private List<RecruitTile> recruitTiles = new ArrayList<>();

    public RecruitMenu(RecruitCallback recruitCallback) {
        background.setX(0);
        background.setY(0);
        this.recruitCallback = recruitCallback;

        this.recruitCallback.setRecruitListener(change -> {
            if (change.wasAdded()) {
                for (RecruitTile tile : recruitTiles) {
                    if (tile.getUnit() == null) {
                        tile.setUnit(change.getElementAdded());
                        break;
                    }
                }
            } else {
                for (RecruitTile tile : recruitTiles) {
                    if (tile.getUnit() != null && tile.getUnit().equals(change.getElementRemoved())) {
                        tile.setUnit(null);
                        break;
                    }
                }
            }
        });

        recruitCallback.addRecruitNode(background);

        int offset = 65;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 6; y++) {
                RecruitTile tile = new RecruitTile(35 + x * offset, 120 + y * offset);
                recruitTiles.add(tile);
                tile.setSelectionCallback(recruitCallback::selectRecruit);
                recruitCallback.addRecruitNode(tile.getGroup());
            }
        }
    }

    private void compactRecruits() {
        for (int i = 0; i < recruitTiles.size(); i += 1) {
            if (recruitTiles.get(i).getUnit() == null) {
                for (int j = i + 1; j < recruitTiles.size(); j += 1) {
                    if (recruitTiles.get(j).getUnit() != null) {
                        recruitTiles.get(i).setUnit(recruitTiles.get(j).getUnit());
                        recruitTiles.get(j).setUnit(null);
                        i = j;
                        break;
                    }
                }
            }
        }
    }

    public interface RecruitCallback {
        public void addRecruitNode(Node node);

        public void setRecruitListener(SetChangeListener<Unit> recruitListener);

        public void selectRecruit(Unit recruit);

        public ObservableList<Unit> getAvailableRecruits();
    }
}
