package com.example.json2;

public class Country {
    private String continent;
    private String CountryName;
    private String capital;
    private String CountryCode;
    private long Area;
    private long population;

    public Country(String continent, String countryName, String capital, String CountryCode, long area, long population) {
        this.continent = continent;
        CountryName = countryName;
        this.capital = capital;
        this.CountryCode = CountryCode;
        Area = area;
        this.population = population;
    }

    public Country() {
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public String getContinent() {
        return continent;
    }

    public String getCountryName() {
        return CountryName;
    }

    public String getCapital() {
        return capital;
    }

    public long getArea() {
        return Area;
    }

    public long getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return "Continent: " + continent + "\n" +
                "CountryName: " + CountryName + "\n" +
                "Capital: " + capital + "\n" +
                "Area: " + Area + "\n" +
                "Population: " + population;
    }
}
