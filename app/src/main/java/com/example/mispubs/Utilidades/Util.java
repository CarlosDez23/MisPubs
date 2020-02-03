package com.example.mispubs.Utilidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    /**
     * Método para comprimir una imagen antes de transformarla a Base64
     * @param myBitmap
     * @return
     */
    public static Bitmap comprimirImagen(Bitmap myBitmap) {
        Bitmap bitmap = null;
        try{
            float porcentaje = 360 / (float) myBitmap.getWidth();
            bitmap= Bitmap.createScaledBitmap(myBitmap, 360, (int) (myBitmap.getHeight()*porcentaje), false);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return bitmap;
    }


    public static String resumirPassword (byte[] datos)  {
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

    private static String Hexadecimal(byte []resumen){
        String hex="";
        for (int i=0;i<resumen.length;i++){
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1) hex+=0;
            hex+=h;
        }
        return hex;
    }
}
