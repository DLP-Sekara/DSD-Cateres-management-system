package model;

public class Customer {
    private String cid ;
    private String customerName ;
    private String address ;
    private String factory;
    private String contact ;
    private String gender;
    private String sid ;

    public Customer() {
    }

    public Customer(String cid, String customerName, String address, String factory, String contact, String gender, String sid) {
        this.cid = cid;
        this.customerName = customerName;
        this.address = address;
        this.factory = factory;
        this.contact = contact;
        this.gender = gender;
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cid='" + cid + '\'' +
                ", customerName='" + customerName + '\'' +
                ", address='" + address + '\'' +
                ", factory='" + factory + '\'' +
                ", contact='" + contact + '\'' +
                ", gender='" + gender + '\'' +
                ", sid='" + sid + '\'' +
                '}';
    }
}
