package com.example.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

	public static boolean validaPermission(int requestCode, Activity activity, String[] permissions){

		//Vamos verificar a versão que o android esta rodando no cel do cliente
		if (Build.VERSION.SDK_INT >= 23){

			List<String> listaPermissao = new ArrayList<>();

			//Percorre as permissões passadas por parametro para verificar se já está liberada
			for (String permissao: permissions){

				boolean permissaoValida = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

				if (!permissaoValida){
					listaPermissao.add(permissao);
				}
			}

			//Caso a lista esteja vazia não é necessário solicitar permissão
			if (listaPermissao.isEmpty())
				return true;

			String[] novasPermissoes = new String[listaPermissao.size()];
			listaPermissao.toArray(novasPermissoes);


			//Solicita permissao
			ActivityCompat.requestPermissions(activity, novasPermissoes,requestCode);

		}
		return true;
	}

}
