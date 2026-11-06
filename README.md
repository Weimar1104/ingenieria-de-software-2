# Banco de Alimentos Inteligente para Medellín
## Sistema de Gestión de Donaciones de Alimentos
**Ingeniería de Software II — Corporación Universitaria Remington (2026)**  
**Autores:** Albert Higuita Bautista y Weimar Esteban Serna Asprilla  
**Docente:** Gloria Amparo Lora Patiño  

---

## 1. Descripción del Proyecto
Plataforma web construida con **Spring Boot 3 (Java 21)** bajo el patrón arquitectónico **Modelo–Vista–Controlador (MVC)**, diseñada para resolver la gestión de donaciones, el control de inventario en bodega central, el despacho de alimentos hacia poblaciones vulnerables en Medellín y la integración de un **motor de analítica e Inteligencia Artificial (Groq Cloud / GPT OSS 120B)** para diagnóstico de mermas y predicción de demanda de alimentos.

El sistema aplica estrictamente el principio de responsabilidad única (**SRP**), la normalización relacional en **Tercera Forma Normal (3FN)** y las 16 reglas de negocio documentadas en la especificación técnica del proyecto (ECS-01).

---

## 2. Decisiones Arquitectónicas y Tecnológicas

- **Lenguaje y Runtime:** Java 21 LTS (Oracle JDK).
- **Framework Principal:** Spring Boot 3.2.5.
- **Patrón de Arquitectura:** MVC en capas desacopladas con SRP:
  - `model.entity`: 10 entidades JPA en 3FN con claves foráneas, unicidad y validación.
  - `model.repository`: Repositorios Spring Data JPA para acceso a datos.
  - `service` e `impl`: Capa de lógica de negocio y aplicación estricta de las reglas de negocio (SRP).
  - `service.AnalisisIAService`: Servicio de integración con el LLM en la nube (Groq Cloud API).
  - `controller`: Controladores web Spring MVC que gestionan las peticiones HTTP y redirecciones.
  - `templates`: Vistas HTML5 renderizadas con Thymeleaf con diseño sobrio académico en escala de grises y navegación superior por botones tipo pestaña.
- **Seguridad:** Spring Security con autenticación basada en roles y contraseñas cifradas mediante **BCrypt**.
- **Servidor Web:** Apache Tomcat 10 embebido (no requiere configuración de servidores externos).
- **Base de Datos:**
  - **Producción / Desarrollo Local:** MySQL en XAMPP (`localhost:3306/banco_alimentos`).
  - El sistema crea la base de datos y actualiza las tablas automáticamente mediante `spring.jpa.hibernate.ddl-auto=update`.
- **Módulo de Inteligencia Artificial (Groq Cloud API):**
  - **Modelo:** `openai/gpt-oss-120b` (con fallback de arquitectura para modelos de alto rendimiento).
  - **Funcionalidad:** Diagnóstico de riesgos de merma por caducidad, predicción de demanda en comedores comunitarios y asistente experto en logística de bancos de alimentos.

---

## 3. Reglas de Negocio Implementadas (RN)

- **RN-01 / RN-02 / RN-03:** Unicidad estricta de correo de usuarios y nombres de roles.
- **RN-04:** Solo se pueden registrar donaciones vinculadas a donantes activos.
- **RN-07:** Cada producto del catálogo tiene un único registro consolidado en el inventario central.
- **RN-12:** Las entregas y beneficiarios son **exclusivos para estratos socioeconómicos 1, 2 y 3** (se rechaza automáticamente cualquier intento de entrega a estratos 4, 5 y 6).
- **RN-14:** Todo alimento es sometido a evaluación sanitaria; los productos en estado `RECHAZADO` no ingresan al inventario disponible.
- **RN-16:** No se permite formalizar ninguna entrega que sobregire o supere el stock disponible en bodega. Al autorizar la entrega, el stock se descuenta automáticamente.

---

## 4. Estructura de Navegación por Botones (UI)

La barra de navegación superior presenta accesos directos con estilo visual de botón sobrio:
1. **[ 📦 Inventario Central ] (`/`)**: Matriz de existencias físicas en tiempo real de alimentos no perecederos.
2. **[ 📥 Recepción de Donaciones ] (`/donaciones`)**: Formulario para registro de donantes, lotes, vencimiento y control de calidad sanitario (RN-04 / RN-14).
3. **[ 📤 Despacho y Entregas ] (`/entregas`)**: Asignación de raciones a beneficiarios vulnerables (RN-12 / RN-16) con generación de actas.
4. **[ 🧠 Análisis IA Predictivo ] (`/ia`)**: Motor de inferencia y asistente logístico con conexión a Groq Cloud.

---

## 5. Instrucciones de Ejecución Rápida

### Requisitos Previos
1. Tener Java 21 instalado (`java --version`).
2. Abrir el panel de control de **XAMPP** e iniciar el servicio **MySQL** (puerto 3306).  
   *(No es necesario crear la base de datos manualmente; la aplicación la genera automáticamente con el usuario `root` sin contraseña).*

### Pasos para Iniciar
1. Abrir una terminal (PowerShell o CMD) en la carpeta del proyecto:
   ```bash
   cd "C:\Users\USUARIO\Downloads\DOCUMENTOS INGENIERIA DE SOFTWARE 2\banco-alimentos"
   ```

2. Ejecutar la aplicación con el Maven Wrapper incluido:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```
   *(O si tienes Maven global instalado: `mvn spring-boot:run`)*

3. Abrir en tu navegador web preferido:
   ```text
   http://localhost:8080
   ```

---

## 6. Usuarios y Credenciales por Defecto

El sistema incluye una semilla de datos (`DataInitializer`) que crea usuarios preconfigurados:

| Usuario | Contraseña | Rol Funcional |
| :--- | :--- | :--- |
| `admin` | `admin123` | Administrador del Sistema |
| `coordinador` | `donaciones123` | Coordinador de Donaciones |
| `bodega` | `bodega123` | Responsable de Inventario y Bodega |

---

## 7. Ejecución de Pruebas Unitarias y de Integración

Para ejecutar la suite de pruebas automatizadas que validan las reglas de negocio (JUnit 5 + Spring Boot Test):
```bash
.\mvnw.cmd test
```
# donaciones
