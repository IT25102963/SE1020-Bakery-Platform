package lk.sliit.it25.bakeryweb.customer;

public class Customer {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;

    public Customer(String name,String email,String phone,String address,String password){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public String toCSV() {
        return name + "," + email + "," + phone + "," + address + "," + password;
    }
}
