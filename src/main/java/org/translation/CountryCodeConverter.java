package org.translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {

    private final Map<String, String> codeToName = new HashMap<>();
    private final Map<String, String> nameToCode = new HashMap<>();

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     *
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean isFirstLine = true; // Skip header line

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip empty lines or comment lines
                if (line.isEmpty()) {
                    continue;
                }

                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Split the line on tab character
                String[] parts = line.split("\t");

                if (parts.length >= 4) {
                    String countryName = parts[0].trim();
                    String alpha3Code = parts[2].trim().toLowerCase();

                    if (!countryName.isEmpty() && !alpha3Code.isEmpty()) {
                        codeToName.put(alpha3Code, countryName);
                        nameToCode.put(countryName, alpha3Code);
                    }
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error reading country codes from " + filename, ex);
        }
    }

    /**
     * Returns the name of the country for the given country code.
     *
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        if (code == null) {
            return null;
        }
        return codeToName.get(code.trim().toLowerCase());
    }

    /**
     * Returns the code of the country for the given country name.
     *
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        if (country == null) {
            return null;
        }
        return nameToCode.get(country.trim());
    }

    /**
     * Returns how many countries are included in this code converter.
     *
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return codeToName.size();
    }
}