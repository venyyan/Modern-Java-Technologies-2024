package bg.sofia.uni.fmi.mjt.itinerary.algorithm;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public class TripEdge {
    private BigDecimal weight;
    private CityNode node;
    private VehicleType vehicleType;
    private BigDecimal priceForTrip;

    TripEdge() { }

    TripEdge(BigDecimal weight, CityNode node, VehicleType vehicleType) {
        this.weight = weight;
        this.node = node;
        this.vehicleType = vehicleType;
    }

    public CityNode getCityNode() {
        return node;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setPriceForTrip(BigDecimal price) {
        this.priceForTrip = price;
    }

    public BigDecimal getPriceForTrip() {
        return priceForTrip;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
