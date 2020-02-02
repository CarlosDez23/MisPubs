package com.example.mispubs.ui.Perfil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;

import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.UsuarioRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActualizarDialog extends AppCompatDialogFragment {

    private Usuario u;
    private FragmentPerfil fragmentPerfil;
    //Elementos de la interfaz
    private EditText email_input_dialog, password_input_dialog, usuario_input_dialog;
    private TextView email_error_dialog, password_error_dialog, usuario_error_dialog;
    private CardView min8caraacteres_dialog, una_mayus_dialog, un_numero_dialog, emailok_dialog;

    //Vista
    private View root;


    //Para validar el formulario
    private boolean
            tieneCaracteres = false,
            tieneMayus = false,
            tieneNumero = false,
            emailCorrecto = false,
            esApto = false;


    private Usuario user;

    public ActualizarDialog(FragmentPerfil perfil, Usuario usuario) {
        this.fragmentPerfil = perfil;
        this.user = usuario;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        root = inflater.inflate(R.layout.dialog_actualizar, null);

        builder.setView(root)
                .setTitle("Actualizar Usuario")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentPerfil.crearActualizarUsuario(user.getNombre(), user.getCorreo(), user.getPassword());
                    }
                })
                .setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String email = email_input_dialog.getText().toString(),
                                password = password_input_dialog.getText().toString(),
                                usuario = usuario_input_dialog.getText().toString();

                        if (email.length() > 0 && password.length() > 0 && usuario.length() > 0) {
                            if (esApto) {
                                u = new Usuario(usuario, email, password, "imagen");
                                if (u != null){
                                    fragmentPerfil.crearActualizarUsuario(u.getNombre(),u.getCorreo(),u.getPassword());
                                }
                            } else {
                                Toast.makeText(getContext(), "Se deben cumplir todos los requisitos",
                                        Toast.LENGTH_LONG).show();
                                fragmentPerfil.crearActualizarUsuario(user.getNombre(), user.getCorreo(), user.getPassword());

                            }
                        } else {
                            Toast.makeText(getContext(), "Tienes que rellenar todos los campos",
                                    Toast.LENGTH_LONG).show();
                            fragmentPerfil.crearActualizarUsuario(user.getNombre(), user.getCorreo(), user.getPassword());
                        }
                    }
                });

        llamarVistas();
        inputChange();

        return builder.create();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        fragmentPerfil.crearActualizarUsuario(user.getNombre(), user.getCorreo(), user.getPassword());
    }


    private void llamarVistas() {

        email_input_dialog = root.findViewById(R.id.email_input_field_dialog);
        email_error_dialog = root.findViewById(R.id.email_error_text_dialog);
        password_error_dialog = root.findViewById(R.id.password_error_text_dialog);
        password_input_dialog = root.findViewById(R.id.password_input_field_dialog);
        usuario_input_dialog = root.findViewById(R.id.usuario_input_field_dialog);
        usuario_error_dialog = root.findViewById(R.id.usuario_error_text_dialog);
        min8caraacteres_dialog = root.findViewById(R.id.p_item_1_icon_parent_dialog);
        una_mayus_dialog = root.findViewById(R.id.p_item_2_icon_parent_dialog);
        un_numero_dialog = root.findViewById(R.id.p_item_3_icon_parent_dialog);
        emailok_dialog = root.findViewById(R.id.p_item_4_icon_parent_dialog);

    }

    private void inputChange() {
        email_input_dialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password_input_dialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @SuppressLint("ResourceType")
    private void registrationDataCheck() {
        String password = password_input_dialog.getText().toString(),
                email = email_input_dialog.getText().toString(),
                usuario = usuario_input_dialog.getText().toString();

        checkEmpty(email, password, usuario);

        if (password.length() >= 8) {
            tieneCaracteres = true;
            min8caraacteres_dialog.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            tieneCaracteres = false;
            min8caraacteres_dialog.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }
        if (password.matches("(.*[A-Z].*)")) {
            tieneMayus = true;
            una_mayus_dialog.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            tieneMayus = false;
            una_mayus_dialog.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }
        if (password.matches("(.*[0-9].*)")) {
            tieneNumero = true;
            un_numero_dialog.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            tieneNumero = false;
            un_numero_dialog.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }

        if (email.matches("^(.+)@(.+)$")) {
            emailCorrecto = true;
            emailok_dialog.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            emailCorrecto = false;
            emailok_dialog.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }

        checkAllData(email);
    }

    private void checkEmpty(String email, String password, String usuario) {
        if (password.length() > 0 && password_error_dialog.getVisibility() == View.VISIBLE) {
            password_error_dialog.setVisibility(View.GONE);
        }
        if (email.length() > 0 && email_error_dialog.getVisibility() == View.VISIBLE) {
            email_error_dialog.setVisibility(View.GONE);
        }
        if (usuario.length() > 0 && usuario_error_dialog.getVisibility() == View.VISIBLE) {
            usuario_error_dialog.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ResourceType")
    private void checkAllData(String email) {
        if (tieneCaracteres && tieneMayus && tieneNumero && emailCorrecto && email.length() > 0) {
            esApto = true;
            //btnRegistroParent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            esApto = false;
            //btnRegistroParent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }
    }

}
