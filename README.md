# Inmobiliaria La Punta - App para Propietarios

## 🔹 Primera Entrega

Aplicación Android para propietarios de inmuebles desarrollada en Java con Android Studio.

### ✅ Funcionalidades implementadas:

- **Login** con Retrofit + autenticación JWT
- Guardado seguro del token en SharedPreferences
- Menú navegable (Drawer + Bottom Navigation)
- Visualización de datos del perfil del propietario
- Logout con limpieza de token
- Protección de rutas (no se puede acceder sin login)

### 🧑‍💻 Tecnologías y Arquitectura:

- Patrón **MVVM**
- **ViewBinding**
- **Retrofit + Gson** para consumo de API
- **Navigation Component**
- SharedPreferences para persistencia de token

### 📋 Próximas entregas:
- Edición de perfil y cambio de contraseña
- Gestión de inmuebles
- Listado de contratos y pagos

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
