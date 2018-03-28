package tn.esprit.salmen.sqlitecrud.models;

/**
 * Created by Salmen on 26/03/2018.
 */

public class Country {

    int id;
    String countryName;
    long population;

    public Country() {
    }

    public Country(String countryName, long population) {
        this.countryName = countryName;
        this.population = population;
    }

    public Country(int id, String countryName, long population) {
        this.id = id;
        this.countryName = countryName;
        this.population = population;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }
}
