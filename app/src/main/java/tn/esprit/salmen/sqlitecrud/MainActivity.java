package tn.esprit.salmen.sqlitecrud;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

import tn.esprit.salmen.sqlitecrud.adapters.CountryAdapter;
import tn.esprit.salmen.sqlitecrud.helpers.SQLiteCountryHelper;
import tn.esprit.salmen.sqlitecrud.models.Country;

public class MainActivity extends AppCompatActivity {

    ArrayList<Country> countries;
    SQLiteCountryHelper db;
    Button btnSubmit;
    PopupWindow pwindo;
    Activity activity;
    ListView listView;
    CountryAdapter countryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        db= new SQLiteCountryHelper(this);
        listView = (ListView) findViewById(android.R.id.list);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPopUp();
            }
        });
        Log.d("MainActivity: ", "Before reading mainactivity");
        countries = (ArrayList) db.getAllCountries();

        for (Country country : countries) {
            String log = "Id: " + country.getId() + " ,Name: " + country.getCountryName() + " ,Population: " + country.getPopulation();
            // Writing Countries to log
            Log.d("Name: ", log);
        }


        CountryAdapter countryAdapter = new CountryAdapter(this, countries, db);
        listView.setAdapter(countryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(), "You Selected " + countries.get(position).getCountryName() + " as Country", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addPopUp() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.popup,
                (ViewGroup) activity.findViewById(R.id.popup_element));
        pwindo = new PopupWindow(layout, 600, 670, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        final EditText countryEdit = (EditText) layout.findViewById(R.id.editTextCountry);
        final EditText populationEdit = (EditText) layout.findViewById(R.id.editTextPopulation);

        Button save = (Button) layout.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryStr = countryEdit.getText().toString();
                String population = populationEdit.getText().toString();

                if(TextUtils.isEmpty(population))
                    population = "0";

                Country country = new Country(countryStr, Long.parseLong(population));
                db.addCountry(country);
                if(countryAdapter==null)
                {
                    countryAdapter = new CountryAdapter(activity, countries, db);
                    listView.setAdapter(countryAdapter);
                }
                countryAdapter.countries = (ArrayList) db.getAllCountries();
                ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                for (Country country1 : countries) {
                    String log = "Id: " + country1.getId() + " ,Name: " + country1.getCountryName() + " ,Population: " + country1.getPopulation();
                    // Writing Countries to log
                    Log.d("Name: ", log);
                }
                pwindo.dismiss();
            }
        });
    }
}
