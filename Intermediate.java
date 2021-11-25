import java.io.*;
import java.net.*;

public class Intermediate {
    public int intermediatePort = 23;
    public int serverPort = 69;
    public DatagramSocket receiveSocket, sendReceiveSocket;
    public InetAddress serverAddress;

    public Intermediate()
    {}
    
    public static void main(String[] args)
    {
        Intermediate m = new Intermediate();
        m.start();
        while(true) m.run();
    }
    
    //Initiates the intermediate
    public void start(){
        try {
            System.out.println("Intermediate connecting on " + InetAddress.getLocalHost().getHostAddress() + ":" + intermediatePort);
            receiveSocket = new DatagramSocket(intermediatePort, InetAddress.getLocalHost());
            sendReceiveSocket = new DatagramSocket();
            serverAddress = InetAddress.getLocalHost();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    
    //Collects data from server and client, displays them, then sends it to
    public void run()
    {
        try {
            byte data[] = new byte[100];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            InetAddress clientAddress;
            int clientPort;
            
            //////////////////////////////////START OF CLIENT INFORMATION COLLECTION//////////////////////////////////
            // Receive packet from client
            receiveSocket.receive(dp);
            clientAddress = dp.getAddress();
            clientPort = dp.getPort();
           
            System.out.println("Intermediate running request from " + clientAddress + ":" + clientPort);
            System.out.println("Request bytes: " + dp.getData());
            System.out.println("Request string: " + makeString(dp.getData(), dp.getLength()));
            System.out.println();
            
            // Sends packet to the server
            dp = new DatagramPacket(dp.getData(), dp.getLength(), serverAddress, serverPort);
            sendReceiveSocket.send(dp);
            
            //////////////////////////////////START OF SERVER INFORMATION COLLECTION//////////////////////////////////
            // Receive response from server
            data = new byte[100];
            dp = new DatagramPacket(data, data.length);
            sendReceiveSocket.receive(dp);

            System.out.println("Intermediate received response from server");
            System.out.println("Response bytes: " + dp.getData());
            System.out.print("Response string: ");
            data = dp.getData();
            int length = dp.getLength();
            for (int i = 0; i < length; i++) {
                System.out.print(data[i]);
            }
            System.out.println();
            System.out.println();

            // Sends info from server back to the client
            dp = new DatagramPacket(dp.getData(), dp.getLength(), clientAddress, clientPort);
            sendReceiveSocket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }             
    }
    
    //Converts the data into a string
    public String makeString(byte[] data, int length)
    {
        StringBuilder str= new StringBuilder();
        String filename = "";
        String mode = "";
        int i = 1;
        
        // Finds if action is read, write or invalid
        if (data[0] == 0 && data[1] == 1) {
            str.append("Read");
        } else if (data[0] == 0 && data[1] == 2) {
            str.append("Write");
        } else {
            str.append("Invalid");
        }
        str.append(" ");
        
        // Extract the filename
        while (data[++i] != 0 && i < length) {
            filename += (char)data[i]; 
        }
        str.append(filename);
        str.append(" ");
        
        // Extract the transfer mode
        while (data[++i] != 0 && i < length) {
            mode += (char)data[i]; 
        }
        str.append(mode);
        return str.toString();
    }
}
