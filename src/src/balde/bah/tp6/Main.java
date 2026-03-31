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
            java.net.URL fxmlUrl = getClass().getResource("CrosswordView.fxml");
            if (fxmlUrl == null) {
                java.nio.file.Path fallback = java.nio.file.Paths.get("src/src/balde/bah/tp6/CrosswordView.fxml");
                if (java.nio.file.Files.exists(fallback)) {
                    fxmlUrl = fallback.toUri().toURL();
                }
            }

            if (fxmlUrl == null) {
                throw new IllegalStateException("CrosswordView.fxml introuvable dans les ressources ni à src/src/balde/bah/tp6/");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            CrosswordController controller = loader.getController();
            controller.handleNewGrid();

            Scene scene = new Scene(root, 800, 600);

            // Chargement du CSS avec chemin fixe + fallback
            java.net.URL cssUrl = getClass().getResource("Style.css");
            if (cssUrl == null) {
                java.nio.file.Path fallbackCss = java.nio.file.Paths.get("src/src/balde/bah/tp6/Style.css");
                if (java.nio.file.Files.exists(fallbackCss)) {
                    cssUrl = fallbackCss.toUri().toURL();
                }
            }
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Attention : Style.css introuvable, application exécutée sans CSS.");
            }

            // Ctrl+W pour fermer la fenêtre
            scene.setOnKeyPressed(e -> {
                if (e.isControlDown() && e.getCode() == KeyCode.W) {
                    primaryStage.close();
                }
            });

            primaryStage.setTitle("Jeu de Mots Croisés - Université de Rennes 1-PROGRAMMATION D'APPLICATIONS");
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
