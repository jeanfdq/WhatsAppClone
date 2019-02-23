package com.example.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.whatsapp.R;
import com.example.whatsapp.helper.Session;
import com.example.whatsapp.models.Mensagem;

import java.util.List;



public class MensagemAdapter extends ArrayAdapter<Mensagem> {

	private Context context;
	private List<Mensagem> mensagens;

	//Dados do Remetente
	private String idRemetente;

	//Mensagem que irá aparecer
	private String mensagemFull;

	public MensagemAdapter(Context c, List<Mensagem> objects) {
		super(c, 0, objects);

		this.context    = c;
		this.mensagens  = objects;

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		if (mensagens != null){

			//Vamos recuperar o identificador do user logado
			Session session = new Session(context);
			idRemetente = session.getIdenticadorUser();

			Mensagem mensagem = mensagens.get(position);

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

			if (idRemetente.equals(mensagem.getIdRemetente())){
				view = inflater.inflate(R.layout.layout_mensagem_enviada, parent, false);
			}else {
				view = inflater.inflate(R.layout.layout_mensagem_recebida, parent, false);
			}


			//Recupera o textview para exibição
			TextView textoMensagem = view.findViewById(R.id.msg_recebida_tv);

			textoMensagem.setText( mensagem.getMensagemEnviada());

		}

		return view;
	}
}
