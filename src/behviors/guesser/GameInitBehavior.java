package behviors.guesser;

import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import tools.AgentLogger;

public class GameInitBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	/**
	 * We could use Behaviour.myAgent field instead of 'agent'. See {@link behviors.provider.GameBehavior}.
	 */
	AgentGuesser agent;

	public GameInitBehavior(AgentGuesser a) {
		this.agent = a;
	}

	public void action() {
		// Wait for a message from AgentA containing lower and upper bounds
		agent.doWait();
		ACLMessage message = agent.receive();
		AgentLogger.logACLMessage(message);

		agent.setNbTrials(0);

		if (message != null && message.getContent() != null) {
			String wordLength = message.getContent();
//			String[] tab = message.getContent().split(";");
			try {
//				agent.setLowerBound(Integer.parseInt(tab[0]));
//				agent.setUpperBound(Integer.parseInt(tab[1]));
				agent.setWordLength(Integer.parseInt(wordLength));
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				agent.doDelete();
			}
		}

	}

}
