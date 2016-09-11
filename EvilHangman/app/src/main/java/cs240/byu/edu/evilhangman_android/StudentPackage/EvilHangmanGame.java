package cs240.byu.edu.evilhangman_android.StudentPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EvilHangmanGame implements StudentEvilHangmanGameController {
    private int guesses;
    private Set<String> words;
    private TreeSet<Character> usedLetters;
    private String currentWord;

    public GAME_STATUS getGameStatus() {
        if (currentWord.indexOf('-') == -1) return GAME_STATUS.PLAYER_WON;
        else if (guesses == 0) return GAME_STATUS.PLAYER_LOST;
        return GAME_STATUS.NORMAL;
    }

    public int getNumberOfGuessesLeft() {
        return guesses;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public Set<Character> getUsedLetters() {
        return usedLetters;
    }

    public void setNumberOfGuesses(int guesses) { this.guesses = guesses; }

    public void startGame(InputStreamReader inputReader, int wordLength) {
        words = new HashSet<>();
        usedLetters = new TreeSet<>();

        char[] a = new char[wordLength];
        Arrays.fill(a, '-');
        currentWord = String.copyValueOf(a);

        try {
            BufferedReader reader = new BufferedReader(inputReader);
            while (reader.ready()) {
                String word = reader.readLine();
                if (word.length() == wordLength) words.add(word);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if (usedLetters.contains(guess)) throw new GuessAlreadyMadeException();
        usedLetters.add(guess);

        TreeMap<String,Set<String>> partitionedWords = new TreeMap<>();
        Iterator<String> it = words.iterator();
        while (it.hasNext()) {
            String word = it.next();
            StringBuilder pattern = new StringBuilder();
            for (int i = 0; i < word.length(); ++i) {
                if (word.charAt(i) == guess) pattern.append(guess);
                else pattern.append('-');
            }
            if (!partitionedWords.containsKey(pattern.toString())) {
                partitionedWords.put(pattern.toString(), new HashSet<String>());
            }
            partitionedWords.get(pattern.toString()).add(word);
        }

        int largestWordSize = 0;
        ArrayList<String> largestPattern = new ArrayList<>();
        for (Map.Entry<String,Set<String>> entry: partitionedWords.entrySet()) {
            String pattern = entry.getKey();
            if (partitionedWords.get(pattern).size() > largestWordSize) {
                largestWordSize = partitionedWords.get(pattern).size();
                largestPattern.clear();
                largestPattern.add(pattern);
            } else if (partitionedWords.get(pattern).size() == largestWordSize) {
                largestPattern.add(pattern);
            }
        }
        mergePattern(largestPattern.get(0));

        int difference = count(largestPattern.get(0), guess);
        if (difference == 0) {
            System.out.printf("Sorry, there are no %c's.\n",guess);
            --guesses;
        } else if (difference == 1) System.out.printf("Yes, there is 1 %c.\n",guess);
        else System.out.printf("Yes, there are %d %c's.\n",difference,guess);

        words = partitionedWords.get(largestPattern.get(0));
        return partitionedWords.get(largestPattern.get(0));
    }

    public void mergePattern(String pattern) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pattern.length(); ++i) {
            if (currentWord.charAt(i) != '-') sb.append(currentWord.charAt(i));
            else if (pattern.charAt(i) != '-') sb.append(pattern.charAt(i));
            else sb.append('-');
        }
        currentWord = sb.toString();
    }

    public int count(String pattern, char guess) {
        int count = 0;
        for (int i = 0; i < pattern.length(); ++i) {
            if (pattern.charAt(i) == guess) ++count;
        }
        return count;
    }
}
