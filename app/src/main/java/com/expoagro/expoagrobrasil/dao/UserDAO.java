package com.expoagro.expoagrobrasil.dao;

import android.widget.Toast;

import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Fabricio on 6/15/2017.
 */

public class UserDAO implements InterfaceDAO<Usuario> {

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference("Usuário");
    }

    public void save(Usuario user) {
        if (user == null) {
            return;
        }
        DatabaseReference mDatabase = getReference();
        mDatabase.child(user.getId()).setValue(user);
    }

    public void update(Usuario user) {
        if (user == null) {
            return;
        }
        DatabaseReference mDatabase = getReference();
        mDatabase.child(user.getId()).setValue(user);
    }

    public void delete(final String id) {
        getReference().child(id).removeValue();
    }

}
