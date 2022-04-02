import Processor.Processor;
import State.State;

public class Main {
    private static final int numberOfProcessors = 10;
    private static final int port = 281203;

    public static void main(String[] args) {
        Processor [] processors = new Processor[numberOfProcessors];

        for (int i = 0; i < numberOfProcessors; i++) {
            processors[i] = new Processor(i, i == numberOfProcessors -1 ? 0 : i + 1, State.UNKNOWN, port);
        }
    }
}
