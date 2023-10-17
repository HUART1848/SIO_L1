package sio.groupH;

import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;
import sio.tsp.TspTour;

import java.util.Arrays;

public final class Main {
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

      if (minNew != minLength) {
        minLength = minNew;
        minIdx = i;
      }

      if (maxNew != maxLength) {
        maxLength = maxNew;
        maxIdx = i;
      }

      averageLength += tours[i].length() * (1.0 / tours.length);
    }

    sb.append("====\n");
    sb.append(String.format("min length: %d started from %d\n", minLength, minIdx));
    sb.append(String.format("diff from optimal (%d): %d%%\n", optimalLength, Math.round(100 * (double) minLength / optimalLength)));

    sb.append("\n");
    sb.append(String.format("max length: %d started from %d\n", maxLength, maxIdx));
    sb.append(String.format("diff from optimal (%d): %d%%\n", optimalLength, Math.round(100 * (double) maxLength / optimalLength)));

    sb.append("\n");
    sb.append(String.format("average length: %d\n", Math.round(averageLength)));
    sb.append(String.format("diff from optimal (%d): %d%%\n", optimalLength, Math.round(100 * averageLength / optimalLength)));

    sb.append("====\n");

    return sb.toString();
  }

  private static TspTour[] computeToursFromFile(String file, TspConstructiveHeuristic heuristic) {
    TspData data;

    try {
      data = TspData.fromFile(file);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }

    TspTour[] tours = new TspTour[data.getNumberOfCities()];

    for (int i = 0; i < data.getNumberOfCities(); ++i) {
      tours[i] = heuristic.computeTour(data, i);
    }

    return tours;
  }

  public static void main(String[] args) {
    // TODO
    //  - Implémentation des classes NearestNeighbor et DoubleEndsNearestNeighbor ;
    //  - Affichage des statistiques dans la classe Main ;
    //  - Documentation abondante des classes comprenant :
    //    - la javadoc, avec auteurs et description des implémentations ;
    //    - des commentaires sur les différentes parties de vos algorithmes.

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
      TspTour[] tours = computeToursFromFile(dataFiles[i], new NearestNeighbor());
      String stats = formatStatsForTours(tours, optimalLengths[i]);

      System.out.println(dataFiles[i]);
      System.out.println(stats);
    }
  }
}
