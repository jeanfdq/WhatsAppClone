package com.example.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.whatsapp.helper.Domains.nameFilePreferences;

public class Session {

	private final SharedPreferences mSharedPrefences;

	//Chaves Staticas
	private final String IDENTIFICADOR_USER = "identificadorUsuarioLogado";
	private final String NOME_USER = "nomeUsuarioLogado";
	private final String EMAIL_USER = "emailUsuarioLogado";
	private final String TELEFONE_USER = "telefoneUsuarioLogado";

	private SharedPreferences.Editor editor;

	public Session(Context context) {

		this.mSharedPrefences = context.getSharedPreferences(String.valueOf(nameFilePreferences.appWhats), context.MODE_PRIVATE);
		editor = mSharedPrefences.edit();
	}

	public void setSession(String key, String value){editor.putString(key, value.trim()).commit();}

	public String getSession(String key){
		return mSharedPrefences.getString(key,null);
	}

	public void destroySession(String key){
		editor.remove(key).commit();
	}


	public void setDadosUser(String identificadorUser, String nomeUser, String emailUser, String telefoneUser){
		editor.putString(IDENTIFICADOR_USER, identificadorUser.trim());
		editor.putString(NOME_USER, nomeUser.trim());
		editor.putString(EMAIL_USER, emailUser.trim());
		editor.putString(TELEFONE_USER, telefoneUser.trim());
		editor.commit();
	}

	public String getIdenticadorUser(){

		return mSharedPrefences.getString(IDENTIFICADOR_USER,null);
	}

	public String getNomeUser(){

		return mSharedPrefences.getString(NOME_USER,null);
	}

	public String getEmailUser(){

		return mSharedPrefences.getString(EMAIL_USER,null);
	}

	public String getTelefoneUser(){

		return mSharedPrefences.getString(TELEFONE_USER,null);
	}


}
