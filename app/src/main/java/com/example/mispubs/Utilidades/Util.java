package com.example.mispubs.Utilidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

    /**
     * Método para convertir una cadena en base64 a Bitmap
     *
     * @param b64String cadena Base 64
     * @return Bitmap
     */
    public static Bitmap base64ToBitmap(String b64String) {
        byte[] imageAsBytes = Base64.decode(b64String.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    /**
     * Método para convertir una imagen en Base64
     *
     * @param bitmap Bitmap
     * @return Cadena Base74
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    /**
     * Método para comprimir una imagen antes de transformarla a Base64
     *
     * @param myBitmap
     * @return
     */
    public static Bitmap comprimirImagen(Bitmap myBitmap) {
        Bitmap bitmap = null;
        try {
            float porcentaje = 360 / (float) myBitmap.getWidth();
            bitmap = Bitmap.createScaledBitmap(myBitmap, 360, (int) (myBitmap.getHeight() * porcentaje), false);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bitmap;
    }

    /**
     * Para resumir la contraseña y devolverla como una string en Hexadecimal
     *
     * @param datos
     * @return
     */
    public static String resumirPassword(byte[] datos) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(datos);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(Hexadecimal(md.digest()));

        return Hexadecimal(md.digest());
    }

    /**
     * Método para convertir en hexadecimal un array de bytes con el resumen de una
     * cadena
     * @param resumen
     * @return
     */
    private static String Hexadecimal(byte[] resumen) {
        String hex = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1) hex += 0;
            hex += h;
        }
        return hex;
    }

    /**
     * Método para comprobar que el dispositivo tiene conexión a internet
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    /**
     * Método que recibe una cadena con una fecha (en la BBDD las guardamos como cadenas)
     * y la convierte a un objeto Date, con el que podemos operar para saber la diferencia
     * de días y así poder validar el token de la sesión
     * @param fecha
     * @return
     */
    public static Date parseFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return fechaDate;
    }

    /**
     * Para generar el token del usuario
     * @param nombreUsuario
     * @return
     */
    public static String generarToken(String nombreUsuario){
        String cadena = nombreUsuario+"mispubs";
        byte[]datos = cadena.getBytes();
        String token = resumirPassword(datos);
        return token;
    }

    /**
     * Devuelve las fechas de inicio y fin del token
     * @return
     */

    public static String[] gestionFechas(){
        String[] fechas = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaComoCadena = sdf.format(new Date());
        fechas[0] = fechaComoCadena;
        fechas[1] = fechaLimite();
        return fechas;
    }

    /**
     * Generamos la fecha de caducidad sumándole 7 días a la de inicio
     * @return
     */
    private static String fechaLimite(){
        String fechaLimite;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fechaLimite= sdf.format(calendar.getTime());
        return fechaLimite;
    }
}
