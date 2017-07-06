package com.expoagro.expoagrobrasil.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.controller.AnunciosActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Fabricio on 6/22/2017.
 */

public class FirebaseLogin {


    public static void deleteAccount() {
        UserDAO userDAO = new UserDAO();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userDAO.delete(user.getUid());
            user.delete().addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        System.out.println("Profile is deleted");
                    } else {
                        System.out.println("Failed to delete account!");
                    }
                }
            });
        }
    }

    public static void createFirebaseUser(final Activity activity, FirebaseAuth auth, Usuario user) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getSenha())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            System.out.println("Authentication failed." + task.getException());
                        } else {
                            System.out.println("Authentication sucessul.");
                        }
                    }
                });
    }

    public static void firebaseAuthentication(final Activity activity, FirebaseAuth auth, String email, String senha, final ProgressDialog progress) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(activity, R.string.error_login, Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        } else {
                            if (task.getResult().getUser().isEmailVerified()) {
                                System.out.println("Autorizado.");
                                Intent it = new Intent(activity, AnunciosActivity.class);
                                activity.startActivity(it);
                                activity.finish();
                            } else {
                                Toast.makeText(activity, "Confirme seu e-mail em seu provedor antes de realizar o login.", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                progress.dismiss();
                            }
                        }
                    }
                });
    }
}
