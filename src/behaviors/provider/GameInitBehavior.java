package behaviors.provider;

import agents.AgentProvider;
import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import tools.AgentLogger;

/**
 * Behavior class responsible for initializing the game parameters and communicating with the AgentGuesser.
 *
 * This behavior waits for a message from the AgentGuesser to start the game. Once the message is received,
 * it initializes the game parameters using the AgentProvider.
 * It then sends the length of the secret word to the AgentGuesser to provide hints for the guessing process.
 *
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class GameInitBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	/**
	 * We could use Behaviour.myAgent field instead of 'agent'. See {@link GameBehavior}.
	 */
	AgentProvider agent;

	public GameInitBehavior(AgentProvider a) {
		this.agent = a;
	}

	public void action() {
		// Wait for a message from AgentGuesser to start the game
		agent.doWait();
		ACLMessage message = agent.receive();
		AgentLogger.logACLMessage(message);

		// Initialize game parameters (secret word, number of trials...)
		agent.initGame();

		// Send lower and upper bounds to AgentGuesser
		ACLMessage response = new ACLMessage(ACLMessage.INFORM);
		//reponse.setContent(agent.getLowerBound() + ";" + agent.getUpperBound());
		response.setContent("Secret word length: " + Integer.valueOf(agent.getWordLength()).toString());
		response.addReceiver(AgentGuesser.ID);
		agent.send(response);

	}

}
