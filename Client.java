import java.io.*;
import java.net.*;

public class Client {
    protected DatagramSocket socket;
    
    public Client()
    {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Client c = new Client();
        c.test(c);
    }
    
    //Runs 5 write, 5 read, and 1 invalid test
    public void test(Client c){
        c.write("Test_1.txt");
        c.read("Test_2.txt");
        c.write("Test_3.txt");
        c.read("Test_4.txt");
        c.write("Test_5.txt");
        c.read("Test_6.txt");
        c.write("Test_7.txt");
        c.read("Test_8.txt");
        c.write("Test_9.txt");
        c.read("Test_10.txt");
        c.invalidRequest("Test_11.txt");
    }
    
    //Sends a read request
    public void read(String s)
    {
        byte[] msg = makePacket(1, s, "ASCII");
        String str = makeString(1, s, "ASCII");
        con(msg, str);
    }
    
    //Sends a write request
    public void write(String s)
    {
        byte[] msg = makePacket(2, s, "ASCII");
        String str = makeString(2, s, "ASCII");
        con(msg, str);
    }
    
    //Sends an invalid request
    public void invalidRequest(String s)
    {
        byte[] msg = makePacket(3, s, "ASCII");
        con(msg, s);
    }
    
    //Prints, sends message, and waits for response
    public void con(byte[] msg, String str) {
        try {
            // Log to terminal
            System.out.println("Client sending (bytes): " + msg);
            System.out.println("Client sending (string): " + str);
            System.out.println();
            
            DatagramPacket dp = new DatagramPacket (msg, msg.length, InetAddress.getLocalHost(), 23);
            socket.send(dp);
            receive();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    //Waits to receive a message then prints it
    public void receive()
    {
        byte[] data = new byte[100];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        try {
            socket.receive(receivePacket);
            data = receivePacket.getData();
            int dataLength = receivePacket.getLength();
            
            System.out.print("Client received response: ");
            for ( int i = 0; i < dataLength; i++ ) {
                System.out.print(data[i]);
            }
            System.out.println();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
    }
    
    //Creates a byte array containing the data
    public byte[] makePacket(int i, String s, String m)
    {
        try {
            // Creates the byte array with the starting 0
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            b.write(0);
            
            // Sets what type of action is called (1 for read; 2 for write; and 3 for invalid)
            if (i == 1) b.write(1);      // Read  
            else if (i == 2) b.write(2); // Write
            else b.write(3);             // Invalid
            
            //Sets the name bytes followed by a 0
            if (s != null) {
                b.write(s.getBytes());
            }
            b.write(0);
            
            //Sets the mode bytes finalized with a 0
            b.write(m.toString().getBytes());
            b.write(0);
            
            return b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        return null;
    }
    
    //Creates a string containing the data
    public String makeString(int i, String s, String m)
    {
        try {
            StringBuilder str= new StringBuilder();
            str.append(0);
            
            //Sets what type of action is called (1 for read; 2 for write; and 3 for invalid)
            if (i == 1) str.append(1); // read request flag byte
            else if (i == 2) str.append(2); // write request flag byte
            else str.append(3); // undefined request
            
            //Adds the name followed by a 0
            if (null != s) {
                str.append(s);
            }
            str.append(0);
            
            //Set the mode finalized with a 0
            if (null != m) {
                str.append(m);
            }
            str.append(0);
            
            return str.toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }
}