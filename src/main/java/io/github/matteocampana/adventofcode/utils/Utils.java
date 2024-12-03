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

@SuppressWarnings("CallToPrintStackTrace")
public class Utils {

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
                Files.createDirectory(dayPath);
            }

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
                System.out.println("Input for day " + inputDateString + " saved to file");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void getPuzzleInput(int day, String sessionCookie) {
        Path inputPath = Paths.get("src/main/resources/input");
        System.out.println("Getting input for day " + day);


        try {
            // check if the folders path src/main/resources/input exists otherwise create
            // the necessary folders
            if (!Files.exists(inputPath)) {
                Path resourcesPath = Paths.get("src/main/resources");

                // create the necessary folders
                if (!Files.exists(resourcesPath))
                    Files.createDirectory(resourcesPath);

                if (!Files.exists(inputPath))
                    Files.createDirectory(inputPath);

                Files.createDirectory(resourcesPath);
            }

            Path dayPath = inputPath.resolve("day" + day);

            if (!Files.exists(dayPath)) {
                Files.createDirectory(dayPath);
            }

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
        } catch (IOException | URISyntaxException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public static void getAllPuzzleInputs(String sessionCookie) {

        // get the current date and set the maximum day

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

        try (Scanner scanner = new Scanner(System.in)) {
            do {
                System.out.println("Welcome to the Advent of Code 2024 puzzle input downloader!");
                System.out.println("To get the input for a specific day, you need to provide your session cookie.\n");

                System.out.println("To get your session cookie, follow these steps:");
                System.out.println("1. Open your browser and go to https://adventofcode.com/2024");
                System.out.println("2. Log in with your account");
                System.out.println("3. Open the developer tools (F12) and go to the 'Application' tab");
                System.out.println("4. Find the 'session' cookie in the 'Cookies' section and copy its value");

                System.out.print("Enter your session cookie: ");
                String sessionCookie = scanner.nextLine();

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
                        // noinspection CallToPrintStackTrace
                        e.printStackTrace();
                    }
                } else if (choice.equalsIgnoreCase("day")) {
                    System.out.print("Enter the day number for which you want to get the input (e.g., 1): ");
                    int day = scanner.nextInt();
                    getPuzzleInput(day, sessionCookie);
                } else if (choice.equalsIgnoreCase("all")) {
                    getAllPuzzleInputs(sessionCookie);
                } else {
                    System.out.println("Invalid choice. Please enter 'date', 'day', or 'all'.");
                }
                System.out.println("Do you want to download another input? (yes/no)");
                choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("no")) {
                    break;
                }
            } while (true);
        }

    }
}
