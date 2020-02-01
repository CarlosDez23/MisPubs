package com.example.mispubs.ui.Mapas;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


public class FragmentMapas extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient mPosicion;
    private Location ultimaLocalizacion;
    private LatLng posicionActual;
    private Marker marcador;
    private LatLng posicionPub;
    private Pub pub;

    public FragmentMapas(LatLng posPub, Pub pubMapa) {
        this.posicionPub = posPub;
        this.pub = pubMapa;
    }

    public FragmentMapas() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fraagment_maps_mis_pubs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //pintamos el mapa
        mPosicion = LocationServices.getFusedLocationProviderClient(getActivity());
        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        //Ajustamos el mapa y obtenemos nuestra posicion
        map.setMinZoomPreference(17.0f);
        map.setOnMarkerClickListener(this);
        map.setMyLocationEnabled(true);
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setMapToolbarEnabled(true);
        obtenerPosicion();
        addMarcador(this.posicionPub, this.pub);
    }

    /**
     * Obtenemos nuestra posicion
     */
    public void obtenerPosicion() {
        try {

            Task<Location> local = mPosicion.getLastLocation();
            local.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        ultimaLocalizacion = task.getResult();
                        if (ultimaLocalizacion != null) {
                            posicionActual = new LatLng(ultimaLocalizacion.getLatitude(),ultimaLocalizacion.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLng(posicionActual));
                        } else {
                            Toast.makeText(getContext(), "No puede obternerse la situacion actual",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("GPS", "No se encuetra la última posición.");
                        Log.e("GPS", "Exception: %s", task.getException());
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    public void addMarcador(LatLng pos, Pub pub) {
        marcador = map.addMarker(new MarkerOptions()
                .position(pos)
                .title(pub.getNombre())
                .snippet(pub.getEstilo())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador))//defaultMarker(BitmapDescriptorFactory.HUE_VIOLET
        );
    }
}
