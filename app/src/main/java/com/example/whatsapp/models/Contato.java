package com.example.whatsapp.models;

import com.example.whatsapp.config.configFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Contato {

	private String nome;
	private String email;
	private String telefone;


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public void gravContato(String identificadorUser, String identificadorContato){
		DatabaseReference referenceFirebase = configFirebase.getFirebase();
		referenceFirebase.child("Contatos").child(identificadorUser).child(identificadorContato).setValue(this);
	}
}
