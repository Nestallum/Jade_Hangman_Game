package behaviors.guesser;

import agents.AgentProvider;
import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import tools.AgentLogger;

/**
 * Behavior class responsible for managing the guessing process and communication with the AgentProvider.
 *
 * This behavior performs the guessing action by sending a proposed letter to the AgentProvider,
 * waiting for its response, and processing the response to update the game status and guess progress.
 *
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class GameBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	AgentGuesser agent;
	
	int status;

	public GameBehavior(AgentGuesser a) {
		this.agent = a;
		status = -1;
	}

	public void action() {
		// Send the guess to the AgentProvider 
		String guess = agent.guess();
		ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
		message.setContent(guess);
		message.addReceiver(AgentProvider.ID);
		agent.send(message);
		
		// Wait for AgentProvider's response
		agent.doWait();

		// Handle response
		ACLMessage response = agent.receive();
		AgentLogger.logACLMessage(response);
		if (response != null && response.getContent() != null) {
			try {
				int status = Integer.parseInt(response.getContent().split(";")[0]);
				agent.setStatus(status);
				
				char letter = guess.charAt(0);
				agent.getUsedLetters().add(letter);
				
				// If the guessed letter was in the word, update the guess progress and the last guessed letter
				if (status == 1) {
					String progress = response.getContent().split(";")[1];
					agent.setGuessProgress(progress);
				}
			} catch (Exception e) {
				System.out.println(agent.getAID().getLocalName() + " : Error");
				agent.doDelete();
			}
		}
		else
			System.out.println("No message received or null content.");
	}

	/**
	 * Returns 0 if all letters have been found or the word itself, otherwise 1.
	 * The result of this method will be used in the transitions of the FSMBehaviour.  
	 */
	public int onEnd() {
		return Math.abs(status);
	}

}
