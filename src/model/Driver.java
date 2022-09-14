package model;

public class Driver {
    private String driverId;
    private String driverName;
    private String driverContact;
    private String vehicleType;

    public Driver(String finalId, String text, String supplierAddressText, String supplierContactText, double v) {
    }

    public Driver(String driverId, String driverName, String driverContact, String vehicleType) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.vehicleType = vehicleType;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + driverId + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverContact='" + driverContact + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                '}';
    }
}
