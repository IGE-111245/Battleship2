package battleship;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FogGUI extends Application {
    private boolean[][] revelado = new boolean[10][10];

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        desenhaTabuleiro(gc);

        canvas.setOnMouseClicked(e -> {
            int x = (int)(e.getX() / 50);
            int y = (int)(e.getY() / 50);
            revela(x, y, gc);
        });

        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root, 500, 500));
        stage.setTitle("Battleship Fog of War");
        stage.show();
    }

    private void desenhaTabuleiro(GraphicsContext gc) {
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, 500, 500);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        for (int i = 0; i <= 10; i++) {
            gc.strokeLine(i*50, 0, i*50, 500);
            gc.strokeLine(0, i*50, 500, i*50);
        }
        // Nevoeiro
        gc.setFill(new Color(0.3, 0.3, 0.3, 0.8));
        gc.fillRect(0, 0, 500, 500);
    }

    private void revela(int x, int y, GraphicsContext gc) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10 || revelado[y][x]) return;
        revelado[y][x] = true;
        // Simula hit/miss (liga à tua Fleet depois)
        boolean hit = Math.random() > 0.8;
        gc.setFill(hit ? Color.RED : Color.BLUE);
        gc.fillRect(x*50 + 5, y*50 + 5, 40, 40);
        System.out.println("Tiro (" + x + "," + y + ") - " + (hit ? "Hit!" : "Miss"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

