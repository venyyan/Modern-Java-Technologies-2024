package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.algorithm.ShortestPathSearch;
import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

public class RideRight implements ItineraryPlanner {
    private List<Journey> schedule;
    public RideRight(List<Journey> schedule) {
        this.schedule = schedule;
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {
        setStartEndCityPresence(start, destination);

        SequencedCollection<Journey> path;
        if (!allowTransfer) {
            path = new ArrayList<>();
            getPathWithNoTransfer(path, start, destination);
        } else {
            path = ShortestPathSearch.getCheapestPath(ShortestPathSearch
                .aStarSearch(start, destination, this.schedule));
            if (path == null) {
                throw new NoPathToDestinationException("There is no path satisfying the conditions!");
            }
        }
        return path;
    }

    private void setStartEndCityPresence(City start, City destination) throws CityNotKnownException {
        if (start == null) {
            throw new IllegalArgumentException("Start city is null!");
        }

        if (destination == null) {
            throw new IllegalArgumentException("Destination city is null!");
        }

        boolean startCityFound = false;
        boolean destinationCityFound = false;

        for (Journey journey : schedule) {
            if (journey.from().equals(start) || journey.to().equals(start)) {
                startCityFound = true;
            }

            if (journey.from().equals(destination) || journey.to().equals(destination)) {
                destinationCityFound = true;
            }
        }
        if (!startCityFound) {
            throw new CityNotKnownException("Start city is not present in the list of provided journeys!");
        }

        if (!destinationCityFound) {
            throw new CityNotKnownException("Destination city is not present in the list of provided journeys!");
        }
    }

    private void getPathWithNoTransfer(SequencedCollection<Journey> path, City start, City destination)
        throws NoPathToDestinationException {
        BigDecimal smallestPrice = BigDecimal.valueOf(Double.MAX_VALUE);
        for (Journey journey : schedule) {
            if (journey.from().equals(start) && journey.to().equals(destination)) {
                if (smallestPrice.compareTo(journey.price()) > 0) {
                    smallestPrice = journey.price();
                    if (path.size() == 1) {
                        path.removeFirst();
                    }
                    path.add(journey);
                }
            }
        }
        if (path.isEmpty()) {
            throw new NoPathToDestinationException("There is no path satisfying the conditions!");
        }
    }
}
