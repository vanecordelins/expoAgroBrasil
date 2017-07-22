package com.expoagro.expoagrobrasil.dao;

import com.expoagro.expoagrobrasil.model.Produto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Fabricio on 7/17/2017.
 */

public class ProdutoDAO {


    public static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("Produto");
    }

    public void save(Produto produto, String userID) {
        if (produto == null) {
            return;
        }
        DatabaseReference mDatabase = getDatabaseReference();
        produto.setId(mDatabase.push().getKey());
        mDatabase.child(userID).child(produto.getId()).setValue(produto);
    }

    public void update(Produto produto, String userID) {
        if (produto == null) {
            return;
        }
        DatabaseReference mDatabase = getDatabaseReference();
        mDatabase.child(userID).child(produto.getId()).setValue(produto);
    }

    public void delete(String id) {
        getDatabaseReference().child(id).removeValue();
    }

    public void delete(Produto produto, String userID) {
        getDatabaseReference().child(userID).child(produto.getId()).removeValue();
    }
}
