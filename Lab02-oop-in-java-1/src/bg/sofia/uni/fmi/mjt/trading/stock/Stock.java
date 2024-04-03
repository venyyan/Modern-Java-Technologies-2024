package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

public abstract class Stock implements StockPurchase {
    protected int quantity;
    protected LocalDateTime purchaseTimestamp;
    protected double purchasePricePerUnit;

    public Stock(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        this.purchasePricePerUnit = purchasePricePerUnit;
        this.purchaseTimestamp = purchaseTimestamp;
        this.quantity = quantity;
    }
}
