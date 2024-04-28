package test;


import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private static Board instance = null;
    private final Tile[][] tiles = new Tile[15][15];

    public Tile[][] getTiles() {
        return Arrays.stream(tiles).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    private boolean wordFitsBoard(Word word) {
        if (word.getRow() < 0 || word.getCol() < 0)
            return false;
        int start = word.isVertical() ? word.getRow() : word.getCol();
        return start + word.getTiles().length <= tiles.length;
    }

    private int[][] getNeighbors(Word word) {
        int[][] neighbors = new int[word.getTiles().length * 2 + 2][2];
        int row = word.getRow();
        int col = word.getCol();
        int wordLen = word.getTiles().length;
        int i = 0;
        if (word.isVertical()) {
            for (; i < wordLen; i++) {
                neighbors[i][0] = row + i;
                neighbors[i][1] = col - 1;
            }
            for (; i < wordLen * 2; i++) {
                neighbors[i][0] = row + i - wordLen;
                neighbors[i][1] = col + 1;
            }
            neighbors[i][0] = row - 1;
            neighbors[i][1] = col;
            neighbors[i + 1][0] = row + wordLen + 1;
            neighbors[i + 1][1] = col;
        } else {
            for (; i < wordLen; i++) {
                neighbors[i][0] = row - 1;
                neighbors[i][1] = col + i;
            }
            for (; i < wordLen * 2; i++) {
                neighbors[i][0] = row + 1;
                neighbors[i][1] = col + i - wordLen;
            }
            neighbors[i][0] = row;
            neighbors[i][1] = col - 1;
            neighbors[i + 1][0] = row;
            neighbors[i + 1][1] = col + wordLen + 1;
        }
        return neighbors;
    }

    public boolean boardLegal(Word word) {
        //checks if the word fits in the board
        if (!wordFitsBoard(word))
            return false;

        //checks if it is the first word, if it is it must be in the middle
        boolean flag = false;
        int start = word.isVertical() ? word.getRow() : word.getCol();
        if (tiles[7][7] == null) {
            if (word.isVertical() && word.getCol() != 7)
                return false;
            if (!word.isVertical() && word.getRow() != 7)
                return false;
            return 7 >= start && 7 <= start + word.getTiles().length;
        }
        // if it is not the first word, it must not contradict with another word
        // it also must lay on another word
        else {
            // check if the word contradicts another word
            int i;
            if (word.isVertical()) {
                for (i = 0; i < word.getTiles().length; i++)
                    if (getTiles()[i + word.getRow()][word.getCol()] != null && getTiles()[i + word.getRow()][word.getCol()] != word.getTiles()[i])
                        return false;
            } else {
                for (i = 0; i < word.getTiles().length; i++)
                    if (getTiles()[word.getRow()][i + word.getCol()] != null && getTiles()[word.getRow()][i + word.getCol()] != word.getTiles()[i])
                        return false;
            }

            // check if the word lies on another word
            int[][] neighbors = getNeighbors(word);
            for (int[] n : neighbors) {
                if (n[0] >= 0 && n[0] < getTiles().length && n[1] >= 0 && n[1] < getTiles().length && getTiles()[n[0]][n[1]] != null) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    private boolean dictionaryLegal(Word word) {
        //meanwhile it stays true
        return true;
    }

    private ArrayList<Word> getWords(Word word) {
        // all new words
        ArrayList<Word> words = new ArrayList<>();
        words.add(word);
        // go right and left (or up and down if vertical)
        if (word.isVertical() &&
                (word.getRow() != 0 && getTiles()[word.getRow() - 1][word.getCol()] != null ||
                        word.getRow() + word.getTiles().length != tiles.length && getTiles()[word.getRow() + word.getTiles().length][word.getCol()] != null)) {
            //go all the way up and down
            ArrayList<Tile> newWordsTiles = new ArrayList<>();
            int currentRow = word.getRow();
            while (currentRow > 0 && getTiles()[currentRow - 1][word.getCol()] != null)
                currentRow--;
            int newWordsRow = currentRow;
            int newWordsCol = word.getCol();
            newWordsTiles.add(getTiles()[currentRow][word.getCol()]);
            while (currentRow < 14 && (getTiles()[currentRow + 1][word.getCol()] != null || currentRow + 1 < word.getRow() + word.getTiles().length)) {
                currentRow++;
                if (getTiles()[currentRow][word.getCol()] != null)
                    newWordsTiles.add(getTiles()[currentRow][word.getCol()]);
                else
                    newWordsTiles.add(word.getTiles()[currentRow - word.getRow()]);
            }
            words.add(new Word(newWordsTiles.toArray(new Tile[0]), newWordsRow, newWordsCol, true));
        } else if (!word.isVertical() &&
                (word.getCol() != 0 && getTiles()[word.getRow()][word.getCol() - 1] != null ||
                        word.getCol() + word.getTiles().length != tiles.length && getTiles()[word.getRow()][word.getCol() + word.getTiles().length] != null)) {
            //go all the way left and right
            ArrayList<Tile> newWordsTiles = new ArrayList<>();
            int currentCol = word.getCol();
            while (currentCol > 0 && getTiles()[word.getRow()][currentCol - 1] != null)
                currentCol--;
            int newWordsRow = word.getRow();
            int newWordsCol = currentCol;
            newWordsTiles.add(getTiles()[word.getRow()][currentCol]);
            while (currentCol < 14 && (getTiles()[word.getRow()][currentCol + 1] != null || currentCol + 1 < word.getCol() + word.getTiles().length)) {
                currentCol++;
                if (getTiles()[word.getRow()][currentCol] != null)
                    newWordsTiles.add(getTiles()[word.getRow()][currentCol]);
                else
                    newWordsTiles.add(word.getTiles()[currentCol - word.getCol()]);
            }
            words.add(new Word(newWordsTiles.toArray(new Tile[0]), newWordsRow, newWordsCol, false));
        }

        //for i in word go up and down (or left to right)
        if (word.isVertical()) {
            for (int i = 0; i < word.getTiles().length; i++) {
                int indicesRow = word.getRow() + i;
                int indicesCol = word.getCol();
                if (getTiles()[indicesRow][indicesCol] == null &&
                        (indicesCol != 0 && getTiles()[indicesRow][indicesCol - 1] != null ||
                                indicesCol != tiles.length - 1 && getTiles()[indicesRow][indicesCol + 1] != null)) {
                    //go all the way left and right
                    ArrayList<Tile> newWordsTiles = new ArrayList<>();
                    int currentCol = indicesCol;
                    while (currentCol > 0 && getTiles()[indicesRow][currentCol - 1] != null)
                        currentCol--;
                    int newWordsRow = indicesRow;
                    int newWordsCol = currentCol;
                    if (getTiles()[indicesRow][currentCol] != null)
                        newWordsTiles.add(getTiles()[indicesRow][currentCol]);
                    else
                        newWordsTiles.add(word.getTiles()[i]);
                    while (currentCol < 14 && (getTiles()[indicesRow][currentCol + 1] != null || currentCol + 1 == indicesCol)) {
                        currentCol++;
                        if (getTiles()[indicesRow][currentCol] != null)
                            newWordsTiles.add(getTiles()[indicesRow][currentCol]);
                        else
                            newWordsTiles.add(word.getTiles()[i]);
                    }
                    words.add(new Word(newWordsTiles.toArray(new Tile[0]), newWordsRow, newWordsCol, false));
                }
            }
        } else {
            for (int i = 0; i < word.getTiles().length; i++) {
                int indicesRow = word.getRow();
                int indicesCol = word.getCol() + i;
                if (getTiles()[indicesRow][indicesCol] == null &&
                        (indicesRow != 0 && getTiles()[indicesRow - 1][indicesCol] != null ||
                                indicesRow != tiles.length - 1 && getTiles()[indicesRow + 1][indicesCol] != null)) {
                    //go all the way up and down
                    ArrayList<Tile> newWordsTiles = new ArrayList<>();
                    int currentRow = indicesRow;
                    while (currentRow > 0 && getTiles()[currentRow - 1][indicesCol] != null)
                        currentRow--;
                    int newWordsRow = currentRow;
                    int newWordsCol = indicesCol;
                    if (getTiles()[currentRow][indicesCol] != null)
                        newWordsTiles.add(getTiles()[currentRow][indicesCol]);
                    else
                        newWordsTiles.add(word.getTiles()[i]);
                    while (currentRow < 14 && (getTiles()[currentRow + 1][indicesCol] != null || currentRow + 1 == indicesRow)) {
                        currentRow++;
                        if (getTiles()[currentRow][indicesCol] != null)
                            newWordsTiles.add(getTiles()[currentRow][indicesCol]);
                        else
                            newWordsTiles.add(word.getTiles()[i]);
                    }
                    words.add(new Word(newWordsTiles.toArray(new Tile[0]), newWordsRow, newWordsCol, true));
                }
            }
        }
        return words;
    }

    private int[] getTileScoreDoublesAndTriples(Tile tile, int row, int col) {
        int triples = 0;
        int doubles = 0;
        int score = 0;
        if (row == 7 && col == 7) {
            if (getTiles()[7][7] == null)
                doubles++;
            score += tile.score;
        } else if ((row == 0 || row == 7 || row == 14) && (col == 0 || col == 7 || col == 14)) {
            triples++;
            score += tile.score;
        } else if ((row == col || row == 14 - col) && row != 5 && row != 6 && row != 8 && row != 9) {
            doubles++;
            score += tile.score;
        } else if (row % 4 == 1 && col % 4 == 1) {
            score += tile.score * 3;
        } else if (row == col
                || row == 14 - col
                || (row == 0 && (col == 3 || col == 11))
                || (row == 2 && (col == 6 || col == 8))
                || (row == 3 && (col == 0 || col == 7 || col == 14))
                || (row == 6 && (col == 2 || col == 12))
                || (row == 7 && (col == 3 || col == 11))
                || (row == 8 && (col == 2 || col == 12))
                || (row == 11 && (col == 0 || col == 7 || col == 14))
                || (row == 12 && (col == 6 || col == 8))
                || (row == 14 && (col == 3 || col == 11))) {
            score += tile.score * 2;
        } else {
            score += tile.score;
        }
        return new int[]{triples, doubles, score};
    }

    private int getScore(Word word) {
        int triples = 0;
        int doubles = 0;
        int score = 0;

        int row = word.getRow();
        int col = word.getCol();
        for (int i = 0; i < word.getTiles().length; i++) {
            int[] arr = getTileScoreDoublesAndTriples(word.getTiles()[i], row, col);
            triples += arr[0];
            doubles += arr[1];
            score += arr[2];
            if (word.isVertical())
                row += 1;
            else
                col += 1;
        }
        for (int i = 0; i < triples; i++)
            score *= 3;
        for (int i = 0; i < doubles; i++)
            score *= 2;
        return score;
    }

    public int tryPlaceWord(Word word) {
        // creates the real word (without nulls)
        Tile[] realTiles = new Tile[word.getTiles().length];
        for (int i = 0; i < word.getTiles().length; i++) {
            if (word.getTiles()[i] != null) {
                realTiles[i] = word.getTiles()[i];
            } else {
                if (word.isVertical()) {
                    realTiles[i] = getTiles()[word.getRow() + i][word.getCol()];
                } else {
                    realTiles[i] = getTiles()[word.getRow()][word.getCol() + i];
                }
            }
        }
        word = new Word(realTiles, word.getRow(), word.getCol(), word.isVertical());
        if (!boardLegal(word))
            return 0;
        ArrayList<Word> newWords = getWords(word);
        int scores_sum = 0;
        for (Word w : newWords) {
            if (!dictionaryLegal(w))
                return 0;
            scores_sum += getScore(w);
        }
        // בצע השמה למילה בלוח
        if (word.isVertical()) {
            for (int i = 0; i < word.getTiles().length; i++) {
                if (getTiles()[word.getRow() + i][word.getCol()] == null) {
                    this.tiles[word.getRow() + i][word.getCol()] = word.getTiles()[i];
                }
            }
        } else {
            for (int i = 0; i < word.getTiles().length; i++) {
                if (getTiles()[word.getRow()][word.getCol() + i] == null) {
                    this.tiles[word.getRow()][word.getCol() + i] = word.getTiles()[i];
                }
            }
        }
        return scores_sum;
    }

    //Singleton implementation
    public static Board getBoard() {
        if (instance == null)
            instance = new Board();
        return instance;
    }
}
