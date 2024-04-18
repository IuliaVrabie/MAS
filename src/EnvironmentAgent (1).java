import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EnvironmentAgent extends Agent {
    private int temperature = 20; // Initial temperature

    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                // Template to listen for messages related to temperature change or sensing
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = myAgent.receive(mt);

                if (msg != null) {
                    String content = msg.getContent();
                    if ("DECREASE".equals(content)) {
                        temperature--;
                        System.out.println("Environment: Decreased temperature to " + temperature);
                    } else if ("INCREASE".equals(content)) {
                        temperature++;
                        System.out.println("Environment: Increased temperature to " + temperature);
                    } else if ("SENSE".equals(content)) {
                        // Respond with the current temperature
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(String.valueOf(temperature));
                        myAgent.send(reply);
                    }
                } else {
                    block();
                }
            }
        });
    }
}
