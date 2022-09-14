package model;

public class SystemUser {
    private String sid;
    private String userName;
    private String password;

    public SystemUser() {
    }

    public SystemUser(String sid, String userName, String password) {
        this.sid = sid;
        this.userName = userName;
        this.password = password;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SystemUser{" +
                "sid='" + sid + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
