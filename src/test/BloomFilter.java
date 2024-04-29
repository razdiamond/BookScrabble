package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    BitSet bitSet;
    String[] hashFuncs;

    private int[] runHashFunctions(String word) {
        int[] results = new int[hashFuncs.length];
        for (int i = 0; i < hashFuncs.length; i++) {
            try {
                MessageDigest md = MessageDigest.getInstance(hashFuncs[i]);
                byte[] bts = md.digest(word.getBytes());
                BigInteger bi = new BigInteger(1, bts);
                int val = bi.intValue();
                results[i] = Math.abs(val) % bitSet.size();
            }
            catch (NoSuchAlgorithmException e) {
                System.err.println(e.getMessage());
            }
        }
        return results;
    }

    public BloomFilter(int nBits, String... hashFuncs) {
        bitSet = new BitSet(nBits);
        this.hashFuncs = hashFuncs;
    }

    public void add(String word) {
        int[] results = runHashFunctions(word);
        for (int result : results)
            bitSet.set(result);
    }

    public boolean contains(String word) {
        int[] results = runHashFunctions(word);
        for (int result : results)
            if (!bitSet.get(result))
                return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i))
                sb.append("1");
            else
                sb.append("0");
        }
        return sb.toString();
    }
}
