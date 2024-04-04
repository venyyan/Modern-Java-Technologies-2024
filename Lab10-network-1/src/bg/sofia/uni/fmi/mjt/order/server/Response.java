package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.order.Order;

import java.util.Collection;

public record Response(Status status, String additionalInfo, Collection<Order> orders) {
    private enum Status {
        OK, CREATED, DECLINED, NOT_FOUND
    }

    public static Response create(int id) {
        return new Response(Status.CREATED, Integer.toString(id), null);
    }

    public static Response ok(Collection<Order> orders) {
        return new Response(Status.OK, "", orders);
    }

    public static Response decline(String errorMessage) {
        return new Response(Status.DECLINED, errorMessage, null);
    }

    public static Response notFound(int id) {
        return new Response(Status.NOT_FOUND, Integer.toString(id), null);
    }

    public String readable() {
        StringBuilder ordersString = new StringBuilder("{");

        switch (status) {
            case CREATED -> appendCreatedStatus(ordersString);
            case OK -> appendOkStatus(ordersString);
            case DECLINED -> appendDeclinedStatus(ordersString);
            case NOT_FOUND -> appendNotFoundStatus(ordersString);
        }

        return ordersString.toString();
    }

    private void appendCreatedStatus(StringBuilder ordersString) {
        ordersString.append("\"status\":\"CREATED\", \"additionalInfo\":\"ORDER_ID=")
            .append(additionalInfo).append("\"}");
    }

    private void appendOkStatus(StringBuilder ordersString) {
        ordersString.append("\"status\":\"OK\", \"orders\":[");

        int index = 0;
        for (Order current : orders) {
            appendOrderDetails(ordersString, current);

            if ((index++) != orders.size() - 1) {
                ordersString.append(",");
            }
        }

        ordersString.append("]}");
    }

    private void appendOrderDetails(StringBuilder ordersString, Order order) {
        ordersString.append("{\"id\":").append(order.id()).append(", \"tShirt\":")
            .append("{\"size\":\"").append(order.tShirt().size()).append("\", \"color\":\"")
            .append(order.tShirt().color()).append("\"}, \"destination\":\"")
            .append(order.destination()).append("\"}");
    }

    private void appendDeclinedStatus(StringBuilder ordersString) {
        ordersString.append("\"status\":\"DECLINED\", \"additionalInfo\":\"").append(additionalInfo).append("\"}");
    }

    private void appendNotFoundStatus(StringBuilder ordersString) {
        ordersString.append("\"status\":\"NOT_FOUND\", \"additionalInfo\":\"Order with id = ")
            .append(additionalInfo).append(" does not exist.\"}");
    }
}