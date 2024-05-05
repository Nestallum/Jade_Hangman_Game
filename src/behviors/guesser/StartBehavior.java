package behviors.guesser;

import agents.AgentProvider;
import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class StartBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	/**
	 * We could use Behaviour.myAgent field instead of 'agent'. See {@link behviors.provider.GameBehavior}.
	 */
	AgentGuesser agent;
	
	public StartBehavior(AgentGuesser a){
		this.agent = a;
	}
	
	
	@Override
	public void action() {
		// Wait for 10 seconds before starting the game
		// This could be helpful if you need to sniff agents in the GUI for example. 
		agent.doWait(10000);
		
		// Send a message to AgentProvider to request to start the game
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.setContent("START GAME");
		message.addReceiver(AgentProvider.ID);
		agent.send(message);
	}

}
