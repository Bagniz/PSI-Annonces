import java.io.Serializable;

public class Request implements Serializable {
    private Command command;
    private Ad ad;
    private Category category;
    private Client client;
    private String newPassword;
    private boolean areMine;

    public Request() {
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean isAreMine() {
        return areMine;
    }

    public void setAreMine(boolean areMine) {
        this.areMine = areMine;
    }
}
