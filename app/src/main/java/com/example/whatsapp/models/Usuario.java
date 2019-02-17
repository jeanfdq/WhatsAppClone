package com.example.whatsapp.models;

import com.example.whatsapp.config.configFirebase;
import com.example.whatsapp.helper.ValidaEmail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.Date;

public class Usuario {

	private String id;
	private String nome;
	private String email;
	private String telefone;
	private String senha;
	private boolean telefone_valide;

	//por exigencia do Firebase é preciso criar um construtor mesmo q seja vazio
	public Usuario(){

	}

	@Exclude //Fazendo isso o firebase não ira salvar a senha no Realtime database
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public String setNome(String nome) {

		String retorno = "";

		if (nome.equals("")){
			retorno = "Informe o nome!";
		}else{
			this.nome = nome;
		}
		return retorno;

	}

	public String getEmail() {
		return email;
	}

	public boolean setEmail(String email) {

		boolean isEmailValid = false;

		isEmailValid = ValidaEmail.validar(email);
		if (isEmailValid)
			this.email = email;
		else
			this.email = "";

		return  isEmailValid;

	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Exclude //Fazendo isso o firebase não ira salvar a senha no Realtime database
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isTelefone_valide() {
		return telefone_valide;
	}

	public void setTelefone_valide(boolean telefone_valide) {
		this.telefone_valide = telefone_valide;
	}



	public void salvarUsuario(){
		DatabaseReference referenceFirebase = configFirebase.getFirebase();
		referenceFirebase.child("Usuarios").child(getId()).setValue(this);
	}
}
