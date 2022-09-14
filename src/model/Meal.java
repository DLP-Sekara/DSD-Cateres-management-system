package model;

public class Meal {
    private String mealNo;
    private String mealName;
    private String mealType;
    private double cost;

    public Meal() {
    }

    public Meal(String mealNo, String mealName, String mealType, double cost) {
        this.mealNo = mealNo;
        this.mealName = mealName;
        this.mealType = mealType;
        this.cost = cost;
    }

    public String getMealNo() {
        return mealNo;
    }

    public void setMealNo(String mealNo) {
        this.mealNo = mealNo;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "mealNo='" + mealNo + '\'' +
                ", mealName='" + mealName + '\'' +
                ", mealType='" + mealType + '\'' +
                ", cost=" + cost +
                '}';
    }
}
