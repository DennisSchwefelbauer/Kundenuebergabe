package UebergabeKunde.Constructor;

public class Finished {
    private String name;
    private String vehicle;
    private String worker;
    private String notCustomer;
    private String date;

    public Finished(String name, String vehicle, String worker, String notCustomer, String date) {
        this.name = name;
        this.vehicle = vehicle;
        this.worker = worker;
        this.notCustomer = notCustomer;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getWorker() {
        return worker;
    }

    public String getNotCustomer() {
        return notCustomer;
    }

    public String getDate() {
        return date;
    }
}
