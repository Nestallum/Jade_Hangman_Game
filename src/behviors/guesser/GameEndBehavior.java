package behviors.guesser;

import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;

public class GameEndBehavior extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	/**
	 * We could use Behaviour.myAgent field instead of 'agent'. See {@link behviors.provider.GameBehavior}.
	 */
	AgentGuesser agent;

	public GameEndBehavior(AgentGuesser a) {
		this.agent = a;
	}

	public void action() {
		agent.doDelete();
	}

}
