package com.example.whatsapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.whatsapp.R;
import com.example.whatsapp.adapter.MensagemAdapter;
import com.example.whatsapp.config.configFirebase;
import com.example.whatsapp.helper.Base64EncodeCode;
import com.example.whatsapp.helper.Session;
import com.example.whatsapp.helper.getCurrentDateTime;
import com.example.whatsapp.models.Mensagem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConversasActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private String nomeContato, emailContato;
	private ListView lvMensagem;
	private EditText edtMensagem;
	private ImageButton btnMensagem;
	private List<Mensagem> mensagens;
	private ArrayAdapter<Mensagem> adapter;

	//Dados do Remetente
	private String identificadorRemetente;

	//Dados do Destinatário
	private String identificadorDestinatario;

	//Instancia do Firebase
	private DatabaseReference referenceFirebase;

	//Cria um Listener para recuperar as mensagens
	private ValueEventListener listener;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversas);

		//Pegando parametros passado pela Intent---
		Bundle parameters = getIntent().getExtras();
		if (parameters != null) {
			nomeContato = parameters.getString("nome");
			emailContato = parameters.getString("email");

			//Coloca o e-mail do detinatario em Base64
			identificadorDestinatario = Base64EncodeCode.Encode64(emailContato);
		}
		//-----------------------------------------

		//Recupera o identificador do usuário logado
		Session session = new Session(this);
		identificadorRemetente = session.getIdenticadorUser();

		//Mapeando as views---------------------------------
		toolbar = findViewById(R.id.conversasToolbar);
		lvMensagem = findViewById(R.id.mensagem_lv);
		edtMensagem = findViewById(R.id.mensagemEdt);
		btnMensagem = findViewById(R.id.mensagemImgBtn);
		//--------------------------------------------------

		//Configurando a toolbar----------------------
		toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
		toolbar.setLogo(R.drawable.ic_account_circle_white);
		toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
		toolbar.setTitle(nomeContato);
		setSupportActionBar(toolbar);
		//--------------------------------------------

		//Monta o ListView das mensagens
		mensagens = new ArrayList<>();
//		adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mensagens);
		adapter = new MensagemAdapter(this, mensagens);
		lvMensagem.setAdapter(adapter);


		//Recupera as mensagens do firebase------------------------------------------------
		referenceFirebase = configFirebase.getFirebase()
				.child("Mensagem")
				.child(identificadorRemetente)
				.child(identificadorDestinatario);

		listener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {

				mensagens.clear();
				for (DataSnapshot data : dataSnapshot.getChildren()) {

					if (data != null){

						Mensagem mensagem = data.getValue(Mensagem.class);
						mensagens.add(mensagem);
					}


				}
				adapter.notifyDataSetChanged();

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		};
		referenceFirebase.addValueEventListener(listener);
		//---------------------------------------------------------------------------------


		//Botão de envio de mensagem----------------------------------
		btnMensagem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				//Recuperando a mensagem digitada
				String textoDigitado = edtMensagem.getText().toString();

				if (!textoDigitado.isEmpty()) {

					//Vamos gravar a mensagem no Firebase
					Mensagem mensagem = new Mensagem();
					mensagem.setIdRemetente(identificadorRemetente);
					mensagem.setMensagemEnviada(textoDigitado.trim());
					mensagem.setDateMensagem(getCurrentDateTime.getDateTime());

					mensagem.salvarMensagem(identificadorRemetente, identificadorDestinatario);
					mensagem.salvarMensagem(identificadorDestinatario, identificadorRemetente);
					edtMensagem.setText("");
				}

			}
		});
		//------------------------------------------------------------

	}

	@Override
	protected void onStop() {
		super.onStop();
		referenceFirebase.removeEventListener(listener);
	}
}
