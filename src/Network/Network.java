package Network;

import Network.Processor.Processor;
import State.processorState;

public class Network {
    private Processor[] network;
    private long leaderID = -1;

    public Network(int size) {

        network = new Processor[size];

        for (int i = 0; i < size; i++) {
            network[i] = new Processor(i, i == size -1 ? 0 : i + 1, processorState.UNKNOWN);
        }
    }

    /**
     * Carries out leader election and stores the leader's id in a private property leaderID
     */
    public void electLeader() {
        for (Processor processor: network) {
            processor.start();

        }


        for (Processor processor: network) {
            try {
                Thread.sleep(100);
                processor.join();
                if (processor.isLeader()) {
                    leaderID = processor.getMyID();
                }
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
        }
        System.out.println("leader: " + leaderID);
    }
}
