package com.expoagro.expoagrobrasil.dao;

import com.expoagro.expoagrobrasil.model.Servico;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Fabricio on 7/19/2017.
 */

public class ServicoDAO implements InterfaceDAO<Servico>{

    public static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("Serviço");
    }

    @Override
    public void save(Servico servico) {
        if (servico == null) {
            return;
        }
        DatabaseReference mDatabase = getDatabaseReference();
        servico.setId(mDatabase.push().getKey());
        mDatabase.child(servico.getId()).setValue(servico);
    }

    @Override
    public void update(Servico servico) {
        if (servico == null) {
            return;
        }
        DatabaseReference mDatabase = getDatabaseReference();
        mDatabase.child(servico.getId()).setValue(servico);
    }

    @Override
    public void delete(final String idServico) {
        getDatabaseReference().child(idServico).removeValue();
    }
}
