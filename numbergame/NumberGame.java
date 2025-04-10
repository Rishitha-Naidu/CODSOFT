package com.example.numbergame;  // Specify your desired package name

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NumberGame {
    private static final int EASY_MIN = 1;
    private static final int EASY_MAX = 50;
    private static final int MEDIUM_MIN = 1;
    private static final int MEDIUM_MAX = 100;
    private static final int HARD_MIN = 1;
    private static final int HARD_MAX = 200;
    
    private static int minNumber;
    private static int maxNumber;
    private static int maxAttempts;
    private static int hintCount;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int totalScore = 0;
        int roundsPlayed = 0;
        boolean playAgain = true;

        System.out.println("=== Welcome to the Number Guessing Game! ===");
        System.out.println("Choose your difficulty level:");
        System.out.println("1. Easy (1-50, 15 attempts, 3 hints)");
        System.out.println("2. Medium (1-100, 10 attempts, 2 hints)");
        System.out.println("3. Hard (1-200, 8 attempts, 1 hint)");
        
        int difficulty = 0;
        while (difficulty < 1 || difficulty > 3) {
            System.out.print("Enter difficulty (1-3): ");
            difficulty = scanner.nextInt();
            if (difficulty < 1 || difficulty > 3) {
                System.out.println("Invalid choice! Please enter 1, 2, or 3.");
            }
        }

        // Set game parameters based on difficulty
        switch (difficulty) {
            case 1:
                minNumber = EASY_MIN;
                maxNumber = EASY_MAX;
                maxAttempts = 15;
                hintCount = 3;
                break;
            case 2:
                minNumber = MEDIUM_MIN;
                maxNumber = MEDIUM_MAX;
                maxAttempts = 10;
                hintCount = 2;
                break;
            case 3:
                minNumber = HARD_MIN;
                maxNumber = HARD_MAX;
                maxAttempts = 8;
                hintCount = 1;
                break;
        }

        System.out.println("\n=== Game Rules ===");
        System.out.println("I'm thinking of a number between " + minNumber + " and " + maxNumber);
        System.out.println("You have " + maxAttempts + " attempts to guess the number");
        System.out.println("You have " + hintCount + " hints available");
        System.out.println("\nScoring System:");
        System.out.println("- You get more points for guessing in fewer attempts");
        System.out.println("- Maximum points per round: " + maxAttempts);
        System.out.println("- Points = " + maxAttempts + " - number of attempts + 1");
        System.out.println("- Using a hint reduces your maximum possible score by 2 points\n");

        while (playAgain) {
            roundsPlayed++;
            int secretNumber = random.nextInt(maxNumber - minNumber + 1) + minNumber;
            int attempts = 0;
            boolean guessedCorrectly = false;
            int roundScore = 0;
            int hintsUsed = 0;
            long startTime = System.currentTimeMillis();

            System.out.println("\n=== Round " + roundsPlayed + " ===");
            System.out.println("Current Total Score: " + totalScore);
            System.out.println("Average Score: " + (roundsPlayed > 1 ? String.format("%.2f", (double)totalScore / (roundsPlayed - 1)) : "0.00"));
            System.out.println("Hints remaining: " + (hintCount - hintsUsed));
            System.out.println("You have " + maxAttempts + " attempts to guess the number.");

            while (attempts < maxAttempts && !guessedCorrectly) {
                System.out.print("\nEnter your guess (or type 'hint' for a hint): ");
                String input = scanner.next().toLowerCase();
                
                if (input.equals("hint")) {
                    if (hintsUsed < hintCount) {
                        hintsUsed++;
                        String hint = getHint(secretNumber, minNumber, maxNumber);
                        System.out.println("Hint: " + hint);
                        System.out.println("Hints remaining: " + (hintCount - hintsUsed));
                        continue;
                    } else {
                        System.out.println("Sorry, you've used all your hints!");
                        continue;
                    }
                }

                int guess;
                try {
                    guess = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number or 'hint'");
                    continue;
                }

                attempts++;

                if (guess < minNumber || guess > maxNumber) {
                    System.out.println("Please enter a number between " + minNumber + " and " + maxNumber);
                    attempts--; // Don't count invalid attempts
                    continue;
                }

                if (guess < secretNumber) {
                    System.out.println("Too low! Attempts left: " + (maxAttempts - attempts));
                    if (Math.abs(guess - secretNumber) > (maxNumber - minNumber) / 2) {
                        System.out.println("You're way off! Try a much higher number.");
                    }
                } else if (guess > secretNumber) {
                    System.out.println("Too high! Attempts left: " + (maxAttempts - attempts));
                    if (Math.abs(guess - secretNumber) > (maxNumber - minNumber) / 2) {
                        System.out.println("You're way off! Try a much lower number.");
                    }
                } else {
                    long endTime = System.currentTimeMillis();
                    long timeElapsed = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
                    roundScore = (maxAttempts - attempts + 1) - (hintsUsed * 2);
                    System.out.println("\n=== Round Complete ===");
                    System.out.println("Congratulations! You guessed the correct number in " + attempts + " attempts!");
                    System.out.println("Time taken: " + timeElapsed + " seconds");
                    System.out.println("Round Score: " + roundScore + " points");
                    System.out.println("Hints used: " + hintsUsed);
                    guessedCorrectly = true;
                }

                if (!guessedCorrectly) {
                    System.out.println("Current round progress: " + attempts + "/" + maxAttempts + " attempts used");
                    if (attempts == maxAttempts / 2) {
                        System.out.println("Halfway through your attempts! Keep going!");
                    }
                }
            }

            if (!guessedCorrectly) {
                System.out.println("\n=== Round Complete ===");
                System.out.println("Sorry, you've run out of attempts. The number was: " + secretNumber);
                System.out.println("Round Score: 0 points");
            }

            totalScore += roundScore;
            System.out.println("\n=== Game Statistics ===");
            System.out.println("Current Total Score: " + totalScore);
            System.out.println("Rounds Played: " + roundsPlayed);
            System.out.println("Average Score: " + String.format("%.2f", (double)totalScore / roundsPlayed));
            System.out.println("Best Possible Score: " + (roundsPlayed * maxAttempts));
            System.out.println("Score Percentage: " + String.format("%.2f", (double)totalScore / (roundsPlayed * maxAttempts) * 100) + "%");

            System.out.print("\nDo you want to play again? (yes/no): ");
            String response = scanner.next().toLowerCase();
            playAgain = response.equals("yes");
        }

        System.out.println("\n=== Final Game Statistics ===");
        System.out.println("Total Rounds Played: " + roundsPlayed);
        System.out.println("Final Total Score: " + totalScore);
        System.out.println("Final Average Score: " + String.format("%.2f", (double)totalScore / roundsPlayed));
        System.out.println("Best Possible Score: " + (roundsPlayed * maxAttempts));
        System.out.println("Final Score Percentage: " + String.format("%.2f", (double)totalScore / (roundsPlayed * maxAttempts) * 100) + "%");
        System.out.println("\nThank you for playing!");
        
        scanner.close();
    }

    private static String getHint(int secretNumber, int min, int max) {
        Random random = new Random();
        int hintType = random.nextInt(3);
        
        switch (hintType) {
            case 0:
                return "The number is " + (secretNumber % 2 == 0 ? "even" : "odd");
            case 1:
                int range = (max - min) / 4;
                if (secretNumber <= min + range) {
                    return "The number is in the first quarter of the range";
                } else if (secretNumber <= min + 2 * range) {
                    return "The number is in the second quarter of the range";
                } else if (secretNumber <= min + 3 * range) {
                    return "The number is in the third quarter of the range";
                } else {
                    return "The number is in the last quarter of the range";
                }
            case 2:
                return "The number is " + (secretNumber > (min + max) / 2 ? "greater than " + (min + max) / 2 : "less than or equal to " + (min + max) / 2);
            default:
                return "The number is between " + min + " and " + max;
        }
    }
}
