package view.tm;

public class OrderDetailsTm {
    private String customerName;
    private int breakfast;
    private int lunch;
    private int dinner;

    public OrderDetailsTm() {
    }

    public OrderDetailsTm(String customerName, int breakfast, int lunch, int dinner) {
        this.customerName = customerName;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    @Override
    public String toString() {
        return "OrderDetailsTm{" +
                "customerName='" + customerName + '\'' +
                ", breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                '}';
    }
}
