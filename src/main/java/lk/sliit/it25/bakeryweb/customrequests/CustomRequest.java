package lk.sliit.it25.bakeryweb.customrequests;

public class CustomRequest {
    private String requestId;
    private String customerName;
    private String tiers;
    private String theme;
    private String status;

    public CustomRequest(String requestId, String customerName, String tiers, String theme, String status) {
        this.requestId = requestId;
        this.customerName = customerName;
        this.tiers = tiers;
        this.theme = theme;
        this.status = status;
    }

    public String getRequestId() { return requestId; }
    public String getCustomerName() { return customerName; }
    public String getTiers() { return tiers; }
    public String getTheme() { return theme; }
    public String getStatus() { return status; }
}