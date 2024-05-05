package behviors.provider;

import agents.AgentProvider;
import agents.AgentGuesser;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import tools.AgentLogger;
import tools.TrialResult;

public class GameBehavior extends OneShotBehaviour {
    private static final long serialVersionUID = 1L;

    int status;
    AgentProvider agent;

    public GameBehavior(AgentProvider a) {
        this.agent = a;
        status = -1;
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
        TrialResult tr = ((AgentProvider) agent).checkTrial(letter);
        status = tr.getStatus();
        if(status != 0) { // Send the result while the game is not over.
	        String result = tr.toString();
	        ACLMessage response = new ACLMessage(ACLMessage.INFORM);
	        response.setContent(result);
	        response.addReceiver(AgentGuesser.ID);
	        agent.send(response);
	        System.out.println(agent.getAID().getLocalName() + ": Attempts left: " + agent.getNbTrials());
        }
    }

    public int onEnd() {
        return Math.abs(status);
    }
}
