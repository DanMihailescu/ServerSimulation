import java.io.*;
import java.net.*;

public class Server {
    protected DatagramSocket receiveSocket;
    protected int port = 69;    //Server port
    
    public Server()
    {}
    
    public static void main(String[] args)
    {
        Server s = new Server();
        s.start();
    }

    //Initiates the server
    public void start()
    {
       try {
           System.out.println("Server connecting on " + InetAddress.getLocalHost().getHostAddress() + ":" + port);
           receiveSocket = new DatagramSocket(port, InetAddress.getLocalHost());
       } catch (SocketException e) {
           e.printStackTrace();
       } catch (UnknownHostException e) {
           e.printStackTrace();
       }
        
       while(true) receive();
    }
    
    //Waits to get data from intermediate
     public void receive()
    {
        try {
            byte[] data = new byte[100];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            receiveSocket.receive(dp);
            respond(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Responds to a received packet
    protected void respond(DatagramPacket receivedPacket)
    {
        try {
            //Displays the receive message from the intermediate
            String packetStr = makeString(receivedPacket.getData(), receivedPacket.getLength());
            String row = readOrWrite(receivedPacket.getData(), receivedPacket.getLength());
            System.out.println("Server received (bytes): " + receivedPacket.getData());
            System.out.println("Server received (string): " + packetStr);
            System.out.println();
            
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet;
            byte data[] = new byte[] {0, 0, 0, 0};
            
            //Checks if the message is a read, write, or invalid
            if (packetStr != "Invalid" && row == "Read") data = new byte[] {0, 3, 0, 1};
            else if (packetStr != "Invalid" && row == "Write")  data = new byte[] {0, 4, 0, 0};  
            else {
                System.out.println("Invalid request");
                System.exit(0);
            }
            
            //Prints the response that will be sent back
            System.out.print("Server response to " + receivedPacket.getAddress() + ":" + receivedPacket.getPort() + " : ");
            for (byte d : data) {
                System.out.print(d);
            }
            System.out.println();
            System.out.println();
            
            //Sends the response to intermediate
            packet = new DatagramPacket(data, data.length, receivedPacket.getAddress(), receivedPacket.getPort());
            socket.send(packet);
            
            //Closes the socket
            socket.close();
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
        
        //Finds if action is read, write or invalid
        if (data[0] == 0 && data[1] == 1) {
            str.append("Read");
        } else if (data[0] == 0 && data[1] == 2) {
            str.append("Write");
        } else {
            return "Invalid";
        }
        str.append(" ");
        
        //Extracts the filename
        while (data[++i] != 0 && i < length) {
            filename += (char)data[i]; 
        }
        if (filename == null || filename.isEmpty()) return "Invalid";
        str.append(filename);
        str.append(" ");
        
        //Extracts the mode
        while (data[++i] != 0 && i < length) {
            mode += (char)data[i]; 
        }
        if (mode == null || mode.isEmpty()) return "Invalid";
        str.append(mode);
        
        //Sends back completed string
        return str.toString();
    }
    
    //Checks if received packet is read or write
    public String readOrWrite(byte[] data, int length) {
        if (data[0] == 0 && data[1] == 1) return "Read";
        else return "Write";
    }
}
   
    

