package com.expoagro.expoagrobrasil.dao;

import com.expoagro.expoagrobrasil.model.Servico;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Fabricio on 7/19/2017.
 */

public class ServicoDAO  {

    public static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("Serviço");
    }

    public void save(Servico servico, String userID) {
        if (servico == null) {
            return;
        }
        DatabaseReference mDatabase = getDatabaseReference();
        servico.setId(mDatabase.push().getKey());
        mDatabase.child(userID).child(servico.getId()).setValue(servico);
    }

    public void update(Servico servico, String userID) {
        if (servico == null) {
            return;
        }
        DatabaseReference mDatabase = getDatabaseReference();
        mDatabase.child(userID).child(servico.getId()).setValue(servico);
    }

    public void delete(Servico servico, String userID) {
        getDatabaseReference().child(userID).child(servico.getId()).removeValue();
    }
}
