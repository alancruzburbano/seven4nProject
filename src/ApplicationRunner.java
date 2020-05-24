import models.*;
import utils.ReadProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ApplicationRunner {

    private static ReadProperties propertiesReader;

    public static void main(String[] args) {

        propertiesReader = new ReadProperties();
        propertiesReader.initProperties();
        Integer dronesAvailable = readNumericalValueByKey("drones-available");
        Integer droneCapacity = readNumericalValueByKey("drone-capacity");
        ExecutorService deliveryExecutorService = Executors.newFixedThreadPool(dronesAvailable);
        try {
            List<Path> orders = Files.list(Paths.get("input/"))
                    .filter(f -> f.toString().toLowerCase().endsWith(".txt"))
                    .collect(Collectors.toList());

            for (int i = 0; i < orders.size(); i++) {
                if (i <= dronesAvailable) {
                    Order order = new Order(orders.get(i));
                    Drone drone = new Drone(droneCapacity);
                    Deliverable deliver = new Deliver(order, drone);
                    Runnable ordersDelivery = new OrderManager(deliver);
                    deliveryExecutorService.execute(ordersDelivery);
                } else {
                    System.out.println("all the drones are busy..");
                    i = orders.size() - 1;
                }
            }
            deliveryExecutorService.shutdown();
            System.out.println("Orders processed");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int readNumericalValueByKey(String key) {
        return Integer.valueOf(propertiesReader
                .readPropertieByKey(key));
    }

}
