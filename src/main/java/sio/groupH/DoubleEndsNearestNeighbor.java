package sio.groupH;

import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

import java.util.Arrays;

public final class DoubleEndsNearestNeighbor implements TspConstructiveHeuristic {
  private final int DEFAULT_SEARCH_INDEX = -1;

  /**
   * Coté d'une tournée pour l'heuristique <i>Double-ended nearest neighbour</i>
   */
  private enum DENNSide {
    HEAD,
    TAIL
  }

  /**
   * @param side     Coté de la tournée où se situe la ville la plus proche
   * @param index    Index de la ville trouvée
   * @param distance Distance avec la ville trouvée
   * @author Farouk Ferchichi & Hugo Huart
   */
  private record DENNFindResult(DENNSide side, int index, long distance) {
  }

  /**
   * Cherche la plus proche ville visitable
   *
   * @param data        Données d'un TSP
   * @param isAvailable Tableau indiquant si une ville peut être visitée
   * @param head        Ville de tête pour calculer les distances
   * @param tail        Ville de queue pour calculer les distances
   * @return Résultat de la recherche
   * @author Farouk Ferchichi & Hugo Huart
   */
  private DENNFindResult findClosestAvailableCity(TspData data, boolean[] isAvailable, int head, int tail) {
    int closest = DEFAULT_SEARCH_INDEX;
    DENNSide curSide = DENNSide.HEAD;
    long minDistance = Long.MAX_VALUE;
    long headDistance;
    long tailDistance;

    for (int i = 0; i < data.getNumberOfCities(); ++i) {
      headDistance = data.getDistance(head, i);
      tailDistance = data.getDistance(tail, i);

      if (isAvailable[i] && (headDistance < minDistance || tailDistance < minDistance)) {
        // La tête prime dans le cas d'une égalité avec la queue
        if (headDistance <= tailDistance) {
          minDistance = headDistance;
          closest = i;
          curSide = DENNSide.HEAD;
        } else {
          minDistance = tailDistance;
          closest = i;
          curSide = DENNSide.TAIL;
        }
      }
    }

    return new DENNFindResult(curSide, closest, minDistance);
  }

  /**
   * Applique une heuristique <i>Double-ended nearest neighbour</i> a des données d'un TSP
   *
   * @param data           Data of problem instance
   * @param startCityIndex Index of starting city, if needed by the implementation
   * @return Tournée obtenue
   * @throws IllegalArgumentException si les données ne sont pas applicables
   * @author Farouk Ferchichi & Hugo Huart
   */
  @Override
  public TspTour computeTour(TspData data, int startCityIndex)  throws IllegalArgumentException {
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

    // Ville de tête
    int s = startCityIndex;

    // Ville de queue
    int t = startCityIndex;

    // Ville ajoutée à la tournée lors de l'itération
    int v = startCityIndex;

    DENNFindResult result;
    for (int i = 0; i < data.getNumberOfCities(); ++i) {
      tour[i] = v;
      isAvailable[v] = false;

      result = findClosestAvailableCity(data, isAvailable, s, t);
      v = result.index;

      if (v != DEFAULT_SEARCH_INDEX) {
        // Si on n'est pas dans le cas de la dernière ville visitée
        totalDistance += result.distance;
      }

      if (result.side == DENNSide.HEAD) {
        s = v;
      } else {
        t = v;
      }
    }

    return new TspTour(data, tour, totalDistance);
  }
}
