import Network.Network;


public class Main {
    private static final int numberOfProcessors = 10;
    private static final int port = 281203;

    public static void main(String[] args) {

        Network network = new Network(10);


        network.electLeader();


    }

    




}
