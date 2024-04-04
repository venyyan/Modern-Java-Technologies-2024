
package bg.sofia.uni.fmi.mjt.itinerary.algorithm;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SequencedCollection;

public class ShortestPathSearch {
    public static CityNode aStarSearch(City start, City target, List<Journey> schedule) {
        List<CityNode> inspected = new ArrayList<>();
        PriorityQueue<CityNode> toInspect = new PriorityQueue<>();

        CityNode startCityNode = new CityNode(start.name(), start.location());
        CityNode targetCityNode = new CityNode(target.name(), target.location());

        startCityNode.setF(startCityNode.calculateHeuristic(targetCityNode));
        toInspect.add(startCityNode);

        while (!toInspect.isEmpty()) {
            CityNode currentCityNode = toInspect.peek();

            if (currentCityNode.getName().equals(target.name())) {
                return currentCityNode;
            }

            setCurrentCityNodeNeighbours(schedule, currentCityNode, toInspect, inspected);
            inspectCurrentCityNodeNeighbours(currentCityNode, targetCityNode, toInspect, inspected);

            toInspect.remove(currentCityNode);
            inspected.add(currentCityNode);
        }
        return null;
    }

    public static SequencedCollection<Journey> getCheapestPath(CityNode target) {
        if (target == null) {
            return null;
        }

        SequencedCollection<Journey> cheapestPath = new ArrayList<>();

        while (target.getParent() != null) {
            Journey journey = new Journey(target.getBestFoundPathToNode().getVehicleType(),
                target.getParent().getCorrespondingCity(), target.getCorrespondingCity(),
                target.getBestFoundPathToNode().getPriceForTrip());

            cheapestPath.add(journey);
            target = target.getParent();
        }

        return cheapestPath.reversed();
    }

    private static void setNeighbourData(CityNode neighbourCityNode, CityNode currentCity, BigDecimal totalWeight,
                                         CityNode targetCity, TripEdge neighbour) {
        neighbourCityNode.setParent(currentCity);
        neighbourCityNode.setG(totalWeight);
        neighbourCityNode.setF(totalWeight.add(neighbourCityNode.calculateHeuristic(targetCity)));
        neighbourCityNode.getBestFoundPathToNode().setVehicleType(neighbour.getVehicleType());
    }

    private static BigDecimal getTotalJourneyPrice(Journey journey) {
        BigDecimal greenTaxPrice = journey.vehicleType().getGreenTax().multiply(journey.price());
        return journey.price().add(greenTaxPrice);
    }

    private static void setCurrentCityNodeNeighbours(List<Journey> schedule, CityNode currentCity,
                                                     PriorityQueue<CityNode> toInspect,
                                                     List<CityNode> inspected) {
        for (Journey journey : schedule) {
            if (journey.from().name().equals(currentCity.getName())) {
                boolean isInspected = false;
                for (int i = 0; i < inspected.size(); i++) {
                    if (inspected.get(i).getName().equals(journey.to().name())) {
                        isInspected = true;
                        break;
                    }
                }
                if (!isInspected) {
                    boolean isNotInspectedYet = false;
                    for (CityNode node : toInspect) {
                        if (node.getName().equals(journey.to().name())) {
                            BigDecimal totalPrice = getTotalJourneyPrice(journey);
                            TripEdge neighbourTripEdge = new TripEdge(totalPrice, node, journey.vehicleType());
                            neighbourTripEdge.setPriceForTrip(journey.price());
                            currentCity.addNeighbour(neighbourTripEdge);
                            isNotInspectedYet = true;
                            break;
                        }
                    }

                    if (!isNotInspectedYet) {
                        setIfNotInspected(journey, currentCity);
                    }
                }
            }
        }
    }

    private static void setIfNotInspected(Journey journey, CityNode currentCity) {
        CityNode neighbourCityNode = new CityNode(journey.to().name(), journey.to().location());
        neighbourCityNode.getBestFoundPathToNode().setPriceForTrip(journey.price());

        BigDecimal totalPrice = getTotalJourneyPrice(journey);

        TripEdge neighbourTripEdge = new TripEdge(totalPrice, neighbourCityNode, journey.vehicleType());
        neighbourTripEdge.setPriceForTrip(journey.price());
        currentCity.addNeighbour(neighbourTripEdge);
    }

    private static void inspectCurrentCityNodeNeighbours(CityNode currentCityNode, CityNode targetCityNode,
                                                         PriorityQueue<CityNode> toInspect,
                                                         List<CityNode> inspected) {
        for (TripEdge neighbour : currentCityNode.getNeighbors()) {
            CityNode neighbourCityNode = neighbour.getCityNode();
            BigDecimal totalWeight = neighbour.getWeight().add(currentCityNode.getG());

            if (!toInspect.contains(neighbourCityNode) && !inspected.contains(neighbourCityNode)) {
                setNeighbourData(neighbourCityNode, currentCityNode, totalWeight, targetCityNode, neighbour);
                toInspect.add(neighbourCityNode);
            } else {
                if (totalWeight.compareTo(neighbourCityNode.getG()) < 0) {
                    setNeighbourData(neighbourCityNode, currentCityNode, totalWeight, targetCityNode, neighbour);
                    neighbourCityNode.getBestFoundPathToNode().setPriceForTrip(neighbour.getPriceForTrip());

                    if (inspected.contains(neighbourCityNode)) {
                        inspected.remove(neighbourCityNode);
                        toInspect.add(neighbourCityNode);
                    }
                }
            }
        }
    }
}
