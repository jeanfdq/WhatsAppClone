package com.example.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.whatsapp.helper.Domains.nameFilePreferences;

public class Session {

	private final SharedPreferences mSharedPrefences;

	//Chaves Staticas
	private final String IDENTIFICADOR_USER = "identificadorUsuarioLogado";

	public Session(Context context) {

		this.mSharedPrefences = context.getSharedPreferences(String.valueOf(nameFilePreferences.appWhats), context.MODE_PRIVATE);
	}

	public void setSession(String key, String value){mSharedPrefences.edit().putString(key, value.trim()).commit();}

	public String getSession(String key){
		return mSharedPrefences.getString(key,null);
	}

	public void destroySession(String key){
		mSharedPrefences.edit().remove(key).commit();
	}


	public void setIdentificadorUser(String identificadorUser){
		mSharedPrefences.edit().putString(IDENTIFICADOR_USER, identificadorUser.trim()).commit();
	}

	public String getIdenticadorUser(){

		return mSharedPrefences.getString(IDENTIFICADOR_USER,null);
	}


}
