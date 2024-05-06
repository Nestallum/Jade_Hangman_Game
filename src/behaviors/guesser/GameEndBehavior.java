package behaviors.guesser;

import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior class responsible for ending the guessing agent after the game is over.
 *
 * This behavior simply deletes the AgentGuesser after the game is over.
 *
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class GameEndBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	AgentGuesser agent;

	public GameEndBehavior(AgentGuesser a) {
		this.agent = a;
	}

	public void action() {
		agent.doDelete();
	}

}
