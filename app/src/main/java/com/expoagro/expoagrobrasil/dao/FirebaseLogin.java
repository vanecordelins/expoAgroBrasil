package com.expoagro.expoagrobrasil.dao;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.view.AnunciosActivity;
import com.expoagro.expoagrobrasil.view.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Fabricio on 6/22/2017.
 */

public class FirebaseLogin {

    public static void createFirebaseUser(final Activity activity, FirebaseAuth auth, Usuario user) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getSenha())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(activity, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_LONG).show();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(activity, "Authentication failed." + task.getException(), Toast.LENGTH_LONG).show();
                            System.out.println(task.getException());
                        } else {
                            Toast.makeText(activity, "Authentication sucessul.", Toast.LENGTH_LONG).show();
                            activity.finish();
                        }
                    }
                });
    }

    public static void firebaseAuthentication(final Activity activity, FirebaseAuth auth, String email, String senha) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(activity, "Não foi possível realizar o Login. Tente Novamente", Toast.LENGTH_LONG).show();
                        } else {
                            System.out.println("Autorizado.");
                            Intent it = new Intent(activity, AnunciosActivity.class);
                            activity.startActivity(it);
                        }
                    }
                });
    }
}
