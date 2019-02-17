package com.example.whatsapp.helper;

import com.example.whatsapp.config.configFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class Logout {

	public static boolean Logout() {

		FirebaseAuth auth;
		auth = configFirebase.getFirebaseAutenticacao();
		auth.signOut();

		return true;
	}
}
