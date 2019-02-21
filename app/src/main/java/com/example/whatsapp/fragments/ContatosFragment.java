package com.example.whatsapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.whatsapp.R;
import com.example.whatsapp.config.configFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

	private ListView listview;
	private ArrayAdapter adapter;

	private DatabaseReference firebaseReference;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_contatos, container, false);


		listview = view.findViewById(R.id.frgContatos_lv_contatos);
		//adapter = new ArrayAdapter(getActivity(), R.layout.layout_list_contatos,contatos);
		listview.setAdapter(adapter);

		firebaseReference = configFirebase.getFirebase();

		return view;
	}

}
