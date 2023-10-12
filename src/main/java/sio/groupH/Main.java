package sio.groupH;

import sio.tsp.TspData;
import sio.tsp.TspTour;

public final class Main {
  private String getStats(TspTour tour) {
    return "";
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

    String[] dataFiles = new String[] {
        "data/att532.dat",
        "data/rat575.dat",
        "data/rl1889.dat",
        "data/u574.dat",
        "data/u1817.dat",
        "data/vm1748.dat"
    };

    try {
      for (String file : dataFiles) {
        TspData data = TspData.fromFile(file);

        TspTour tour  = new NearestNeighbor().computeTour(data, 0);
        System.out.printf("%s -> NN: %d\n", file, tour.length());
      }
    } catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
