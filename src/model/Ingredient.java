package model;

public class Ingredient {
   private String mealNo;
    private String shopNo;
    private String ingredientName;
    private int qty;
    private double price;

    public Ingredient() {
    }

    public Ingredient(String mealNo, String shopNo, String ingredientName, int qty, double price) {
        this.mealNo = mealNo;
        this.shopNo = shopNo;
        this.ingredientName = ingredientName;
        this.qty = qty;
        this.price = price;
    }

    public String getMealNo() {
        return mealNo;
    }

    public void setMealNo(String mealNo) {
        this.mealNo = mealNo;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "mealNo='" + mealNo + '\'' +
                ", shopNo='" + shopNo + '\'' +
                ", ingredientName='" + ingredientName + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
