package sio.groupH;

import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;
import sio.tsp.TspTour;

import java.util.Arrays;

public final class Main {

  /**
   * Affiche différentes informations pour un groupe de solutions d'un TSP
   *
   * @param tours         Tournées trouvées pour un TSP
   * @param optimalLength Longueur optimale connue a priori utilisée pour le calcul des statistiques
   * @return Statistiques formatées (min, max et moyenne, avec villes de départ) sur les solutions
   * @author Farouk Ferchichi & Huart Huart
   */
  private static String formatStatsForTours(TspTour[] tours, int optimalLength) {
    if (tours == null || tours.length == 0) {
      return "";
    }

    StringBuilder sb = new StringBuilder();

    long minLength = tours[0].length();
    int minIdx = 0;

    long maxLength = minLength;
    int maxIdx = 0;

    double averageLength = 0.0;

    long minNew, maxNew;
    for (int i = 0; i < tours.length; ++i) {
      minNew = Math.min(minLength, tours[i].length());
      maxNew = Math.max(maxLength, tours[i].length());

      // Tournée de taille min
      if (minNew != minLength) {
        minLength = minNew;
        minIdx = i;
      }

      // Tournée de taille max
      if (maxNew != maxLength) {
        maxLength = maxNew;
        maxIdx = i;
      }

      // Moyenne de la taille des tournées
      averageLength += (double) tours[i].length() / tours.length;
    }

    sb.append(String.format("min length: %d started from %d\n", minLength, minIdx));
    sb.append(String.format("diff from optimal (%d): %d%%\n", optimalLength, Math.round(100 * (double) minLength / optimalLength)));

    sb.append("\n");
    sb.append(String.format("max length: %d started from %d\n", maxLength, maxIdx));
    sb.append(String.format("diff from optimal (%d): %d%%\n", optimalLength, Math.round(100 * (double) maxLength / optimalLength)));

    sb.append("\n");
    sb.append(String.format("average length: %d\n", Math.round(averageLength)));
    sb.append(String.format("diff from optimal (%d): %d%%\n", optimalLength, Math.round(100 * averageLength / optimalLength)));

    return sb.toString();
  }

  /**
   * Applique une heuristique constructive à partir de chaque ville d'un TSP donné
   *
   * @param file      Nom d'un fichier contenant des données TSP
   * @param heuristic Heuristique constructive à appliquer
   * @return Tournées obtenues
   * @author Farouk Ferchichi & Huart Huart
   */
  private static TspTour[] computeToursFromFile(String file, TspConstructiveHeuristic heuristic) throws Exception {
    TspData data;
    data = TspData.fromFile(file);

    TspTour[] tours = new TspTour[data.getNumberOfCities()];

    for (int i = 0; i < data.getNumberOfCities(); ++i) {
      tours[i] = heuristic.computeTour(data, i);
    }

    return tours;
  }

  public static void main(String[] args) {
    // Longueurs optimales :
    // att532 : 86729
    // rat575 : 6773
    // rl1889 : 316536
    // u574   : 36905
    // u1817  : 57201
    // vm1748 : 336556

    // Exemple de lecture d'un jeu de données :
    // TspData data = TspData.fromFile("data/att532.dat");

    String[] dataFiles = new String[]{
        "data/att532.dat",
        "data/rat575.dat",
        "data/rl1889.dat",
        "data/u574.dat",
        "data/u1817.dat",
        "data/vm1748.dat"
    };

    int[] optimalLengths = new int[]{
        86729,
        6773,
        316536,
        36905,
        57201,
        336556
    };

    for (int i = 0; i < dataFiles.length; ++i) {
      TspTour[] NNTours;
      TspTour[] DENNTours;

      try {
        NNTours = computeToursFromFile(dataFiles[i], new NearestNeighbor());
        DENNTours = computeToursFromFile(dataFiles[i], new DoubleEndsNearestNeighbor());
      } catch (Exception e) {
        System.out.printf("Erreur: %s", e.getMessage());
        return;
      }

      String NNStats = formatStatsForTours(NNTours, optimalLengths[i]);
      String DENNStats = formatStatsForTours(DENNTours, optimalLengths[i]);

      System.out.println(dataFiles[i]);

      System.out.println("NN Stats:");
      System.out.println(NNStats);

      System.out.println("DENN Stats:");
      System.out.println(DENNStats);
      System.out.println("**********************");
    }
  }
}
