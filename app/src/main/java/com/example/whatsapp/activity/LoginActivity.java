package com.example.whatsapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.configFirebase;
import com.example.whatsapp.helper.Domains;
import com.example.whatsapp.helper.Permissao;
import com.example.whatsapp.helper.Session;
import com.example.whatsapp.helper.ValidaEmail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

	private String[] permissions = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.INTERNET};

	private EditText edtEmail, edtSenha;
	private Button btnLogin;
	private String numbreCel;

	//FirebaseAuth
	private FirebaseAuth autenticacao;
	//Cria um Listener do Auth
	private FirebaseAuth.AuthStateListener authListener;

	//DataBase
	private FirebaseDatabase database;

	//Refence do banco do Firebase
	private DatabaseReference reference;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//Vamos verificar as permissões necessárias
		Permissao.validaPermission(1, LoginActivity.this, permissions);

		edtEmail = findViewById(R.id.edtLoginEmail);
		edtSenha = findViewById(R.id.edtLoginSenha);
		btnLogin = findViewById(R.id.btnLoginLogin);

		btnLogin.setOnClickListener(this);

	}


	@Override
	public void onClick(View view) {

		int id = view.getId();

		if (id == R.id.btnLoginLogin) {


			String email = edtEmail.getText().toString().trim();
			String senha = edtSenha.getText().toString().trim();

			if (!ValidaEmail.validar(email)) {
				Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
			} else if (senha.isEmpty()) {
				Toast.makeText(this, "Informe a senha!", Toast.LENGTH_SHORT).show();
			} else if (senha.length() < 6) {
				Toast.makeText(this, "Senha deve conter 6 dígitos!", Toast.LENGTH_SHORT).show();
			} else {

				autenticacao = configFirebase.getFirebaseAutenticacao();
				autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(Task<AuthResult> task) {

						if (task.isSuccessful()) {

							reference = configFirebase.getFirebase().child("Usuarios").child(task.getResult().getUser().getUid());
							reference.addChildEventListener(new ChildEventListener() {
								@Override
								public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

									if (dataSnapshot.getKey().equals("telefone"))
										numbreCel = dataSnapshot.getValue().toString().trim();

									if (dataSnapshot.getKey().equals("telefone_valide")) {
										if (dataSnapshot.getValue().toString().equals("false")) {
											AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
											alert.setCancelable(false);
											alert.setTitle("Código de validação");
											alert.setIcon(R.drawable.ic_message_green);
											alert.setMessage("Um SMS será enviado para " + numbreCel);
											alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {


													boolean enviado = enviaSMS(numbreCel);

													if (enviado) {

														startActivity(new Intent(LoginActivity.this, ValidatorCodeActivity.class));
														finish();
													}

												}
											});
											AlertDialog dialog = alert.create();

											//Colocar um designe no bottao do dialog
											//										dialog.setOnShowListener(new DialogInterface.OnShowListener() {
											//											@Override
											//											public void onShow(DialogInterface dialog) {
											//												Button positive = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
											//
											//												final Drawable positiveD = getResources().getDrawable(R.drawable.bg_btn_validator_code);
											//												positive.setBackground(positiveD);
											//												positive.invalidate();
											//											}
											//										});
											dialog.show();

											Button bq = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
											bq.setTextColor(Color.parseColor("#000000"));

										} else {
											startActivity(new Intent(LoginActivity.this, MainActivity.class));
										}
									}
								}

								@Override
								public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

								}

								@Override
								public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

								}

								@Override
								public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

								}

								@Override
								public void onCancelled(@NonNull DatabaseError databaseError) {

								}
							});

						} else {

							Toast.makeText(LoginActivity.this, "Usuário/Senha inválido!", Toast.LENGTH_SHORT).show();

						}

					}
				});

			}


		}

	}

	public boolean enviaSMS(String telefone) {

		//Para gerar token de 4 digitos
		Random numberRandom = new Random();
		String token = String.valueOf(numberRandom.nextInt(8999) + 1000);

		//numero do simulador
		String telefone2 = "5554";

		try {
			SmsManager smsmanager = SmsManager.getDefault();
			smsmanager.sendTextMessage(telefone2, null, "Token de validação: " + token, null, null);

			//salva os dados do cliente no SharedPreference
			Session session = new Session(LoginActivity.this);
			session.setSession(String.valueOf(Domains.keyPreferences.tokenValidator), token.trim());

			return true;
		} catch (Exception e) {
			Log.e("Whats", e.getMessage());
			return false;
		}

	}

	//Esse método esta ligado ao xml da activity "onclick" do TextView
	public void createUser(View view) {
		startActivity(new Intent(LoginActivity.this, CreateUserActivity.class));
	}

	//Para tratar a ação do usuário caso ele negue as permissões
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		boolean IsPermissionDenied = false;

		//vamos percorrer as permissoes
		for (int result : grantResults) {

			if (result == PackageManager.PERMISSION_DENIED) {
				android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(LoginActivity.this);
				alert.setTitle("Permissões negadas!");
				alert.setMessage("Para utilizar o app, é necessário aceitar as permissões.");
				alert.setCancelable(false);
				alert.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
				android.app.AlertDialog dialog = alert.create();
				dialog.show();
				IsPermissionDenied = true;
			}
		}

		if (!IsPermissionDenied) {
			startActivity(new Intent(this, ValidatorCodeActivity.class));
		}

	}
}


