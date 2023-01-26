package tictactoe.server;

import tictactoe.*;

import java.net.*;

/**
 *
 */
public class Server extends Thread {
    private int port;
    private CustomSocket client1;
    private CustomSocket client2;

    private Grid grid = null;

    /**
     *
     */
    public Server () {
        this(9876);
    }

    /**
     *
     * @param port
     */
    public Server (int port) {
        this.port = port;
        // Start the server and wait for first client
    }

    /**
     *
     */
    @Override
    public void run(){
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("En attente de joueur...");

            client1 = new CustomSocket(server.accept(), false);
            System.out.println("Joueur 1 connecté");
            client2 = new CustomSocket(server.accept(), false);
            System.out.println("Joueur 2 connecté");

            selectDimensions();
            System.out.println("Dimensions sélectionnées");

            startGame();


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public String getIpAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "0.0.0.0";
        }
    }

    /**
     * Function that send a message to ask client1 to choose the size and the dimension of the grid and process the answer.
     */
    public void selectDimensions(){
        //System.out.println("Sending client1 to select dimensions");
        NetworkMessage msg = new NetworkMessage(ProtocolAction.SelectDimensions);
        client1.send(msg);

        boolean isDimensionSelected = false;

        //Loop if the answer is not correct and wait for an other answer
        while(!isDimensionSelected){
            NetworkMessage answer;
            try{
                answer = client1.read();
            }
            //Problem with reading the answer
            catch (Exception e){
                //Set answer to null
                answer = new NetworkMessage(ProtocolAction.NONE);
                System.out.println("Error on reading");
            }
            ProtocolAction action = answer.getProtocolAction();

            //If the answer is an message with the action AnswerDimension
            if (action == ProtocolAction.AnswerDimensions) {
                String[] parameters = answer.getParameters();
                //If the answer have 2 parameters and the size is greater than 2
                if(parameters.length == 2 && Integer.parseInt(parameters[0]) > 2){
                    //If the dimension is 2D
                    if(Integer.parseInt(parameters[1]) == 2){
                        grid = new Grid2D(Integer.parseInt(parameters[0]));
                        isDimensionSelected = true;
                    }
                    //If the dimension is 3D
                    else if(Integer.parseInt(parameters[1]) == 3){
                        //TODO use Grid3D when ready
                        isDimensionSelected = true;
                    }
                }
            }
            //If the client1 didn't answer correctly the server send an error message
            if(!isDimensionSelected) {
                error(client1, "Erreur lors de la saisi des dimensions de la grille.");
            }
        }
    }

    public void startGame(){
        NetworkMessage msg = new NetworkMessage(ProtocolAction.StartGame);
        client1.send(msg);
        client2.send(msg);
    }

    public void endGame(){

    }

    public void play(){

    }

    public void validate(){

    }

    public void error(CustomSocket client, String message){
        String[] param = {message};
        NetworkMessage msg = new NetworkMessage(ProtocolAction.Error, param);
        client.send(msg);
    }

    public void networkError(){

    }

    public void resumeGame(){

    }

}
