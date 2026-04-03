# TestRestClientTestWebClient

Este proyecto demuestra la implementación de microservicios híbridos utilizando la **Arquitectura Hexagonal** y dos enfoques para la comunicación entre servicios en Spring Boot 3.2+: **WebClient** (usado de forma bloqueante) y **RestClient** (sincrónico).

## 🏗️ Arquitectura del Proyecto

El proyecto sigue los principios de **Arquitectura Hexagonal** (Puertos y Adaptadores), dividiendo la lógica en capas para asegurar que el dominio sea independiente de la infraestructura (Bases de datos, APIs externas, etc.).

### Estructura de Capas
- **Domain**: Modelos de negocio, puertos de entrada (Use Cases) y puertos de salida (Repositorios/Clientes).
- **Application**: Implementación de casos de uso, DTOs y Mappers (MapStruct).
- **Infrastructure**: Adaptadores JPA, controladores REST y clientes HTTP (WebClient/RestClient).

---

## 🚀 Microservicios

### 1. `cuenta-ahorro-service`
Este servicio gestiona las cuentas de ahorro y consume información de `cuenta-corriente-service`.

- **Tecnología de Cliente**: **WebClient** (Spring WebFlux).
- **Comunicación**: Híbrida (se utiliza `.block()` para integrarse en un flujo sincrónico).
- **Responsabilidad**: Consultar saldos de cuentas corrientes.

### 2. `cuenta-corriente-service`
Este servicio gestiona las cuentas corrientes y consume información de `cuenta-ahorro-service`.

- **Tecnología de Cliente**: **RestClient** (Nueva API fluida de Spring 6).
- **Comunicación**: Sincrónica.
- **Responsabilidad**: Consultar información de ahorro relacionada.

---

## 🧪 Pruebas Unitarias e Integración

El proyecto implementa una estrategia de pruebas robusta utilizando diversas herramientas de Spring Boot Test:

- **@DataJpaTest**: Para pruebas de rebanada (slice tests) de la capa de persistencia, validando el mapeo de entidades y el comportamiento de los repositorios H2.
- **@WebMvcTest** + **WebTestClient**: Para pruebas de controladores REST sin levantar el servidor completo. `WebTestClient` permite aserciones fluidas sobre los endpoints MVC y el manejo de respuestas híbridas.
- **@RestClientTest**: Pruebas especializadas para los adaptadores que utilizan la nueva API `RestClient`, verificando la serialización/deserialización y la integración con servicios externos.
- **MockWebServer (OkHttp)**: Utilizado para pruebas unitarias de adaptadores `WebClient`, permitiendo simular un servidor real con respuestas programadas.
- **MockRestServiceServer**: Utilizado para interceptar y simular las respuestas de los servicios remotos (Micro-A/Micro-B) de forma aislada.
- **AssertJ**: Para aserciones descriptivas y legibles en todas las capas.

---

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA** (H2 Database)
- **MapStruct**
- **Lombok**
- **Maven**

## 🏁 Cómo Ejecutar

1. **Clonar el repositorio.**
2. **cuenta-ahorro-service**:
   ```bash
   cd cuenta-ahorro-service
   mvn spring-boot:run
   ```
   *Puerto por defecto: 8081*
3. **cuenta-corriente-service**:
   ```bash
   cd cuenta-corriente-service
   mvn spring-boot:run
   ```
   *Puerto por defecto: 8082*

---

## 📬 Postman Collection

Se ha incluido una colección de Postman (`Microservicios Híbrido — Hexagonal.postman_collection.json`) en la raíz del proyecto para facilitar las pruebas de los endpoints.

---

## 📖 Documentación de APIs

### cuenta-ahorro-service (Puerto 8081)
- `GET /api/ahorro`: Listar cuentas.
- `GET /api/ahorro/{id}/saldo-corriente`: Consulta sincronizada a Micro-B.

### cuenta-corriente-service (Puerto 8082)
- `GET /api/corriente/{id}`: Obtener detalle.
- `GET /api/corriente/{id}/ahorro-info`: Consulta sincrónica a Micro-A.
