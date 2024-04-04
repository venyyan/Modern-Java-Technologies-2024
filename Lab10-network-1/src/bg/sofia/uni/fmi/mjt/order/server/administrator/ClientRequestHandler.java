package bg.sofia.uni.fmi.mjt.order.server.administrator;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {
    private Socket socket;
    OrderRepository repository;

    private static final int SIZE_TOKEN = 1;
    private static final int COLOR_TOKEN = 2;
    private static final int DESTINATION_TOKEN = 3;
    public ClientRequestHandler(Socket socket, OrderRepository repository) {
        this.socket = socket;
        this.repository = repository;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] inputLineTokens = inputLine.split(" ");
                String mainCommand = inputLineTokens[0];

                Response response = getResponse(mainCommand, inputLineTokens, out);

                out.println(response.readable());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Response getResponse(String mainCommand, String[] inputLineTokens, PrintWriter out) {
        Response response;
        if (mainCommand.equals("request")) {
            String size = inputLineTokens[SIZE_TOKEN].split("=")[1];
            String color = inputLineTokens[COLOR_TOKEN].split("=")[1];
            String destination = inputLineTokens[DESTINATION_TOKEN].split("=")[1];
            response = repository.request(size, color, destination);
        } else if (mainCommand.equals("get")) {
            String getCommand = inputLineTokens[SIZE_TOKEN];

            if (getCommand.equals("all")) {
                response = repository.getAllOrders();
            } else if (getCommand.equals("all-successful")) {
                response = repository.getAllSuccessfulOrders();
            } else if (getCommand.equals("my-order")) {
                String orderId = inputLineTokens[COLOR_TOKEN].split("=")[1];
                response = repository.getOrderById(Integer.parseInt(orderId));
            } else {
                response = Response.decline("");
            }
        } else {
            out.println("Unknown command");
            response = Response.decline("");
        }
        return response;
    }
}
