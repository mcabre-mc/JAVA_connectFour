package hw10;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import static jdk.internal.net.http.common.Utils.close;

/**
 * The class helps the server class to communicate and handel the client classes.
 *  * @author Vijay Gangayadi Venkatesh
 *  * @author Sagar Suhas Karambelkar
 */
public class ConnectFourClientHandler extends Thread implements ConnectFourProtocol {
    private Socket player1,player2;
    private Scanner scanner1,scanner2;
    private PrintStream printer1,printer2;
    private int playerNumber;
    private ConnectFour gameBoard;
    private boolean gameContinue ;

    /**
     * The constructor sets the player sockets and creates the streams to communicate to the client.
     * @param player1 Socket of the first player.
     * @param player2 Socket of the second player.
     */
    public ConnectFourClientHandler(Socket player1,Socket player2)
    {
        try {
            this.player1 = player1;
            this.player2 = player2;
            scanner1 = new Scanner(this.player1.getInputStream());
            scanner2 = new Scanner(this.player2.getInputStream());
            printer1 = new PrintStream(this.player1.getOutputStream());
            printer2 = new PrintStream(this.player2.getOutputStream());
            gameBoard = new ConnectFour();
            gameContinue = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Simulates one turn using a particular scanner and printer
     * @param scanner
     * @param printer
     */
    public void oneTurn(Scanner scanner, PrintStream printer, PrintStream otherPrinter) throws ConnectFourException {
        try {
            printer.println(CONNECT);
            otherPrinter.println(CONNECT);
            printer.println(MAKE_MOVE);
            String whichMove = scanner.nextLine();
            System.out.println(whichMove);
            int moveCol = Integer.parseInt(whichMove.split(" ")[1]);
            try {
                gameBoard.makeMove(moveCol);
            } catch (ConnectFourException ce) {

                System.out.println(ce);
            }
            System.out.println(gameBoard);
            String moveMadeString = MOVE_MADE + " " + moveCol;
            printer.println(moveMadeString);
            otherPrinter.println(moveMadeString);
            if (gameBoard.hasWonGame()) {
                printer.println(GAME_WON);
                otherPrinter.println(GAME_LOST);
                player1.close();
                player2.close();
                scanner1.close();
                scanner2.close();
                printer.close();
                otherPrinter.close();
                gameContinue = false;
            }
            if (gameBoard.hasTiedGame()) {
                printer.println(GAME_TIED);
                otherPrinter.println(GAME_TIED);
                close();
                player1.close();
                player2.close();
                scanner1.close();
                scanner2.close();
                printer.close();
                otherPrinter.close();
                gameContinue = false;

            }
        }
        catch(IOException ie)
        {
            printer.println(ERROR);
            otherPrinter.println(ERROR);
        }


    }

    @Override
    /**
     * The run method executes the oneTurn method for each player.
     */
    public void run() {
        while(gameContinue)
        {
            try {
                oneTurn(scanner1, printer1, printer2);
                if(!gameContinue)
                {
                    break;
                }
                oneTurn(scanner2, printer2, printer1);//Player one moves
            } catch (ConnectFourException e) {
                e.printStackTrace();
            }
        }

    }
}
