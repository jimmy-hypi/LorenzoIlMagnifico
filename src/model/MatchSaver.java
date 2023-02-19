package model;

import exception.MatchFullException;

import java.io.*;

public class MatchSaver {


    private MatchSaver(){}

    public static void saveMatchOnExistingFile(Match match, int id) throws IOException {
        try {
            ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(("src/main/resources/files/match" + id + ".txt")));
            (outStream).writeObject(match);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void saveMatch(Match match, int id) throws IOException{
        try {
            ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(new File("src/main/resources/files/match" + id + ".txt")));
            (outStream).writeObject(match);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Match readMatch(int id) throws FileNotFoundException, IOException, ClassNotFoundException{
        ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("src/main/resources/files/match" + id + ".txt"));
        Match match = (Match) inStream.readObject();
        inStream.close();
        return match;
    }
}