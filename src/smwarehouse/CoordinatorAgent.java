package smwarehouse;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CoordinatorAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("Agente Coordinador activado: " + getAID().getName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    String content = msg.getContent();
                    String robotName = msg.getSender().getLocalName();

                    if (content.startsWith("Registro inicial desde")) {
                        System.out.println("Coordinator recibió un mensaje de: "
                                + msg.getSender().getName()
                                + " | Contenido: "
                                + content);

                        String assignedZone;

                        switch (robotName) {
                            case "robot1":
                                assignedZone = "Zona A";
                                break;
                            case "robot2":
                                assignedZone = "Zona B";
                                break;
                            case "robot3":
                                assignedZone = "Zona C";
                                break;
                            default:
                                assignedZone = "Zona Sin Asignar";
                                break;
                        }

                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("Asignación inicial: " + assignedZone);
                        send(reply);

                        System.out.println("Coordinator asignó " + assignedZone + " a " + robotName);

                    } else if (content.startsWith("Incidencia detectada en")) {
                        System.out.println("Coordinator registró incidencia: " + content);

                        ACLMessage incidentReply = msg.createReply();
                        incidentReply.setPerformative(ACLMessage.INFORM);
                        incidentReply.setContent("Incidencia recibida. Mantén vigilancia en tu zona y espera instrucciones.");
                        send(incidentReply);

                        System.out.println("Coordinator respondió a la incidencia reportada por " + robotName);

                    } else {
                        System.out.println("Coordinator recibió mensaje no reconocido de "
                                + robotName + ": " + content);
                    }

                } else {
                    block();
                }
            }
        });
    }
}