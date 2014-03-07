package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.collections.SetChangeListener;

/**
 * Controls the recruit menu interface
 *
 * @author Amandeep Gill
 */
public class RecruitMenu {

    public interface RecruitCallback {
        public void setRecruitListener(SetChangeListener<Unit> recruitListener);
    }
}
