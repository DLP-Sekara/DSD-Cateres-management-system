package model;

import java.util.Date;

public class Transport {
    private String transportNo;
    private String transportType;
    private String date;
    private String time;
    private double cost;
    private String driverId;

    public Transport() {
    }

    public Transport(String transportNo, String transportType, String date, String time, double cost, String driverId) {
        this.transportNo = transportNo;
        this.transportType = transportType;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.driverId = driverId;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "transportNo='" + transportNo + '\'' +
                ", transportType='" + transportType + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", cost=" + cost +
                ", driverId='" + driverId + '\'' +
                '}';
    }
}
