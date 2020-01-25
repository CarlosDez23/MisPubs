package com.example.mispubs.REST;

public class APIUtils {

    private static final String server = "192.168.1.89";
    private static final String port = "6969";
    private static final String API_URL = "http://"+server+":"+port+"/";

    public APIUtils() {
    }

    public static UsuarioRest getService(){
        return RetrofitClient.getClient(API_URL).create(UsuarioRest.class);
    }
}
