package behaviors.provider;

import agents.AgentProvider;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior class responsible for ending the game and deleting the AgentProvider.
 *
 * This behavior checks if the game has ended either because the word has been found
 * or because there are no more attempts remaining. It prints appropriate messages
 * indicating the outcome of the game and deletes the AgentProvider.
 *
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class GameEndBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;

	AgentProvider agent;

	public GameEndBehavior(AgentProvider a) {
		this.agent = a;
	}

	public void action() {		
		// Agent Guesser found the word
		if(agent.getGuessProgress().equals(agent.getSecretWord()))
			System.out.println(agent.getAID().getLocalName() 
					+ ": Word '" + agent.getSecretWord() + "' found! Agent Deletion.");
		// No more trials
		else if(agent.getNbTrials() == 0)
			System.out.println(agent.getAID().getLocalName() 
				+ " : Game over! No more attempts remaining. Agent Deletion.");
		agent.doDelete();
	}
}
