package bg.sofia.uni.fmi.mjt.itinerary.algorithm;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Location;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CityNode implements Comparable<CityNode> {
    private static final BigDecimal AVERAGE_COST_PER_KM = BigDecimal.valueOf(20);
    private static final int METERS_IN_KM = 1000;

    private String name;
    private Location location;
    private CityNode parent;
    private List<TripEdge> neighbors;
    private BigDecimal f;
    private BigDecimal g;
    private TripEdge bestFoundPathToNode;

    public CityNode(String name, Location location) {
        this.name = name;
        this.location = location;
        this.f = BigDecimal.valueOf(0);
        this.g = BigDecimal.valueOf(0);
        this.neighbors = new ArrayList<>();
        this.bestFoundPathToNode = new TripEdge();
    }

    @Override
    public int compareTo(CityNode other) {
        BigDecimal fScaled = this.f.setScale(1, RoundingMode.HALF_UP);
        BigDecimal otherScaled = other.f.setScale(1, RoundingMode.HALF_UP);

        if (fScaled.equals(otherScaled)) {
            return this.name.compareTo(other.name);
        }
        return this.f.compareTo(other.f);
    }

    public BigDecimal calculateHeuristic(CityNode target) {
        return (distanceBetween(target).multiply(AVERAGE_COST_PER_KM))
            .divide(BigDecimal.valueOf(METERS_IN_KM), RoundingMode.DOWN);
    }

    public void addNeighbour(TripEdge newNeighbour) {
        neighbors.add(newNeighbour);
    }

    public void setF(BigDecimal f) {
        this.f = f;
    }

    public void setG(BigDecimal g) {
        this.g = g;
    }

    public BigDecimal getG() {
        return g;
    }

    public String getName() {
        return name;
    }

    public void setParent(CityNode parent) {
        this.parent = parent;
    }

    public CityNode getParent() {
        return parent;
    }

    public List<TripEdge> getNeighbors() {
        return neighbors;
    }

    public TripEdge getBestFoundPathToNode() {
        return bestFoundPathToNode;
    }

    public City getCorrespondingCity() {
        return new City(this.getName(), this.location);
    }

    private BigDecimal distanceBetween(CityNode other) {
        BigDecimal xDifference = BigDecimal.valueOf(Math.abs(location.x() - other.location.x()));
        BigDecimal yDifference = BigDecimal.valueOf(Math.abs(location.y() - other.location.y()));

        return xDifference.add(yDifference);
    }
}
