package com.example.mispubs.Utilidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Util {

    /**
     * Método para convertir una cadena en base64 a Bitmap
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

}