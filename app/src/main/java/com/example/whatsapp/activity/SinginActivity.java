package com.example.whatsapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.whatsapp.R;
import com.example.whatsapp.helper.Domains.keyPreferences;
import com.example.whatsapp.helper.Permissao;
import com.example.whatsapp.helper.Session;
import com.example.whatsapp.helper.testToast;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

public class SinginActivity extends Activity implements View.OnClickListener {

	private ImageView imageViewLogo;
	private EditText edtName,edtNumberCountryPhone, edtNumberCel;
	private Button  btnSalvar;
	private Animation animation;
	private String[] permissions = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.INTERNET};



	@Override
	protected void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sigin);

		//Vamos verificar as permissões necessárias
		Permissao.validaPermission(1,SinginActivity.this,permissions );


		//Mapear os capos da tela
		imageViewLogo           = findViewById(R.id.imgLogo);
		edtName                 = findViewById(R.id.edtName);
		edtNumberCountryPhone   = findViewById(R.id.edtNumberCountryPhone);
		edtNumberCel            = findViewById(R.id.edtNumberCel);
		btnSalvar               = findViewById(R.id.btnSalvar);

		//Ativar o botao para clique
		btnSalvar.setOnClickListener(SinginActivity.this);

		//Adicionar a animação na imagem
		animation = AnimationUtils.loadAnimation(this, R.anim.animation_slide_in_left_to_right);
		imageViewLogo.setAnimation(animation);

		//Incluindo mascara Number Country do Phone
		SimpleMaskFormatter NumberMaskCountryPhone = new SimpleMaskFormatter("+NN");
		MaskTextWatcher mtwCountryPhone = new MaskTextWatcher(edtNumberCountryPhone, NumberMaskCountryPhone);
		edtNumberCountryPhone.addTextChangedListener(mtwCountryPhone);

		//Incluindo mascara Number Country do Phone
		SimpleMaskFormatter NumberMaskPhone = new SimpleMaskFormatter("(NN) N NNNN-NNNN");
		MaskTextWatcher mtwNumberPhone = new MaskTextWatcher(edtNumberCel, NumberMaskPhone);
		edtNumberCel.addTextChangedListener(mtwNumberPhone);
	}


	@Override
	public void onClick(View view) {

		int id = view.getId();

		if (id == R.id.btnSalvar){


			boolean salvarData = false;

			if (edtName.getText().toString().equals(null) || edtName.getText().toString().equals("")) {
				edtName.setError("Informe o seu nome!");
			} else if (edtNumberCountryPhone.getText().toString().equals("")) {
				edtNumberCountryPhone.setError("Informe Código do país!");
			} else if (edtNumberCel.getText().toString().equalsIgnoreCase("")) {
				edtNumberCel.setError("Número do celular inválido!");
			} else if (edtNumberCel.getText().toString().length() < 16) {
				edtNumberCel.setError("Número do celular inválido!");
			} else {
				salvarData = true;
			}

			//Guardar os dados e gerar o token
			if (salvarData) {

				//Para gerar token de 4 digitos
				Random numberRandom = new Random();
				String token = String.valueOf(numberRandom.nextInt(8999) + 1000);

				//Pega o nome
				String nome = edtName.getText().toString().trim();

				//Pega o telefone formatado
				String numberCel = edtNumberCountryPhone.getText().toString().replace("+", "");
				numberCel += edtNumberCel.getText().toString().replace("(", "").replace(")", "").replace("-", "").replace(" ", "");

				HashMap<String, String> dataClient = new HashMap<>();
				dataClient.put("usuario", nome);
				dataClient.put("telefone", numberCel);
				dataClient.put("token", token);

				numberCel = "5554"; //telefone do emulador

				boolean enviado = enviaSMS(numberCel, "Token de validação: " + token);

				if (enviado){

					//salva os dados do cliente no SharedPreference
					Session session = new Session(this);
					session.setSession(String.valueOf(keyPreferences.tokenValidator),token.trim());

					startActivity(new Intent(SinginActivity.this, ValidatorCodeActivity.class));
					finish();
				}

			}

		}

	}

	private boolean enviaSMS(String telefone, String mensagem){

		try{
			SmsManager smsmanager = SmsManager.getDefault();
			smsmanager.sendTextMessage(telefone, null, mensagem, null, null);
			return true;
		}catch(Exception e){
			Log.e("Whats",e.getMessage());
			return false;
		}




	}

	//Para tratar a ação do usuário caso ele negue as permissões
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		boolean IsPermissionDenied = false;

		//vamos percorrer as permissoes
		for(int result:grantResults){

			if (result == PackageManager.PERMISSION_DENIED){
				AlertDialog.Builder alert = new AlertDialog.Builder(SinginActivity.this);
				alert.setTitle("Permissões negadas!");
				alert.setMessage("Para utilizar o app, é necessário aceitar as permissões.");
				alert.setCancelable(false);
				alert.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
				AlertDialog dialog = alert.create();
				dialog.show();
				IsPermissionDenied = true;
			}
		}

		if (!IsPermissionDenied){
			startActivity(new Intent(this, ValidatorCodeActivity.class));
		}

	}

	public void msgToast(String msg){
		testToast toast = new testToast(this, msg);
	}

}
