package org.example.gogame.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.Optional;

public class GoClient extends Application {

    @Override
    public void start(Stage primaryStage) {
        String serverAddress = askForServerAddress();
        if (serverAddress == null) {
            return;
        }

        try {
            String[] parts = serverAddress.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);

            Socket socket = new Socket(host, port);

            GuiView view = new GuiView(19);
            ClientGameController controller = new ClientGameController(socket, view);

            view.setController(controller);

            Scene scene = new Scene(view.getRoot(), 600, 700);
            primaryStage.setTitle("Go Game Client - JavaFX");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            primaryStage.setOnCloseRequest(e -> controller.handleUserInput("quit"));

            primaryStage.show();

            controller.startListener();

        } catch (Exception e) {
            showError("Connection Error", "Could not connect to server: " + e.getMessage());
        }
    }

    private String askForServerAddress() {
        TextInputDialog dialog = new TextInputDialog("localhost:1111");
        dialog.setTitle("Server Connection");
        dialog.setHeaderText("Connect to Go Server");
        dialog.setContentText("Please enter server address (host:port):");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}