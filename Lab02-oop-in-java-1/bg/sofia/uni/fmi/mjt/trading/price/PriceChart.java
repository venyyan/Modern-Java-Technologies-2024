package bg.sofia.uni.fmi.mjt.trading.price;

import bg.sofia.uni.fmi.mjt.trading.Utils;

public class PriceChart implements PriceChartAPI{
    private double microsoftStockPrice;
    private double googleStockPrice;
    private double amazonStockPrice;
    public PriceChart(double microsoftStockPrice, double googleStockPrice, double amazonStockPrice) {
        this.microsoftStockPrice = microsoftStockPrice;
        this.googleStockPrice = googleStockPrice;
        this.amazonStockPrice = amazonStockPrice;
    }
    @Override
    public double getCurrentPrice(String stockTicker) {
        if (stockTicker == null) {
            return 0.00;
        }
        switch (stockTicker) {
            case "MSFT" -> {
                return Utils.round(microsoftStockPrice, 2);
            }
            case "AMZ" -> {
                return Utils.round(amazonStockPrice, 2);
            }
            case "GOOG" -> {
                return Utils.round(googleStockPrice, 2);
            }
            default -> {
                return 0.00;
            }
        }
    }

    @Override
    public boolean changeStockPrice(String stockTicker, int percentChange) {
        if (percentChange <= 0 || stockTicker == null)
            return false;

        switch (stockTicker) {
            case "MSFT" -> {
                microsoftStockPrice += ((percentChange / 100.0) * microsoftStockPrice);
            }
            case "AMZ" -> {
                amazonStockPrice += ((percentChange / 100.0) * amazonStockPrice);
            }
            case "GOOG" -> {
                googleStockPrice += ((percentChange / 100.0) * googleStockPrice);
            }
            default -> {
                return false;
            }
        }
        return true;
    }
}