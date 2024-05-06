package tools;

import jade.lang.acl.ACLMessage;

/**
 * Utility class for logging ACLMessage objects.
 *
 * This class provides a static method for logging ACLMessage objects.
 *
 * Example usage:
 * {@code AgentLogger.logACLMessage(message);}
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class AgentLogger {

	public static void logACLMessage(ACLMessage message) {
		if (message != null) {
			System.out.println("\n" + message.getSender().getLocalName() 
					+ "> " + message.getContent());
		}
	}
}
