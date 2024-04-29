package test;


import java.io.File;
import java.util.Scanner;

public class Dictionary {
    CacheManager existingWordsLRUCacheManager;
    CacheManager notExistingWordsLFUCacheManager;
    BloomFilter bloomFilter;
    String[] fileNames;

    private void addFileWordsToBloomFilter(String fileName) {
        try (Scanner sc = new Scanner(new File(fileName))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] words = line.split(" ");
                for (String word : words)
                    this.bloomFilter.add(word);
            }
        } catch (Exception e) {
            System.out.println("you got some exception");
        }
    }

    public Dictionary(String... fileNames) {
        this.existingWordsLRUCacheManager = new CacheManager(400, new LRU());
        this.notExistingWordsLFUCacheManager = new CacheManager(100, new LFU());
        this.fileNames = fileNames;
        bloomFilter = new BloomFilter(256, "MD5", "SHA1");
        for (String fileName : fileNames) {
            addFileWordsToBloomFilter(fileName);
        }
    }

    public boolean query(String word) {
        if (this.existingWordsLRUCacheManager.query(word))
            return true;
        else if (this.notExistingWordsLFUCacheManager.query(word))
            return false;
        boolean contained = this.bloomFilter.contains(word);
        if (contained)
            existingWordsLRUCacheManager.add(word);
        else
            notExistingWordsLFUCacheManager.add(word);
        return contained;
    }

    public boolean challenge(String word) {
        try {
            boolean contained = IOSearcher.search(word, fileNames);
            if (contained)
                existingWordsLRUCacheManager.add(word);
            else
                notExistingWordsLFUCacheManager.add(word);
            return contained;
        } catch (Exception e) {
            return false;
        }
    }
}
