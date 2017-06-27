package com.expoagro.expoagrobrasil.dao;

import com.expoagro.expoagrobrasil.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabricio on 6/15/2017.
 */

public class UserDAO implements InterfaceDAO<Usuario> {

    private static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference("Usuário");
    }

    public void save(Usuario user) {
        if (user == null) {
            return;
        }
        DatabaseReference mDatabase = getReference();
        user.setId(mDatabase.push().getKey());
        mDatabase.child(user.getId()).setValue(user);
    }

    public void update(Usuario user) {
        if (user == null) {
            return;
        }
        getReference().child(user.getId()).setValue(user);
    }

    public void delete(String id) {

        getReference().child(id).removeValue();
    }

    public void read(final String uid) {
        final List<Usuario> users = new ArrayList<>();
        getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    if (user.getKey().equals(uid)) {
                        Usuario target = user.getValue(Usuario.class);
                        users.add(target);
                        break;
                    }
                }
                // found, update GUI here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public void readAll() {
        final List<Usuario> users = new ArrayList<>();

        getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    Usuario target = user.getValue(Usuario.class);
                    users.add(target);
                }

                // all data retrieved, update GUI here if needed. Assynchronous method
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

}
