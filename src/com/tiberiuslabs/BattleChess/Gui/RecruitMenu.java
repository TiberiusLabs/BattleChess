package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.collections.SetChangeListener;

import java.util.*;

/**
 * Controls the recruit menu interface
 * @author Amandeep Gill
 */
public class RecruitMenu {

    public interface RecruitCallback {
        public void setRecruitListener(SetChangeListener<Unit> recruitListener);
    }
}
