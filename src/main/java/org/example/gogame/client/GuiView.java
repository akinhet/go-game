package org.example.gogame.client;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.example.gogame.StoneColor;

public class GuiView {
    private final int size;
    private final BorderPane root;
    private final Pane boardPane;
    private final Label statusLabel,
                        colorLabel;
    private ClientGameController controller;

    private Circle[][] stones;

    private static final int CELL_SIZE = 30;
    private static final int PADDING = 20;

    public GuiView(int size) {
        this.size = size;
        this.root = new BorderPane();
        this.boardPane = new Pane();
        this.statusLabel = new Label("Connecting...");
        this.colorLabel = new Label("");
        this.stones = new Circle[size][size];

        setupUI();
    }

    public void setController(ClientGameController controller) {
        this.controller = controller;
    }

    public Parent getRoot() {
        return root;
    }

    private void setupUI() {
        int boardPixelSize = (size - 1) * CELL_SIZE + 2 * PADDING;
        boardPane.setPrefSize(boardPixelSize, boardPixelSize);

        Rectangle background = new Rectangle(boardPixelSize, boardPixelSize);
        background.setFill(Color.web("#DCB35C"));
        boardPane.getChildren().add(background);

        for (int i = 0; i < size; i++) {
            Line vLine = new Line(PADDING + i * CELL_SIZE, PADDING, PADDING + i * CELL_SIZE, PADDING + (size - 1) * CELL_SIZE);
            Line hLine = new Line(PADDING, PADDING + i * CELL_SIZE, PADDING + (size - 1) * CELL_SIZE, PADDING + i * CELL_SIZE);
            boardPane.getChildren().addAll(vLine, hLine);
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int finalX = x;
                int finalY = y;

                Circle stone = new Circle(CELL_SIZE / 2.0 - 2);
                stone.setCenterX(PADDING + x * CELL_SIZE);
                stone.setCenterY(PADDING + y * CELL_SIZE);
                stone.setVisible(false); // Na start puste
                stones[x][y] = stone;

                Circle clickArea = new Circle(CELL_SIZE / 2.0);
                clickArea.setCenterX(PADDING + x * CELL_SIZE);
                clickArea.setCenterY(PADDING + y * CELL_SIZE);
                clickArea.setFill(Color.TRANSPARENT);

                clickArea.setOnMouseClicked(e -> handleBoardClick(finalX, finalY));

                boardPane.getChildren().addAll(stone, clickArea);
            }
        }

        root.setCenter(boardPane);

        HBox bottomPanel = new HBox(10);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setStyle("-fx-padding: 10; -fx-background-color: #EEE;");
        statusLabel.setStyle("-fx-font-weight: bold;");
        bottomPanel.getChildren().add(statusLabel);
        root.setBottom(bottomPanel);

        HBox topPanel = new HBox(10);
        topPanel.setStyle("-fx-padding: 10;");
        colorLabel.setStyle("-fx-font-weight: bold;");

        Button passBtn = new Button("PASS");
        passBtn.setOnAction(e -> {
            if (controller != null) controller.handleUserInput("pass");
        });

        topPanel.getChildren().addAll(colorLabel, passBtn);
        root.setTop(topPanel);
    }

    private void handleBoardClick(int x, int y) {
        if (controller != null) {
            controller.handleUserInput(x + " " + y);
        }
    }

    public void updateBoard(int x, int y, StoneColor color) {
        Platform.runLater(() -> {
            if (x >= 0 && x < size && y >= 0 && y < size) {
                Circle stone = stones[x][y];
                if (color == StoneColor.EMPTY) {
                    stone.setVisible(false);
                } else {
                    stone.setVisible(true);
                    stone.setFill(color == StoneColor.BLACK ? Color.BLACK : Color.WHITE);
                    stone.setStroke(color == StoneColor.BLACK ? Color.BLACK : Color.WHITE);
                }
            }
        });
    }

    public void setMessage(String msg) {
        Platform.runLater(() -> statusLabel.setText(msg));
    }

    public void setColor(String color) {
        Platform.runLater(() -> {
            statusLabel.setText("You are playing as: " + color);
            colorLabel.setText(color);
        });
    }

    public void setTurn(boolean myTurn) {
        Platform.runLater(() -> {
            if (myTurn) {
                root.setStyle("-fx-border-color: green; -fx-border-width: 5;");
            } else {
                root.setStyle("-fx-border-color: transparent; -fx-border-width: 5;");
            }
        });
    }

    public void setErr(String err) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message from the server");
            alert.setHeaderText("Message from the server");
            alert.setContentText(err);
            alert.showAndWait();
        });
    }
}