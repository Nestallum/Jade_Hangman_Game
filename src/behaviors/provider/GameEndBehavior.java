package behaviors.provider;

import agents.AgentProvider;
import jade.core.behaviours.OneShotBehaviour;

public class GameEndBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	/**
	 * We could use Behaviour.myAgent field instead of 'agent'. See {@link GameBehavior}.
	 */
	AgentProvider agent;

	public GameEndBehavior(AgentProvider a) {
		this.agent = a;
	}

	public void action() {
		if(agent.getGuessProgress().equals(agent.getSecretWord()))
			System.out.println(agent.getAID().getLocalName() 
					+ ": Word '" + agent.getSecretWord() + "' found!");
		else if(agent.getNbTrials() == 0)
			System.out.println(agent.getAID().getLocalName() 
				+ " : Game over! No more attempts remaining.");
		agent.doDelete();
	}
}
