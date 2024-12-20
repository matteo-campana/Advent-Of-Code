package io.github.matteocampana.adventofcode.utils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;

@SuppressWarnings("CallToPrintStackTrace")
public class Utils {

    private static void downloadPuzzleInput(int day, String sessionCookie, Path dayPath)
            throws IOException, URISyntaxException {
        URL url = new URI("https://adventofcode.com/2024/day/" + day + "/input").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", "session=" + sessionCookie);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                FileWriter fileWriter = new FileWriter(dayPath.resolve("input.txt").toFile())) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                fileWriter.write(inputLine + "\n");
            }
            System.out.println("Input for day " + day + " saved to file");
        }
    }

    public static void getPuzzleInput(Date inputDate, String sessionCookie) {
        Path resourcesPath = Paths.get("src/main/resources/input");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String inputDateString = dateFormat.format(inputDate);
        System.out.println("Getting input for day " + inputDateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Path dayPath = resourcesPath.resolve("day" + day);
        try {
            if (!Files.exists(dayPath)) {
                Files.createDirectories(dayPath);
            }
            downloadPuzzleInput(day, sessionCookie, dayPath);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void getPuzzleInput(int day, String sessionCookie) {
        Path inputPath = Paths.get("src/main/resources/input");
        System.out.println("Getting input for day " + day);

        try {
            if (!Files.exists(inputPath)) {
                Files.createDirectories(inputPath);
            }

            Path dayPath = inputPath.resolve("day" + day);
            if (!Files.exists(dayPath)) {
                Files.createDirectories(dayPath);
            }
            downloadPuzzleInput(day, sessionCookie, dayPath);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void getAllPuzzleInputs(String sessionCookie) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int maxDay = 25;

        System.out.println("Getting all inputs for Advent of Code 2024 from day 1 to day " + currentDay);

        if (currentYear == 2024 && currentMonth == Calendar.DECEMBER && currentDay < 25) {
            maxDay = currentDay;
        }

        for (int day = 1; day <= maxDay; day++) {
            getPuzzleInput(day, sessionCookie);
        }
    }

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("Welcome to the Advent of Code 2024 puzzle input downloader!");

            String sessionCookie = dotenv.get("SESSION_COOKIE");
            if (sessionCookie == null || sessionCookie.isEmpty()) {
                System.out.println("Session cookie not found in .env file.");
                System.out.println("To get the input for a specific day, you need to provide your session cookie.\n");

                System.out.println("To get your session cookie, follow these steps:");
                System.out.println("1. Open your browser and go to https://adventofcode.com/2024");
                System.out.println("2. Log in with your account");
                System.out.println("3. Open the developer tools (F12) and go to the 'Application' tab");
                System.out.println("4. Find the 'session' cookie in the 'Cookies' section and copy its value");
                System.out.print("Enter your session cookie: ");
                sessionCookie = scanner.nextLine();
            }

            while (true) {
                System.out.print("Do you want to enter a date, a day number, or download all inputs? (date/day/all): ");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("date")) {
                    System.out.print("Enter the date for which you want to get the input (e.g., 12-01-2024): ");
                    String inputDateString = scanner.nextLine();

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date inputDate = dateFormat.parse(inputDateString);
                        getPuzzleInput(inputDate, sessionCookie);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (choice.equalsIgnoreCase("day")) {
                    System.out.print("Enter the day number for which you want to get the input (e.g., 1): ");
                    int day = Integer.parseInt(scanner.nextLine());
                    getPuzzleInput(day, sessionCookie);
                } else if (choice.equalsIgnoreCase("all")) {
                    getAllPuzzleInputs(sessionCookie);
                } else {
                    System.out.println("Invalid choice. Please enter 'date', 'day', or 'all'.");
                }
                System.out.println("\nDo you want to download another input? (yes/no)");

                choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("no")) {
                    break;
                }
                System.out.println("\n\n" + "#".repeat(100));
            }
        }
    }
}
