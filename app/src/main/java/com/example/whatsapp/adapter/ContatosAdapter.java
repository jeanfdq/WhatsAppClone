package com.example.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.whatsapp.R;
import com.example.whatsapp.models.Contato;

import java.util.List;


public class ContatosAdapter extends ArrayAdapter<Contato> {

	private Context context;
	private List<Contato> contatos;


	public ContatosAdapter(Context pContext, List<Contato> objects) {
		super(pContext, 0, objects);

		this.context    =pContext;
		this.contatos   = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;

		if (contatos != null){

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.layout_list_contatos, parent, false);



			//Vamos recuperar os contatos da lista
			Contato contato = contatos.get(position);

			TextView nomeContato = view.findViewById(R.id.layout_list_contatos_tv_nome);
			nomeContato.setText(contato.getNome());

			TextView emailContato = view.findViewById(R.id.layout_list_contatos_tv_email);
			emailContato.setText(contato.getEmail());

		}

		return view;
	}
}
