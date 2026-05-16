# Inmobiliaria La Punta - App para Propietarios

## 🔹 Segunda Entrega

Aplicación Android para propietarios de inmuebles desarrollada en Java con Android Studio, orientada a la gestión y visualización de propiedades, contratos y datos personales desde la perspectiva del locador
### ✅ Funcionalidades implementadas:

* **Login / Logout Seguro:** Autenticación basada en Retrofit + JWT con almacenamiento local seguro en `SharedPreferences`. El sistema protege todas las rutas del aplicativo, asegurando que solo los usuarios autenticados consuman la API utilizando la identidad recuperada del token.
* **Fragment Inicio (Ubicación):** Pantalla principal que integra la API de Google Maps para mostrar de forma interactiva la localización física de la sucursal de la inmobiliaria.
* **Gestión de Perfil Completa:** * Visualización detallada de la información del propietario logueado.
  * Edición y actualización de datos personales (Nombre, Apellido, DNI, Teléfono, Email) directamente hacia la base de datos de manera dinámica.
  * Control independiente para la modificación y cambio de contraseña por separado.

### 🧑‍💻 Tecnologías y Arquitectura:

* Patrón **MVVM** (Model-View-ViewModel)
* **ViewBinding** para una interacción segura con los elementos del layout
* **Retrofit + Gson** para el consumo eficiente de la API REST
* **Navigation Component** (Drawer Layout + Bottom Navigation)
* **Google Maps SDK** para la integración del mapa de ubicación 
* **SharedPreferences** para persistencia de token

---

### 📋 Próximas funcionalidades (Siguientes etapas):
* Listado dinámico de inmuebles del propietario logueado.
* Habilitar / Deshabilitar la disponibilidad de un inmueble específico desde la app.
* Carga de nuevos inmuebles al sistema.

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
