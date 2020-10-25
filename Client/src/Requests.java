public enum Requests {
    ADDAD("ADDAD","Add a new Ad");

    private String stringValue;
    private String information;

    Requests(String stringValue, String information)
    {
        this.stringValue = stringValue;
        this.information = information;
    }

    public String getInformation() {
        return information;
    }

    public String getStringValue() {
        return stringValue;
    }
}
