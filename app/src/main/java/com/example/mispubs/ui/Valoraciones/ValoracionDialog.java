package com.example.mispubs.ui.Valoraciones;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mispubs.MainActivity;
import com.example.mispubs.Modelo.Valoracion;
import com.example.mispubs.R;
import com.example.mispubs.REST.ValoracionRest;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValoracionDialog extends AppCompatDialogFragment {

    //Elementos de la interfaz
    private TextInputLayout tvValoracion;
    private RatingBar rbValoracion;

    //Vista
    private View root;

    //Id del pub valorado (la id del usuario la cojemos del main activity)
    int idPub;

    //Para el REST
    ValoracionRest valoracionRest;

    public ValoracionDialog(int idPub, ValoracionRest valoracionRest) {
        this.idPub = idPub;
        this.valoracionRest = valoracionRest;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        root = inflater.inflate(R.layout.dialog_realizarvaloracion, null);

        builder.setView(root)
                .setTitle("Valoración")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (rbValoracion.getRating() == 0.0) {
                            Toast.makeText(getContext(), "Tienes que valorar el pub",
                                    Toast.LENGTH_LONG).show();
                        } else if (tvValoracion.getEditText().getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Tienes que realizar un comentario",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Valoracion v = new Valoracion(MainActivity.getUsuario().getId(),
                                    idPub, (int) rbValoracion.getRating(),
                                    tvValoracion.getEditText().getText().toString());
                            realizarValoracion(v);
                            Toast.makeText(root.getContext(), "Valoración realizada",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

        tvValoracion = root.findViewById(R.id.tvValoracionValoracion);
        rbValoracion = root.findViewById(R.id.rbValoracionDialog);
        return builder.create();
    }

    private void realizarValoracion(Valoracion valoracion) {
        Call<Valoracion> call = valoracionRest.insertarValoracion(valoracion);
        call.enqueue(new Callback<Valoracion>() {
            @Override
            public void onResponse(Call<Valoracion> call, Response<Valoracion> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<Valoracion> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}
