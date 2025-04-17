import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameBoard board = new GameBoard(10, 10);    // 10x10 grid
        Scene scene = new Scene(board.getGrid(), 600, 600);

        primaryStage.setTitle("Tactics Game Grid");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
