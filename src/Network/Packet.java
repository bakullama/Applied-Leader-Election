package Network;


public record Packet(int fromID, int toID, int currentRound, String message) {
    public static Packet toPacket(String line) {
        String[] data = line.split("[#]");
        int fromID, toID, currentRound;
        String message;

        fromID = Integer.parseInt(data[0].replace("::=", ""));
        toID = Integer.parseInt(data[1]);
        currentRound = Integer.parseInt(data[2]);
        message = data[3].substring(0, data[3].length()-3);



        return new Packet(fromID, toID, currentRound, message);

    }

    @Override
    public String toString() {
        return "::=" + fromID +
                "#" + toID +
                "#" + currentRound +
                "#" + message +
                "=::";
    }
}