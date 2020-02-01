package com.example.mispubs.ui.Valoraciones;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.mispubs.R;
import com.example.mispubs.Utilidades.Util;
import com.google.android.material.textfield.TextInputLayout;

public class ValoracionDetalleDialog extends AppCompatDialogFragment {

    //Elementos de la interfaz
    private TextView tvValoracionUsuario;
    private TextInputLayout tvValoracionDetalle;
    private RatingBar rbValoracionDetalle;
    private ImageView ivUsuario;


    //Datos que necesitamos
    private String nombreUsuario;
    private float valoracion;
    private String texto;
    private String img;


    public ValoracionDetalleDialog(String nombreUsuario, float valoracion, String texto, String img) {
        this.nombreUsuario = nombreUsuario;
        this.valoracion = valoracion;
        this.texto = texto;
        this.img = img;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_detallevaloracion, null);

        builder.setView(view)
                .setTitle("Detalle")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        rbValoracionDetalle = view.findViewById(R.id.rbValoracionDetalle);
        tvValoracionUsuario = view.findViewById(R.id.tvValoracionDetalleNombreUsuario);
        tvValoracionDetalle = view.findViewById(R.id.tvCadena);
        ivUsuario = view.findViewById(R.id.ivImagenUsuario);
        //Metemos los valores
        rbValoracionDetalle.setRating(valoracion);
        rbValoracionDetalle.setIsIndicator(true);
        tvValoracionUsuario.setText(nombreUsuario);
        tvValoracionDetalle.getEditText().setText(texto);
        ivUsuario.setImageBitmap(Util.base64ToBitmap(img));

        return builder.create();
    }
}
