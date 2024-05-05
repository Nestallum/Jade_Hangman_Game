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
	
	int status;

	public GameBehavior(AgentGuesser a) {
		this.agent = a;
		status = -1;
	}

	public void action() {
		char proposedLetter = agent.guess();
		ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
		message.setContent(Character.toString(proposedLetter));
		message.addReceiver(AgentProvider.ID);
		agent.send(message);

		
		// Wait for AgentProvider's response
		agent.doWait();

		ACLMessage reponse = agent.receive();
		AgentLogger.logACLMessage(reponse);
		if (reponse != null && reponse.getContent() != null) {
			try {
				int status = Integer.parseInt(reponse.getContent().split(";")[0]);
				String progress = reponse.getContent().split(";")[1];
				if (status == -1) {
					// continuer
				} else if (status == 1) {
					// nouvelle lettre trouvée, mettre à jour le dictionnaire
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
