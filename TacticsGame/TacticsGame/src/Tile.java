import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
    private final int row;
    private final int col;
    private final boolean isWalkable;
    private final String type;
    private final Rectangle background;
    private boolean isSelected = false;
    private Unit unit = null;
    private final GameBoard board;

    public Tile(int row, int col, String type, boolean isWalkable, GameBoard board) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.isWalkable = isWalkable;
        this.board = board;

        // create a background rectangle for the tile
        background = new Rectangle(60,60);
        background.setStroke(Color.BLACK);
        setFillByType(type);

        getChildren().add(background);

        setOnMouseClicked(e -> {
            Unit currentUnit = board.getCurrentUnit();

            // select phase
            if (board.getSelectedTile() == null) {
                // select tile if it has a unit
                if (unit != null && unit == currentUnit) {
                    board.setSelectedTile(this);
                    highlightSelected(true);
                }
                // action phase (move/attack)
            } else {
                Tile from = board.getSelectedTile();

                if (from.getUnit() != currentUnit) {
                    // prevents acting with units out of turn
                    return;
                }

                int dr = Math.abs(this.getRow() - from.getRow());
                int dc = Math.abs(this.getCol() - from.getCol());
                boolean isAdjacent = (dr + dc == 1);

                if (isAdjacent) {
                    if (this.unit == null && isWalkable) {
                        board.moveUnit(from, this);
                        board.endTurn();    // end turn after a move
                    } else if (this.unit != null && !this.unit.getTeam().equals(from.getUnit().getTeam())) {
                        board.attackUnit(from, this);
                        board.endTurn();    // end turn after an attack;
                    }
                }

                from.highlightSelected(false);
                board.setSelectedTile(null);
            }
        });
    }

    private void setFillByType(String type) {
        switch (type) {
            case "grass" -> background.setFill(Color.LIGHTGREEN);
            case "wall" -> background.setFill(Color.DARKGRAY);
            case "water" -> background.setFill(Color.CORNFLOWERBLUE);
            default -> background.setFill(Color.LIGHTGREY);
        }
    }

    private void updateBorder() {
        if (isSelected) {
            background.setStroke(Color.GOLD);
            background.setStrokeWidth(3);
        } else {
            background.setStroke(Color.BLACK);
            background.setStrokeWidth(1);
        }
    }

    public void placeUnit(Unit unit) {
        if (this.unit != null) return;  // optional: prevent stacking

        this.unit = unit;

        unit.getHpText().setTranslateY(-25);    // moves text above circle

        getChildren().addAll(unit.getAvatar(), unit.getHpText());
    }

    public void removeUnit() {
        if (unit != null) {
            getChildren().remove(unit.getAvatar());
            getChildren().remove(unit.getHpText());
            unit = null;
        }
    }

    public void highlightSelected(boolean selected) {
        if (selected) {
            background.setStroke(Color.GOLD);
            background.setStrokeWidth(3);
        } else {
            background.setStroke(Color.BLACK);
            background.setStrokeWidth(1);
        }
    }






    /*
    Getter Methods
     */

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public String getType() {
        return type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Unit getUnit() {
        return unit;
    }
}
