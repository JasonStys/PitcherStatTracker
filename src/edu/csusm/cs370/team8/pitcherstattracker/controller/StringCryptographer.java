package edu.csusm.cs370.team8.pitcherstattracker.controller;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringCryptographer {

    // Private function that gets a hashed string from a plaintext string.
    private static String stringHash(String input) {
        byte[] bytehash;
        // Prepends the length as added security.
        String str = Integer.toString(input.length()) + input;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            bytehash = md.digest(str.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, bytehash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            // Pad with leading zeros
            while (hexString.length() < 64)
            {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Public function that takes two strings, hashes them, interleaves them together then hashes again.
    // In effect, both the username and password must be a pair.
    public static String getPassHash(String username, String password) {
        String userHash = stringHash(username);
        String passHash = stringHash(password); // Hash both input strings
        StringBuilder fullHash = new StringBuilder();
        // For the minimum length between them
        for (int i = 0; i < userHash.length() || i < passHash.length(); i++) {
            if (i < userHash.length())
                fullHash.append(userHash.charAt(i)); // Add next character from userHash if it exists.
            if (i < passHash.length())
                fullHash.append(passHash.charAt(i)); // Add next character from passHash if it exists
        }
        return stringHash(fullHash.toString()); // hash the interleaved result, which is our response.
    }

    // From https://stackoverflow.com/a/34710967
    public static String sanitizeFilename(String input) {
        return input.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
}
