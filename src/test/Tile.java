package test;


import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Tile {
    private static final int LENGTH_OF_THE_ABC = 26;
    public final char letter;
    public final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag {
        private final int[] ORIGINAL_QUANTITIES = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        private final int[] quantities = Arrays.copyOf(ORIGINAL_QUANTITIES, LENGTH_OF_THE_ABC);
        private final Tile[] tiles = new Tile[LENGTH_OF_THE_ABC];
        private static Bag instance = null;
        private static int size = 98;

        private Bag() {
            int[] scores = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
            for (int i = 0; i < LENGTH_OF_THE_ABC; i++) {
                char c = (char) ('A' + i);
                tiles[i] = new Tile(c, scores[i]);
            }
        }

        public Tile getRand() {
            // returns a random tile from the bag
            int sumOfQuantities = this.size();
            if (sumOfQuantities == 0)
                return null;
            Random random = new Random();
            int randomNumber = random.nextInt(sumOfQuantities);
            int tempSum = 0;
            for (int i = 0; i < LENGTH_OF_THE_ABC; i++) {
                tempSum += quantities[i];
                if (randomNumber < tempSum) {
                    quantities[i] -= 1;
                    size--;
                    return tiles[i];
                }
            }
            return null;
        }

        public Tile getTile(char c) {
            int index = c - 'A';
            if (c < 'A' || c > 'Z' || this.quantities[index] == 0)
                return null;
            size--;
            quantities[index] -= 1;
            return tiles[index];
        }

        public int size() {
            // returns the number of tiles in the bag
            return size;
        }

        public void put(Tile t) {
            int index = t.letter - 'A';
            if (quantities[index] < ORIGINAL_QUANTITIES[index]) {
                quantities[index] += 1;
                size++;
            }
        }

        public int[] getQuantities() {
            return Arrays.copyOf(quantities, LENGTH_OF_THE_ABC);
        }

        //Singleton implementation
        public static Bag getBag() {
            if (instance == null)
                instance = new Bag();
            return instance;
        }
    }
}
