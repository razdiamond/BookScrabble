package test;


import java.io.*;
import java.util.Arrays;
import java.util.List;

public class BookScrabbleHandler implements ClientHandler {
    private DictionaryManager dictionaryManager;

    public BookScrabbleHandler() {
        dictionaryManager = new DictionaryManager();
    }

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(inFromclient));
        PrintWriter outToServer = new PrintWriter(outToClient);
        try {
            String line = serverInput.readLine();
            if (line != null) {
                List<String> parts = Arrays.asList(line.split(","));
                if (parts.size() > 1) {
                    String action = parts.get(0);
                    List<String> bookNames = parts.subList(1, parts.size() - 1);
                    String word = parts.get(parts.size() - 1);
                    String[] bookNamesArray = bookNames.toArray(new String[0]);
                    String[] args = Arrays.copyOf(bookNamesArray, bookNamesArray.length + 1);
                    args[args.length - 1] = word;
                    if (action.equals("Q")) {
                        if (dictionaryManager.query(args)) {
                            outToServer.println("true\n");
                            outToServer.flush();
                        }
                        else {
                            outToServer.println("false\n");
                            outToServer.flush();
                        }
                    } else if (action.equals("C")) {
                        if (dictionaryManager.challenge(args)) {
                            outToServer.println("true\n");
                            outToServer.flush();
                        }
                        else {
                            outToServer.println("false\n");
                            outToServer.flush();
                        }
                    }
                }
            }
            // reader = serverInput
            // writer = outToServer
        } catch (IOException ignored) {

        } finally {
            try {
                serverInput.close();
                outToServer.close();
            } catch (IOException ignored) {}
        }
    }

    @Override
    public void close() {

    }

}
