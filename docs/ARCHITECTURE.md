# Arquitectura Ledgercloud

Arquitectura modular tipo Clean Architecture + DDD para SaaS de facturación.

---

## Diagrama de dependencias

```
                    ┌─────────────┐
                    │    domain   │  ← Centro. Cero dependencias externas.
                    └──────┬──────┘
                           │
           ┌───────────────┼───────────────┐
           │               │               │
           ▼               ▼               ▼
    ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
    │ application  │ │infrastructure│ │    web      │
    │ (use cases)  │ │ (JPA, Redis, │ │(controllers,│
    │              │ │  RabbitMQ)   │ │   Vaadin)   │
    └──────┬──────┘ └──────┬──────┘ └──────┬──────┘
           │               │               │
           └───────────────┴───────────────┘
                    │
                    ▼
              domain (solo)
```

**Regla de oro:** Las dependencias apuntan siempre hacia el dominio. Nunca al revés.

---

## Capas del sistema

### domain (LA MÁS IMPORTANTE)

Aquí vive el negocio.

**NO debe depender de Spring, JPA ni ningún framework.**

- reglas de negocio
- estados (enums)
- validaciones de dominio
- lógica rica en entidades

**Domain rico, no anémico:** Las entidades tienen comportamiento (`invoice.cancel()`, `invoice.markAsPaid()`), no solo getters/setters.

**Split Domain vs JPA:** El modelo de dominio (`Invoice`) es un POJO puro. La entidad JPA (`InvoiceEntity`) vive en infrastructure. El repositorio mapea entre ambos.

---

### application

Orquesta casos de uso.

- crear factura
- cancelar factura
- enviar factura

**Permitido:** `@Service`, `@Transactional` para DI y transacciones.

**Prohibido:** `@Entity`, `@Table`, anotaciones JPA, `HttpServletRequest`, anotaciones de Spring MVC.

La lógica de negocio vive en domain. Application solo orquesta.

---

### infrastructure

Lo técnico. Aquí sí vive Spring.

- JPA (entidades, repositorios)
- Redis
- RabbitMQ
- APIs externas (DIAN, pasarelas)

**Ports & Adapters:** Las interfaces están en domain (ports). Las implementaciones están aquí (adapters).

---

### web

Entrada al sistema.

- Controllers REST (finos: reciben request → llaman use case → devuelven response)
- Vaadin Views (delegan a use cases, sin lógica de negocio)

---

### shared

Cosas reutilizables entre módulos.

**Regla:** Si algo es específico de un dominio, va al módulo. Shared NO es basurero.

- security
- exceptions
- tenancy
- util (solo utilidades genéricas: fechas, strings)

---

## Estructura de un módulo

```
modules/[nombre]
├── domain/
│   ├── model/       ← entidades de dominio (POJOs)
│   ├── repository/  ← interfaces (ports)
│   ├── service/     ← servicios de dominio
│   └── events/      ← eventos de dominio (InvoiceCreatedEvent)
├── application/
│   ├── usecase/     ← casos de uso
│   └── dto/         ← request/response
├── infrastructure/
│   ├── persistence/ ← JPA entities, adapters
│   ├── messaging/   ← RabbitMQ (si aplica)
│   └── external/    ← clientes HTTP (si aplica)
└── web/
    ├── controller/
    └── view/        ← Vaadin (si aplica)
```

**Eventos de dominio:** Viven en `domain/events/`. El publicador (RabbitMQ) está en infrastructure.

---

## Comunicación entre módulos

- **Evitar:** Dependencias directas entre módulos (Invoice importando Payment).
- **Preferir:** Eventos. Ej: `InvoicePaidEvent` → Payment lo consume vía RabbitMQ.

---

## Checklist por capa

| Capa | Responsabilidad | Spring | JPA |
|------|-----------------|--------|-----|
| **domain** | Negocio puro, reglas, estados | NO | NO |
| **application** | Casos de uso, orquestación | @Service, @Transactional | NO |
| **infrastructure** | Persistencia, mensajería, APIs | SÍ | SÍ |
| **web** | Controllers, Views | SÍ | NO |
| **shared** | Cross-cutting | Parcial | NO |

---

## Estrategia de testing

- **Domain:** Tests unitarios puros. Sin Spring. Rápidos.
- **Application:** Tests con mocks de repositorios. Sin Spring opcional.
- **Infrastructure + Web:** Tests de integración con Testcontainers.
