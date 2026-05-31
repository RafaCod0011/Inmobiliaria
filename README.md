# Inmobiliaria La Punta - App para Propietarios

## 🔹 Entrega Final (Proyecto Completo)

Aplicación Android para propietarios de inmuebles desarrollada en Java con Android Studio, orientada a la gestión, visualización y administración de propiedades, contratos, datos de inquilinos y el control del histórico de pagos desde la perspectiva del locador.

---

### ✅ Funcionalidades Implementadas (Historial + Nueva Entrega):

* **Login / Logout Seguro:** Autenticación basada en **Retrofit + JWT** con almacenamiento local seguro en `SharedPreferences`. El sistema protege todas las rutas del aplicativo, asegurando que solo los usuarios autenticados consuman la API utilizando la identidad recuperada del token (sin enviar explícitamente el ID del propietario).
* **Resetear Contraseña:** Opción de recuperación integrada ("Me olvidé la contraseña") accesible directamente desde la pantalla de Login.
* **Fragment Inicio (Ubicación):** Pantalla principal que integra la API de Google Maps para mostrar de forma interactiva la localización física de la sucursal de la inmobiliaria.
* **Gestión de Perfil Completa:**
  * Visualización detallada de la información del propietario logueado.
  * Edición y actualización de datos personales (Nombre, Apellido, DNI, Teléfono, Email) directamente hacia la base de datos de manera dinámica.
  * Control independiente para la modificación y cambio de contraseña por separado.
* **Listado Dinámico de Inmuebles:** Visualización interactiva en tiempo real de todas las propiedades pertenecientes al propietario autenticado.
* **Habilitar / Deshabilitar Inmueble:** Modificación inmediata del estado de disponibilidad de un inmueble específico desde la vista de detalle.
* **Alta de Nuevo Inmueble con Foto:** Formulario completo para registrar una nueva propiedad en el sistema. Los inmuebles creados se inicializan por defecto como **deshabilitados (no disponibles)**.
* **Visualización de Inquilinos (¡Nuevo!):** Acceso al detalle completo del locatario que ocupa un inmueble específico, incluyendo datos de contacto y su garante.
* **Auditoría de Contratos Activos (¡Nuevo!):** Listado y desglose de los contratos de locación vigentes vinculados a las propiedades del usuario logueado.
* **Control e Historial de Pagos (¡Nuevo!):** Panel financiero integrado que expone de forma cronológica los recibos de cobro de alquiler emitidos por la agencia.

---

### 🧑‍💻 Tecnologías y Arquitectura:

* Patrón **MVVM** (Model-View-ViewModel)
* **ViewBinding** para una interacción segura con los elementos del layout
* **Retrofit + Gson** para el consumo eficiente de la API REST
* **Navigation Component** (Drawer Layout + Bottom Navigation)
* **Google Maps SDK** para la integración del mapa de ubicación 
* **SharedPreferences** para persistencia de token

---

### ⚙️ Detalles Técnicos de la Entrega Final (Módulos Nuevos)

#### 1. Módulo de Inquilinos (`InquilinosViewModel`)
Este componente se encarga de recuperar mediante Retrofit los inmuebles que se encuentran actualmente bajo un régimen de alquiler activo. Al seleccionar una propiedad de la lista, el ViewModel procesa y expone a la vista de manera reactiva mediante `LiveData` la información detallada del locatario responsable, mapeando estrictamente los campos del servidor: *Código de inquilino, Nombre, Apellido, DNI, Email y Teléfono*, además de los datos de su *Fiador/Garante* (Nombre y Teléfono).

#### 2. Detalle de Contratos vigentes (`ContratosViewModel`)
Filtra y obtiene de la API REST los acuerdos contractuales activos asociados a las propiedades del propietario autenticado (resolviendo su identidad de forma implícita mediante el token JWT). El ViewModel maneja de forma asíncrona la respuesta exitosa para renderizar en la interfaz el documento digital con sus campos obligatorios: *Código de contrato, Fecha de Inicio, Fecha de Finalización, Monto del alquiler pactado en pesos* y el nombre completo del *Inquilino* asociado.

#### 3. Historial de Recibos Financieros (`PagosViewModel`)
Anidado dentro de la vista del contrato, este ViewModel realiza una petición asíncrona para traer la secuencia cronológica de cobros registrados en la agencia para esa locación en particular. El flujo mapea los datos de los pagos y los expone en un listado ordenado controlando los estados mediante observables para mostrar: *Código de pago, Número de pago correlativo, Código de contrato vinculado, Importe exacto abonado en pesos y la Fecha de pago*.

---

### 🗺️ Configurar API Key de Google Maps.

Sigue estos pasos para configurar correctamente la clave de Google Maps en el proyecto:

1. **Crear el archivo de secretos**  
   Crea un archivo llamado `secrets.properties` en la raíz del proyecto y añade tu clave con el siguiente formato:
   ```properties
   MAPS_API_KEY=TU_API_KEY_AQUÍ 
   ```
2. Crea un archivo llamado `local.defaults.properties` en la raíz del proyecto con un valor de respaldo:
    ```propierties
    MAPS_API_KEY=DEFAULT_API_KEY
    ```
3. Verifica que en el archivo `AndroidManifest.xml`, dentro de la etiqueta `<application>`, la clave quede referenciada de la siguiente manera:
   ```xml
    <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="${MAPS_API_KEY}" />
   ```
4. Sincronizar Gradle.
   
    

### 🚀 Cómo ejecutar el proyecto

1. Clonar el repositorio
2. Abrir el proyecto en Android Studio
3. Esperar que Gradle sincronice
4. Ejecutar en emulador o dispositivo físico

---

### 👥 Integrantes del grupo

- Facundo Martín García – DNI: 28399283
- Victor Angel Aguilera – DNI: 36220045
- Rafael Nicolas Cuello – DNI: 39396258
- Martin Nahuel Becerra – DNI: 47266622
