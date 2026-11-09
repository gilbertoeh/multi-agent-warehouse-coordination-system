package smwarehouse2;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorAgent extends Agent {

    private Map<String, Zone> zones;
    private List<Incident> incidents;
    private int incidentCounter = 1;
    private ScoreManager scoreManager;
    private SimulationConfig simulationConfig;

    @Override
    protected void setup() {
        System.out.println("Agente Coordinador activado: " + getAID().getName());

        zones = new LinkedHashMap<>();
        incidents = new ArrayList<>();
        scoreManager = new ScoreManager();

        simulationConfig = new SimulationConfig(
                ScenarioType.SINGLE_INCIDENT,
                5,
                3
        );
        /* Dejo este escenario porque es el más completo y equilibrado, ya que muestra:
        * asignación, patrulla, dtección, respuesta, resolución y mejora del resumen cuantitativo
        */

        System.out.println("Configuración de simulación activa:");
        System.out.println(simulationConfig);

        zones.put("Zona A", new Zone("Zona A", null, false));
        zones.put("Zona B", new Zone("Zona B", null, false));
        zones.put("Zona C", new Zone("Zona C", null, false));

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

                        updateZoneAssignment(assignedZone, robotName);

                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("Asignación inicial: " + assignedZone + " | Escenario: " + simulationConfig.getScenarioType());
                        send(reply);

                        System.out.println("Coordinator asignó " + assignedZone + " a " + robotName);
                        printCurrentSystemState();

                    } else if (content.startsWith("Incidencia detectada en")) {
                        System.out.println("Coordinator registró incidencia: " + content);

                        String zoneName = extractZoneName(content);
                        Incident incident = new Incident(
                                "INC-" + incidentCounter++,
                                zoneName,
                                robotName,
                                false
                        );
                        incidents.add(incident);

                        ACLMessage incidentReply = msg.createReply();
                        incidentReply.setPerformative(ACLMessage.INFORM);
                        incidentReply.setContent("Incidencia recibida. Procede con la resolución y reporta el cierre.");
                        send(incidentReply);

                        System.out.println("Coordinator respondió a la incidencia reportada por " + robotName);
                        printCurrentSystemState();

                    } else if (content.startsWith("Incidencia resuelta en")) {
                        System.out.println("Coordinator registró resolución: " + content);

                        String zoneName = extractZoneName(content);
                        markIncidentAsResolved(zoneName, robotName);

                        System.out.println("Coordinator marcó como resuelta la incidencia atendida por " + robotName);
                        printCurrentSystemState();

                    } else if (content.startsWith("Robot no disponible en")) {
                        System.out.println("Coordinator registró indisponibilidad: " + content);

                        String zoneName = extractZoneName(content);
                        markZoneAsUncovered(zoneName);

                        ACLMessage unavailableReply = msg.createReply();
                        unavailableReply.setPerformative(ACLMessage.INFORM);
                        unavailableReply.setContent("Cobertura comprometida recibida. El coordinador registra tu indisponibilidad.");
                        send(unavailableReply);

                        System.out.println("Coordinator marcó la " + zoneName + " como no cubierta.");
                        printCurrentSystemState();

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

    private void updateZoneAssignment(String zoneName, String robotName) {
        Zone zone = zones.get(zoneName);
        if (zone != null) {
            zone.setAssignedRobot(robotName);
            zone.setCovered(true);
        }
    }

    private void markZoneAsUncovered(String zoneName) {
        Zone zone = zones.get(zoneName);
        if (zone != null) {
            zone.setCovered(false);
        }
    }

    private void markIncidentAsResolved(String zoneName, String robotName) {
        for (int i = incidents.size() - 1; i >= 0; i--) {
            Incident incident = incidents.get(i);
            if (!incident.isResolved()
                    && incident.getZoneName().equals(zoneName)
                    && incident.getReportedBy().equals(robotName)) {
                incident.setResolved(true);
                break;
            }
        }
    }

    private String extractZoneName(String content) {
        if (content.contains("Zona A")) {
            return "Zona A";
        } else if (content.contains("Zona B")) {
            return "Zona B";
        } else if (content.contains("Zona C")) {
            return "Zona C";
        }
        return "Zona desconocida";
    }

    private int countCoveredZones() {
        int count = 0;
        for (Zone zone : zones.values()) {
            if (zone.isCovered()) {
                count++;
            }
        }
        return count;
    }

    private int countResolvedIncidents() {
        int count = 0;
        for (Incident incident : incidents) {
            if (incident.isResolved()) {
                count++;
            }
        }
        return count;
    }

    private void printCurrentSystemState() {
        System.out.println("----- ESTADO ACTUAL DEL SISTEMA -----");
        System.out.println("Escenario activo: " + simulationConfig.getScenarioType());
        System.out.println("Ciclos configurados: " + simulationConfig.getTotalCycles());
        System.out.println("Robots configurados: " + simulationConfig.getTotalRobots());

        System.out.println("Zonas:");
        for (Zone zone : zones.values()) {
            System.out.println(zone);
        }

        System.out.println("Incidencias registradas:");
        if (incidents.isEmpty()) {
            System.out.println("Sin incidencias registradas.");
        } else {
            for (Incident incident : incidents) {
                System.out.println(incident);
            }
        }

        int coveredZones = countCoveredZones();
        int totalIncidents = incidents.size();
        int resolvedIncidents = countResolvedIncidents();
        int unattendedZones = zones.size() - coveredZones;
        int unattendedIncidents = totalIncidents - resolvedIncidents;
        int lateResponses = 0;

        int totalScore = scoreManager.calculateScore(
                coveredZones,
                totalIncidents,
                resolvedIncidents,
                unattendedZones,
                unattendedIncidents,
                lateResponses
        );

        SimulationResult result = new SimulationResult(
                totalScore,
                totalIncidents,
                resolvedIncidents,
                coveredZones
        );

        System.out.println("Resumen cuantitativo:");
        System.out.println(result);

        System.out.println("-------------------------------------");
    }
}