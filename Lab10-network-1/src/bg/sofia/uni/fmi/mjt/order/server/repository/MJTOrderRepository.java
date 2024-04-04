package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

public class MJTOrderRepository implements OrderRepository {
    private final List<Order> orders;
    private int id = 1;

    public MJTOrderRepository() {
        orders = new ArrayList<>();
    }

    @Override
    public Response request(String size, String color, String destination) {
        validateTokens(size, color, destination);
        boolean isValidSize = isSizeValid(size);
        boolean isValidColor = isColorValid(color);
        boolean isValidDestination = isDestinationValid(destination);

        Size enumSize = getEnumSize(isValidSize, size);
        Color enumColor = getEnumColor(isValidColor, color);
        Destination enumDestination = getEnumDestination(isValidDestination, destination);

        if (!isValidSize || !isValidColor || !isValidDestination) {
            orders.add(new Order(-1, new TShirt(enumSize, enumColor), enumDestination));
            String result = getInvalidFields(isValidColor, isValidSize, isValidDestination);

            return Response.decline(result);
        } else {
            Order newOrder = new Order(id, new TShirt(enumSize, enumColor), enumDestination);
            orders.add(newOrder);
            id++;

            return Response.create(id - 1);
        }
    }

    private void validateTokens(String size, String color, String destination) {
        if (size == null) {
            throw new IllegalArgumentException("Size cannot be null!");
        }

        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null!");
        }

        if (destination == null) {
            throw new IllegalArgumentException("Destination cannot be null!");
        }
    }

    private Size getEnumSize(boolean isValidSize, String size) {
        Size enumSize;
        if (isValidSize) {
            enumSize = Size.valueOf(size);
        } else {
            enumSize = Size.UNKNOWN;
        }
        return enumSize;
    }

    private Color getEnumColor(boolean isValidColor, String color) {
        Color enumColor;
        if (isValidColor) {
            enumColor = Color.valueOf(color);
        } else {
            enumColor = Color.UNKNOWN;
        }
        return enumColor;
    }

    private Destination getEnumDestination(boolean isValidDestination, String destination) {
        Destination enumDestination;
        if (isValidDestination) {
            enumDestination = Destination.valueOf(destination);
        } else {
            enumDestination = Destination.UNKNOWN;
        }
        return enumDestination;
    }

    private String getInvalidFields(boolean isValidColor, boolean isValidSize, boolean isValidDestination) {
        boolean isFirstComma = !isValidColor && !isValidSize;

        StringBuilder invalidFields = new StringBuilder("invalid=");

        if (!isValidSize) {
            invalidFields.append("size");
        }

        if (isFirstComma) {
            invalidFields.append(",");
        }

        if (!isValidColor) {
            invalidFields.append("color");
        }

        if (!isValidDestination && !isFirstComma) {
            if (invalidFields.toString().equals("invalid=")) {
                invalidFields.append("destination");
            } else {
                invalidFields.append(",destination");
            }
        } else if (!isValidDestination) {
            invalidFields.append(",destination");
        }

        return invalidFields.toString();
    }

    @Override
    public Response getOrderById(int id) {
        Optional<Order> foundOrder = orders.stream()
            .filter(order -> order.id() == id)
            .findFirst();

        if (foundOrder.isPresent()) {
            return Response.ok(List.of(foundOrder.get()));
        }
        return Response.notFound(id);
    }

    @Override
    public Response getAllOrders() {
        return Response.ok(orders);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        List<Order> successfulOrders = orders.stream()
            .filter(o -> o.id() != -1)
            .toList();

        return Response.ok(successfulOrders);
    }

    private boolean isSizeValid(String size) {
        for (Size enumSize : Size.values()) {
            if (size.equals(enumSize.name())) {
                return true;
            }
        }
        return false;
    }

    private boolean isDestinationValid(String destination) {
        for (Destination enumDestination : Destination.values()) {
            if (destination.equals(enumDestination.name())) {
                return true;
            }
        }
        return false;
    }

    private boolean isColorValid(String color) {
        for (Color enumColor : Color.values()) {
            if (color.equals(enumColor.name())) {
                return true;
            }
        }
        return false;
    }
}
