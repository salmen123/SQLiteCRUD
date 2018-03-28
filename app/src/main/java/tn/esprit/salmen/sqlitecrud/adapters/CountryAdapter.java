package tn.esprit.salmen.sqlitecrud.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import tn.esprit.salmen.sqlitecrud.R;
import tn.esprit.salmen.sqlitecrud.helpers.SQLiteCountryHelper;
import tn.esprit.salmen.sqlitecrud.models.Country;

/**
 * Created by Salmen on 26/03/2018.
 */

public class CountryAdapter extends BaseAdapter {

    private Activity context;
    public ArrayList<Country> countries;
    private PopupWindow pwindo;
    SQLiteCountryHelper db;
    BaseAdapter ba;

    public CountryAdapter(Activity context, ArrayList<Country> countries, SQLiteCountryHelper db) {
        this.context = context;
        this.countries = countries;
        this.db = db;
    }

    public static class CountryHolder
    {
        TextView textViewId;
        TextView textViewCountry;
        TextView textViewPopulation;
        Button editButton;
        Button deleteButton;
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        CountryHolder ch;
        if (convertView == null) {
            ch = new CountryHolder();
            row = inflater.inflate(R.layout.item_country, null, true);

            ch.textViewId = (TextView) row.findViewById(R.id.textViewId);
            ch.textViewCountry = (TextView) row.findViewById(R.id.textViewCountry);
            ch.textViewPopulation = (TextView) row.findViewById(R.id.textViewPopulation);
            ch.editButton = (Button) row.findViewById(R.id.edit);
            ch.deleteButton = (Button) row.findViewById(R.id.delete);

            // store the holder with the view.
            row.setTag(ch);
        }
        else {
            ch = (CountryHolder) convertView.getTag();
        }

        ch.textViewCountry.setText(countries.get(position).getCountryName());
        ch.textViewId.setText("" + countries.get(position).getId());
        ch.textViewPopulation.setText("" + countries.get(position).getPopulation());
        final int positionPopup = position;
        ch.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Save: ", "" + positionPopup);
                editPopup(positionPopup);
            }
        });
        ch.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Last Index", "" + positionPopup);

                //     Integer index = (Integer) view.getTag();
                db.deleteCountry(countries.get(positionPopup));

                //      countries.remove(index.intValue());
                countries = (ArrayList) db.getAllCountries();
                Log.d("Country size", "" + countries.size());
                notifyDataSetChanged();
            }
        });
        return  row;
    }

    public void editPopup(final int positionPopup)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.popup,
                (ViewGroup) context.findViewById(R.id.popup_element));
        pwindo = new PopupWindow(layout, 600, 670, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        final EditText countryEdit = (EditText) layout.findViewById(R.id.editTextCountry);
        final EditText populationEdit = (EditText) layout.findViewById(R.id.editTextPopulation);
        countryEdit.setText(countries.get(positionPopup).getCountryName());
        populationEdit.setText("" + countries.get(positionPopup).getPopulation());
        Log.d("Name: ", "" + countries.get(positionPopup).getPopulation());
        Button save = (Button) layout.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryStr = countryEdit.getText().toString();
                String population = populationEdit.getText().toString();
                Country country = countries.get(positionPopup);
                country.setCountryName(countryStr);
                country.setPopulation(Long.parseLong(population));
                db.updateCountry(country);
                countries = (ArrayList) db.getAllCountries();
                notifyDataSetChanged();
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
