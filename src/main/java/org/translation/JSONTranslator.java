package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // Instance variables to store the country and language mapping
    private final Map<String, Map<String, String>> countryTranslations = new HashMap<>();

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
        try {
            String jsonString = Files.readString(
                    Paths.get(getClass().getClassLoader().getResource(filename).toURI())
            );
            parseJSONData(new JSONArray(jsonString));

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Parses the JSON data and populates the countryTranslations map.
     *
     * @param jsonArray The JSONArray containing country data
     */
    private void parseJSONData(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject countryObject = jsonArray.getJSONObject(i);
            String countryCode = countryObject.optString("alpha3");

            if (isValidCountryCode(countryCode)) {
                countryTranslations.put(countryCode.toLowerCase(), getTranslations(countryObject));
            }
        }
    }

    /**
     * Checks if a country code is valid (non-null and non-empty).
     *
     * @param countryCode The country code to check
     * @return true if valid, false otherwise
     */
    private boolean isValidCountryCode(String countryCode) {
        return countryCode != null && !countryCode.isEmpty();
    }

    /**
     * Extracts translations from a given JSONObject, skipping non-language keys.
     *
     * @param countryObject The JSONObject containing country data
     * @return A map of language codes to translations
     */
    private Map<String, String> getTranslations(JSONObject countryObject) {
        Map<String, String> translations = new HashMap<>();
        for (String key : countryObject.keySet()) {
            if (isLanguageKey(key)) {
                Object value = countryObject.get(key);
                if (value instanceof String) {
                    translations.put(key, (String) value);
                }
            }
        }
        return translations;
    }

    /**
     * Determines if a key is a valid language key (not a country-related key).
     *
     * @param key The key to check
     * @return true if it is a language key, false otherwise
     */
    private boolean isLanguageKey(String key) {
        return !"id".equals(key) && !"alpha2".equals(key) && !"alpha3".equals(key) && !"numeric".equals(key);
    }

    /**
     * Returns the language abbreviations for all languages whose translations are
     * available for the given country.
     *
     * @param country The country code (alpha3)
     * @return List of language abbreviations available for this country
     */
    @Override
    public List<String> getCountryLanguages(String country) {
        Map<String, String> translations = countryTranslations.get(country.toLowerCase());
        if (translations == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(translations.keySet());
    }

    /**
     * Returns the country abbreviations for all countries whose translations are
     * available from this Translator.
     *
     * @return List of country abbreviations
     */
    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countryTranslations.keySet());
    }

    /**
     * Returns the name of the country based on the specified country abbreviation and language abbreviation.
     *
     * @param country  The country code (alpha3)
     * @param language The language code (alpha2)
     * @return The name of the country in the given language or null if no translation is available
     */
    @Override
    public String translate(String country, String language) {
        Map<String, String> translations = countryTranslations.get(country.toLowerCase());
        if (translations == null) {
            return null;
        }
        return translations.getOrDefault(language, null);
    }
}
