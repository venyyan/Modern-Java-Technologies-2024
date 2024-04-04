package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {
    private int buildableArea;
    Map<String, E> plots;

    public Plot(int buildableArea) {
        this.buildableArea = buildableArea;
        plots = new HashMap<>();
    }

    @Override
    public void construct(String address, E buildable) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is null or blank!");
        }

        if (buildable == null) {
            throw new IllegalArgumentException("Buildable is null!");
        }

        if (buildable.getArea() > this.buildableArea) {
            throw new InsufficientPlotAreaException("The required area exceeds the remaining plot area!");
        }

        if (plots.containsKey(address)) {
            throw new BuildableAlreadyExistsException("Buildable already exists!");
        }
        this.plots.put(address, buildable);
        this.buildableArea -= buildable.getArea();
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("Buildables is null or empty!");
        }

        for (String address : buildables.keySet()) {
            if (this.plots.containsKey(address)) {
                throw new BuildableAlreadyExistsException("Plot is already occupied!");
            }
        }

        int buildablesArea = 0;
        for (Buildable buildable : buildables.values()) {
            buildablesArea += buildable.getArea();
        }

        if (buildablesArea > buildableArea) {
            throw new InsufficientPlotAreaException("The provided buildables exceed the remaining plot area!");
        }
        this.plots.putAll(buildables);
        this.buildableArea -= buildablesArea;
    }

    @Override
    public void demolish(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is null or blank!");
        }

        if (!plots.containsKey(address)) {
            throw new BuildableNotFoundException("Buildable does not exist!");
        }

        this.buildableArea += this.plots.get(address).getArea();
        this.plots.remove(address);
    }

    @Override
    public void demolishAll() {
        int allAreas = 0;
        for (Buildable buildable : plots.values()) {
            allAreas += buildable.getArea();
        }
        this.buildableArea += allAreas;
        this.plots.clear();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        Map<String, E> unmodifiablePlots = Map.copyOf(this.plots);
        return unmodifiablePlots;
    }

    @Override
    public int getRemainingBuildableArea() {
        return this.buildableArea;
    }
}
