package simplesocketclient;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//This thread runs separately and in parallel
//with the GUI. This one is solely listening 
//for a message from the socket server that
//will indicate that the button wired to the
//Raspberry Pi was pressed.
class ButtonClickReceivedThread implements Runnable {

    private Socket client;
    javax.swing.JButton button;
    private javax.swing.JLabel label;

    //We're passing a label and button to this thread
    //because we will be controlling them based on
    //a message sent from the server.
    ButtonClickReceivedThread(Socket client, javax.swing.JLabel label, javax.swing.JButton button) {
        this.client = client;
        this.label = label;
        this.button = button;
    }

    public void run() {
        String line = "";
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (true) {
                //Has something come in from the server?
                if (in.ready()) {
                    //Read the message from the server.
                    line = in.readLine();

                    //Send data back to client
                    System.out.println("ButtonClickReceivedThread: " + line);

                    //If the message is what we are looking
                    //for (like this) - process it.
                    if (line.equals("button_clicked")) {
                        label.setForeground(Color.green);
                        label.setVisible(true);
                        button.setVisible(true);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception message: " + ex.getMessage());
        }
    }
}
