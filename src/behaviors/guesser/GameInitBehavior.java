package behaviors.guesser;

import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import tools.AgentLogger;

/**
 * Behavior class responsible for initializing the game parameters based on the information received from the AgentProvider.
 *
 * This behavior waits for a message from the AgentProvider containing information about the word to be guessed.
 * It extracts the word length from the message content, initializes the game parameters
 * and preprocesses the word list to prepare for the guessing process.
 *
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class GameInitBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	AgentGuesser agent;

	public GameInitBehavior(AgentGuesser a) {
		this.agent = a;
	}

	public void action() {
		// Wait for a message from AgentProvider containing informations on the word.
		agent.doWait();
		
		ACLMessage message = agent.receive();
		AgentLogger.logACLMessage(message);

		if (message != null && message.getContent() != null) {
			int wordLength = Integer.parseInt(message.getContent().split(": ")[1]);
			try {
				// Initializes game parameters for agent Guesser and preprocesses the word list
				agent.setWordLength(wordLength);
				agent.setGuessProgress("_".repeat(agent.getWordLength()));
				agent.preprocessWords();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				agent.doDelete();
			}
		}

	}

}
