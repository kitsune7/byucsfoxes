package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame {
	private Set<String> words;
	private int guesses;
	private TreeSet<Character> guessedLetters;
	private String curPattern;

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Not enough arguments provided.");
			System.out.println("Usage: java EvilHangmanGame dictionaryPath wordlength guesses");
		} else if (Integer.parseInt(args[1]) < 2) {
			System.out.println("Your word needs to be at least 2 characters long!");
		} else if (Integer.parseInt(args[2]) < 1) {
			System.out.println("You need at least 1 guess!");
		} else {
			EvilHangmanGame game = new EvilHangmanGame();
			game.setGuesses(Integer.parseInt(args[2]));
			game.startGame(new File(args[0]), Integer.parseInt(args[1]));
		}
	}
	
	public void setGuesses(int guesses) { this.guesses = guesses; }
	
	public void startGame(File dictionary, int wordLength) {
		words = new HashSet<String>();
		guessedLetters = new TreeSet<Character>();
		
		char[] a = new char[wordLength];
		Arrays.fill(a, '-');
		curPattern = String.copyValueOf(a);
		
		try {
			Scanner scanner = new Scanner(dictionary);
			while (scanner.hasNext()) {
				String word = scanner.next();
				if (word.length() == wordLength) words.add(word);
			}
			scanner.close();
			
			boolean redo;
			String full_guess;
			Scanner input = new Scanner(System.in);
			while (guesses > 0) {
				do {
					displayInfo();
					redo = false;
					
					System.out.print("Enter a guess: ");
					full_guess = input.nextLine();
					
					if (full_guess.length() == 0) {
						System.out.println("Woah there! Type a character before you push enter!");
						redo = true;
					} else if (full_guess.length() > 1) {
						System.out.println("You can only enter one character at a time!");
						redo = true;
					} else if (!Character.isLetter(full_guess.charAt(0))) {
						System.out.println("Invalid character! Only letters are allowed.");
						redo = true;
					} else {
						try {
							words = makeGuess(full_guess.charAt(0));
							if (curPattern.indexOf('-') == -1) {
								System.out.println("You win!");
								System.out.printf("The word was %s.\n", curPattern);
								return;
							}
						} catch (GuessAlreadyMadeException e) {
							System.out.println("You already guessed that letter!");
							redo = true;
						}
					}
					
					System.out.println();
				} while (redo);
			}
			input.close();
			
			System.out.println("You lose!");
			Iterator<String> it = words.iterator();
			System.out.printf("The word was %s.\n", it.next());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		if (guessedLetters.contains(guess)) throw new GuessAlreadyMadeException();
		guessedLetters.add(guess);
		
		TreeMap<String,Set<String>> partitionedWords = new TreeMap<String,Set<String>>();
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
		ArrayList<String> largestPattern = new ArrayList<String>();
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
		
		return partitionedWords.get(largestPattern.get(0));
	}
	
	public void displayInfo() {
		if (guesses > 1) System.out.printf("You have %d guesses left.\n",guesses);
		else if (guesses == 1) System.out.println("You have 1 guess left.");
		
		System.out.print("Guessed letters:");
		Iterator<Character> it = guessedLetters.iterator();
		while (it.hasNext()) {
			System.out.print(" ");
			System.out.print(it.next());
		}
		
		System.out.printf("\nWord: %s\n",curPattern);
	}
	
	public void mergePattern(String pattern) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pattern.length(); ++i) {
			if (curPattern.charAt(i) != '-') sb.append(curPattern.charAt(i));
			else if (pattern.charAt(i) != '-') sb.append(pattern.charAt(i));
			else sb.append('-');
		}
		curPattern = sb.toString();
	}
	
	public int count(String pattern, char guess) {
		int count = 0;
		for (int i = 0; i < pattern.length(); ++i) {
			if (pattern.charAt(i) == guess) ++count;
		}
		return count;
	}

}
