package sio.groupH;

import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.IntStream;

public final class NearestNeighbor implements TspConstructiveHeuristic {
  private final int DEFAULT_SEARCH_INDEX = -1;

  private int findClosestAvailableCity(TspData data, boolean[] isAvailable, int originCity) {
    int closestCity = DEFAULT_SEARCH_INDEX;
    long closestDistance = Long.MAX_VALUE;
    long currentDistance;

    for (int nextCity = 0; nextCity < data.getNumberOfCities(); ++nextCity) {
      currentDistance = data.getDistance(originCity, nextCity);
      if (currentDistance < closestDistance && isAvailable[nextCity]) {
        closestCity = nextCity;
        closestDistance = currentDistance;
      }
    }

    return closestCity;
  }

  @Override
  public TspTour computeTour(TspData data, int startCityIndex) {
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
    int current = startCityIndex;
    int next;

    for (int i = 0; i < data.getNumberOfCities(); ++i) {
      tour[i] = current;
      isAvailable[current] = false;

      next = findClosestAvailableCity(data, isAvailable, current);
      if (next != DEFAULT_SEARCH_INDEX) {
        // Si on n'est pas dans le cas de la dernière visitée
        totalDistance += data.getDistance(current, next);
      }

      current = next;
    }

    return new TspTour(data, tour, totalDistance);
  }
}
