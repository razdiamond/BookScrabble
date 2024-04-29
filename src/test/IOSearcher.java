package test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class IOSearcher {
    private static boolean bookContains(String fileName, String word) throws Exception {
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(word))
                    return true;
            }
        }
        return false;
    }

    public static boolean search (String word, String... fileNames) throws Exception {
        for (String fileName : fileNames)
            if (bookContains(fileName, word))
                return true;
        return false;
    }
}
