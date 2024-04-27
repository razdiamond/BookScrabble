package test;


import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private static Board instance = null;
    private Tile[][] tiles = new Tile[15][15];

    public Tile[][] getTiles() {
        Tile[][] copy = new Tile[tiles.length][];
        for (int i = 0; i < tiles.length; i++)
            copy[i] = Arrays.copyOf(tiles[i], tiles[i].length);
        return copy;
    }

    public boolean boardLegal(Word word) {
        //checks if the word fits in the board
        if (word.getRow() < 0 || word.getRow() > tiles.length - 1 || word.getCol() < 0 || word.getCol() > tiles.length - 1)
            return false;
        int start = word.isVertical() ? word.getRow() : word.getCol();
        if (start + word.getTiles().length > tiles.length - 1)
            return false;
        //checks if it is the first word, if it is it must be in the middle
        boolean flag = false;
        if (tiles[7][7] == null) {
            if (word.isVertical() && word.getCol() != 7)
                return false;
            if (!word.isVertical() && word.getRow() != 7)
                return false;
            if (7 < start || 7 > start + word.getTiles().length)
                return false;
            flag = true;
        }
        // if it is not the first word, it must lay on another word
        //it also should not contradict another word
        else {
            // check contradictions
            if (word.isVertical()) {
                int col = word.getCol();
                for (int i = word.getRow(); i < word.getRow() + word.getTiles().length; i++) {
                    if (tiles[i][col] != null && tiles[i][col] != word.getTiles()[i - word.getRow()]) {
                        return false;
                    }
                }
            }
            else {
                int row = word.getRow();
                for (int j = word.getCol(); j < word.getCol() + word.getTiles().length; j++) {
                    if (tiles[row][j] != null && tiles[row][j] != word.getTiles()[j - word.getCol()]) {
                        return false;
                    }
                }
            }
            // check if the word lies on another word
            int[][] neighbors = new int[word.getTiles().length * 2 + 2][2];
            if (word.isVertical()) {
                int i = 0;
                for (; i < word.getTiles().length; i++) {
                    neighbors[i][0] = word.getRow() + i;
                    neighbors[i][1] = word.getCol() - 1;
                }
                for (; i < word.getTiles().length * 2; i++) {
                    neighbors[i][0] = word.getRow() + i - word.getTiles().length;
                    neighbors[i][1] = word.getCol() + 1;
                }
                neighbors[i][0] = word.getRow() - 1;
                neighbors[i][1] = word.getCol();
                neighbors[i + 1][0] = word.getRow() + word.getTiles().length + 1;
                neighbors[i + 1][1] = word.getCol();
            }
            else {
                int i = 0;
                for (; i < word.getTiles().length; i++) {
                    neighbors[i][0] = word.getRow() - 1;
                    neighbors[i][1] = word.getCol() + i;
                }
                for (; i < word.getTiles().length * 2; i++) {
                    neighbors[i][0] = word.getRow() + 1;
                    neighbors[i][1] = word.getCol() + i - word.getTiles().length;
                }
                neighbors[i][0] = word.getRow();
                neighbors[i][1] = word.getCol() - 1;
                neighbors[i + 1][0] = word.getRow();
                neighbors[i + 1][1] = word.getCol() + word.getTiles().length + 1;
            }
            for (int[] n : neighbors) {
                if (n[0] < 0 || n[0] >= getTiles().length || n[1] < 0 || n[1] >= getTiles().length || getTiles()[n[0]][n[1]] == null)
                    continue;
                else {
                    flag = true;
                    break;
                }
            }
            // check if the word contradicts another word
            if (word.isVertical()) {
                for (int i = 0; i < word.getTiles().length; i++) {
                    if (getTiles()[i + word.getRow()][word.getCol()] != null && getTiles()[i + word.getRow()][word.getCol()] != word.getTiles()[i]) {
                        return false;
                    }
                }
            }
            else {
                for (int i = 0; i < word.getTiles().length; i++) {
                    if (getTiles()[word.getRow()][i + word.getCol()] != null && getTiles()[word.getRow()][i + word.getCol()] != word.getTiles()[i]) {
                        return false;
                    }
                }
            }
        }
        return flag;
    }

    private boolean dictionaryLegal(Word word) {
        //בינתיים true
        return true;
    }

    private ArrayList<Word> getWords(Word word) {
        // לעשות מערך דינאמי ולשים בו את ווררד תחילה
        ArrayList<Word> words = new ArrayList<Word>();
        words.add(word);
        // go right and left (or up and down if vertical)
        if (word.isVertical() &&
                (word.getRow() != 0 && getTiles()[word.getRow() - 1][word.getCol()] != null ||
                        word.getRow() + word.getTiles().length != tiles.length - 1 && getTiles()[word.getRow() + word.getTiles().length][word.getCol()] != null)) {
            //go all the way up and down
            ArrayList<Tile> newWordsTiles = new ArrayList<>();
            int currentRow = word.getRow();
            while (currentRow > 0 && getTiles()[currentRow - 1][word.getCol()] != null)
                currentRow--;
            int newWordsRow = currentRow;
            int newWordsCol = word.getCol();
            newWordsTiles.add(getTiles()[currentRow][word.getCol()]);
            while (currentRow < 14 && (getTiles()[currentRow + 1][word.getCol()] != null || currentRow + 1 == word.getRow())) {
                currentRow++;
                newWordsTiles.add(getTiles()[currentRow][word.getCol()]);
            }
            words.add(new Word(newWordsTiles.toArray(new Tile[newWordsTiles.size()]), newWordsRow, newWordsCol, true));
        }
        else if (!word.isVertical() &&
                (word.getCol() != 0 && getTiles()[word.getRow()][word.getCol() - 1] != null ||
                        word.getCol() + word.getTiles().length != tiles.length - 1 && getTiles()[word.getRow()][word.getCol() + word.getTiles().length] != null)) {
            //go all the way left and right
            ArrayList<Tile> newWordsTiles = new ArrayList<>();
            int currentCol = word.getCol();
            while (currentCol > 0 && getTiles()[word.getRow()][currentCol - 1] != null)
                currentCol--;
            int newWordsRow = word.getRow();
            int newWordsCol = currentCol;
            newWordsTiles.add(getTiles()[word.getRow()][currentCol]);
            while (currentCol < 14 && (getTiles()[word.getRow()][currentCol + 1] != null || currentCol + 1 == word.getCol())) {
                currentCol++;
                newWordsTiles.add(getTiles()[word.getRow()][currentCol]);
            }
            words.add(new Word(newWordsTiles.toArray(new Tile[newWordsTiles.size()]), newWordsRow, newWordsCol, false));
        }

        //for i in word go up and down (or left to right)
        if (word.isVertical()) {
            for (int i = 0; i < word.getTiles().length; i++) {
                int indexsRow = word.getRow() + i;
                int indexsCol = word.getCol();
                if (getTiles()[indexsRow][indexsCol] == null &&
                        (indexsCol != 0 && getTiles()[indexsRow][indexsCol - 1] != null ||
                            indexsCol != tiles.length - 1 && getTiles()[indexsRow][indexsCol + 1] != null)) {
                    //go all the way left and right
                    ArrayList<Tile> newWordsTiles = new ArrayList<>();
                    int currentCol = indexsCol;
                    while (currentCol > 0 && getTiles()[indexsRow][currentCol - 1] != null)
                        currentCol--;
                    int newWordsRow = indexsRow;
                    int newWordsCol = currentCol;
                    if (getTiles()[indexsRow][currentCol] != null)
                        newWordsTiles.add(getTiles()[indexsRow][currentCol]);
                    else
                        newWordsTiles.add(word.getTiles()[i]);
                    while (currentCol < 14 && (getTiles()[indexsRow][currentCol + 1] != null || currentCol + 1 == indexsCol)) {
                        currentCol++;
                        if (getTiles()[indexsRow][currentCol] != null)
                            newWordsTiles.add(getTiles()[indexsRow][currentCol]);
                        else
                            newWordsTiles.add(word.getTiles()[i]);
                    }
                    words.add(new Word(newWordsTiles.toArray(new Tile[newWordsTiles.size()]), newWordsRow, newWordsCol, false));
                }
            }
        }
        else {
            for (int i = 0; i < word.getTiles().length; i++) {
                int indexsRow = word.getRow();
                int indexsCol = word.getCol() + i;
                if (getTiles()[indexsRow][indexsCol] == null &&
                        (indexsRow != 0 && getTiles()[indexsRow - 1][indexsCol] != null ||
                                indexsRow != tiles.length - 1 && getTiles()[indexsRow + 1][indexsCol] != null)) {
                    //go all the way up and down
                    ArrayList<Tile> newWordsTiles = new ArrayList<>();
                    int currentRow = indexsRow;
                    while (currentRow > 0 && getTiles()[currentRow - 1][indexsCol] != null)
                        currentRow--;
                    int newWordsRow = currentRow;
                    int newWordsCol = indexsCol;
                    if (getTiles()[currentRow][indexsCol] != null)
                        newWordsTiles.add(getTiles()[currentRow][indexsCol]);
                    else
                        newWordsTiles.add(word.getTiles()[i]);
                    while (currentRow < 14 && (getTiles()[currentRow + 1][indexsCol] != null || currentRow + 1 == indexsRow)) {
                        currentRow++;
                        if (getTiles()[currentRow][indexsCol] != null)
                            newWordsTiles.add(getTiles()[currentRow][indexsCol]);
                        else
                            newWordsTiles.add(word.getTiles()[i]);
                    }
                    words.add(new Word(newWordsTiles.toArray(new Tile[newWordsTiles.size()]), newWordsRow, newWordsCol, true));
                }
            }
        }
        return words;
    }

    private int getScore(Word word) {
        int tripples = 0;
        int doubles = 0;
        int score = 0;

        if (word.isVertical()) {
            for (int i = 0; i < word.getTiles().length; i++) {
                int row = word.getRow() + i;
                int col = word.getCol();
                if (row == 7 && col == 7) {
                    if (getTiles()[7][7] == null)
                        doubles++;
                    score += word.getTiles()[i].score;
                }
                else if ((row == 0 || row == 7 || row == 14) && (col == 0 || col == 7 || col == 14)) {
                    tripples++;
                    score += word.getTiles()[i].score;
                }
                else if ((row == col || row == 14 - col) && row != 5 && row != 6 && row != 8 && row != 9) {
                    doubles++;
                    score += word.getTiles()[i].score;
                }
                else if (row % 4 == 1 && col % 4 == 1) {
                    score += word.getTiles()[i].score * 3;
                }
                else if (row == col
                        || (row == 0 && (col == 3 || col == 11))
                        || (row == 2 && (col == 6 || col == 8))
                        || (row == 3 && (col == 0 || col == 7 || col == 14))
                        || (row == 6 && (col == 2 || col == 12))
                        || (row == 7 && (col == 3 || col == 11))
                        || (row == 8 && (col == 2 || col == 12))
                        || (row == 11 && (col == 0 || col == 7 || col == 14))
                        || (row == 12 && (col == 6 || col == 8))
                        || (row == 14 && (col == 3 || col == 11))) {
                    score += word.getTiles()[i].score * 2;
                }
                else {
                    score += word.getTiles()[i].score;
                }
            }
        }
        else {
            for (int i = 0; i < word.getTiles().length; i++) {
                int row = word.getRow();
                int col = word.getCol() + i;
                if (row == 7 && col == 7) {
                    if (getTiles()[7][7] == null)
                        doubles++;
                    score += word.getTiles()[i].score;
                }
                else if ((row == 0 || row == 7 || row == 14) && (col == 0 || col == 7 || col == 14)) {
                    tripples++;
                    score += word.getTiles()[i].score;
                }
                else if ((row == col || row == 14 - col) && row != 5 && row != 6 && row != 8 && row != 9) {
                    doubles++;
                    score += word.getTiles()[i].score;
                }
                else if (row % 4 == 1 && col % 4 == 1) {
                    score += word.getTiles()[i].score * 3;
                }
                else if (row == col
                        || (row == 0 && (col == 3 || col == 11))
                        || (row == 2 && (col == 6 || col == 8))
                        || (row == 3 && (col == 0 || col == 7 || col == 14))
                        || (row == 6 && (col == 2 || col == 12))
                        || (row == 7 && (col == 3 || col == 11))
                        || (row == 8 && (col == 2 || col == 12))
                        || (row == 11 && (col == 0 || col == 7 || col == 14))
                        || (row == 12 && (col == 6 || col == 8))
                        || (row == 14 && (col == 3 || col == 11))) {
                    score += word.getTiles()[i].score * 2;
                }
                else {
                    score += word.getTiles()[i].score;
                }
            }
        }
        for (int i = 0; i < tripples; i++)
            score *= 3;
        for (int i = 0; i < doubles; i++)
            score *= 2;
        return score;
    }

    public int tryPlaceWord(Word word) {
        Tile[] realTiles = new Tile[word.getTiles().length];
        for (int i = 0; i < word.getTiles().length; i++) {
            if (word.getTiles()[i] != null) {
                realTiles[i] = word.getTiles()[i];
            }
            else {
                if (word.isVertical()) {
                    realTiles[i] = getTiles()[word.getRow() + i][word.getCol()];
                }
                else {
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
        }
        else {
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
