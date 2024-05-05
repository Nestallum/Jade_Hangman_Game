package behviors.provider;

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
		System.out.println(agent.getAID().getLocalName() 
				+ " : Game over. Number of trials : " + agent.getNbTrials());
		agent.doDelete();
	}

}
