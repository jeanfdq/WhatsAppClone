package com.example.whatsapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ConversasActivity;
import com.example.whatsapp.adapter.ContatosAdapter;
import com.example.whatsapp.config.configFirebase;
import com.example.whatsapp.helper.Session;
import com.example.whatsapp.models.Contato;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

	private String identificadorUser;

	private List<Contato> lista_contatos;

	private ListView listview;
	private ArrayAdapter adapter;

	private DatabaseReference firebaseReference;

	private ValueEventListener listenerContatos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_contatos, container, false);

		//Retorna identificador do usuario conectado
		Session session = new Session(getActivity());
		identificadorUser = session.getIdenticadorUser();

		//Instanciando Contatos
		lista_contatos = new ArrayList<>();


		listview = view.findViewById(R.id.frgContatos_lv_contatos);
		//adapter = new ArrayAdapter(getActivity(), R.layout.layout_list_contatos,lista_contatos);

		//Vamos fazer um adapter customizado
		adapter = new ContatosAdapter(getActivity(), lista_contatos);

		listview.setAdapter(adapter);


		firebaseReference = configFirebase.getFirebase();
		firebaseReference = firebaseReference.child("Contatos").child(identificadorUser);

		listenerContatos = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

				lista_contatos.clear();

				for (DataSnapshot dados:dataSnapshot.getChildren()){

					Contato contatos = dados.getValue(Contato.class);
					lista_contatos.add(contatos);
				}
				adapter.notifyDataSetChanged();

			}


			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		};


		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(getActivity(), ConversasActivity.class);

				Contato contato = lista_contatos.get(position);
				intent.putExtra("nome",contato.getNome());
				intent.putExtra("email",contato.getEmail());

				startActivity(intent);

			}
		});

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		//Somente quando o fragment for inicializado iremos atualizar o listener
		firebaseReference.addValueEventListener(listenerContatos);

	}

	@Override
	public void onStop() {
		super.onStop();
		firebaseReference.removeEventListener(listenerContatos); //melhor é remover o Listener para nao ficar utilizando recurso
	}
}
