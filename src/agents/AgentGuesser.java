package agents;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import behaviors.guesser.GameBehavior;
import behaviors.guesser.GameEndBehavior;
import behaviors.guesser.GameInitBehavior;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

/**
 * AgentGuesser class responsible for guessing the hidden word provided by the AgentProvider.
 *
 * This agent attempts to guess the hidden word provided by the AgentProvider in the Hangman game.
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class AgentGuesser extends Agent {

	private static final long serialVersionUID = 1L;
	private static final String INIT_BEHAVIOR = "INIT";
	private static final String GAME_BEHAVIOR = "GAME";
	private static final String END_BEHAVIOR = "END";

	public static AID ID = new AID("AgentGuesser", AID.ISLOCALNAME);

	private String guessProgress;
	private int status;
	private int wordLength;
	private List<String> dictionary; // List of words
	private List<Character> usedLetters;
	private char wrongLetter;
	ArrayList<Character> alphabetList = new ArrayList<>(Arrays.asList(
            'e', 't', 'a', 'o', 'i', 'n', 's', 'h', 'r', 'd', 'l', 'c', 'u', 'm',
            'w', 'f', 'g', 'y', 'p', 'b', 'v', 'k', 'j', 'x', 'q', 'z'
        )); // Characters ranked from most to least frequent in the English language
	private static final String FILE_PATH = "20k_words.txt";
    
	public void setup() {
		FSMBehaviour behaviour = new FSMBehaviour(this);

		// States
		behaviour.registerFirstState(new GameInitBehavior(this), INIT_BEHAVIOR);
		behaviour.registerState(new GameBehavior(this), GAME_BEHAVIOR);
		behaviour.registerLastState(new GameEndBehavior(this), END_BEHAVIOR);

		// Transitions
		// Default transition from INIT_BEHAVIOR to GAME_BEHAVIOR
		behaviour.registerDefaultTransition(INIT_BEHAVIOR, GAME_BEHAVIOR);
		// If onEnd() method of behavior corresponding to state GAME_BEHAVIOR returns 1, 
		// then we should loop back to the same state.
		behaviour.registerTransition(GAME_BEHAVIOR, GAME_BEHAVIOR, 1);
		// If onEnd() method of behavior corresponding to state GAME_BEHAVIOR returns 0, 
		// then we need to move to behavior corresponding to END_BEHAVIOR state.
		behaviour.registerTransition(GAME_BEHAVIOR, END_BEHAVIOR, 0);

		addBehaviour(behaviour);
	}
	
	/**
	 * Initializes game-related information.
	 */
	public void initGuesserVariables(int wordLength) {
		setWordLength(wordLength);
		setGuessProgress("_".repeat(this.getWordLength()));
		preprocessWords();
	}
	
	/**
     * Reads words from 20k_words.txt and pre-processes them, filtering out words of the same length as the secret word.
     */
	public void preprocessWords() {
		dictionary = new ArrayList<>();
		usedLetters = new ArrayList<>();
		
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	if(line.trim().length() == wordLength)
                	dictionary.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
     * Filters words based the guessProgress structure.
     *
     * @return The filtered list of words.
     */
	public ArrayList<String> filterDictionary() {
        ArrayList<String> filteredWords = new ArrayList<>();

        // Adds a word only if its structure matches that of guessProgress.
        for (String word : dictionary) {
    		if(!word.contains(String.valueOf(wrongLetter))) {
	        	boolean isMatch = true;
	            for (int i = 0; i < guessProgress.length(); i++) {
	                char guessedLetter = guessProgress.charAt(i);
	                char wordLetter = word.charAt(i);
	                if (guessedLetter != '_' && guessedLetter != wordLetter) {
	                    isMatch = false;
	                    break;
	                }
	            }
	            if (isMatch)
	            	filteredWords.add(word);
        	}
        }
        return filteredWords;
    }
	
	/**
     * Finds the most frequent non-used letter among the words.
     *
     * @return The most frequent letter.
     */
	public char findMostFrequentLetter(){
        Map<Character, Integer> letterCount = new HashMap<>();
        
        // Iterate over each word in the list of words and count the occurrences of each letter,
        // updating the count for each letter if it hasn't been used before.
        for (String word : dictionary) {
            for (char letter : word.toCharArray()) {
            	if(!usedLetters.contains(letter))
            		letterCount.put(letter, letterCount.getOrDefault(letter, 0) + 1);
            }
        }

        // Find the letter with the highest count
        char mostFrequentLetter = ' ';
        int maxCount = 0;
        for (Map.Entry<Character, Integer> entry : letterCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequentLetter = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        return mostFrequentLetter;
    }

	/**
	 * Generates and returns either the only possible word according to the filtering
	 * or the letter with the highest probability of appearing in the word for the hangman game.
	 * 
	 * @return The generated word or letter as a String.
	 */
	public String guessLetter() {
		
		// If no letters have been guessed yet, we cannot filter words,
	    // so we simply choose the next most frequent letter in the English language.
		if(guessProgress.equals("_".repeat(wordLength))) {
			System.out.println("Compatible word(s): all the words in the list.");
			return String.valueOf(alphabetList.remove(0));
		}
		
		// filter the words based on the last guessed letter.
		dictionary = filterDictionary(); // filter based on the new letter find or the new wrong letter.
		System.out.println("Compatible word(s): " + dictionary);
				
		// If a letter has been guessed and the game is in progress
		// and the word list is sufficiently filtered, return the only possible word.
		if(status == 1 && dictionary.size() == 1)
			if(dictionary.size() == 1)
				return dictionary.get(0);
		
		// Return the most frequent letter among the ones we have never picked.
		return String.valueOf(findMostFrequentLetter());
	}
	
	// Getters, Setters
	public int getWordLength() {
		return wordLength;
	}

	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}

	public String getGuessProgress() {
		return guessProgress;
	}

	public void setGuessProgress(String guessProgress) {
		this.guessProgress = guessProgress;
	}
	
	public List<Character> getUsedLetters() {
		return usedLetters;
	}

	public void setUsedLetters(List<Character> usedLetters) {
		this.usedLetters = usedLetters;
	}
	
	public char getWrongLetter() {
		return wrongLetter;
	}

	public void setWrongLetter(char wrongLetter) {
		this.wrongLetter = wrongLetter;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
