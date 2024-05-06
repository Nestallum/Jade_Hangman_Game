package agents;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import behviors.guesser.GameBehavior;
import behviors.guesser.GameEndBehavior;
import behviors.guesser.GameInitBehavior;
import behviors.guesser.StartBehavior;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

public class AgentGuesser extends Agent {

	private static final long serialVersionUID = 1L;
	private static final String END_BEHAVIOR = "END";
	private static final String GAME_BEHAVIOR = "GAME";
	private static final String INIT_BEHAVIOR = "INIT";
	private static final String START_BEHAVIOR = "START";

	public static AID ID = new AID("AgentGuesser", AID.ISLOCALNAME);

	private String guessProgress;
	private char lastGuessedLetter;
	private int status;
	private int wordLength;
	private List<String> words; // List of words
	private List<Character> usedLetters;

	ArrayList<Character> alphabetList = new ArrayList<>(Arrays.asList(
            'e', 't', 'a', 'o', 'i', 'n', 's', 'h', 'r', 'd', 'l', 'c', 'u', 'm',
            'w', 'f', 'g', 'y', 'p', 'b', 'v', 'k', 'j', 'x', 'q', 'z'
        )); // Characters ranked from most to least frequent in the English language
	private static final String FILE_PATH = "words.txt";
    
	public void setup() {
		FSMBehaviour behaviour = new FSMBehaviour(this);

		// States
		behaviour.registerFirstState(new StartBehavior(this), START_BEHAVIOR);
		behaviour.registerState(new GameInitBehavior(this), INIT_BEHAVIOR);
		behaviour.registerState(new GameBehavior(this), GAME_BEHAVIOR);
		behaviour.registerLastState(new GameEndBehavior(this), END_BEHAVIOR);

		// Transitions
		// Default transition from START_BEHAVIOR to INIT_BEHAVIOR
		behaviour.registerDefaultTransition(START_BEHAVIOR, INIT_BEHAVIOR);
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
	
	public void preprocessWords() {
		words = new ArrayList<>();
		usedLetters = new ArrayList<>();
		
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	if(line.trim().length() == wordLength)
                	words.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public ArrayList<String> filterWords() {
        ArrayList<String> filteredWords = new ArrayList<>();

        for (String word : words) {
            if (word.contains(String.valueOf(lastGuessedLetter)))
            	filteredWords.add(word);
        }
        return filteredWords;
    }
	
	public char findMostFrequentLetter(){
        Map<Character, Integer> letterCount = new HashMap<>();

        // Parcourir chaque mot dans la liste de mots
        for (String word : words) {
            // Parcourir chaque lettre dans le mot
            for (char letter : word.toCharArray()) {
                // Mettre à jour le compteur pour cette lettre
            	if(!usedLetters.contains(letter))
            		letterCount.put(letter, letterCount.getOrDefault(letter, 0) + 1);
            }
        }

        // Trouver la lettre avec le compteur le plus élevé
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
	 * Generates a letter from the alphabet list {@link #alphabetList}.
	 * This method picks and removes the generated letter from the alphabet list.
	 *
	 * @return The generated letter.
	 */
	public char guess() {
		if(guessProgress.equals("_".repeat(wordLength)))
			return alphabetList.remove(0);
		
		if(status == 1) {
			words = filterWords();
		}
		
		return findMostFrequentLetter();		
	}
	
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

	public char getLastGuessedLetter() {
		return lastGuessedLetter;
	}

	public void setLastGuessedLetter(char lastGuessedLetter) {
		this.lastGuessedLetter = lastGuessedLetter;
	}
	
	public List<Character> getUsedLetters() {
		return usedLetters;
	}

	public void setUsedLetters(List<Character> usedLetters) {
		this.usedLetters = usedLetters;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
