package com.expoagro.expoagrobrasil.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.controller.LoginActivity;
import com.expoagro.expoagrobrasil.controller.MenuActivity;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Fabricio on 6/22/2017.
 */

public class FirebaseLogin {


    public static void deleteAccount(final Activity activity) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserDAO userDAO = new UserDAO();
            userDAO.delete(user.getUid());
            user.delete().addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        final ProdutoDAO pdao = new ProdutoDAO();

                        ProdutoDAO.getDatabaseReference().orderByChild("idUsuario").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot prod : dataSnapshot.getChildren()) {
                                    Produto produto = prod.getValue(Produto.class);
                                    pdao.delete(produto.getId());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("Não foi possível deletar os produtos deste usuário.");
                            }
                        });
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(activity, "Perfil deletado com sucesso.", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(activity, LoginActivity.class);
                        activity.startActivity(it);
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "Falha ao deletar perfil.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
                                Intent it = new Intent(activity, MenuActivity.class);
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
