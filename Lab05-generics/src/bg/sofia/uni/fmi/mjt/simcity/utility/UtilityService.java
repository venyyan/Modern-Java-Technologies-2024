package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.HashMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {
    Map<UtilityType, Double> taxRates;
    public UtilityService(Map<UtilityType, Double> taxRates) {
        this.taxRates = taxRates;
    }

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null) {
            throw new IllegalArgumentException("Utility type is null!");
        }

        if (billable == null) {
            throw new IllegalArgumentException("Billable type is null!");
        }

        double costs = taxRates.get(utilityType);
        switch (utilityType) {
            case WATER -> costs *= billable.getWaterConsumption();
            case ELECTRICITY -> costs *= billable.getElectricityConsumption();
            case NATURAL_GAS -> costs *= billable.getNaturalGasConsumption();
        }
        return costs;
    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException("Billable type is null!");
        }

        return taxRates.get(UtilityType.ELECTRICITY) * billable.getElectricityConsumption()
            + taxRates.get(UtilityType.WATER) * billable.getWaterConsumption()
            + taxRates.get(UtilityType.NATURAL_GAS) * billable.getNaturalGasConsumption();
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException("Billable is null");
        }

        double waterCostsDiff = Math.abs(getUtilityCosts(UtilityType.WATER, firstBillable) -
            getUtilityCosts(UtilityType.WATER, secondBillable));
        double electricityCostsDiff = Math.abs(getUtilityCosts(UtilityType.ELECTRICITY, firstBillable) -
            getUtilityCosts(UtilityType.ELECTRICITY, secondBillable));
        double naturalGasCostsDiff = Math.abs(getUtilityCosts(UtilityType.NATURAL_GAS, firstBillable) -
            getUtilityCosts(UtilityType.NATURAL_GAS, secondBillable));

        Map<UtilityType, Double> costsDifference = new HashMap<>();
        costsDifference.put(UtilityType.WATER, waterCostsDiff);
        costsDifference.put(UtilityType.ELECTRICITY, electricityCostsDiff);
        costsDifference.put(UtilityType.NATURAL_GAS, naturalGasCostsDiff);

        Map<UtilityType, Double> unmodifiableCostsDifference = Map.copyOf(costsDifference);
        return unmodifiableCostsDifference;
    }
}
