import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Unit {
    private final String name;
    private final Circle avatar;
    private int maxHP;
    private int currentHP;
    private int attack;
    private String team;
    private Text hpText;

    public Unit(String name, Color color, String team, int maxHP, int attack) {
        this.name = name;
        this.team = team;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.attack = attack;

        this.hpText = new Text(getHPDisplay());

        this.avatar = new Circle(14);   // radius
        avatar.setFill(color);
        avatar.setStroke(Color.BLACK);
    }

    public String getName() {
        return name;
    }

    public Circle getAvatar() {
        return avatar;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getAttack() {
        return attack;
    }

    public String getTeam() {
        return team;
    }

    public void takeDamage(int amount) {
        currentHP -= amount;
        if (currentHP < 0) {
            currentHP = 0;
        }
    }

    public String getHPDisplay() {
        return currentHP + " / " + maxHP;
    }

    public Text getHpText() {
        return hpText;
    }

    public void updateHPText() {
        hpText.setText(getHPDisplay());
    }

    public boolean isAlive() {
        return currentHP > 0;
    }
}
