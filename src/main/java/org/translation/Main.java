package org.translation;

import java.util.*;

/**
 * Main class for this program.
 * The system will:
 * - prompt the user to pick a country name from a list
 * - prompt the user to pick the language they want it translated to from a list
 * - output the translation
 * - at any time, the user can type quit to quit the program
 */
public class Main {

    private static final String QUIT = "quit";
    private static final CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
    private static final LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

    /**
     * This is the main entry point of our Translation System!
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     *
     * @param args not used by the program
     */
    public static void main(String[] args) {

        // Using JSONTranslator to try out the whole program
        Translator translator = new JSONTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     *
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String countryName = promptForCountry(translator);
            if (QUIT.equalsIgnoreCase(countryName)) {
                break;
            }

            String countryCode = countryCodeConverter.fromCountry(countryName);
            if (countryCode == null) {
                System.out.println("Error: Country code not found for " + countryName);
                continue;
            }

            String languageName = promptForLanguage(translator, countryCode);
            if (QUIT.equalsIgnoreCase(languageName)) {
                break;
            }

            String languageCode = languageCodeConverter.fromLanguage(languageName);
            if (languageCode == null) {
                System.out.println("Error: Language code not found for " + languageName);
                continue;
            }

            String translation = translator.translate(countryCode, languageCode);
            if (translation == null) {
                System.out.println("Translation not available.");
            } else {
                System.out.println(countryName + " in " + languageName + " is " + translation);
            }

            System.out.println("Press enter to continue or type 'quit' to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (QUIT.equalsIgnoreCase(textTyped.trim())) {
                break;
            }
        }
    }

    private static String promptForCountry(Translator translator) {
        List<String> countryCodes = translator.getCountries();

        // Convert country codes to country names and sort them
        List<String> countryNames = new ArrayList<>();
        Map<String, String> nameToCodeMap = new HashMap<>();

        for (String code : countryCodes) {
            String name = countryCodeConverter.fromCountryCode(code);
            if (name != null) {
                countryNames.add(name);
                nameToCodeMap.put(name, code);
            }
        }

        Collections.sort(countryNames);

        // Print the country names
        for (String name : countryNames) {
            System.out.println(name);
        }

        System.out.println("Select a country from above (or type 'quit' to exit):");

        Scanner s = new Scanner(System.in);
        String input = s.nextLine().trim();

        if (QUIT.equalsIgnoreCase(input)) {
            return QUIT;
        }

        if (!nameToCodeMap.containsKey(input)) {
            System.out.println("Invalid country. Please try again.");
            return promptForCountry(translator);
        }

        return input;
    }

    private static String promptForLanguage(Translator translator, String countryCode) {

        // Get the list of language codes for the country
        List<String> languageCodes = translator.getCountryLanguages(countryCode);

        // Convert language codes to language names and sort them
        List<String> languageNames = new ArrayList<>();
        Map<String, String> nameToCodeMap = new HashMap<>();

        for (String code : languageCodes) {
            String name = languageCodeConverter.fromLanguageCode(code);
            if (name != null) {
                languageNames.add(name);
                nameToCodeMap.put(name, code);
            }
        }

        Collections.sort(languageNames);

        // Print the language names
        for (String name : languageNames) {
            System.out.println(name);
        }

        System.out.println("Select a language from above (or type 'quit' to exit):");

        Scanner s = new Scanner(System.in);
        String input = s.nextLine().trim();

        if (QUIT.equalsIgnoreCase(input)) {
            return QUIT;
        }

        if (!nameToCodeMap.containsKey(input)) {
            System.out.println("Invalid language. Please try again.");
            return promptForLanguage(translator, countryCode);
        }

        return input;
    }
}