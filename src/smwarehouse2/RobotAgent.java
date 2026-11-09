package smwarehouse2;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RobotAgent extends Agent {

    private String assignedZone;
    private RobotStatus status;
    private String activeScenario;

    @Override
    protected void setup() {
        assignedZone = null;
        status = RobotStatus.IDLE;
        activeScenario = null;

        System.out.println("Robot activado: " + getAID().getName());
        printRobotState();

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
                                + " recibió asignación completa: "
                                + content);

                        String[] parts = content.split("\\|");

                        assignedZone = parts[0].replace("Asignación inicial: ", "").trim();

                        if (parts.length > 1) {
                            activeScenario = parts[1].replace("Escenario: ", "").trim();
                        }

                        if ("COVERAGE_FAILURE".equals(activeScenario) && getLocalName().equals("robot3")) {
                            status = RobotStatus.UNAVAILABLE;

                            System.out.println("Robot " + getLocalName()
                                    + " queda no disponible en "
                                    + assignedZone
                                    + " bajo el escenario "
                                    + activeScenario);
                            printRobotState();

                            reportUnavailability();
                        } else {
                            status = RobotStatus.PATROLLING;

                            System.out.println("Robot " + getLocalName()
                                    + " comienza patrulla en "
                                    + assignedZone);
                            printRobotState();

                            maybeReportIncident();
                        }

                    } else if (content.startsWith("Incidencia recibida.")) {
                        System.out.println("Robot " + getLocalName()
                                + " recibió respuesta del coordinador ante la incidencia: "
                                + content);

                        reportResolution();

                        status = RobotStatus.PATROLLING;
                        printRobotState();

                    } else if (content.startsWith("Cobertura comprometida recibida.")) {
                        System.out.println("Robot " + getLocalName()
                                + " recibió confirmación del coordinador sobre su indisponibilidad: "
                                + content);
                        printRobotState();

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

    private void maybeReportIncident() {
        boolean shouldReport = false;

        if ("SINGLE_INCIDENT".equals(activeScenario) && getLocalName().equals("robot1")) {
            shouldReport = true;
        } else if ("MULTI_INCIDENT".equals(activeScenario)
                && (getLocalName().equals("robot1") || getLocalName().equals("robot2"))) {
            shouldReport = true;
        }

        if (shouldReport) {
            reportIncident();
        }
    }

    private void reportIncident() {
        ACLMessage incidentMsg = new ACLMessage(ACLMessage.INFORM);
        incidentMsg.addReceiver(new AID("coordinator", AID.ISLOCALNAME));
        incidentMsg.setContent("Incidencia detectada en " + assignedZone + " por " + getLocalName());

        status = RobotStatus.RESPONDING_INCIDENT;
        send(incidentMsg);

        System.out.println("Robot " + getLocalName()
                + " notificó una incidencia al coordinador en "
                + assignedZone
                + " bajo el escenario "
                + activeScenario);
        printRobotState();
    }

    private void reportResolution() {
        if (status == RobotStatus.RESPONDING_INCIDENT) {
            ACLMessage resolvedMsg = new ACLMessage(ACLMessage.INFORM);
            resolvedMsg.addReceiver(new AID("coordinator", AID.ISLOCALNAME));
            resolvedMsg.setContent("Incidencia resuelta en " + assignedZone + " por " + getLocalName());
            send(resolvedMsg);

            System.out.println("Robot " + getLocalName()
                    + " informó al coordinador que la incidencia ha sido resuelta en "
                    + assignedZone);
        }
    }

    private void reportUnavailability() {
        ACLMessage unavailableMsg = new ACLMessage(ACLMessage.INFORM);
        unavailableMsg.addReceiver(new AID("coordinator", AID.ISLOCALNAME));
        unavailableMsg.setContent("Robot no disponible en " + assignedZone + " por " + getLocalName());
        send(unavailableMsg);

        System.out.println("Robot " + getLocalName()
                + " notificó al coordinador que no está disponible en "
                + assignedZone);
    }

    private void printRobotState() {
        System.out.println("----- ESTADO DEL ROBOT -----");
        System.out.println("Robot: " + getLocalName());
        System.out.println("Zona asignada: " + assignedZone);
        System.out.println("Escenario activo del robot: " + activeScenario);
        System.out.println("Estado actual: " + status);
        System.out.println("----------------------------");
    }
}