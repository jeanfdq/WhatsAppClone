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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

	private ListView listview;
	private ArrayAdapter adapter;
	private ArrayList<String> contatos;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_contatos, container, false);

//		contatos = new ArrayList<>();
//		contatos.add("Jean");
//		contatos.add("Jean");
//		contatos.add("Jean");
//		contatos.add("Jean");
//

//
//		listview = view.findViewById(R.id.frgContatos_lv_contatos);
//		adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,contatos);
//		listview.setAdapter(adapter);

		return view;
	}

}
