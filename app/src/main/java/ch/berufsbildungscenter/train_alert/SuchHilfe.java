package ch.berufsbildungscenter.train_alert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.List;

import ch.berufsbildungscenter.train_alert.Database.Favoriten;
import ch.berufsbildungscenter.train_alert.Database.FavoritenDatabase;
import ch.berufsbildungscenter.train_alert.JSON.JSONSuchHilfe;


public class SuchHilfe extends ActionBarActivity {
    final SuchHilfe suchHilfe = this;
    int textFeldId;

    private List<String> vorschlaege;

    public List<String> getVorschlaege() {
        return vorschlaege;
    }

    public void setVorschlaege(List<String> vorschlaege) {
        this.vorschlaege = vorschlaege;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_such_hilfe);
        Intent intent = getIntent();
        String ort = intent.getStringExtra("ort");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView favListView = (ListView) findViewById(R.id.listView2);
        FavoritenDatabase favoritenDatabase = new FavoritenDatabase(getApplicationContext());
        try {
            //Try to open the DB connection
            favoritenDatabase.open();
        } catch (SQLException e) {
            Log.v("DATABASETEST", e.toString());
        }

            final List<Favoriten> favoritens = favoritenDatabase.getAllFavoriten();
            for (int i = 0; i < favoritens.size(); i++) {
                arrayAdapter.add(favoritens.get(i).getName());
            }
        favListView.setAdapter(arrayAdapter);
        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (textFeldId == R.id.editVon) {
                    MainActivity.textVon.setText(favoritens.get(position).getName());
                } else if (textFeldId == R.id.editNach) {
                    MainActivity.textNach.setText(favoritens.get(position).getName());
                } else if (textFeldId == R.id.editVia) {
                    MainActivity.textVia.setText(favoritens.get(position).getName());
                }
                finish();
            }
        });


        textFeldId = getIntent().getIntExtra("selectedEdit", R.id.editVon);

        final EditText myTextBox = (EditText) findViewById(R.id.editText);
        myTextBox.setText(ort);
        myTextBox.setSelection(myTextBox.getText().length());
        myTextBox.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                JSONSuchHilfe jsonSuchHilfe = new JSONSuchHilfe(suchHilfe);
                jsonSuchHilfe.execute(myTextBox.getText().toString());
            }
        });
    }

    public void setData(List<String> result) {
        final List<String> resultset = result;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView verbListView = (ListView) findViewById(R.id.listView2);
        for (int i = 0; i < resultset.size(); i++) {
            arrayAdapter.add(resultset.get(i));
        }
        verbListView.setAdapter(arrayAdapter);
        verbListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (textFeldId == R.id.editVon) {
                    MainActivity.textVon.setText(resultset.get(position).toString());
                } else if (textFeldId == R.id.editNach) {
                    MainActivity.textNach.setText(resultset.get(position).toString());
                } else if (textFeldId == R.id.editVia) {
                    MainActivity.textVia.setText(resultset.get(position).toString());
                }
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_such_hilfe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
