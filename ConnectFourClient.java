package hw10;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import static jdk.internal.net.http.common.Utils.close;

/**
 * This class is the client which connects with the server and communicates with server and user.
 *  * @author Vijay Gangayadi Venkatesh
 *  * @author Sagar Suhas Karambelkar
 */
public class ConnectFourClient implements ConnectFourProtocol {
    public static void createConnection(String host, int port) throws IOException, ConnectFourException {
        Socket socket = new Socket(host,port);
        OutputStream os = socket.getOutputStream();
        PrintStream ps = new PrintStream(os);
        InputStream is = socket.getInputStream();
        Scanner serverMessage = new Scanner(is);
        Scanner stdInScanner = new Scanner(System.in);
        String isConnected = serverMessage(serverMessage);
        if (isConnected.equals(CONNECT)){
            System.out.println("Connected!");
        }
        playGame(isConnected,stdInScanner,ps,serverMessage);
    }

    /**
     * The function gets the connected server message and returns the message.
     * @param serverMessage The scanner for reading the messages from server.
     * @return The message returned from server as String.
     */
    public static String serverMessage(Scanner serverMessage)
    {
        String message = serverMessage.nextLine();
        return message;
    }

    /**
     * The game logic at client end is implemented.
     * @param connected connected message from the server.
     * @param stdInScanner  standard input scanner to read values from the console.
     * @param ps    input stream to get the message from server to client.
     * @param serverMessage scanner to read message from server.
     * @throws ConnectFourException
     */
    public static void playGame(String connected, Scanner stdInScanner, PrintStream ps, Scanner serverMessage) throws ConnectFourException {
        ConnectFour boardCopy = new ConnectFour();
        while (connected.equals(CONNECT)) {

            String makeMoveString = serverMessage.nextLine();
            String[] makeMoveStringSplit = makeMoveString.split(" ");
            if(makeMoveStringSplit[0].equals(MAKE_MOVE)) {
                System.out.println("Your turn! Enter a column: ");
                boolean flag = true;
                int columnNumber = 0;
                while(flag) {
                    columnNumber = stdInScanner.nextInt();
                    if (columnNumber >= 0 && columnNumber < 7)
                    {
                        flag = false;
                    }
                    else{
                        System.out.println("Invalid Column! Please give numbers between 0-6 ");
                        System.out.println("Your turn! Enter a column: ");
                        continue;
                    }
                    ConnectFour.Move[][] board = boardCopy.getBoard();
                    if(board[columnNumber][0] != ConnectFour.Move.NONE){
                        System.out.println("Column is full! Please select another column. ");
                        System.out.println("Your turn! Enter a column: ");
                        flag = true;
                        continue;

                    }
                    else{flag =false;}
                }
                String moveString = MOVE + " " + columnNumber;
                ps.println(moveString);
                String moveMade = serverMessage.nextLine();
                String[] moveMadeSplit = moveMade.split(" ");
                int whichMove = Integer.parseInt(moveMadeSplit[1]);
                boardCopy.makeMove(whichMove);
                System.out.println("A move has been made in column " + whichMove);
                System.out.println(boardCopy);
                connected = serverMessage.nextLine();
            }
            else if (makeMoveStringSplit[0].equals(MOVE_MADE))
            {
                int whichMove = Integer.parseInt(makeMoveStringSplit[1]);
                boardCopy.makeMove(whichMove);
                System.out.println("A move has been made in column " + whichMove);
                System.out.println(boardCopy);
                connected = serverMessage.nextLine();
            }
            if (connected.equals(GAME_WON))
            {
                System.out.println("You won!");

            }
            if (connected.equals(GAME_LOST))
            {
                System.out.println("You lost!");

            }
            if (connected.equals(ERROR))
            {
                close();

            }

            if (connected.equals(GAME_TIED))
            {
                System.out.println("Match tied!");

            }
        }

    }

    /**
     * Gets the host address and port and runs the game logic in client end.
     * @param args  The host name and port number is given in console.
     * @throws IOException
     * @throws ConnectFourException
     */
    public static void main(String[] args) throws IOException, ConnectFourException {

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        createConnection(hostName,portNumber);
    }
}
