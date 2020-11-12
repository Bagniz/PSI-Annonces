import java.io.Serializable;

public enum  Command implements Serializable {
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
    DELETECLIENT("DELETECLIENT","Delete your account"),
    LOGOUT("LOGOUT", "Log out");

    private final String stringCommand;
    private final String commandInformation;

    Command(String stringCommand, String commandInformation) {
        this.stringCommand = stringCommand;
        this.commandInformation = commandInformation;
    }

    public String getStringCommand() {
        return stringCommand;
    }

    public String getCommandInformation() {
        return commandInformation;
    }
}
