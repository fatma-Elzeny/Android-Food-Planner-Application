package com.example.foodplanner;

import java.util.HashMap;
import java.util.Map;

public class CountryFlagHelper {
    private static final Map<String, String> COUNTRY_FLAG_MAP = new HashMap<>();

    static {
        // Complete list of country flags (using ISO country codes)
        COUNTRY_FLAG_MAP.put("American", "us");
        COUNTRY_FLAG_MAP.put("British", "gb");
        COUNTRY_FLAG_MAP.put("Canadian", "ca");
        COUNTRY_FLAG_MAP.put("Chinese", "cn");
        COUNTRY_FLAG_MAP.put("Croatian", "hr");
        COUNTRY_FLAG_MAP.put("Dutch", "nl");
        COUNTRY_FLAG_MAP.put("Egyptian", "eg");
        COUNTRY_FLAG_MAP.put("Filipino", "ph");
        COUNTRY_FLAG_MAP.put("French", "fr");
        COUNTRY_FLAG_MAP.put("Greek", "gr");
        COUNTRY_FLAG_MAP.put("Indian", "in");
        COUNTRY_FLAG_MAP.put("Irish", "ie");
        COUNTRY_FLAG_MAP.put("Italian", "it");
        COUNTRY_FLAG_MAP.put("Jamaican", "jm");
        COUNTRY_FLAG_MAP.put("Japanese", "jp");
        COUNTRY_FLAG_MAP.put("Kenyan", "ke");
        COUNTRY_FLAG_MAP.put("Malaysian", "my");
        COUNTRY_FLAG_MAP.put("Mexican", "mx");
        COUNTRY_FLAG_MAP.put("Moroccan", "ma");
        COUNTRY_FLAG_MAP.put("Polish", "pl");
        COUNTRY_FLAG_MAP.put("Portuguese", "pt");
        COUNTRY_FLAG_MAP.put("Russian", "ru");
        COUNTRY_FLAG_MAP.put("Spanish", "es");
        COUNTRY_FLAG_MAP.put("Thai", "th");
        COUNTRY_FLAG_MAP.put("Tunisian", "tn");
        COUNTRY_FLAG_MAP.put("Turkish", "tr");
        COUNTRY_FLAG_MAP.put("Vietnamese", "vn");
    }

    public static String getFlagUrl(String countryName) {
        String countryCode = COUNTRY_FLAG_MAP.getOrDefault(countryName, "unknown");
        return String.format("https://flagcdn.com/w160/%s.png", countryCode.toLowerCase());
    }
}
