package com.example.whatsapp.helper;

import android.util.Base64;


public class Base64EncodeCode {

	public static String Encode64(String text){
		return Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)",""); //retira o enter da string
	}

	public static String Decode64(String text){
		return new String(Base64.decode(text, Base64.DEFAULT));
	}

}
