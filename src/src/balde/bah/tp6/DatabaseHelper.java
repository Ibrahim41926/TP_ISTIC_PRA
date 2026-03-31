package src.balde.bah.tp6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper {
    private Connection connexion;

    public DatabaseHelper() {
        try {
            connexion = connecterBD();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion initiale : " + e.getMessage());
        }
    }

    /**
     * Établit la connexion avec la base de données de l'ISTIC
     */
    public static Connection connecterBD() throws SQLException {
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver MySQL non trouvé - " + e.getMessage());
        }
        String url = "jdbc:mysql://mysqln.istic.univ-rennes1.fr:3306/base_ibalde";
        String username = "user_ibalde";
        String password = "12345678";
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Récupère la liste des grilles disponibles
     */
    public Map<Integer, String> availableGrids() {
        Map<Integer, String> grids = new HashMap<>();
        String query = "SELECT numero_grille, nom_grille, hauteur, largeur FROM GRID";
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("numero_grille");
                String desc = String.format("%s (%dx%d)",
                        rs.getString("nom_grille"),
                        rs.getInt("hauteur"),
                        rs.getInt("largeur"));
                grids.put(id, desc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grids;
    }

    /**
     * Extrait une grille aléatoire depuis la base de données
     * @throws Exception 
     */
    public Crossword extractRandomGrid() throws Exception {
        String request = "SELECT numero_grille, hauteur, largeur FROM GRID ORDER BY RAND() LIMIT 1";
        try (PreparedStatement pstmt = connexion.prepareStatement(request)) {
            ResultSet results = pstmt.executeQuery();
            if (results != null && results.next()) {
                int gridKey = results.getInt("numero_grille");
                int hauteur  = results.getInt("hauteur");
                int largeur  = results.getInt("largeur");
                return loadGrid(gridKey, hauteur, largeur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extrait une grille précise par son numéro
     */
    public Crossword extractGrid(int numGrille) {
        String sqlGrille = "SELECT hauteur, largeur FROM GRID WHERE numero_grille = ?";
        try (PreparedStatement psGrille = connexion.prepareStatement(sqlGrille)) {
            psGrille.setInt(1, numGrille);
            ResultSet rsGrille = psGrille.executeQuery();
            if (rsGrille.next()) {
                int h = rsGrille.getInt("hauteur");
                int w = rsGrille.getInt("largeur");
                return loadGrid(numGrille, h, w);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'extraction de la grille : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Méthode commune : charge les mots d'une grille depuis la BDD
     */
    private Crossword loadGrid(int numGrille, int h, int w) throws Exception {
        Crossword cw = new Crossword(h, w);

        // Toutes les cases en noir par défaut
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                cw.setBlackSquare(i, j, true);
            }
        }

        String sqlMots = "SELECT ligne, colonne, solution, definition, horizontal FROM CROSSWORD WHERE numero_grille = ?";
        try (PreparedStatement psMots = connexion.prepareStatement(sqlMots)) {
            psMots.setInt(1, numGrille);
            ResultSet rsMots = psMots.executeQuery();

            while (rsMots.next()) {
                int row      = rsMots.getInt("ligne") - 1;
                int col      = rsMots.getInt("colonne") - 1;
                String sol   = rsMots.getString("solution");
                String def   = rsMots.getString("definition");
                boolean isHoriz = rsMots.getBoolean("horizontal");

                // Ajout de l'indice
                Clue indice = new Clue(def, row, col, isHoriz);
                if (isHoriz) {
                    cw.getHorizontalClues().add(indice);
                } else {
                    cw.getVerticalClues().add(indice);
                }

                // Placement des lettres
                for (int i = 0; i < sol.length(); i++) {
                    int currR = isHoriz ? row     : row + i;
                    int currC = isHoriz ? col + i : col;
                    cw.setBlackSquare(currR, currC, false);
                    cw.setSolution(currR, currC, sol.charAt(i));
                }
            }
        }
        return cw;
    }
}
