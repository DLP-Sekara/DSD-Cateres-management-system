package model;

public class OrderDetails {
    private String mealNo;
    private String oid;
    private int breakfast;
    private int lunch;
    private int dinner;
    private double price;
    private String mealTime;

    public OrderDetails() {
    }

    public OrderDetails(String mealNo, String oid, int breakfast, int lunch, int dinner, double price, String mealTime) {
        this.mealNo = mealNo;
        this.oid = oid;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.price = price;
        this.mealTime = mealTime;
    }

    public String getMealNo() {
        return mealNo;
    }

    public void setMealNo(String mealNo) {
        this.mealNo = mealNo;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public int getLunch() {
        return lunch;
    }

    public void setLunch(int lunch) {
        this.lunch = lunch;
    }

    public int getDinner() {
        return dinner;
    }

    public void setDinner(int dinner) {
        this.dinner = dinner;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "mealNo='" + mealNo + '\'' +
                ", oid='" + oid + '\'' +
                ", breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                ", price=" + price +
                ", mealTime='" + mealTime + '\'' +
                '}';
    }
}
