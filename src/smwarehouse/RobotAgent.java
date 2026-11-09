package smwarehouse;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RobotAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("Robot activado: " + getAID().getName());

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("coordinator", AID.ISLOCALNAME));
        msg.setContent("Registro inicial desde " + getLocalName());
        send(msg);

        System.out.println("Robot " + getLocalName() + " envió mensaje al coordinador.");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage reply = receive();

                if (reply != null) {
                    String content = reply.getContent();

                    if (content.startsWith("Asignación inicial:")) {
                        System.out.println("Robot " + getLocalName()
                                + " recibió su zona asignada: "
                                + content);

                        String zoneName = content.replace("Asignación inicial: ", "");
                        System.out.println("Robot " + getLocalName()
                                + " comienza patrulla en "
                                + zoneName);

                        if (getLocalName().equals("robot1")) {
                            ACLMessage incidentMsg = new ACLMessage(ACLMessage.INFORM);
                            incidentMsg.addReceiver(new AID("coordinator", AID.ISLOCALNAME));
                            incidentMsg.setContent("Incidencia detectada en " + zoneName + " por " + getLocalName());
                            send(incidentMsg);

                            System.out.println("Robot " + getLocalName()
                                    + " notificó una incidencia al coordinador en "
                                    + zoneName);
                        }

                    } else if (content.startsWith("Incidencia recibida.")) {
                        System.out.println("Robot " + getLocalName()
                                + " recibió respuesta del coordinador ante la incidencia: "
                                + content);

                    } else {
                        System.out.println("Robot " + getLocalName()
                                + " recibió mensaje no reconocido: "
                                + content);
                    }

                } else {
                    block();
                }
            }
        });
    }
}