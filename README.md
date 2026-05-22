# Inmobiliaria La Punta - App para Propietarios

## 🔹 Tercera Entrega

Aplicación Android para propietarios de inmuebles desarrollada en Java con Android Studio, orientada a la gestión, visualización y administración de propiedades, contratos y datos personales desde la perspectiva del locador.

---

### ✅ Funcionalidades Implementadas (Historial + Nueva Entrega):

* **Login / Logout Seguro:** Autenticación basada en **Retrofit + JWT** con almacenamiento local en `SharedPreferences`. El sistema protege las rutas del aplicativo, asegurando que solo los usuarios autenticados consuman la API utilizando la identidad recuperada directamente del token del lado del servidor (sin enviar explícitamente el ID del propietario).
* **Fragment Inicio (Ubicación):** Pantalla principal que integra la API de Google Maps para mostrar de forma interactiva la localización física de la sucursal de la inmobiliaria.
* **Gestión de Perfil Completa:**
  * Visualización detallada de la información del propietario logueado.
  * Edición y actualización de datos personales (Nombre, Apellido, DNI, Teléfono, Email) directamente hacia la base de datos de manera dinámica.
  * Control independiente para la modificación y cambio de contraseña por separado.
* **Listado Dinámico de Inmuebles:** Visualización interactiva en tiempo real de todas las propiedades pertenecientes al propietario autenticado.
* **Habilitar / Deshabilitar Inmueble:** Modificación inmediata del estado de disponibilidad de un inmueble específico desde la vista de detalle.
* **Alta de Nuevo Inmueble con Foto:** Formulario completo para registrar una nueva propiedad en el sistema. Los inmuebles creados se inicializan por defecto como **deshabilitados (no disponibles)**.

---

### 🧑‍💻 Tecnologías y Arquitectura

* **Patrón MVVM** (Model-View-ViewModel).
* **ViewBinding** para una interacción segura con los elementos del layout.
* **Retrofit + Gson** para el consumo eficiente de la API REST.
* **Navigation Component** (Drawer Layout + Bottom Navigation).
* **Google Maps SDK** para la integración del mapa de ubicación.
* **SharedPreferences** para la persistencia del token de sesión.

---

### ⚙️ Detalles Técnicos de la Tercera Entrega

#### 1. Listado de Inmuebles (`InmueblesViewModel`)
Se realiza una petición asíncrona mediante un servicio `GET` de Retrofit adjuntando el token recuperado de la sesión. Cuenta con estados reactivos (`LiveData`) para controlar el flujo de carga (`cargandoMutable`) y el manejo de excepciones de red o respuestas fallidas (`errorMutable`). Incluye una función `refresh()` para mantener actualizada la lista tras sufrir modificaciones.

#### 2. Cambio de Estado / Disponibilidad (`DetalleInmuebleViewModel`)
Permite conmutar la propiedad `isDisponible()` de un inmueble. Envía una petición reactiva a la API mediante un objeto mutado y actualiza asíncronamente el estado visual en la interfaz de usuario en función de la respuesta exitosa del servidor, emitiendo alertas tipo `Toast` informativas.

#### 3. Registro de Propiedad con Carga de Imagen (`NuevoInmuebleViewModel`)
* **Procesamiento de Archivos:** Captura la URI de la fotografía seleccionada en el dispositivo mediante un `ActivityResultLauncher` y la transforma en un flujo de bytes (`byte[]`) comprimido en formato **JPEG**.
* **Petición Multipart:** Para el envío simultáneo de la entidad de datos y el archivo de imagen, se implementa una solicitud **`Multipart`** de Retrofit. El modelo de datos del inmueble se serializa a una cadena **JSON** (`RequestBody` de tipo `application/json`), mientras que la imagen se empaqueta de forma independiente como un `MultipartBody.Part`.

---

### 📋 Próximas funcionalidades (Siguientes etapas):
* Listar contratos asociados por cada Inmueble y sus respectivos históricos de pagos y datos del Inquilino.

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
