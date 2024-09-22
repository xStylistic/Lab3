package org.translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides the service of converting language codes to their names.
 */
public class LanguageCodeConverter {

    private final Map<String, String> codeToName = new HashMap<>();
    private final Map<String, String> nameToCode = new HashMap<>();

    /**
     * Default constructor which will load the language codes from "language-codes.txt"
     * in the resources folder.
     */
    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the language code data from.
     *
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public LanguageCodeConverter(String filename) {

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

                if (parts.length >= 2) {
                    String languageName = parts[0].trim();
                    String languageCode = parts[1].trim().toLowerCase();

                    if (!languageName.isEmpty() && !languageCode.isEmpty()) {
                        codeToName.put(languageCode, languageName);
                        nameToCode.put(languageName, languageCode);
                    }
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error reading language codes from " + filename, ex);
        }
    }

    /**
     * Returns the name of the language for the given language code.
     *
     * @param code the language code
     * @return the name of the language corresponding to the code
     */
    public String fromLanguageCode(String code) {
        if (code == null) {
            return null;
        }
        return codeToName.get(code.trim().toLowerCase());
    }

    /**
     * Returns the code of the language for the given language name.
     *
     * @param language the name of the language
     * @return the 2-letter code of the language
     */
    public String fromLanguage(String language) {
        if (language == null) {
            return null;
        }
        return nameToCode.get(language.trim());
    }

    /**
     * Returns how many languages are included in this code converter.
     *
     * @return how many languages are included in this code converter.
     */
    public int getNumLanguages() {
        return codeToName.size();
    }
}