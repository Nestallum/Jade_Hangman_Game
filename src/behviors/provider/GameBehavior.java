package behviors.provider;

import agents.AgentProvider;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import tools.AgentLogger;

public class GameBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	/**
	 * -1 if proposed number is less than secret number.
	 *  1 if proposed number is bigger than secret number.
	 *  0 is proposed number is equal to the secret number.
	 */
	int result;

	public GameBehavior(AgentProvider a) {
		myAgent = a; // or setAgent(a);
		result = -1;
	}

	public void action() {
		// Wait for a trial
		myAgent.doWait(); // or getAgent().doWait();
		ACLMessage message = myAgent.receive();
		AgentLogger.logACLMessage(message);
		char letter = ' '; 
		try {
			letter = message.getContent().charAt(0);
		} catch (Exception ex) {
			System.out.println("Error : invalid message");
			myAgent.doDelete();
		}

		// Response
		// checkTrial() method is defined in AgentProvider class, not in Agent. 
		// So myAgent must be casted to AgentProvider class in order to call this method.
		result = ((AgentProvider)myAgent).checkTrial(letter);  
 		ACLMessage reponse = new ACLMessage(ACLMessage.INFORM);
		reponse.setContent(Integer.toString(result));
		reponse.addReceiver(new AID("AgentGuesser", AID.ISLOCALNAME));
		myAgent.send(reponse);

	}

	/**
	 * Returns 0 if the proposed letter was the last to find in the secret word, else 1. 
	 * This result of this method will be used in the transitions of the FSMBehaviour defined for AgentProvider.  
	 */
	public int onEnd() {
		return Math.abs(result);
	}
}
