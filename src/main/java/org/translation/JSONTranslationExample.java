package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A minimal example of reading and using the JSON data from resources/sample.json.
 */
public class JSONTranslationExample {

    public static final int CANADA_INDEX = 30;
    private final JSONArray jsonArray;

    // Note: CheckStyle is configured so that we are allowed to omit javadoc for constructors
    public JSONTranslationExample() {
        try {
            // This next block of code reads in a file from the resources folder as a String,
            // which we then create a new JSONArray object from.
            URL resourceUrl = getClass().getClassLoader().getResource("sample.json");
            String jsonString = Files.readString(
                    Paths.get(resourceUrl.toURI())
            );
            this.jsonArray = new JSONArray(jsonString);
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the Spanish translation of Canada.
     * @return the Spanish translation of Canada
     */
    public String getCanadaCountryNameSpanishTranslation() {

        JSONObject canada = jsonArray.getJSONObject(CANADA_INDEX);
        return canada.getString("es");
    }

    /**
     * Returns the name of the country based on the provided country and language codes.
     * @param countryCode the country, as its three-letter code.
     * @param languageCode the language to translate to, as its two-letter code.
     * @return the translation of country to the given language or "Country not found" if there is no translation.
     */
    public String getCountryNameTranslation(String countryCode, String languageCode) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject countryObject = jsonArray.getJSONObject(i);
            String alpha3 = countryObject.getString("alpha3");
            if (alpha3.equals(countryCode)) {
                if (countryObject.has(languageCode)) {
                    return countryObject.getString(languageCode);
                } else {
                    return "Translation not available";
                }
            }
        }
        return "Country not found";
    }

    /**
     * Prints the Spanish translation of Canada.
     * @param args not used
     */
    public static void main(String[] args) {
        JSONTranslationExample jsonTranslationExample = new JSONTranslationExample();

        System.out.println("Spanish translation of Canada:");
        System.out.println(jsonTranslationExample.getCanadaCountryNameSpanishTranslation());

        String translation = jsonTranslationExample.getCountryNameTranslation("can", "es");
        System.out.println("\nTranslation of 'can' in 'es':");
        System.out.println(translation);

        // Additional examples
        translation = jsonTranslationExample.getCountryNameTranslation("usa", "fr");
        System.out.println("\nTranslation of 'usa' in 'fr':");
        System.out.println(translation);

        translation = jsonTranslationExample.getCountryNameTranslation("fra", "de");
        System.out.println("\nTranslation of 'fra' in 'de':");
        System.out.println(translation);

        translation = jsonTranslationExample.getCountryNameTranslation("xyz", "en");
        System.out.println("\nTranslation of 'xyz' in 'en':");
        System.out.println(translation);
    }
}