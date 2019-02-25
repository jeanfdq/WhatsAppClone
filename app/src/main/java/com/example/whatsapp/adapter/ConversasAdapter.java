package com.example.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.whatsapp.R;
import com.example.whatsapp.models.UltimaConversa;

import java.util.List;

public class ConversasAdapter extends ArrayAdapter<UltimaConversa> {

	private Context context;
	private List<UltimaConversa> conversas;

	public ConversasAdapter(Context pContext, List<UltimaConversa> objects) {
		super(pContext, 0, objects);

		this.context = pContext;
		this.conversas = objects;

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;

		if (conversas != null) {

			UltimaConversa ultimaConversa = conversas.get(position);

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_list_conversas, parent, false);

			TextView nameUser = view.findViewById(R.id.layout_list_conversas_tv_name);
			nameUser.setText(ultimaConversa.getNomeDestinatario());

			TextView ultimaMensagem = view.findViewById(R.id.layout_list_conversas_tv_msg);
			ultimaMensagem.setText(ultimaConversa.getUltimaConversa());


		}

		return view;

	}

	@Override
	public int getCount() {
		return conversas.size();
	}

}
