package com.myapp.myapplicationgit;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SingUp_Tap_Fragment extends Fragment {

    EditText nName,nEmail,nPassword,cPassword;
    Button create;
    ProgressBar bar;

    private String name = "";
    private String email = "";
    private String password = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    View objectSingUpFragment;


    public SingUp_Tap_Fragment() {
        // Required empty public constructor
    }

    public void createUser(){
        try {
            if (!validateForm()) {
                return;
            }

            bar.setVisibility(View.VISIBLE);
            name = nName.getText().toString();
            email = nEmail.getText().toString();
            password = nPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Map<String, Object> map = new HashMap<>();
                        map.put("Username", name);
                        map.put("email", email);
                        map.put("password", password);
                        map.put("foto", null);

                        String id = mAuth.getCurrentUser().getUid();

                        mDatabase.child("User").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if(task2.isSuccessful()){
                                    startActivity(new Intent(getActivity().getApplicationContext(), NavigationActivity.class) );
                                    getActivity().finish();
                                }
                                else {
                                    Toast.makeText(getContext(), "No se creo el usuario", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    bar.setVisibility(View.GONE);
                }

            });


        }
         catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
         }
    }


    private void attachToXML(){
        try {
            nName = objectSingUpFragment.findViewById(R.id.Nname);
            nEmail = objectSingUpFragment.findViewById(R.id.Nemail);
            nPassword = objectSingUpFragment.findViewById(R.id.Npassword);
            create = objectSingUpFragment.findViewById(R.id.btn_Create);
            bar = objectSingUpFragment.findViewById(R.id.loading2);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();

            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createUser();
                }
            });

        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        objectSingUpFragment = inflater.inflate(R.layout.fragment_sing_up__tap_, container, false);
        attachToXML();
        return objectSingUpFragment;
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = nName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            nName.setError("Requerido.");
            valid = false;
        } else {
            nName.setError(null);
        }

        String email = nEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            nEmail.setError("Requerido.");
            valid = false;
        } else {
            nEmail.setError(null);
        }

        String password = nPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            nPassword.setError("Requerido.");
            valid = false;
        } else {
            nPassword.setError(null);
        }


        return valid;
    }
}