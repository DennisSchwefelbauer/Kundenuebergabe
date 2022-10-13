package UebergabeKunde.Constructor;

public class Ready {
    private final String name;
    private final String vehicle;
    private final String info;
    private final String damage;
    private final String talked;
    private final String kv;

    public Ready(String name, String vehicle, String info, String damage, String talked, String kv) {
        this.name = name;
        this.vehicle = vehicle;
        this.info = info;
        this.damage = damage;
        this.talked = talked;
        this.kv = kv;
    }

    public String getName() {
        return name;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getInfo() {
        return info;
    }

    public String getDamage() {
        return damage;
    }

    public String getTalked() {
        return talked;
    }

    public String getKv() {
        return kv;
    }
}
