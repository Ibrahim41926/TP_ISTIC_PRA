package src.balde.bah.tp6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CrosswordView.fxml"));
            Parent root = loader.load();
            CrosswordController controller = loader.getController();
            controller.handleNewGrid();

            Scene scene = new Scene(root, 800, 600);

            // Chargement du CSS avec chemin absolu dans le package
            java.net.URL cssUrl = getClass().getResource("Style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Attention : style.css introuvable, l'application fonctionne sans CSS.");
            }

            // Ctrl+W pour fermer la fenêtre
            scene.setOnKeyPressed(e -> {
                if (e.isControlDown() && e.getCode() == KeyCode.W) {
                    primaryStage.close();
                }
            });

            primaryStage.setTitle("Jeu de Mots Croisés - Université de Rennes 1");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erreur au démarrage de l'application :");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
