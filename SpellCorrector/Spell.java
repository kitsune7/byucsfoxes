import java.io.IOException;

import spell.ISpellCorrector.NoSimilarWordFoundException;
import spell.SpellCorrector;


public class Spell {

	public static void main(String[] args) throws IOException, NoSimilarWordFoundException {
		if (args.length < 2) {
			System.out.println("Not enough arguments provided.");
			System.out.println("USAGE: java Spell dictionaryFile word");
		} else {
			SpellCorrector checker = new SpellCorrector();
			checker.useDictionary(args[0]);
			System.out.println(checker.suggestSimilarWord(args[1]));
		}
	}

}
