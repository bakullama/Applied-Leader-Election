package Processor;

import State.State;

public class Processor {
    private final int myID;
    private final int neighbourID;
    private final int port;
    private int currentRound;
    private int inID;
    private int sendID;
    private State state;
    private int leaderID;

    /**
     * Constructor for a processor. Sets initial values
     *
     * @param myID the ID for this Processor
     * @param neighbourID the ID for the next processor in the network
     * @param initialState the state for the Processor to start at
     * @param port the port the processor listens for connections on
     */
    public Processor(int myID, int neighbourID, State initialState, int port) {
        this.myID = myID;
        this.neighbourID = neighbourID;
        this.state = initialState;
        this.port = port;

        currentRound = 0;
    }
}
