package behviors.guesser;

import agents.AgentProvider;
import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import tools.AgentLogger;

public class GameBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	/**
	 * We could use Behaviour.myAgent field instead of 'agent'. See {@link behviors.provider.GameBehavior}.
	 */
	AgentGuesser agent;
	
	int result;

	public GameBehavior(AgentGuesser a) {
		this.agent = a;
		result = -1;
	}

	public void action() {
		char proposedLetter = agent.guess();
		ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
		message.setContent(String.valueOf(proposedLetter));
		message.addReceiver(AgentProvider.ID);
		agent.send(message);

		// Wait for AgentProvider's response
		agent.doWait();

		ACLMessage reponse = agent.receive();
		if (reponse != null && reponse.getContent() != null) {
			AgentLogger.logACLMessage(reponse);
			try {
				int result = Integer.parseInt(reponse.getContent());
				if (result == -1) {
					// faire quelque chose
				} else if (result == 1) {
					// faire quelque chose
				}
			} catch (Exception e) {
				System.out.println(agent.getAID().getLocalName() + " : Error");
				agent.doDelete();
			}
		}

	}

	/**
	 * Returns 0 if the proposed letter was the last to find in the secret word, else 1. 
	 * This result of this method will be used in the transitions of the FSMBehaviour defined for AgentProvider.  
	 */
	public int onEnd() {
		return Math.abs(result);
	}

}
