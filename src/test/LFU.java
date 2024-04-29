package test;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;

class WordAndFrequency {
    String word;
    int frequency;

    public WordAndFrequency(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordAndFrequency that = (WordAndFrequency) o;
        return Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, frequency);
    }
}

class WordAndFrequencyComparator implements Comparator<WordAndFrequency> {
    @Override
    public int compare(WordAndFrequency w1, WordAndFrequency w2) {
        return w1.frequency - w2.frequency;
    }
}

public class LFU implements CacheReplacementPolicy {
    private final PriorityQueue<WordAndFrequency> priorityQueue;
    private final HashMap<String, Integer> hashMap;

    public LFU() {
        this.priorityQueue = new PriorityQueue<>(new WordAndFrequencyComparator());
        this.hashMap = new HashMap<>();
    }

    @Override
    public void add(String word) {
        if (hashMap.containsKey(word)) {
            priorityQueue.remove(new WordAndFrequency(word, 0));
            hashMap.put(word, hashMap.get(word) + 1);
            priorityQueue.add(new WordAndFrequency(word, hashMap.get(word)));
        }
        else {
            hashMap.put(word, 1);
            priorityQueue.add(new WordAndFrequency(word, 1));
        }
    }

    @Override
    public String remove() {
        String word = Objects.requireNonNull(priorityQueue.poll()).word;
        hashMap.remove(word);
        return word;
    }
}
