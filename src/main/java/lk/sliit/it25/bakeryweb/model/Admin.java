package lk.sliit.it25.bakeryweb.model;

public class Admin {
    private String name;
    private String email;
    private String password;

    public Admin(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String toCSV() {
        return name + "," + email + "," + password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
