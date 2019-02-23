package com.example.whatsapp.models;

import com.example.whatsapp.config.configFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Mensagem {

	private String idRemetente;
	private String mensagemEnviada;
	private String dateMensagem;

	public Mensagem() {
	}

	public String getIdRemetente() {
		return idRemetente;
	}

	public void setIdRemetente(String idRemetente) {
		this.idRemetente = idRemetente;
	}

	public String getMensagemEnviada() {
		return mensagemEnviada;
	}

	public void setMensagemEnviada(String mensagemEnviada) {
		this.mensagemEnviada = mensagemEnviada;
	}

	public String getDateMensagem() {
		return dateMensagem;
	}

	public void setDateMensagem(String dateMensagem) {
		this.dateMensagem = dateMensagem;
	}

	public void salvarMensagem(String Remetente, String Destinatario){

		DatabaseReference firebaseReference = configFirebase.getFirebase().child("Mensagem");
		firebaseReference.child(Remetente).child(Destinatario).push().setValue(this); //é inserido o push para que o android cria um sequence

	}

}
