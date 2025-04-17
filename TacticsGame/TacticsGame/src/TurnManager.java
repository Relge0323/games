import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private final List<Unit> turnOrder = new ArrayList<>();
    private int currentIndex = 0;

    public void addUnit(Unit unit) {
        turnOrder.add(unit);
    }

    public Unit getCurrentUnit() {
        return turnOrder.get(currentIndex);
    }

    public void nextTurn() {
        //remove dead units from list
        turnOrder.removeIf(unit -> !unit.isAlive());

        // if there are no livign units left, the battle is over
        if (turnOrder.isEmpty()) {
            System.out.println("Battle over! No units left.");
            return;
        }

        // cycle to next living unit
        do {
            currentIndex = (currentIndex + 1) % turnOrder.size();
        } while (!getCurrentUnit().isAlive());

        System.out.println(getCurrentUnit().getName() + "'s turn");
    }

    public void start() {
        System.out.println("Battle begins! First turn: " + getCurrentUnit().getName());
    }

    public void removeUnit(Unit unit) {
        // remove a dead unit from turn order
        turnOrder.remove(unit);

        // adjust the currentIndex in case we remove a unit from the front or middle
        if (turnOrder.isEmpty()) {
            currentIndex = 0;
        } else if (currentIndex >= turnOrder.size()) {
            currentIndex = turnOrder.size() - 1;
        }
    }
}
