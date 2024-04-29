package test;


import java.util.HashSet;

public class CacheManager {
	int size;
    CacheReplacementPolicy crp;
    HashSet<String> words = new HashSet<>();

    public CacheManager(int size, CacheReplacementPolicy crp) {
        this.size = size;
        this.crp = crp;
    }

    public boolean query(String word) {
        return words.contains(word);
    }

    void add(String word) {
        if (words.size() < this.size) {
            words.add(word);
            crp.add(word);
        }
        else {
            words.remove(crp.remove());
            words.add(word);
        }
    }
}
