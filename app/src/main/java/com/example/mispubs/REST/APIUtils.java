package com.example.mispubs.REST;

public class APIUtils {

    private static final String server = "80.102.104.105";
    private static final String port = "6969";
    private static final String API_URL = "http://"+server+":"+port+"/";

    public APIUtils() {
    }

    public static UsuarioRest getService(){
        return RetrofitClient.getClient(API_URL).create(UsuarioRest.class);
    }

    public static PubRest getServicePubs(){
        return RetrofitClient.getClient(API_URL).create(PubRest.class);
    }

    public static ValoracionRest getServiceValoraciones(){
        return RetrofitClient.getClient(API_URL).create(ValoracionRest.class);
    }
}
