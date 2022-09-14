package model;

public class Order {
   private String oid;
    private String date;
    private String time;
    private String mealType;
    private double amount;
    private String cid;
    private String transportNo;

    public Order() {
    }

    public Order(String oid, String date, String time, String mealType, double amount, String cid, String transportNo) {
        this.oid = oid;
        this.date = date;
        this.time = time;
        this.mealType = mealType;
        this.amount = amount;
        this.cid = cid;
        this.transportNo = transportNo;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
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

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    @Override
    public String toString() {
        return "Order{" +
                "oid='" + oid + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", mealType='" + mealType + '\'' +
                ", amount=" + amount +
                ", cid='" + cid + '\'' +
                ", transportNo='" + transportNo + '\'' +
                '}';
    }
}
