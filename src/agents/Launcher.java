package agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 * Launcher class responsible for starting the Jade platform and launching the AgentProvider and AgentGuesser agents.
 *
 * This class initializes the Jade platform and creates a main container. It then creates and starts two agents:
 * AgentProvider, which provides the hidden word to be guessed, and AgentGuesser, which attempts to guess the hidden word.
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class Launcher {
	public static void main(String[] args) {
		Runtime runtime = Runtime.instance();
		Profile config = new ProfileImpl("localhost", 8888, null);
		config.setParameter("gui", "true");
		AgentContainer mc = runtime.createMainContainer(config);
		AgentController ac1;
		AgentController ac2;
		try {
			ac1 = mc.createNewAgent("agentProvider", AgentProvider.class.getName(), null);
			ac2 = mc.createNewAgent("agentGuesser", AgentGuesser.class.getName(), null);
			ac1.start();
			ac2.start();
		} catch (StaleProxyException e) {
		}
	}
}
