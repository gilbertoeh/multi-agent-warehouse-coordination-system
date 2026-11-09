# System Architecture

## Overview

The project implements a centralized multi-agent architecture using JADE.

The system is composed of one **Coordinator Agent** and three **Robot Agents** that interact inside a simulated warehouse environment divided into zones.

## Main components

### Coordinator Agent

The coordinator maintains the global state of the simulation and is responsible for:

- managing warehouse zones;
- tracking robot assignments;
- receiving incident notifications;
- registering incident resolution;
- monitoring zone coverage;
- calculating the simulation score;
- printing the final simulation result.

### Robot Agents

Each robot operates as an independent JADE agent and is responsible for:

- receiving its assigned warehouse zone;
- patrolling the assigned zone;
- maintaining its operational state;
- detecting scenario-specific events;
- reporting incidents to the coordinator;
- reporting incident resolution;
- reporting loss of availability when required by the active scenario.

### Warehouse zones

The simulated warehouse is divided into three zones:

- Zone A
- Zone B
- Zone C

During the validated execution, each zone is assigned to one robot agent so that all three zones remain covered.

### Communication

Communication between agents is implemented through **JADE ACL messages**.

Robot agents send information to the coordinator, while the coordinator maintains the global simulation state and processes the messages received from the robots.

## Validated agent configuration

The validated configuration launches:

- `coordinator` → `smwarehouse2.CoordinatorAgent`
- `robot1` → `smwarehouse2.RobotAgent`
- `robot2` → `smwarehouse2.RobotAgent`
- `robot3` → `smwarehouse2.RobotAgent`

The main validated scenario is `SINGLE_INCIDENT`.

## Architecture diagram

```text
                    +----------------------+
                    |   Coordinator Agent  |
                    |----------------------|
                    | Global state         |
                    | Zone assignments     |
                    | Incident tracking    |
                    | Scoring              |
                    +----------+-----------+
                               |
                     JADE ACL messages
                               |
          +--------------------+--------------------+
          |                    |                    |
          v                    v                    v
+----------------+   +----------------+   +----------------+
|  Robot Agent 1 |   |  Robot Agent 2 |   |  Robot Agent 3 |
|----------------|   |----------------|   |----------------|
| Zone A         |   | Zone B         |   | Zone C         |
| Patrol         |   | Patrol         |   | Patrol         |
| Incident flow  |   | Incident flow  |   | Availability   |
+----------------+   +----------------+   +----------------+
```

## Simulation scenarios

The second version of the system defines four scenarios:

- **`BASE`**: normal operation without simulated incidents or coverage failures.
- **`SINGLE_INCIDENT`**: one robot reports an incident that is processed and resolved through the coordinator.
- **`MULTI_INCIDENT`**: multiple robot agents can generate incidents during the simulation.
- **`COVERAGE_FAILURE`**: simulates the temporary unavailability of a robot and the resulting loss of warehouse zone coverage.

### Primary validated scenario

`SINGLE_INCIDENT` is used as the primary validated scenario because it provides a complete and stable execution flow:

1. all three warehouse zones are initially covered;
2. `robot1` reports an incident in Zone A;
3. the coordinator registers and processes the incident;
4. `robot1` reports the incident as resolved;
5. the coordinator updates the final simulation state and score.

This scenario demonstrates the main coordination, communication, incident-management, and evaluation mechanisms of the system in a single end-to-end execution.

