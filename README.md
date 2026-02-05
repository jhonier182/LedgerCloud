# Ledgercloud

SaaS de facturación y contabilidad.

## Arquitectura

Clean Architecture + DDD + Modular Monolith.

- **domain**: Negocio puro, sin Spring
- **application**: Casos de uso
- **infrastructure**: JPA, Redis, RabbitMQ, APIs
- **web**: Controllers, Vaadin Views

Ver [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) para el detalle completo.
