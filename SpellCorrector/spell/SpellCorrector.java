package spell;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
	private Trie dictionary;
	
	public SpellCorrector() {
		dictionary = new Trie();
	}
	
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner scanner = new Scanner(new File(dictionaryFileName));
		while(scanner.hasNext()) {
			dictionary.add(scanner.next());
		}
		scanner.close();
	}

	/*MEMORIZE*/
	@Override
	public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
		if (inputWord == "" || inputWord == " ") throw new NoSimilarWordFoundException();
		if (dictionary.count(inputWord) > 0) return inputWord;
		
		TreeSet<String> distance1 = new TreeSet<String>();
		TreeSet<String> existing = new TreeSet<String>();
		addDelete(inputWord, distance1);
		addTransposition(inputWord, distance1);
		addAlteration(inputWord, distance1);
		addInsertion(inputWord, distance1);
		existing = getKnownWords(distance1);
		if (existing.size() > 0) return tieBreaker(existing);
		
		TreeSet<String> distance2 = new TreeSet<String>();
		Iterator<String> it = distance1.iterator();
		while(it.hasNext()) {
			String input = it.next();
			addDelete(input, distance2);
			addTransposition(input, distance2);
			addAlteration(input, distance2);
			addInsertion(input, distance2);
		}
		existing = getKnownWords(distance2);
		if (existing.size() > 0) return tieBreaker(existing);
		
		throw new NoSimilarWordFoundException();
	}
	
	
	// For debugging
	public void printSet(TreeSet<String> set) {
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public String tieBreaker(TreeSet<String> set) {
		Iterator<String> it = set.iterator();
		String winner = it.next();
		while (it.hasNext()) {
			String current_word = it.next();
			if (dictionary.count(current_word) > dictionary.count(winner)) {
				winner = current_word;
			}
		}
		return winner;
	}
	
	public TreeSet<String> getKnownWords(TreeSet<String> set) {
		TreeSet<String> known = new TreeSet<String>();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String current_word = it.next();
			if (dictionary.count(current_word) > 0) known.add(current_word);
		}
		return known;
	}
	
	public String delete(String s, int index) {
		return s.substring(0,index) + s.substring(index+1);
	}
	
	public void addDelete(String word, TreeSet<String> wordSet) {
		for (int i = 0; i < word.length(); i++) {
			wordSet.add(delete(word,i));
		}
	}
	
	/*MEMORIZE*/
	public String transpose(String s, int index) {
		if (s.length() < index+2) return null;
		StringBuilder sb = new StringBuilder();
		sb.append(s.substring(0,index));
		sb.append(s.substring(index+1,index+2));
		sb.append(s.substring(index,index+1));
		sb.append(s.substring(index+2));
		return sb.toString();
	}
	/*MEMORIZE*/
	public void addTransposition(String word, TreeSet<String> wordSet) {
		for (int i = 0; i < word.length()-1; ++i) {
			if (transpose(word,i) != null) wordSet.add(transpose(word,i));
		}
	}
	
	/*MEMORIZE*/
	public void addAlteration(String word, TreeSet<String> wordSet) {
		for (int i = 0; i < word.length(); ++i) {
			for (int j = 0; j < 26; ++j) {
				wordSet.add(word.substring(0,i)+Character.toString((char) ('a'+j))+word.substring(i+1));
			}
		}
	}
	
	/*MEMORIZE*/
	public String insert(String s, Character c, int index) {
		StringBuilder sb = new StringBuilder();
		sb.append(s.substring(0,index));
		sb.append(c.toString());
		sb.append(s.substring(index));
		return sb.toString();
	}
	/*MEMORIZE*/
	public void addInsertion(String word, TreeSet<String> wordSet) {
		for (int i = 0; i < word.length()+1; ++i) {
			for (int j = 0; j < 26; ++j) {
				wordSet.add(insert(word, (char) ('a'+j), i));
			}
		}
	}

}
