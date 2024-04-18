import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ThermostatAgent extends Agent {
    private AID environmentAgent;

    protected void setup() {
        environmentAgent = new AID("environment", AID.ISLOCALNAME);

        addBehaviour(new TickerBehaviour(this, 1000) { // Check every second
            protected void onTick() {
                // Sense the temperature
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(environmentAgent);
                msg.setContent("SENSE");
                myAgent.send(msg);

                // Receive the response from the environment agent
                ACLMessage reply = myAgent.blockingReceive();
                if (reply != null) {
                    int temperature = Integer.parseInt(reply.getContent());
                    System.out.println("Thermostat: Current temperature is " + temperature);

                    if (temperature < 18) {
                        // Increase the temperature
                        sendMessage("INCREASE");
                    } else if (temperature > 22) {
                        // Decrease the temperature
                        sendMessage("DECREASE");
                    }
                }
            }

            private void sendMessage(String content) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(environmentAgent);
                msg.setContent(content);
                myAgent.send(msg);
            }
        });
    }
}
