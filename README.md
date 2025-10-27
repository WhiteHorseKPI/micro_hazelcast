## Overview

The **micro_hazelcast** project demonstrates a simple micro-services architecture built in Java that uses the in-memory data grid Hazelcast to share state (distributed maps) between services. The codebase is structured into several independent modules (facade-service, logging-service, messages-service) which can operate together in a cluster, making use of Hazelcast for distributed state and communication.

### Key features

* Multiple service modules, each with its own domain responsibility.
* Use of Hazelcast’s distributed map (and possibly topics) to coordinate and share data between services.
* Maven project structure with a parent POM (`pom.xml`) and child modules.
* HTTP endpoints for interactions (via `.http` files included).
* Lightweight, modular design suitable for extension and experimentation.

## Modules

The repository includes the following modules:

| Module             | Responsibility                                                                                                                               | Notes                                         |
| ------------------ | -------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------- |
| `facade-service`   | Acts as the facade or entry-point service for client requests. Possibly coordinates other services and proxies to Hazelcast data structures. | Exposes REST endpoints.                       |
| `logging-service`  | Dedicated to logging or audit functionality; may listen to Hazelcast map/topic events or manage log entries in a shared map.                 | Good for demonstrating cross-service state.   |
| `messages-service` | Responsible for message handling — e.g., publishing, subscribing, or storing messages in Hazelcast.                                          | Illustrates a messaging/communication domain. |

## Getting Started

### Prerequisites

* Java 11 or newer (depending on modules’ source level)
* Maven (3.6+)
* Hazelcast cluster (embedded mode in services or external Hazelcast server)
* (Optional) Docker / Kubernetes if you intend to run multiple nodes easily

### Configuration

* Hazelcast cluster name, network interfaces, and map names can be tuned in each service’s configuration (e.g., `application.properties`, `hazelcast.xml`).
* Logging level, map backup count, eviction policy, and other Hazelcast properties should be reviewed for production-readiness (see Hazelcast logging docs for example). ([docs.hazelcast.com][1])
* If deploying to Kubernetes or containers, consider using Hazelcast’s Kubernetes discovery plugin (or static member list) for clustering.

## Architecture & Design Notes

* The facade service acts as the main interface exposed to clients; it delegates to downstream services and uses Hazelcast as the shared state layer.
* Logging service and messages service each maintain their own domain logic but share data and coordinate via Hazelcast maps or topics.
* Hazelcast enables low-latency, in-memory sharing of data without requiring a traditional database for certain use-cases (cache, state, distributed queue).
* The modular structure makes it easy to add more services (e.g., user-service, analytics-service) by following the same pattern and leveraging Hazelcast.
* For production use, consider scaling each service horizontally — Hazelcast will ensure state consistency and partitioning across nodes.

[1]: https://docs.hazelcast.com/hazelcast/5.3/maintain-cluster/logging?utm_source=chatgpt.com "Configuring Logging | Hazelcast Documentation"
[2]: https://docs.hazelcast.com/hazelcast/5.4/maintain-cluster/monitoring?utm_source=chatgpt.com "Monitoring"
