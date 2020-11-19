import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyJFrame extends JFrame {

    private TextArea textAreaConsole;
    private TextArea writeAreaConsole;
    private TextArea textAreaChat;
    private TextArea writeAreaChat;
    private boolean readFromConsole = false;
    private boolean readFromChat = false;

    public MyJFrame()
    {
        super();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        JPanel consolePanel = new JPanel();
        consolePanel.setLayout(new BoxLayout(consolePanel,BoxLayout.Y_AXIS));
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel,BoxLayout.Y_AXIS));
        Label consoleLabel = new Label("Console");
        textAreaConsole = new TextArea(15,15);
        textAreaConsole.setFont(new Font("Serif", Font.ITALIC, 16));
        textAreaConsole.setEditable(false);

        writeAreaConsole = new TextArea(5,5);
        writeAreaConsole.setFont(new Font("Serif", Font.ITALIC, 16));
        writeAreaConsole.setEditable(true);

        writeAreaConsole.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    readFromConsole = true;
                }
            }
        });

        Label chatLabel = new Label("Chat");
        textAreaChat = new TextArea(15,15);
        textAreaChat.setFont(new Font("Serif", Font.ITALIC, 16));
        textAreaChat.setEditable(false);

        writeAreaChat = new TextArea(5,5);
        writeAreaChat.setFont(new Font("Serif", Font.ITALIC, 16));
        writeAreaChat.setEditable(true);

        writeAreaChat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    readFromChat = true;
                }
            }
        });


        consolePanel.add(consoleLabel);
        consolePanel.add(textAreaConsole);
        consolePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        consolePanel.add(writeAreaConsole);
        chatPanel.add(chatLabel);
        chatPanel.add(textAreaChat);
        chatPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        chatPanel.add(writeAreaChat);
        mainPanel.add(consolePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        mainPanel.add(chatPanel);
        super.add(mainPanel);
    }
    public void printConsole(String string)
    {
        this.textAreaConsole.setText(this.textAreaConsole.getText().toString()+"\n"+string);

    }

    public void printChat(String string)
    {
        this.textAreaChat.setText(this.textAreaChat.getText().toString()+"\n"+string);

    }

    public String readConsole()
    {
        while (this.readFromConsole!=true){
            System.out.println();
        }
        this.readFromConsole= false;
        String string = this.writeAreaConsole.getText();
        this.writeAreaConsole.setText("");
        string = string.replace("\n","");
        printConsole(string);
        return string;
    }
    public String readChat()
    {
        while (this.readFromChat!=true){
            System.out.println();
        }
        this.readFromChat = false;
        String string = this.writeAreaChat.getText();
        string = string.replace("\n","");
        this.writeAreaChat.setText("");
        printChat(string);
        return string;
    }

}
