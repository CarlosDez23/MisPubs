package com.example.mispubs.ui.Pubs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.PubRest;
import com.example.mispubs.Utilidades.Util;
import com.example.mispubs.ui.Mapas.FragmentMapas;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDetallePubs extends Fragment {

    private View root;
    private Pub pub;
    private int modo;
    private final String
            eliminar = "Eliminar Pub",
            modificar = "Modificar Pub",
            guardar = "Guardar Pub";
    private final int
            modoNuevo = 0,
            modoVer = 1,
            modoModificar = 2,
            modoEliminar = 3;
    private Spinner spinnerEstilos;
    private ImageView ivDetallePubs;
    private ArrayList<String> listaEstilos = new ArrayList<String>();
    private TextInputLayout
            nombrePub,
            webPub,
            visitasPub;
    private FloatingActionButton cambiarFotoPub, mapaPub;
    private TextView tvDetallePubsModoBoton;
    private CardView cvDetallePubBotonModo;
    private RelativeLayout btnDetallePubBotonModo;
    private PubRest pubRest;
    private FusedLocationProviderClient mPosicion;
    private Location ultimaLocalizacion;
    private LatLng posicionPub;
    double latitude, longitude;

    //Para la gestión de imágenes
    private static final int GALERIA = 1;
    private static final int CAMARA = 2;


    public FragmentDetallePubs() {
        this.modo = 0;
    }

    public FragmentDetallePubs(Pub pub, int modo) {
        this.pub = pub;
        this.modo = modo;
    }

    private Pub getPub(){
        return this.pub;
    }

    public static FragmentDetallePubs newInstance() {
        return new FragmentDetallePubs();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_detalle_pubs_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pubRest = APIUtils.getServicePubs();

        llamarVistas();

        gestionarModo(this.modo);
    }

    /**
     * Metodo para cargar las vistas de nuestra interfaz
     */
    private void llamarVistas() {
        this.nombrePub = getView().findViewById(R.id.tvDetallePubsNombre);
        this.ivDetallePubs = getView().findViewById(R.id.ivDetallePubs);
        this.webPub = getView().findViewById(R.id.tvDetallePubsWeb);
        this.visitasPub = getView().findViewById(R.id.tvDetallePubsVisitas);
        this.cambiarFotoPub = getView().findViewById(R.id.fabDetallePubsCamara);
        this.cambiarFotoPub.setOnClickListener(listenerBotones);
        this.mapaPub = getView().findViewById(R.id.fabDetallePubsMapa);
        this.spinnerEstilos = getView().findViewById(R.id.spinnerDetallePubsEstilos);
        this.mapaPub = getView().findViewById(R.id.fabDetallePubsMapa);
        this.mapaPub.hide();
        this.mapaPub.setOnClickListener(listenerBotones);
        this.tvDetallePubsModoBoton = getView().findViewById(R.id.tvDetallePubsModoBoton);
        this.cvDetallePubBotonModo = getView().findViewById(R.id.cvDetallePubBotonModo);
        this.btnDetallePubBotonModo = getView().findViewById(R.id.btnDetallePubBotonModo);
        this.btnDetallePubBotonModo.setOnClickListener(listenerBotones);  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.estilos, android.R.layout.simple_spinner_dropdown_item);
        spinnerEstilos.setAdapter(adapter);




    }

    /**
     * Metodo para controlar el onClick de los elementos de nuestra vista
     */
    private View.OnClickListener listenerBotones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.fabDetallePubsMapa:
                    posicionPub = new LatLng(getPub().getLatitud(),getPub().getLongitud());
                    FragmentMapas mapa = new FragmentMapas(posicionPub,getPub());
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, mapa);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case R.id.btnDetallePubBotonModo:
                    gestionarModoBoton(tvDetallePubsModoBoton.getText().toString());
                    break;
                case R.id.fabDetallePubsCamara:
                    gestionImagen();
                    break;
                default:
                    break;
            }
        }
    };

    private void gestionImagen() {
        AlertDialog.Builder fotoDialogo = new AlertDialog.Builder(getContext());
        fotoDialogo.setTitle("Elige un método de entrada");
        String[] fotoDialogoItems = {
                "Seleccionar fotografía de galería",
                "Capturar fotografía desde la cámara"};
        fotoDialogo.setItems(fotoDialogoItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                elegirFotoGaleria();
                                break;
                            case 1:
                                tomarFotoCamara();
                                break;
                        }
                    }
                });
        fotoDialogo.show();
    }

    private void elegirFotoGaleria(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALERIA);
    }


    private void tomarFotoCamara(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMARA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERIA) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    this.ivDetallePubs.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Fallo en la galería", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == CAMARA) {
            Bitmap thumbnail = null;
            try {
                thumbnail = (Bitmap) data.getExtras().get("data");
                this.ivDetallePubs.setImageBitmap(thumbnail);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Fallo en la cámara", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Metodo para rellenar los campos segun el pub selecionado
     */
    private void rellenarCampos() {
        this.nombrePub.getEditText().setText(this.pub.getNombre());
        this.webPub.getEditText().setText(this.pub.getWeb());
        this.visitasPub.getEditText().setText(Integer.toString(this.pub.getVisitas()));
        if (this.pub.getImagen() != null){
            this.ivDetallePubs.setImageBitmap(Util.base64ToBitmap(this.pub.getImagen()));
        }else{
            this.ivDetallePubs.setImageResource(R.drawable.fondo_por_defecto);
        }



        seleccionarEstiloPub();
    }

    /**
     * Gestion del modo en el que entramos al fragmen
     *
     * @param modo
     */
    private void gestionarModo(int modo) {

        switch (modo) {
            case modoNuevo:
                obtenerPosicionActualMapa();
                habilitarDeshabilitar(true);
                this.tvDetallePubsModoBoton.setText(guardar);
                break;
            case modoVer:
                comprobarImagen();
                habilitarDeshabilitar(false);
                rellenarCampos();
                this.mapaPub.show();
                this.cvDetallePubBotonModo.setVisibility(View.INVISIBLE);
                break;
            case modoModificar:
                comprobarImagen();
                habilitarDeshabilitar(true);
                rellenarCampos();
                this.tvDetallePubsModoBoton.setText(modificar);
                break;
            case modoEliminar:
                comprobarImagen();
                habilitarDeshabilitar(false);
                rellenarCampos();
                this.tvDetallePubsModoBoton.setText(eliminar);
                break;
            default:
                break;
        }
    }

    private void comprobarImagen(){
        //Para la gestión de imágenes
        if (pub.getImagen() != null){
            Bitmap bitmap = Util.base64ToBitmap(pub.getImagen());
            this.ivDetallePubs.setImageBitmap(bitmap);

        }else {
            this.ivDetallePubs.setImageResource(R.drawable.fondo_por_defecto);
        }
    }

    /**
     * Gestion del modo que adoptara el boton segun el modo en el que se entro al fragment
     *
     * @param modo
     */
    private void gestionarModoBoton(String modo) {

        switch (modo) {
            case guardar:
                if (comprobarCampos()) {

                    Bitmap img = ((BitmapDrawable)ivDetallePubs.getDrawable()).getBitmap();
                    Bitmap comprimido = Util.comprimirImagen(img);
                    String convertida = Util.bitmapToBase64(comprimido);


                    Pub insertarPub = new Pub(
                            nombrePub.getEditText().getText().toString(),
                            latitude,
                            longitude,
                            spinnerEstilos.getSelectedItem().toString(),
                            Integer.parseInt(visitasPub.getEditText().getText().toString()),
                            webPub.getEditText().getText().toString(),
                            convertida
                    );
                    if (ultimaLocalizacion != null){
                        guardarPub(insertarPub);
                    }else{
                        Toast.makeText(getContext(), "Debes activar la localización para" +
                                " añadir un pub", Toast.LENGTH_LONG).show();
                        obtenerPosicionActualMapa();
                    }

                }

                break;
            case modificar:

                Bitmap img = ((BitmapDrawable)ivDetallePubs.getDrawable()).getBitmap();
                Bitmap comprimido = Util.comprimirImagen(img);
                String convertida = Util.bitmapToBase64(comprimido);

                if (comprobarCampos()) {
                    String
                            nombre = nombrePub.getEditText().getText().toString(),
                            web = webPub.getEditText().getText().toString(),
                            estilo = spinnerEstilos.getSelectedItem().toString();
                    int visitas = Integer.parseInt(visitasPub.getEditText().getText().toString());

                    Pub p = new Pub(nombre, 0.0, 0.0, estilo, visitas, web, convertida);
                    p.setId(pub.getId());

                    modificarPub(p);
                }

                break;
            case eliminar:
                Toast.makeText(getContext(), "eliminar", Toast.LENGTH_LONG).show();
                mostrarDialogoEliminar();
                break;
            default:
                break;
        }

    }

    /**
     * Metodo para mostrar un dialogo para estar seguro de que se desea eliminar el pub
     */
    private void mostrarDialogoEliminar(){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
        deleteDialog.setTitle("¿Estás seguro de querer eliminar el"+pub.getNombre()+"?");
        String[] deleteDialogitems = {"Sí", "No"};
        deleteDialog.setItems(deleteDialogitems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        eliminarPub();
                        break;
                    case 1:
                        Toast.makeText(getContext(), "Pub no eliminada",
                                Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });
        deleteDialog.show();
    }

    /**
     * Metodo para comprobar que los campos no se quedan vacios al guardar o modificar
     * @return
     */
    private boolean comprobarCampos() {
        boolean
                resultado = false,
                nombre = false,
                web = false,
                estilo = false,
                visitas = false;

        if (!nombrePub.getEditText().getText().toString().isEmpty()) {
            nombre = true;
        }
        if (!webPub.getEditText().getText().toString().isEmpty()) {
            web = true;
        }
        if (!spinnerEstilos.getSelectedItem().toString().isEmpty()) {
            estilo = true;
        }
        if (!visitasPub.getEditText().getText().toString().isEmpty()) {
            visitas = true;
        }
        if (nombre && web && estilo && visitas) {
            resultado = true;
        } else {
            Toast.makeText(getContext(), "Debes Rellenar todos los campos", Toast.LENGTH_LONG).show();
        }
        return resultado;
    }


    /**
     * Metodo para habilitar o deshabilitar los elementos de la interfaz
     * segun el modo en el que accedemos al fragment
     *
     * @param accion
     */
    private void habilitarDeshabilitar(boolean accion) {
        this.nombrePub.setEnabled(accion);
        this.webPub.setEnabled(accion);
        this.visitasPub.setEnabled(accion);
        this.spinnerEstilos.setEnabled(accion);
        if (accion == true) {
            this.cambiarFotoPub.show();
        } else {
            this.cambiarFotoPub.hide();
        }
    }


    /**
     * Metodo para seleccionar el estilo del pub en el spinner segun el pub selecionado
     */
    public void seleccionarEstiloPub() {
        switch (this.pub.getEstilo()) {
            case "Rock":
                spinnerEstilos.setSelection(0);
                break;
            case "Reggaeton":
                spinnerEstilos.setSelection(1);
                break;
            case "Clasica":
                spinnerEstilos.setSelection(2);
                break;
            case "Electronica":
                spinnerEstilos.setSelection(3);
                break;
            case "Indie":
                spinnerEstilos.setSelection(4);
                break;
            case "Pop":
                spinnerEstilos.setSelection(5);
                break;
            default:
                break;
        }
    }

    /**
     * Metodo para insertar un pub en la base de datos utilizando el servicio REST. Primero comprobamos
     * que no exista para asi poder guardarlo
     *
     * @param guardarPub
     */
    private void guardarPub(Pub guardarPub) {
        Call<Pub> call = pubRest.buscarPorNombreDelPub(guardarPub.getNombre());
        call.enqueue(new Callback<Pub>() {
            @Override
            public void onResponse(Call<Pub> call, Response<Pub> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 204) {

                        Call<Pub> callInsert = pubRest.insertarPub(guardarPub);
                        callInsert.enqueue(new Callback<Pub>() {
                            @Override
                            public void onResponse(Call<Pub> call, Response<Pub> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(), "Pub registrado",
                                            Toast.LENGTH_LONG).show();

                                    FragmentPubs pubs = new FragmentPubs();
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction transaction = fm.beginTransaction();
                                    transaction.replace(R.id.nav_host_fragment, pubs);
                                    transaction.addToBackStack(null);
                                    transaction.commit();

                                }
                            }

                            @Override
                            public void onFailure(Call<Pub> call, Throwable t) {
                                Log.e("ERROR: ", t.getMessage());
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "El Pub ya existe",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Pub> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    /**
     * Metodo para modificar un pub selecionado utilizando el servicio REST
     * @param modificarPub
     */
    private void modificarPub(Pub modificarPub) {

        Call<Pub> call = pubRest.modificarPub(modificarPub.getId(), modificarPub);
        call.enqueue(new Callback<Pub>() {
            @Override
            public void onResponse(Call<Pub> call, Response<Pub> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Pub actualizado",
                            Toast.LENGTH_LONG).show();

                    FragmentPubs pubs = new FragmentPubs();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, pubs);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<Pub> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    /**
     * Metodo para eliminar el pub selecionado utilizando el servicio REST
     */
    private void eliminarPub() {
        Call<Pub> call = pubRest.eliminarPub(this.pub.getId());
        call.enqueue(new Callback<Pub>() {
            @Override
            public void onResponse(Call<Pub> call, Response<Pub> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), response.body().getNombre() + "Eliminado",
                                Toast.LENGTH_LONG).show();

                        FragmentPubs pubs = new FragmentPubs();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, pubs);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<Pub> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }


    private void obtenerPosicionActualMapa() {
        try {
            mPosicion = LocationServices.getFusedLocationProviderClient(getActivity());
            Task<Location> local = mPosicion.getLastLocation();
            local.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        ultimaLocalizacion = task.getResult();
                        if (ultimaLocalizacion != null) {
                            latitude = ultimaLocalizacion.getLatitude();
                            longitude = ultimaLocalizacion.getLongitude();

                        } else {
                            Toast.makeText(getContext(), "AVISO: Tienes que activar la localización para añadir un Pub",
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Log.d("GPS", "No se encuetra la última posición.");
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}