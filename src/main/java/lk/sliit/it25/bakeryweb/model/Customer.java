package lk.sliit.it25.bakeryweb.model;

public class Customer {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String photoUrl;

    // Constructor
    public Customer(String name, String email, String phone, String address, String password) {
        this(name, email, phone, address, password, "");
    }

    public Customer(String name, String email, String phone, String address, String password, String photoUrl) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.photoUrl = photoUrl == null ? "" : photoUrl;
    }

    // A helper method to easily save to our text file
    public String toCSV() {
        return name + "," + email + "," + phone + "," + address + "," + password + "," + photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl == null ? "" : photoUrl;
    }
}
