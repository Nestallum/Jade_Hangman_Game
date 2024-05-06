package agents;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import behaviors.provider.GameBehavior;
import behaviors.provider.GameEndBehavior;
import behaviors.provider.GameInitBehavior;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import tools.TrialResult;

/**
 * AgentProvider class responsible for providing the hidden word to be guessed by the AgentGuesser.
 *
 * This agent provides the hidden word for the AgentGuesser to guess in the Hangman game.
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class AgentProvider extends Agent {

	private static final long serialVersionUID = 1L;
	private static final String END_BEHAVIOR = "END";
	private static final String INIT_BEHAVIOR = "INIT";
	private static final String GAME_BEHAVIOR = "GAME";

	public static AID ID = new AID("AgentProvider", AID.ISLOCALNAME);

	private String secretWord;
	private int wordLength;
	private int nbTrials;
	private String guessProgress;
	private static final String FILE_PATH = "words.txt";
    private static final Random random = new Random();
    private static List<String> words;
    
    // Loading words from text file
    static {
    	words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void setup() {
		FSMBehaviour behaviour = new FSMBehaviour(this);

		// States
		behaviour.registerFirstState(new GameInitBehavior(this), INIT_BEHAVIOR);
		behaviour.registerState(new GameBehavior(this), GAME_BEHAVIOR);
		behaviour.registerLastState(new GameEndBehavior(this), END_BEHAVIOR);

		// Transitions
		behaviour.registerDefaultTransition(INIT_BEHAVIOR, GAME_BEHAVIOR);
		behaviour.registerTransition(GAME_BEHAVIOR, GAME_BEHAVIOR, 1);
		behaviour.registerTransition(GAME_BEHAVIOR, END_BEHAVIOR, 0);

		addBehaviour(behaviour);
	}

	/**
	 * Generates the secret word for the Hangman game and initializes game-related information.
	 */
	public void initGame() {
		int index = random.nextInt(words.size());
		setSecretWord(words.get(index));
		setWordLength(secretWord.length());
		setGuessProgress("_".repeat(secretWord.length()));
		System.out.println(getLocalName() + " : Secret word: " + secretWord + " (not visible by AgentGuesser)");
		int distinct_letters = (int) secretWord.chars().distinct().count();
		setNbTrials((int) (Math.ceil(distinct_letters*1.5)));
		System.out.println(getLocalName() + " : Number of Trials: " + nbTrials);
	}

	/**
	 * Compares the guessed letter or word with the secret word.
	 *
	 * @param guess The letter or word guessed by the player.
	 * @return A TrialResult object containing the result of the trial (status, guessProgress):
	 *         status : 1 if the guessed letter is present in the secret word.
	 *         status : -1 if the guessed letter is not present in the secret word;
	 *         status : 0 if all letters of the secret word have been guessed or no more attempts remaining;
	 */
	public TrialResult checkTrial(String guess) {
		// Guesser sent a word
		if(guess.length() > 1 && guess.equals(secretWord)) {
			setGuessProgress(guess);
			return new TrialResult(0, guessProgress);
		}
		
		// Else, it's a letter
		char letter = guess.charAt(0);
	    nbTrials--;
	    
	    // Check if the secret word contains the guessed letter
	    if (secretWord.contains(String.valueOf(letter))) {
	        guessProgress = updateProgression(guessProgress, letter);
	        // Check if all letters of the secret word have been guessed or no more attempts remaining
	        if (guessProgress.equals(secretWord) || nbTrials == 0)
	            return new TrialResult(0, guessProgress);
	        return new TrialResult(1, guessProgress); // Guessed letter is present in the secret word
	    }
	    
	    // No more attempts remaining
	    if(nbTrials == 0)
	    	return new TrialResult(0, guessProgress);
	    
	    // Guessed letter is not present in the secret word
	    return new TrialResult(-1, guessProgress);
	}
	
	/**
	 * Updates the progression of the guessed word based on the letter guessed.
	 *
	 * @param guessProgress The current progression of the guessed word.
	 * @param letter The letter guessed by the player.
	 * @return The updated progression of the guessed word.
	 */
	public String updateProgression(String guessProgress, char letter) {
		StringBuilder updatedProgress = new StringBuilder(guessProgress); 
		for(int i=0; i < secretWord.length(); i++) {
			// If the letter at index i in the secret word matches the guessed letter,
	        // update the corresponding position in the guessProgress with the guessed letter
			if(secretWord.charAt(i) == letter) {
				updatedProgress.setCharAt(i, letter);
			}
		}
		return updatedProgress.toString();
	}

	
	// Getters, Setters
	public int getNbTrials() {
		return nbTrials;
	}

	public void setNbTrials(int nbTrials) {
		this.nbTrials = nbTrials;
	}
	
	public String getSecretWord() {
		return secretWord;
	}
	
	public void setSecretWord(String secretWord) {
		this.secretWord = secretWord;
	}
	
	public String getGuessProgress() {
		return guessProgress;
	}

	public void setGuessProgress(String guessProgress) {
		this.guessProgress = guessProgress;
	}

	public int getWordLength() {
		return wordLength;
	}

	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}
}
