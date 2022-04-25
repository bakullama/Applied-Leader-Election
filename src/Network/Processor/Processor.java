package Network.Processor;

import Network.Packet;
import State.processorState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Processor extends Thread {
    private final int myID;
    private final int neighbourID;
    private final int port;
    private final int nextPort;
    private int currentRound;
    private int inID;
    private int sendID;
    private processorState state;
    private int leaderID;

    /**
     * Constructor for a processor. Sets initial values
     *
     * @param myID the ID for this Processor
     * @param neighbourID the ID for the next processor in the network
     * @param initialState the state for the Processor to start at
     */
    public Processor(int myID, int neighbourID, processorState initialState) {
        this.myID = myID;
        this.neighbourID = neighbourID;
        this.state = initialState;
        this.port = myID + 2000;
        nextPort = neighbourID + 2000;

        currentRound = 1;
        leaderID = -1;
    }

    @Override
    public void run() {
        BufferedReader inputStream;
        String input = "";
        PrintWriter outputStream;

        Socket nextProcessorSocket;
        Socket client;
        ServerSocket serverSocket;
        try {
            // Create a new server socket
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            while (leaderID == -1) {
                // Listen for a connection
                if (currentRound != 1) {
                    client = serverSocket.accept();
                    inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    input = inputStream.readLine();
                }


                String message = calculateMessage(input);
                Packet packet = new Packet(myID, neighbourID, currentRound, message);
                nextProcessorSocket = new Socket("localhost", nextPort);

                outputStream = new PrintWriter(nextProcessorSocket.getOutputStream(), true);
                outputStream.println(packet);
                outputStream.close();
                nextProcessorSocket.close();
                currentRound += 1;
            }
            System.out.println("Processor: " + myID + " finished.");
        } catch (IOException err) {
            System.out.println("Error: " + err.getMessage());
        }
    }

    /**
     * Calculates a message to reply based on the input
     *
     * @param input the input message
     * @return the message to send
     */
    @Contract(pure = true)
    private @NotNull String calculateMessage(String input) {
        if (currentRound != 1) {
            Packet inputPacket = Packet.toPacket(input);
            inID = Integer.parseInt(inputPacket.message());
            calculateSendID();
            return Integer.toString(sendID);
        } else {
            return Integer.toString(-1);
        }
    }


    /**
     * An implementation of a modified LCR algorithm which handles termination rounds.
     * Updates internal state and calculated the message to send on
     *
     */
    private void calculateSendID() { // implements LCR with termination round and async start
        if (currentRound == 1) {
            sendID = myID;
        } else {
            if (inID == sendID) { // if the processor receives the same ID twice, then the ID must be the leader
                leaderID = inID;
                sendID = ~sendID;
            } if (inID > myID) {
                sendID = inID;
            } if (inID == myID) {
                state = processorState.LEADER;
                sendID = myID;
                leaderID = myID;
            }
        }
        inID = ~inID; // clear input when ended
    }

    /**
     * Returns true if the processor is the leader
     *
     * @return whether the processor has been elected leader or not
     */
    public boolean isLeader() {
        return (state == processorState.LEADER);
    }

    /**
     * Returns my id
     *
     * @return the current processor's id
     */
    public int getMyID(){
        return myID;
    }


    @Override
    public String toString() {
        return "proc{" +
                "myID=" + myID +
                ", neighbourID=" + neighbourID +
                ", leaderID=" + leaderID +
                ", state=" + state +
                '}';
    }
}
