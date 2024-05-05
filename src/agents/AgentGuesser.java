package agents;

import java.util.ArrayList;
import java.util.Arrays;

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

	private int nbTrials;
	public int upperBound;
	public int lowerBound;
	public int wordLength;
	ArrayList<Character> alphabetList = new ArrayList<>(Arrays.asList(
            'e', 't', 'a', 'o', 'i', 'n', 's', 'h', 'r', 'd', 'l', 'c', 'u', 'm',
            'w', 'f', 'g', 'y', 'p', 'b', 'v', 'k', 'j', 'x', 'q', 'z'
        )); // Characters ranked from most to least frequent in the English language

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

	/**
	 * Generates a letter from the alphabet list {@link #alphabetList}.
	 * This method picks and removes the generated letter from the alphabet list.
	 *
	 * @return The generated letter.
	 */
	public char guess() {
		nbTrials++;
		return alphabetList.remove(0);
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
