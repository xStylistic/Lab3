package org.translation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final Map<String, Map<String, String>> countryTranslations;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     *
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        countryTranslations = new HashMap<>();

        // Read the file to get the data to populate things...
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                throw new RuntimeException("Resource file '" + filename + "' not found.");
            }

            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }

            String jsonString = jsonStringBuilder.toString();

            JSONArray jsonArray = new JSONArray(jsonString);

            // Use the data in the jsonArray to populate your instance variables
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);

                // Get the country code from "alpha3"
                String countryCode = countryObject.getString("alpha3").toLowerCase();

                Map<String, String> languageMap = new HashMap<>();

                // Iterate over the keys to get language codes and translations
                for (String key : countryObject.keySet()) {
                    if (!key.equals("id") && !key.equals("alpha2") && !key.equals("alpha3")) {
                        String languageCode = key.toLowerCase();
                        String translation = countryObject.getString(key);
                        languageMap.put(languageCode, translation);
                    }
                }

                countryTranslations.put(countryCode, languageMap);
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error reading translations from " + filename, ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        Map<String, String> languageMap = countryTranslations.get(country.toLowerCase());

        if (languageMap == null) {
            return new ArrayList<>();
        }

        // Return a copy to avoid aliasing
        return new ArrayList<>(languageMap.keySet());
    }

    @Override
    public List<String> getCountries() {
        // Return a copy to avoid aliasing
        return new ArrayList<>(countryTranslations.keySet());
    }

    @Override
    public String translate(String country, String language) {
        Map<String, String> languageMap = countryTranslations.get(country.toLowerCase());

        if (languageMap == null) {
            return null;
        }

        return languageMap.get(language.toLowerCase());
    }
}