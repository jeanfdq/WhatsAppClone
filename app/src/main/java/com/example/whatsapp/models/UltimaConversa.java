package com.example.whatsapp.models;

import com.example.whatsapp.config.configFirebase;
import com.google.firebase.database.DatabaseReference;

public class UltimaConversa {

	private String idRemetente;
	private String idDestinatario;
	private String ultimaConversa;
	private String nomeRemetente;
	private String nomeDestinatario;
	private String dataConversa;

	public String getIdRemetente() {
		return idRemetente;
	}

	public void setIdRemetente(String idRemetente) {
		this.idRemetente = idRemetente;
	}

	public String getIdDestinatario() {
		return idDestinatario;
	}

	public void setIdDestinatario(String idDestinatario) {
		this.idDestinatario = idDestinatario;
	}

	public String getUltimaConversa() {
		return ultimaConversa;
	}

	public void setUltimaConversa(String ultimaConversa) {
		this.ultimaConversa = ultimaConversa;
	}

	public String getNomeRemetente() {
		return nomeRemetente;
	}

	public void setNomeRemetente(String nomeRemetente) {
		this.nomeRemetente = nomeRemetente;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}
	public String getDataConversa() {
		return dataConversa;
	}

	public void setDataConversa(String dataConversa) {
		this.dataConversa = dataConversa;
	}

	public void salvaUltimaConversa(String idRemetente, String idDestinatario){
		DatabaseReference reference = configFirebase.getFirebase().child("UltimaConversa");
		reference.child(idRemetente).child(idDestinatario).setValue(this);
	}

}
