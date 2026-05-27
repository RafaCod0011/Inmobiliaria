package com.universidad.inmobiliaria.request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.universidad.inmobiliaria.modelo.Contrato;
import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.modelo.Pago;
import com.universidad.inmobiliaria.modelo.Propietario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClient {

    public final static String BASE_URL = "https://capacitacion.alwaysdata.net/";
    public static void borrarToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        sp.edit().clear().apply();   // Borra todo el archivo de token
    }

    public static MiServicioInmobiliaria getServicio(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(MiServicioInmobiliaria.class);
    }

    public interface MiServicioInmobiliaria{

        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

        //Obtener al Propietario que esta usando la app
        @GET("api/Propietarios")
        Call<Propietario> getPropietario(@Header("Authorization") String token);

        //Actualizar Perfil
        @PUT("api/Propietarios/actualizar")
        Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);


        //Obtener Inmuebles
        @GET("api/Inmuebles")
        Call<List<Inmueble>> getInmuebles(@Header("Authorization") String token);


        //Cambiar Contraseña
        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<Void> cambiarPassword(@Header("Authorization") String token,
                                   @Field("currentPassword") String currentPassword,
                                   @Field("newPassword") String newPassword
        );

        // Resetear Contraseña
        @FormUrlEncoded
        @POST("api/Propietarios/email")
        Call<String> resetearPass(@Field("email") String email);

        //Actualizar Inmueble
        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> cambiarDisponibilidad(@Header("Authorization") String token, @Body Inmueble inmueble);

        //Cargar Inmueble
        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<Inmueble> cargarInmueble(@Header("Authorization") String token,
                                      @Part MultipartBody.Part imagen,
                                      @Part("inmueble") RequestBody inmuebleBody);

        //Obtener Inmuebles Alquilados
        @GET("/api/Inmuebles/GetContratoVigente")
        Call<List<Inmueble>> getInmueblesAlquilados(@Header("Authorization") String token);

        // Obtener Contrato vigente por ID de Inmueble
        @GET("api/contratos/inmueble/{id}")
        Call<Contrato> getContratoPorInmueble(@Header("Authorization") String token, @Path("id") int idInmueble);

        // Obtener pagos por contrato
        @GET("api/pagos/contrato/{id}")
        Call<List<Pago>> obtenerPagosPorContrato(
                @Header("Authorization") String authorization,
                @Path("id") int idContrato
        );
    }

    public static void crearToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", "Bearer " + token);
        editor.apply();
    }

    public static String usarToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }
    // ==================== MANEJO CENTRALIZADO DE AUTORIZACIÓN ====================

    /**
     * Gestiona errores de autenticación devueltos por la API.
     * - 401: Unauthorized → Token ausente, inválido o expirado.
     * - 403: Forbidden   → Token válido pero sin permisos.
     * Borra el token y redirige al LoginActivity.
     * @return true si se manejó el error de autorización
     */
    public static boolean manejarErrorAutorizacion(Context context, int codigoError) {
        if (codigoError == 401 || codigoError == 403) {

            // Borrar token
            borrarToken(context);

            // Redirigir al Login limpiando toda la pila
            Intent intent = new Intent(context, com.universidad.inmobiliaria.ui.login.LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

            return true; // Se manejó el error
        }
        return false;
    }

}
