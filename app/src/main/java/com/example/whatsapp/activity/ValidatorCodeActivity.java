package com.example.whatsapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.configFirebase;
import com.example.whatsapp.helper.Domains;
import com.example.whatsapp.helper.Session;
import com.example.whatsapp.models.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ValidatorCodeActivity extends Activity implements View.OnClickListener {

	private EditText edtCodeValidator;
	private Button btnValida;

	private Usuario user;
	private String userID;
	private String userName,userEmail;

	private DatabaseReference reference;
	private ValueEventListener listenerUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validator_code);

		//Pega o email do usuario retornado por parametro
		Intent intent = getIntent();
		Bundle parameters = intent.getExtras();
		userID = parameters.getString("idUser");

		edtCodeValidator    = findViewById(R.id.edtValidatorCode);
		btnValida           = findViewById(R.id.btnValidar);

		btnValida.setOnClickListener(this);

		//Incluindo mascara no Code Validator
		SimpleMaskFormatter maskCodeValidator = new SimpleMaskFormatter("N-N-N-N");
		MaskTextWatcher mtwCodeValidator = new MaskTextWatcher(edtCodeValidator, maskCodeValidator);
		edtCodeValidator.addTextChangedListener(mtwCodeValidator);

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();

		if (id == R.id.btnValidar){

			//Vamos recuperar os dados do cliente para validação do token
			Session session = new Session(this);
			String token = session.getSession(String.valueOf(Domains.keyPreferences.tokenValidator));

			//Vamos tirar a mascara do token
			String tokenInformado = edtCodeValidator.getText().toString().replace("-","").trim();

			int tk = Integer.parseInt(token);
			int tkInf = Integer.parseInt(tokenInformado);

			if (tk == tkInf){

				//Grava os dados do usuario no sharede
				GravaUserSession(userID);

				//atualiza o banco
				reference = configFirebase.getFirebase().child("Usuarios").child(userID);
				reference.child("telefone_valide").setValue(Boolean.TRUE);

				startActivity(new Intent(ValidatorCodeActivity.this, MainActivity.class));
				finish();

			}else{
				Toast.makeText(this, "Token inválido!", Toast.LENGTH_SHORT).show();
			}

		}

	}

	private void GravaUserSession(String userID) {

		//Vamos recuperar os dados do user
		reference = configFirebase.getFirebase().child("Usuarios").child(userID);

		listenerUser = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

				user = dataSnapshot.getValue(Usuario.class);

				Session sessionUser = new Session(ValidatorCodeActivity.this);
				sessionUser.setDadosUser(user.getId(), user.getNome(), user.getEmail(), user.getTelefone());
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		};
		reference.addListenerForSingleValueEvent(listenerUser);



	}
}
