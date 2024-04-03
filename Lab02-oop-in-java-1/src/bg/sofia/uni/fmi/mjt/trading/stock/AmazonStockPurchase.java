package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;
import bg.sofia.uni.fmi.mjt.trading.Utils;
public class AmazonStockPurchase extends Stock{
    public AmazonStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        super(quantity, purchaseTimestamp, purchasePricePerUnit);
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public LocalDateTime getPurchaseTimestamp() {
        return this.purchaseTimestamp;
    }

    @Override
    public double getPurchasePricePerUnit() {
        return Utils.round(this.purchasePricePerUnit, 2);
    }

    @Override
    public double getTotalPurchasePrice() {
        double totalPrice = this.quantity * this.purchasePricePerUnit;
        return Utils.round(totalPrice, 2);
    }

    @Override
    public String getStockTicker() {
        return "AMZ";
    }
}
