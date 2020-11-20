import java.io.Serializable;

public class Message implements Serializable {
    // Attributes
    private int clientId;
    private String clientName;
    private String clientMessage;

    // Constructor
    public Message(int clientId, String clientName, String clientMessage){
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientMessage = clientMessage;
    }

    // Getters and Setters
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }
}
