package test;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;

class WordAndRecently {
    String word;
    int recently;

    public WordAndRecently(String word, int recently) {
        this.word = word;
        this.recently = recently;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordAndRecently that = (WordAndRecently) o;
        return Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, recently);
    }
}

class WordAndRecentlyComparator implements Comparator<WordAndRecently> {
    @Override
    public int compare(WordAndRecently w1, WordAndRecently w2) {
        return w1.recently - w2.recently;
    }
}

public class LRU implements CacheReplacementPolicy {
    private final PriorityQueue<WordAndRecently> priorityQueue;
    private final HashMap<String, Integer> hashMap;
    int time;

    public LRU() {
        this.priorityQueue = new PriorityQueue<>(new WordAndRecentlyComparator());
        this.hashMap = new HashMap<>();
        this.time = 0;
    }

    @Override
    public void add(String word) {
        if (hashMap.containsKey(word)) {
            priorityQueue.remove(new WordAndRecently(word, 0));
            hashMap.put(word, time);
            priorityQueue.add(new WordAndRecently(word, hashMap.get(word)));
        }
        else {
            hashMap.put(word, time);
            priorityQueue.add(new WordAndRecently(word, time));
        }
        this.time++;
    }

    @Override
    public String remove() {
        String word = Objects.requireNonNull(priorityQueue.poll()).word;
        hashMap.remove(word);
        return word;
    }
}
