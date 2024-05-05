package behviors.provider;

import agents.AgentProvider;
import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import tools.AgentLogger;

public class GameBehavior extends OneShotBehaviour {
    private static final long serialVersionUID = 1L;

    int result;
    AgentProvider agent;

    public GameBehavior(AgentProvider a) {
        this.agent = a;
        result = -1;
    }

    public void action() {
        // Wait for a trial
        agent.doWait(); // or getAgent().doWait();
        ACLMessage message = agent.receive();
        AgentLogger.logACLMessage(message);
        char letter = ' ';
        if (message != null && message.getContent() != null) {
	        try {
	            letter = message.getContent().charAt(0);
	        } catch (Exception ex) {
	            System.out.println("Error : invalid message");
	            agent.doDelete();
	        }
        }
        else
        	System.out.println("No message received or null content.");

        // Response
        result = ((AgentProvider) agent).checkTrial(letter);
        ACLMessage response = new ACLMessage(ACLMessage.INFORM);
        response.setContent(Integer.toString(result));
        response.addReceiver(AgentGuesser.ID);
        agent.send(response);
    }

    public int onEnd() {
        return Math.abs(result);
    }
}
