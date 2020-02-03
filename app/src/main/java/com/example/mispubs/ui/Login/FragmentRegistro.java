package com.example.mispubs.ui.Login;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.UsuarioRest;
import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.Utilidades.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentRegistro extends Fragment {

    //Elementos de la interfaz
    private EditText email_input, password_input, usuario_input;
    private TextView email_error, password_error, usuario_error;
    private CardView min8caraacteres, una_mayus, un_numero, emailok;
    private RelativeLayout btnRegistro;
    private CardView btnRegistroParent;

    //Para validar el formulario
    private boolean
            tieneCaracteres = false,
            tieneMayus = false,
            tieneNumero = false,
            emailCorrecto = false,
            esApto = false;

    //Para consumir el servicio REST
    private UsuarioRest usuarioRest;


    public static FragmentRegistro newInstance() {
        return new FragmentRegistro();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registro_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        llamarVistas();
        inputChange();
        setOnClickRegistrarse();
        usuarioRest = APIUtils.getService();

    }

    private void llamarVistas() {

        email_input = getView().findViewById(R.id.email_input_field);
        email_error = getView().findViewById(R.id.email_error_text);
        password_error = getView().findViewById(R.id.password_error_text);
        password_input = getView().findViewById(R.id.password_input_field);
        usuario_input = getView().findViewById(R.id.usuario_input_field);
        usuario_error = getView().findViewById(R.id.usuario_error_text);
        min8caraacteres = getView().findViewById(R.id.p_item_1_icon_parent);
        una_mayus = getView().findViewById(R.id.p_item_2_icon_parent);
        un_numero = getView().findViewById(R.id.p_item_3_icon_parent);
        emailok = getView().findViewById(R.id.p_item_4_icon_parent);
        btnRegistro = getView().findViewById(R.id.registration_button);
        btnRegistroParent = getView().findViewById(R.id.registration_button_parent);

    }

    private void checkEmpty(String email, String password, String usuario) {
        if (password.length() > 0 && password_error.getVisibility() == View.VISIBLE) {
            password_error.setVisibility(View.GONE);
        }
        if (email.length() > 0 && email_error.getVisibility() == View.VISIBLE) {
            email_error.setVisibility(View.GONE);
        }
        if (usuario.length() > 0 && usuario_error.getVisibility() == View.VISIBLE) {
            usuario_error.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ResourceType")
    private void checkAllData(String email) {
        if (tieneCaracteres && tieneMayus && tieneNumero && emailCorrecto && email.length() > 0) {
            esApto = true;
            btnRegistroParent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            esApto = false;
            btnRegistroParent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }
    }

    @SuppressLint("ResourceType")
    private void registrationDataCheck() {
        String password = password_input.getText().toString(),
                email = email_input.getText().toString(),
                usuario = usuario_input.getText().toString();

        checkEmpty(email, password, usuario);

        if (password.length() >= 8) {
            tieneCaracteres = true;
            min8caraacteres.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            tieneCaracteres = false;
            min8caraacteres.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }
        if (password.matches("(.*[A-Z].*)")) {
            tieneMayus = true;
            una_mayus.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            tieneMayus = false;
            una_mayus.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }
        if (password.matches("(.*[0-9].*)")) {
            tieneNumero = true;
            un_numero.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            tieneNumero = false;
            un_numero.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }

        if (email.matches("^(.+)@(.+)$")) {
            emailCorrecto = true;
            emailok.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            emailCorrecto = false;
            emailok.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }

        checkAllData(email);
    }

    private void inputChange() {
        email_input.addTextChangedListener(new TextWatcher() {
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

        password_input.addTextChangedListener(new TextWatcher() {
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

    private void setOnClickRegistrarse() {
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_input.getText().toString(),
                        password = password_input.getText().toString(),
                        usuario = usuario_input.getText().toString();

                byte[] datos = usuario.getBytes();
                String cifrada = Util.resumirPassword(datos);

                if (email.length() > 0 && password.length() > 0 && usuario.length() > 0) {
                    if (esApto) {
                        Usuario u = new Usuario(usuario, email, cifrada, null);
                        if (Util.isOnline(getContext())){
                            insertarUsuario(u);

                        }else{
                            Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(getContext(), "Se deben cumplir todos los requisitos",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (email.length() == 0) {
                        email_error.setVisibility(View.VISIBLE);
                    }
                    if (password.length() == 0) {
                        password_error.setVisibility(View.VISIBLE);
                    }
                    if (usuario.length() == 0) {
                        usuario_error.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * Método para insertar un usuario utilizando el servicio REST. Primero comprobamos
     * que no existe un usuario con ese nombre de usuario en la BBDD. Si no existe insertamos
     */

    private void insertarUsuario(Usuario usuario) {
        Call<Usuario> call = usuarioRest.buscarPorNombreUsuario(usuario.getNombre());
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), "El usuario ya existe",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Call<Usuario> callInsert = usuarioRest.insertarUsuario(usuario);
                        callInsert.enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                                if (response.code() == 200) {
                                    Toast.makeText(getContext(), "Usuario registrado",
                                            Toast.LENGTH_LONG).show();

                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.fragment, new FragmentLogin()).commit();

                                } else {
                                    Toast.makeText(getContext(),
                                            "Ya existe un usuario con ese correo",
                                             Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {
                                Log.e("ERROR: ", t.getMessage());
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getContext(), "Mal", Toast.LENGTH_SHORT).show();
                Log.e("ERROR: ", t.getMessage());
            }
        });

    }
}
