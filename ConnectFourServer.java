package hw10;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;



/**
 * This is the server class.
 * @author Vijay Gangayadi Venkatesh
 * @author Sagar Suhas Karambelkar
 */
public class ConnectFourServer {
    private ConnectFour boardCopy;

    /**
     * The client connection and client handler is used to communicate with the clients.
     * @param args  The port number is given in command line.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket server = new ServerSocket(portNumber);
        int playerNumber = 0;
        while(true) {
            Socket client1 = server.accept(); // Player 1
            System.out.println("Player 1 connected!");
            Socket client2 = server.accept(); // Player 2
            System.out.println("Player 2 connected!");
            ConnectFourClientHandler ch = new ConnectFourClientHandler(client1,client2);
            playerNumber++;
            ch.start();
        }
    }
}