package sio.groupH;

import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

import java.util.Arrays;

public final class NearestNeighbor implements TspConstructiveHeuristic {
  private final int DEFAULT_SEARCH_INDEX = -1;

  /**
   * Cherche la plus proche ville visitable parmi les données
   *
   * @param data        Données d'un TSP
   * @param isAvailable Tableau indiquant si une ville peut être visitée
   * @param current     Ville d'origine pour calculer les distances
   * @return La ville la plus proche (ou index par défaut si aucune ne peut être visitée)
   * @author Farouk Ferchichi & Hugo Huart
   */
  private int findClosestAvailableCity(TspData data, boolean[] isAvailable, int current) {
    int closest = DEFAULT_SEARCH_INDEX;
    long minDistance = Long.MAX_VALUE;
    long curDistance;

    for (int i = 0; i < data.getNumberOfCities(); ++i) {
      curDistance = data.getDistance(current, i);

      // En cas d'égalité, la meilleure ville actuelle prime (qui est de plus petit numéro)
      if (curDistance < minDistance && isAvailable[i]) {
        closest = i;
        minDistance = curDistance;
      }
    }

    return closest;
  }

  /**
   * Applique une heuristique <i>Nearest neighbour</i> a des données d'un TSP
   *
   * @param data           Data of problem instance
   * @param startCityIndex Index of starting city, if needed by the implementation
   * @return Tournée obtenue
   * @throws IllegalArgumentException si les données ne sont pas applicables
   * @author Farouk Ferchichi & Hugo Huart
   */
  @Override
  public TspTour computeTour(TspData data, int startCityIndex) throws IllegalArgumentException {
    if (data.getNumberOfCities() < 3) {
      throw new IllegalArgumentException("There should be at least 3 cities");
    }

    if (startCityIndex < 0 || startCityIndex >= data.getNumberOfCities()) {
      throw new IllegalArgumentException("Start city index should be contained in [0, Number of cities[");
    }

    int[] tour = new int[data.getNumberOfCities()];
    boolean[] isAvailable = new boolean[data.getNumberOfCities()];
    Arrays.fill(isAvailable, true);

    long totalDistance = 0;
    int s = startCityIndex;
    int t;

    for (int i = 0; i < data.getNumberOfCities(); ++i) {
      tour[i] = s;
      isAvailable[s] = false;

      t = findClosestAvailableCity(data, isAvailable, s);
      if (t != DEFAULT_SEARCH_INDEX) {
        // Si on n'est pas dans le cas de la dernière visitée
        totalDistance += data.getDistance(s, t);
      }

      s = t;
    }

    return new TspTour(data, tour, totalDistance);
  }
}
