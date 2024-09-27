package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    public static final String QUIT = "quit";

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        // Using JSONTranslator instead of InLabByHandTranslator
        Translator translator = new JSONTranslator("sample.json");
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter("country-codes.txt");
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter("language-codes.txt");

        runProgram(translator, countryCodeConverter, languageCodeConverter);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     * @param countryCodeConverter the converter for country codes
     * @param languageCodeConverter the converter for language codes
     */
    public static void runProgram(Translator translator, CountryCodeConverter countryCodeConverter,
                                  LanguageCodeConverter languageCodeConverter) {
        while (true) {
            String countryCode = promptForCountry(translator, countryCodeConverter);
            if (QUIT.equalsIgnoreCase(countryCode)) {
                break;
            }

            String languageCode = promptForLanguage(translator, countryCode, languageCodeConverter);
            if (QUIT.equalsIgnoreCase(languageCode)) {
                break;
            }

            String countryName = countryCodeConverter.fromCountryCode(countryCode);
            String languageName = languageCodeConverter.fromLanguageCode(languageCode);

            String translation = translator.translate(countryCode, languageCode);

            if (translation != null) {
                System.out.println(countryName + " in " + languageName + " is " + translation);
            }
            else {
                System.out.println("Translation not available.");
            }

            System.out.println("Press enter to continue or type 'quit' to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (QUIT.equalsIgnoreCase(textTyped)) {
                break;
            }
        }
    }

    private static String promptForCountry(Translator translator, CountryCodeConverter countryCodeConverter) {
        List<String> countryCodes = translator.getCountries();
        List<String> countryNames = new ArrayList<>();
        for (String code : countryCodes) {
            String name = countryCodeConverter.fromCountryCode(code);
            if (!"Unknown code".equals(name)) {
                countryNames.add(name);
            }
        }
        Collections.sort(countryNames);

        for (String name : countryNames) {
            System.out.println(name);
        }

        System.out.println("Select a country from above or type 'quit' to exit:");

        Scanner s = new Scanner(System.in);
        String countryName;
        String countryCode = QUIT;

        do {
            countryName = s.nextLine();
            if (QUIT.equalsIgnoreCase(countryName)) {
                countryCode = QUIT;
                break;
            }

            countryCode = countryCodeConverter.fromCountry(countryName);
            if ("Unknown country".equals(countryCode)) {
                System.out.println("Invalid country selected. Please try again.");
            }
        } while ("Unknown country".equals(countryCode));

        return countryCode;
    }

    private static String promptForLanguage(Translator translator, String countryCode,
                                            LanguageCodeConverter languageCodeConverter) {
        List<String> languageCodes = translator.getCountryLanguages(countryCode);
        List<String> languageNames = new ArrayList<>();
        for (String code : languageCodes) {
            String name = languageCodeConverter.fromLanguageCode(code);
            if (!"Unknown code".equals(name)) {
                languageNames.add(name);
            }
        }
        Collections.sort(languageNames);

        for (String name : languageNames) {
            System.out.println(name);
        }

        System.out.println("Select a language from above or type 'quit' to exit:");

        Scanner s = new Scanner(System.in);
        String languageName;
        String languageCode = QUIT;

        do {
            languageName = s.nextLine();
            if (QUIT.equalsIgnoreCase(languageName)) {
                languageCode = QUIT;
                break;
            }

            languageCode = languageCodeConverter.fromLanguage(languageName);
            if ("Unknown language".equals(languageCode)) {
                System.out.println("Invalid language selected. Please try again.");
            }
        } while ("Unknown language".equals(languageCode));

        return languageCode;
    }
}
