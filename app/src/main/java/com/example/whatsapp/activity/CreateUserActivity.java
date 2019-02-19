package com.example.whatsapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.configFirebase;
import com.example.whatsapp.helper.Base64EncodeCode;
import com.example.whatsapp.helper.Domains;
import com.example.whatsapp.helper.Session;
import com.example.whatsapp.models.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener {

	private EditText edtNome, edtEmail, edtTelefone, edtSenha;
	private Button btnSalvar;

	//Models
	private Usuario usuario;

	//Firebase
	private FirebaseAuth autenticacao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_user);

		edtNome = findViewById(R.id.edtCreateUserNome);
		edtEmail = findViewById(R.id.edtCreateUserEmail);
		edtTelefone = findViewById(R.id.edtCreateUserTelefone);
		edtSenha = findViewById(R.id.edtCreateUserSenha);

		btnSalvar = findViewById(R.id.btnCreateUserSave);
		btnSalvar.setOnClickListener(this);

		//Mascara do telefone
		SimpleMaskFormatter maskTelefone = new SimpleMaskFormatter("+NN (NN) N NNNN-NNNN");
		MaskTextWatcher mtwTelefone = new MaskTextWatcher(edtTelefone, maskTelefone);
		edtTelefone.addTextChangedListener(mtwTelefone);

		//Mascara da senha
		SimpleMaskFormatter maskSenha = new SimpleMaskFormatter("NNNNNN");
		MaskTextWatcher mtwSenha = new MaskTextWatcher(edtSenha, maskSenha);
		edtSenha.addTextChangedListener(mtwSenha);

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();

		if (id == R.id.btnCreateUserSave) {

			boolean isError = false;

			//Instancia de usuário
			usuario = new Usuario();

			String msg = usuario.setNome(edtNome.getText().toString());
			if (!msg.equals("")) {
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

				isError = true;

			}


			if (!usuario.setEmail(edtEmail.getText().toString().trim())) {
				Toast.makeText(this, "E-mail informado inválido!", Toast.LENGTH_SHORT).show();
				isError = true;
			}


			String xTelefone = edtTelefone.getText().toString().trim()
					.replace("+", "")
					.replace(" ", "")
					.replace("(", "")
					.replace(")", "")
					.replace("-", "");
			if (xTelefone.isEmpty()) {
				Toast.makeText(this, "Informe o número do celular!", Toast.LENGTH_SHORT).show();
				isError = true;

			} else if (xTelefone.length() < 10) {
				Toast.makeText(this, "Número do celular inválido!", Toast.LENGTH_SHORT).show();
				isError = true;

			} else {
				usuario.setTelefone(xTelefone);
			}

			String password = edtSenha.getText().toString().trim().replace(" ", "");
			if (password.isEmpty()) {
				Toast.makeText(this, "Informe sua senha de 6 dígitos!", Toast.LENGTH_SHORT).show();
				isError = true;

			} else if (password.length() < 6) {
				Toast.makeText(this, "Sua senha deve conter 6 dígitos!", Toast.LENGTH_SHORT).show();
				isError = true;

			} else {
				usuario.setSenha(password);
			}

			if (!isError) {

				//Vamos deixa o numero do celular não válido
				usuario.setTelefone_valide(false);

				cadastrarUsuario();
			}

		}
	}

	private void cadastrarUsuario() {

		autenticacao = configFirebase.getFirebaseAutenticacao();

		autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
				.addOnCompleteListener(CreateUserActivity.this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {

							try {

								//Vamos colocar o email do user como identificador codificado em Base64
								String identificador = Base64EncodeCode.Encode64(usuario.getEmail());

								usuario.setId(identificador);
								usuario.salvarUsuario();

								//Guarda o identificado do usuário na sessao
								//Sava o identificador do cliente (e-mail)
								Session session = new Session(CreateUserActivity.this);
								session.setSession(String.valueOf(Domains.keyPreferences.identificatorUser), edtEmail.getText().toString().trim());

								//Como automaticamente o usuario cadastrado já fica logado no app, vamos fazer o SingOut
								//autenticacao.signOut(); //foi preciso remover por conta da segurança do firebase

								//Utilizar o finish para encerrar a activity (é executado o destroy do lifecicly)
								finish();

								Toast.makeText(CreateUserActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
							} catch (Exception e) {
								Toast.makeText(CreateUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
							}


						} else {

							String erroExcessao = "";

							try {
								throw task.getException();
							} catch (FirebaseAuthUserCollisionException e) {
								erroExcessao = "E-mail já em uso no app!";
							} catch (Exception e) {
								erroExcessao = "Erro ao cadastrar o usuário!";
								e.printStackTrace();
							}

							Toast.makeText(CreateUserActivity.this, erroExcessao, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
}