package com.example.whatsapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.configFirebase;
import com.example.whatsapp.helper.Domains;
import com.example.whatsapp.helper.Session;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ValidatorCodeActivity extends Activity implements View.OnClickListener {

	private EditText edtCodeValidator;
	private Button btnValida;

	private String userID;

	//Firebase
	private FirebaseAuth auth;
	private FirebaseDatabase database;
	private DatabaseReference reference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validator_code);

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

				//Pega o id do user
				auth = configFirebase.getFirebaseAutenticacao();
				userID = auth.getUid();

				//atualiza o banco
				reference = configFirebase.getFirebase().child("Usuarios").child(userID);
				reference.child("telefone_valide").setValue("true");

				startActivity(new Intent(ValidatorCodeActivity.this, MainActivity.class));
				finish();

			}else{
				Toast.makeText(this, "Token inválido!", Toast.LENGTH_SHORT).show();
			}

		}

	}
}
