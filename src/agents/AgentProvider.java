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

public class AgentProvider extends Agent {

	private static final long serialVersionUID = 1L;
	private static final String END_BEHAVIOR = "END";
	private static final String INIT_BEHAVIOR = "INIT";
	private static final String GAME_BEHAVIOR = "GAME";

	public static AID ID = new AID("AgentProvider", AID.ISLOCALNAME);

	private String secretWord;
	private int wordLength;
	private int remainingLetters;
	private int nbTrials;
	public int upperBound;
	public int lowerBound;
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
	public void initsecretWord() {
		int index = random.nextInt(words.size());
		secretWord = words.get(index);
		remainingLetters = secretWord.length();
		setWordLength(secretWord.length());
		System.out.println(getLocalName() + " : Secret word = " + secretWord + " (not visible by AgentGuesser)");
	}

	/**
	 * Compare {@code letter} with the secret word.
	 * @param letter The letter guessed by the player.
	 * @return -1 if {@code letter} is not present in the secret word;
	 * <br> 0 if all letters of the secret word have been guessed;
	 * <br> 1 if {@code letter} is present in the secret word.
	 */
	public int checkTrial(char letter) {
		nbTrials++;
		if (secretWord.contains(String.valueOf(letter))) {
			int occurences = (int) secretWord.chars().filter(c -> c == letter).count();
			remainingLetters -= occurences;
			if (remainingLetters == 0)
				return 0;
			else
				return 1;
		}
		return -1;
	}

	public void initGame() {
		this.setNbTrials(0);
		this.setUpperBound(100);
		this.setLowerBound(0);
		this.initsecretWord();
	}

	public int getNbTrials() {
		return nbTrials;
	}

	public void setNbTrials(int nbTrials) {
		this.nbTrials = nbTrials;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	public int getWordLength() {
		return wordLength;
	}

	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}

}
