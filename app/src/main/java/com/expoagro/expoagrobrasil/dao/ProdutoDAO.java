package com.expoagro.expoagrobrasil.dao;

import com.expoagro.expoagrobrasil.model.Produto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public void delete(String id) {
        getDatabaseReference().child(id).removeValue();
    }

}
