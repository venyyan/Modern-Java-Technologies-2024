import bg.sofia.uni.fmi.mjt.trading.Portfolio;
import bg.sofia.uni.fmi.mjt.trading.Utils;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChart;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        PriceChartAPI priceChartAPI = new PriceChart(10.0, 2.0, 3.0);

        Portfolio p = new Portfolio("veni", priceChartAPI, 100, 1);

        double a = 123.4555;
        System.out.println(Utils.round(a, 2));


    }
}