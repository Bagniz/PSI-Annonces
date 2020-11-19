public enum Requests {
    CHAT("CHAT", "Chat with an ad poster"),
    SIGNUP("SIGNUP", "Sign up a new client"),
    LOGIN("LOGIN", "Log in an existing client"),
    GETADS("GETADS", "Get available ads"),
    GETAD("GETAD", "Get information of an ad"),
    ADDAD("ADDAD", "Add a new Ad"),
    UPDATEAD("UPDATEAD", "Update the information of an existing ad"),
    DELETEAD("DELETEAD", "Delete an existing ad"),
    GETRESERVEDADS("GETRESERVEDADS", "Get your reserved ads"),
    RESERVEAD("RESERVEAD", "Reserve an ad"),
    UNRESERVEAD("UNRESERVEAD", "Unreserve an ad"),
    GETCLIENTINFO("GETCLIENTINFO", "Get your personnel information"),
    UPDATECLIENT("UPDATECLIENT","Update your personnel information"),
    DELETECLIENT("DELETECLIENT","Delete your account");

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
