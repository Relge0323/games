import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private final GridPane grid;
    private Tile selectedTile = null;
    private TurnManager turnManager = new TurnManager();

    public GameBoard(int rows, int cols) {
        grid = new GridPane();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // for row, randomly assign grass or wall
                String type = Math.random() < 0.2 ? "wall" : "grass";
                boolean isWalkable = !type.equals("wall");

                Tile tile = new Tile(row, col, type, isWalkable, this);
                grid.add(tile, col, row);
            }
        }

        // player unit
        Tile playerTile = getRandomWalkableTile();
        Unit player = new Unit("Knight", Color.DODGERBLUE, "Player", 30, 5);
        playerTile.placeUnit(player);
        turnManager.addUnit(player);

        // spawn enemies on random walkable tiles
        spawnEnemy("Goblin1", Color.CRIMSON, 10, 3);
        spawnEnemy("Goblin2", Color.CRIMSON, 10, 3);
        spawnEnemy("Orc", Color.DARKRED, 15, 3);

        turnManager.start();
    }

    public Unit getCurrentUnit() {
        return turnManager.getCurrentUnit();
    }

    public void nextTurn() {
        turnManager.nextTurn();
    }

    public void endTurn() {
        turnManager.nextTurn();
        System.out.println("Ending turn for: " + turnManager.getCurrentUnit().getName());

        // clear any UI highlights or selections
        if (selectedTile != null) {
            selectedTile.highlightSelected(false);
            selectedTile = null;
        }
    }

    public GridPane getGrid() {
        return grid;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Tile tile) {
        this.selectedTile = tile;
    }

    public void moveUnit(Tile from, Tile to) {
        Unit unit = from.getUnit();
        from.removeUnit();
        to.placeUnit(unit);
    }

    public void attackUnit(Tile attackerTile, Tile defenderTile) {
        Unit attacker = attackerTile.getUnit();
        Unit defender = defenderTile.getUnit();

        if (attacker == null || defender == null) {
            return;
        }

        if (attacker.getTeam().equals(defender.getTeam())) {
            return;
        }

        defender.takeDamage(attacker.getAttack());
        defender.updateHPText();

        // remove unit if defeated
        if (defender.isAlive()) {
            defenderTile.removeUnit();

        }
    }

    private Tile findWalkableTile(int row, int col) {
        for (javafx.scene.Node node : grid.getChildren()) {
            if (node instanceof Tile tile) {
                if (tile.getRow() == row && tile.getCol() == col && tile.isWalkable()) {
                    return tile;
                }
            }
        }
        return null;
    }

    private Tile getRandomWalkableTile() {
        List<Tile> walkableTiles = new ArrayList<>();
        for (javafx.scene.Node node : grid.getChildren()) {
            if (node instanceof Tile tile && tile.isWalkable() && tile.getUnit() == null) {
                walkableTiles.add(tile);
            }
        }
        if (!walkableTiles.isEmpty()) {
            Random rand = new Random();
            return walkableTiles.get(rand.nextInt(walkableTiles.size()));
        }
        return null;
    }

    private void spawnEnemy(String name, Color color, int hp, int attack) {
        Tile tile = getRandomWalkableTile();
        if (tile != null) {
            Unit enemy = new Unit(name, color, "Enemy", hp, attack);
            tile.placeUnit(enemy);
            turnManager.addUnit(enemy);
        }
    }
}
