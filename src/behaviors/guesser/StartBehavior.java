package behaviors.guesser;

import agents.AgentProvider;
import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Behavior class responsible for initiating the game by sending a request message to the AgentProvider.
 *
 * This behavior waits for a short delay before sending a request message to the AgentProvider to start the game.
 * It provides a brief pause before the game begins, which can be helpful for various purposes such as GUI interaction.
 *
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class StartBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	/**
	 * We could use Behaviour.myAgent field instead of 'agent'. See {@link behaviors.provider.GameBehavior}.
	 */
	AgentGuesser agent;
	
	public StartBehavior(AgentGuesser a){
		this.agent = a;
	}
	
	
	@Override
	public void action() {
		// Wait for 2 seconds before starting the game
		// This could be helpful if you need to sniff agents in the GUI for example. 
		agent.doWait(2000);
		
		// Send a message to AgentProvider to request to start the game
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.setContent("START GAME");
		message.addReceiver(AgentProvider.ID);
		agent.send(message);
	}

}
