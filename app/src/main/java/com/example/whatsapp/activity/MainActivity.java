package com.example.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.classes.SectionsPageAdapter;
import com.example.whatsapp.config.configFirebase;
import com.example.whatsapp.fragments.ContatosFragment;
import com.example.whatsapp.fragments.ConversasFragment;
import com.example.whatsapp.helper.Base64EncodeCode;
import com.example.whatsapp.helper.Logout;
import com.example.whatsapp.helper.Session;
import com.example.whatsapp.helper.ValidaEmail;
import com.example.whatsapp.models.Contato;
import com.example.whatsapp.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

	private DatabaseReference referenceFirebase;
	private FirebaseAuth auth;

	private Toolbar toolbar;

	//Tabs------------------------------------------
	private TabLayout tabLayout;
	private SectionsPageAdapter mSectionPageAdapter;
	private ViewPager mViewPager;
	//-----------------------------------------------

	private String emailUser,emailContato;
	private String identificadorUser;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Toolbar--------------------------------
		toolbar = findViewById(R.id.mainToolbar);
		toolbar.setTitle("WhatsApp");
		setSupportActionBar(toolbar);
		//---------------------------------------

		mSectionPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
		mViewPager = findViewById(R.id.mainViewPager);
		setupViewPager(mViewPager);

		tabLayout = findViewById(R.id.mainTabs);
		tabLayout.setupWithViewPager(mViewPager);
	}

	private void setupViewPager(ViewPager viewPager){
		SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
		adapter.addFragment(new ContatosFragment(),"CONTATOS");
		adapter.addFragment(new ConversasFragment(),"CONVERSAS");
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case R.id.action_peopleadd:
				addPeople();
				return true;

			case R.id.action_logout:
				Logout.Logout();
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}

	private void addPeople() {

		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);

		//Configuração do Dialog
		alert.setTitle("Novo contato");
//		alert.setMessage("");
		alert.setCancelable(false);
		alert.setIcon(getResources().getDrawable(R.drawable.ic_person_add_min));

		final EditText edtEmailContato = new EditText(MainActivity.this);
		edtEmailContato.setHint("E-mail do contato.");
		edtEmailContato.setHintTextColor(getResources().getColor(R.color.colorWhite));
		edtEmailContato.setTextColor(getResources().getColor(R.color.colorWhite));
		edtEmailContato.setTextSize(15);
		edtEmailContato.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		alert.setView(edtEmailContato);

		alert.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				emailContato = edtEmailContato.getText().toString();

				if (emailContato.isEmpty()){
					Toast.makeText(MainActivity.this, "Informe o e-mail do contato!",Toast.LENGTH_SHORT).show();
				}else {
					boolean IsEmailValide = ValidaEmail.validar(emailContato);
					if (IsEmailValide){

						//Vamos verificar se o e-mail digitado está cadastrado na base
						emailContato = Base64EncodeCode.Encode64(emailContato);

						referenceFirebase = configFirebase.getFirebase().child("Usuarios").child(emailContato);

						//Vamos realizar uma unica consulta no firebase (O single não fica escutando o firebase, ou seja, se houver uma mudança o app nao sera notificado
						referenceFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot) {

								if (dataSnapshot.getValue() != null){

									Contato contato = dataSnapshot.getValue(Contato.class);

									//Recupera o email do user logado
									Session session = new Session(MainActivity.this);
									identificadorUser = session.getIdenticadorUser();

									//Verifica se o email digitado é o mesmo e-mail que esta logado
									if (emailContato.equals(identificadorUser)){
										Toast.makeText(MainActivity.this, "Não é possível adicionar seu e-mail como contato!",Toast.LENGTH_SHORT).show();
									}else{

										contato.gravContato(identificadorUser,emailContato);

										Toast.makeText(MainActivity.this, "Contato adicionado com sucesso!!",Toast.LENGTH_SHORT).show();

									}


								}else{
									Toast.makeText(MainActivity.this, "E-mail do contato não cadastrado!",Toast.LENGTH_SHORT).show();
								}

							}

							@Override
							public void onCancelled(DatabaseError databaseError) {

							}
						});

					}else{
						Toast.makeText(MainActivity.this, "E-mail do contato inválido!",Toast.LENGTH_SHORT).show();
					}

				}

			}
		});

		alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		alert.create().show();
	}
}
