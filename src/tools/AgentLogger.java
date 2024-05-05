package tools;

import jade.lang.acl.ACLMessage;

public class AgentLogger {

	public static void logACLMessage(ACLMessage message) {
		if (message != null) {
			System.out.println("\n" + message.getSender().getLocalName() 
					+ "> " + message.getContent());
		}
	}
}
