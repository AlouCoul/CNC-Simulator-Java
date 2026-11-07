package ca.ulaval.glo2004.domain;

import java.util.LinkedList;

public class HistoryManager {
    private final LinkedList<CNC> history;
    private int currentIndex;

    public HistoryManager(CNC initialCNC) {
        history = new LinkedList<>();
        history.add(initialCNC.deepCopy());
        currentIndex = 0;
    }

    public void saveState(CNC cnc) {
        while (history.size() > currentIndex + 1) {
            history.removeLast();
        }

        history.add(cnc.deepCopy());
        currentIndex = history.size() - 1;
    }

    public CNC undo() {
        if (currentIndex > 0) {
            currentIndex--;
            return history.get(currentIndex).deepCopy();
        }
        return null;
    }

    public CNC redo() {
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return history.get(currentIndex).deepCopy();
        }
        return null;
    }
}
