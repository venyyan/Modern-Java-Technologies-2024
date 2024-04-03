package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import java.time.LocalDateTime;

import java.util.Arrays;

public class Portfolio implements PortfolioAPI {
    private String owner;
    private PriceChartAPI priceChart;
    private double budget;
    private int maxSize;
    private StockPurchase[] stockPurchases;
    private int stockPurchasesSize;

    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize) {
        this.budget = budget;
        this.maxSize = maxSize;
        this.owner = owner;
        this.priceChart = priceChart;
        if (maxSize != 0)
            this.stockPurchases = new StockPurchase[maxSize];
        else
            this.stockPurchases = new StockPurchase[1];

        this.stockPurchasesSize = 0;
    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize) {
        this(owner, priceChart, budget, maxSize);
        this.stockPurchasesSize = stockPurchases.length;
        if (stockPurchasesSize == 0)
            this.stockPurchases = new StockPurchase[maxSize];
        else
            this.stockPurchases = Arrays.copyOf(stockPurchases, stockPurchases.length);
    }

    @Override
    public StockPurchase buyStock(String stockTicker, int quantity) {
        if (stockTicker == null || quantity <= 0 || this.maxSize <= this.stockPurchasesSize) {
            return null;
        }
        double pricePerUnit = this.priceChart.getCurrentPrice(stockTicker);
        double price = quantity * pricePerUnit;
        if (price > this.budget) {
            return null;
        }

        this.budget -= price;
        StockPurchase purchase;
        switch (stockTicker) {
            case "MSFT" -> {
                purchase = new MicrosoftStockPurchase(quantity, LocalDateTime.now(), pricePerUnit);
            }
            case "AMZ" -> {
                purchase = new AmazonStockPurchase(quantity, LocalDateTime.now(), pricePerUnit);
            }
            case "GOOG" -> {
                purchase = new GoogleStockPurchase(quantity, LocalDateTime.now(), pricePerUnit);
            }
            default -> {
                return null;
            }
        }
        this.priceChart.changeStockPrice(stockTicker, 5);
        this.stockPurchases[stockPurchasesSize++] = purchase;
        return purchase;
    }

    @Override
    public StockPurchase[] getAllPurchases() {
        return this.stockPurchases;
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        StockPurchase[] temp = new StockPurchase[stockPurchasesSize];
        int counter = 0;
        for (int i = 0; i < this.stockPurchasesSize; i++) {
            LocalDateTime currentTimestamp = stockPurchases[i].getPurchaseTimestamp();
            boolean isBefore = currentTimestamp.isBefore(endTimestamp) || currentTimestamp.equals(endTimestamp);
            boolean isAfter = currentTimestamp.isAfter(startTimestamp) || currentTimestamp.equals(startTimestamp);

            if (isAfter && isBefore)
                temp[counter++] = stockPurchases[i];
        }
        StockPurchase[] stockPurchasesAtTimestamp = new StockPurchase[counter];
        for (int i = 0; i < counter; i++) {
            stockPurchasesAtTimestamp[i] = temp[i];
        }
        return stockPurchasesAtTimestamp;
    }

    @Override
    public double getNetWorth() {
        double netWorth = 0;
        for (int i = 0; i < this.stockPurchasesSize; i++) {
            double pricePerUnit = this.priceChart.getCurrentPrice(stockPurchases[i].getStockTicker());
            netWorth += (stockPurchases[i].getQuantity() * pricePerUnit);
        }
        return Utils.round(netWorth, 2);
    }

    @Override
    public double getRemainingBudget() {
        return Utils.round(this.budget, 2);
    }

    @Override
    public String getOwner() {
        return this.owner;
    }
}
