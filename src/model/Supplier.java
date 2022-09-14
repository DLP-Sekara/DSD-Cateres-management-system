package model;

public class Supplier {
    private String shopNo;
    private String shopName;
    private String address;
    private String contact;
    private double payment;

    public Supplier(String text, String supplierShopNameText, String supplierAddressText, String supplierContactText, String supplierPaymentText) {
    }

    public Supplier(String shopNo, String shopName, String address, String contact, double payment) {
        this.shopNo = shopNo;
        this.shopName = shopName;
        this.address = address;
        this.contact = contact;
        this.payment = payment;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "shopNo='" + shopNo + '\'' +
                ", shopName='" + shopName + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", payment=" + payment +
                '}';
    }
}
