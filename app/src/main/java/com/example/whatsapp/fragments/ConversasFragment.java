package com.example.whatsapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ConversasActivity;
import com.example.whatsapp.adapter.ConversasAdapter;
import com.example.whatsapp.config.configFirebase;
import com.example.whatsapp.helper.Base64EncodeCode;
import com.example.whatsapp.helper.Session;
import com.example.whatsapp.models.UltimaConversa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {


	private DatabaseReference databaseReference;
	private ValueEventListener eventListener;

	private String idUsuario;
	private List<UltimaConversa> conversas;

	private ListView listView;
	private ArrayAdapter adapter;


	@Override
	public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_conversas, container, false);

		listView = view.findViewById(R.id.frgConversas_lv_conversa);

		//Recupera o id do usuario logado
		Session session = new Session(getActivity());
		idUsuario = session.getIdenticadorUser();


		//Adapter Customizado
		conversas = new ArrayList<>();
		adapter = new ConversasAdapter(getActivity(), conversas);

		//Ligando o adapter no list view
		listView.setAdapter(adapter);

		//Vamos recuperar os dados
		databaseReference = configFirebase.getFirebase().child("UltimaConversa").child(idUsuario);

		eventListener = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

				conversas.clear();

				for (DataSnapshot data:dataSnapshot.getChildren()){
					UltimaConversa ultimaConversa = data.getValue(UltimaConversa.class);
					conversas.add(ultimaConversa);
				}
				adapter.notifyDataSetChanged();

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		};

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				UltimaConversa conversa = conversas.get(position);

				String nomeContato = conversa.getNomeDestinatario();
				String emailContato = Base64EncodeCode.Decode64(conversa.getIdDestinatario());

				Intent intent = new Intent(getActivity(), ConversasActivity.class);

				intent.putExtra("nome", nomeContato);
				intent.putExtra("email", emailContato);

				startActivity(intent);


			}
		});

		return view;
	}



	@Override
	public void onStart() {
		super.onStart();
		databaseReference.addValueEventListener(eventListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		databaseReference.removeEventListener(eventListener);
	}
}
