package agents;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import behviors.provider.GameBehavior;
import behviors.provider.GameEndBehavior;
import behviors.provider.GameInitBehavior;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import tools.TrialResult;

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
    private static List<String> words;
    private static final Random random = new Random();
    
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
	 * Generates the secret word.
	 * @return The secret word chosen randomly from the list of words.
	 */
	public void initSecretWord() {
		int index = random.nextInt(words.size());
		setSecretWord(words.get(index));
		setWordLength(secretWord.length());
		setGuessProgress("_".repeat(secretWord.length()));
		System.out.println(getLocalName() + " : Secret word: " + secretWord + " (not visible by AgentGuesser)");
		int distinct_letters = (int) secretWord.chars().distinct().count();
		setNbTrials(distinct_letters*2);
		System.out.println(getLocalName() + " : Nb Trials: " + nbTrials);
	}

	/**
	 * Compare {@code letter} with the secret word.
	 * @param letter The letter guessed by the player.
	 * @return -1 if {@code letter} is not present in the secret word;
	 * <br> 0 if all letters of the secret word have been guessed or no more attempts remaining;
	 * <br> 1 if {@code letter} is present in the secret word.
	 */
	public TrialResult checkTrial(char letter) {
	    nbTrials--;
	    if (secretWord.contains(String.valueOf(letter))) {
	        guessProgress = updateProgression(guessProgress, letter);
	        if (guessProgress.equals(secretWord) || nbTrials == 0)
	            return new TrialResult(0, guessProgress);
	        return new TrialResult(1, guessProgress);
	    }
	    if (nbTrials == 0)
	        return new TrialResult(0, guessProgress);
	    return new TrialResult(-1, guessProgress);
	}
	
	public String updateProgression(String guessProgress, char letter) {
		StringBuilder updatedProgress = new StringBuilder(guessProgress); 
		for(int i=0; i < secretWord.length(); i++) {
			if(secretWord.charAt(i) == letter) {
				updatedProgress.setCharAt(i, letter);
			}
		}
		return updatedProgress.toString();
	}

	public void initGame() {
		this.initSecretWord();
	}

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
