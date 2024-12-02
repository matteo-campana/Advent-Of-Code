# Utils Class

The `Utils` class provides utility methods to download puzzle inputs for the Advent of Code 2024 event. It includes methods to download inputs for a specific day, a specific date, or all available inputs up to the current day.

## Methods

### `getPuzzleInput(Date inputDate, String sessionCookie)`

Downloads the puzzle input for a specific date.

- **Parameters:**
  - `inputDate`: The date for which to download the input.
  - `sessionCookie`: The session cookie for authentication.

### `getPuzzleInput(int day, String sessionCookie)`

Downloads the puzzle input for a specific day.

- **Parameters:**
  - `day`: The day number for which to download the input.
  - `sessionCookie`: The session cookie for authentication.

### `getAllPuzzleInputs(String sessionCookie)`

Downloads all puzzle inputs from day 1 to the current day.

- **Parameters:**
  - `sessionCookie`: The session cookie for authentication.

## Usage

To use the `Utils` class, run the `main` method. You will be prompted to enter your session cookie and choose whether to download the input for a specific date, a specific day, or all available inputs.
