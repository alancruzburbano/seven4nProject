package models;

public class OrderManager implements Runnable {

    Deliverable deliver;

    public OrderManager(Deliverable deliver) {
        this.deliver = deliver;
    }

    @Override
    public void run() {
        deliver.executeDelivery();
    }
}
