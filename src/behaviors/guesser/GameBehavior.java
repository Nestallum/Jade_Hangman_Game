package behaviors.guesser;

import agents.AgentProvider;
import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import tools.AgentLogger;

public class GameBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	/**
	 * We could use Behaviour.myAgent field instead of 'agent'. See {@link behaviors.provider.GameBehavior}.
	 */
	AgentGuesser agent;
	
	int status;

	public GameBehavior(AgentGuesser a) {
		this.agent = a;
		status = -1;
	}

	public void action() {
		String guess = agent.guess();
		ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
		message.setContent(guess);
		message.addReceiver(AgentProvider.ID);
		agent.send(message);
		
		// Wait for AgentProvider's response
		agent.doWait();

		ACLMessage reponse = agent.receive();
		AgentLogger.logACLMessage(reponse);
		if (reponse != null && reponse.getContent() != null) {
			try {
				int status = Integer.parseInt(reponse.getContent().split(";")[0]);
				agent.setStatus(status);
				char letter = guess.charAt(0);
				agent.getUsedLetters().add(letter);
				if (status == 1) {
					String progress = reponse.getContent().split(";")[1];
					agent.setGuessProgress(progress);
					agent.setLastGuessedLetter(letter);
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
	 * Returns 0 if the proposed letter was the last to find in the secret word, else 1. 
	 * This result of this method will be used in the transitions of the FSMBehaviour defined for AgentProvider.  
	 */
	public int onEnd() {
		return Math.abs(status);
	}

}
