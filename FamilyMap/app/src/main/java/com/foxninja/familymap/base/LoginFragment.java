package com.foxninja.familymap.base;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foxninja.familymap.R;
import com.foxninja.familymap.model.LoginData;
import com.foxninja.familymap.web.HttpRequest;

/**
 * The fragment used to login.
 */
public class LoginFragment extends Fragment {
    private LoginData loginData;
    private EditText usernameField;
    private EditText passwordField;
    private EditText hostnameField;
    private EditText portField;
    private Button signInBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginData = new LoginData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, parent, false);

        usernameField = (EditText) v.findViewById(R.id.username);
        passwordField = (EditText) v.findViewById(R.id.password);
        hostnameField = (EditText) v.findViewById(R.id.hostname);
        portField = (EditText) v.findViewById(R.id.port);
        signInBtn = (Button) v.findViewById(R.id.signin);

        usernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Left blank on purpose
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginData.setUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Also left blank on purpose
            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginData.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        hostnameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginData.setHostname(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        portField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginData.setPort(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Check to make sure every field is filled in
                if (isEmpty(usernameField)) {
                    Toast.makeText(
                            getActivity(),
                            "Please type in a username.",
                            Toast.LENGTH_LONG
                    ).show();
                } else if (isEmpty(passwordField)) {
                    Toast.makeText(
                            getActivity(),
                            "Please type in a password.",
                            Toast.LENGTH_LONG
                    ).show();
                } else if (isEmpty(hostnameField)) {
                    Toast.makeText(
                            getActivity(),
                            "Please type in the server host.",
                            Toast.LENGTH_LONG
                    ).show();
                } else if (isEmpty(portField)) {
                    Toast.makeText(
                            getActivity(),
                            "Please type in the server port.",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    // Login the user
                    HttpRequest request = new HttpRequest(
                            getActivity(),
                            hostnameField.getText().toString(),
                            portField.getText().toString()
                    );
                    request.login(
                            usernameField.getText().toString(),
                            passwordField.getText().toString()
                    );
                }
            }
        });

        return v;
    }

    public boolean isEmpty(EditText et) {
        return et.getText().toString().trim().length() == 0;
    }
}
