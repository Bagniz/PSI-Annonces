public enum Requests {
    GETADS("GETADS", "Get available ads"),
    GETAD("GETAD", "Get information of an ad"),
    ADDAD("ADDAD", "Add a new Ad"),
    UPDATEAD("UPDATEAD", "Update the information of an existing ad"),
    DELETEAD("DELETEAD", "Delete an existing ad"),
    GETCLIENTINFO("GETCLIENTINFO", "Get your personnel information"),
    UPDATECLIENT("UPDATECLIENT","Update your personnel information"),
    DELETECLIENT("DELETECLIENT","Delete your account"),
    LOGOUT("LOGOUT", "Log out");

    private final String stringValue;
    private final String information;

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
