package test;


import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager instance = null;
    Map<String, Dictionary> map;

    public DictionaryManager() {
        map = new HashMap<>();
    }

    public boolean query(String... args) {
        String word = args[args.length - 1];
        String[] booksList = new String[args.length - 1];
        System.arraycopy(args, 0, booksList, 0, args.length - 1);
        boolean flag = false;
        for (String book : booksList) {
            if (!map.containsKey(book))
                map.put(book, new Dictionary(book));
            if (map.get(book).query(word))
                flag = true;
        }
        return flag;
    }

    public boolean challenge(String... args) {
        String word = args[args.length - 1];
        String[] booksList = new String[args.length - 1];
        System.arraycopy(args, 0, booksList, 0, args.length - 1);
        boolean flag = false;
        for (String book : booksList) {
            if (!map.containsKey(book))
                map.put(book, new Dictionary(book));
            if (map.get(book).challenge(word))
                flag = true;
        }
        return flag;
    }

    public int getSize() {
        // if getSize should return the number of words in all the books
        /*int size = 0;
        for (Map.Entry<String, Dictionary> entry : map.entrySet()) {
            size += entry.getKey().length();
        }
        return size;*/
        // else if getSize should return the number of books in the dictionary
        return map.size();
    }

    //Singleton implementation
    public static DictionaryManager get() {
        if (instance == null)
            instance = new DictionaryManager();
        return instance;
    }
}
