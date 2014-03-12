package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * Controls the recruit menu interface
 *
 * @author Amandeep Gill
 */
public class RecruitMenu {
    private ObservableList<String> availableRecruitsList;
    private ObservableList<Unit> availableRecruits;
    private ListView<String> listView;
    private RecruitCallback recruitCallback;
    private Button recruitButton;

    public RecruitMenu(RecruitCallback recruitCallback, ListView<String> listView, Button recruitButton) {
        this.recruitCallback = recruitCallback;
        this.listView = listView;
        this.listView.setItems(availableRecruitsList);
        this.recruitButton = recruitButton;
        this.availableRecruits = recruitCallback.getAvailableRecruits();

        this.recruitCallback.setRecruitListener(change -> {
            if (change.wasAdded()) {
                availableRecruits.addAll(change.getList());
            } else {
                availableRecruits.removeAll(change.getList());
            }
        });

        this.availableRecruits.addListener((ListChangeListener<? super Unit>) change -> {
            if (change.wasAdded()) {
                for (Object unit : change.getList()) {
                    availableRecruitsList.add(unit.toString());
                }
            } else {
                for (Object unit : change.getList()) {
                    availableRecruitsList.remove(unit.toString());
                }
            }
        });

        this.listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        this.recruitButton.setOnMouseClicked(event -> {
            int index = this.listView.getEditingIndex();
            if (index >= 0) {
                this.recruitCallback.selectRecruit(this.availableRecruits.get(index));
            }
        });
    }

    public interface RecruitCallback {
        public void setRecruitListener(ListChangeListener<Unit> recruitListener);

        public void selectRecruit(Unit recruit);

        public ObservableList<Unit> getAvailableRecruits();
    }
}
