package com.expoagro.expoagrobrasil.dao;

import com.expoagro.expoagrobrasil.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Fabricio on 7/17/2017.
 */

public class ProdutoDAO implements InterfaceDAO<Produto> {

    public static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("Produto");
    }
    @Override
    public void save(Produto produto) {
        if (produto == null) {
            return;
        }
        DatabaseReference mDatabase = getDatabaseReference();
        produto.setId(mDatabase.push().getKey());
        mDatabase.child(produto.getId()).setValue(produto);
    }

    @Override
    public void update(Produto produto) {
        if (produto == null) {
            return;
        }
        DatabaseReference mDatabase = getDatabaseReference();
        mDatabase.child(produto.getId()).setValue(produto);
    }
    @Override
    public void delete(final String id) {

        getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot produto: dataSnapshot.getChildren()) {
                    if (produto.getKey().equals(id)) {
                        Produto prod = produto.getValue(Produto.class);
                        if (prod.getFoto() != null) {
                            StorageReference photoRef;
                            for (int i = 0; i < prod.getFoto().size(); i++) {
                                photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(prod.getFoto().get(i));
                                photoRef.delete();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Cancelado.");
            }
        });

        getDatabaseReference().child(id).removeValue();

    }

}
