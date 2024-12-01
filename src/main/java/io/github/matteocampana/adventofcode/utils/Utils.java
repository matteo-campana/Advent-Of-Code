package io.github.matteocampana.adventofcode.utils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static void GetPuzzleInput(Date inputDate, String sessionCookie) {
        // Define date format for the input file E, MMM dd yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");
        String inputDateString = dateFormat.format(inputDate);

        System.out.println("Getting input for day " + inputDate);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(inputDate);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        try {
            // Get input from the website
            URL url = new URI("https://adventofcode.com/2024/day/" + day + "/input").toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", "session=" + sessionCookie);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    FileWriter fileWriter = new FileWriter("src/main/resources/input_" + inputDateString + ".txt")) {

                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }

                // Save input to file in resources folder
                fileWriter.write(content.toString());

                // Print message
                System.out.println("Input for day " + inputDateString + " saved to file");
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
