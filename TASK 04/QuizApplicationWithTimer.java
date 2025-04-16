import java.util.*;
import java.util.concurrent.*;

class Question {
    private String question;
    private List<String> options;
    private int correctAnswer;
    private int timeLimit; // in seconds

    public Question(String question, List<String> options, int correctAnswer, int timeLimit) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.timeLimit = timeLimit;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public int getTimeLimit() {
        return timeLimit;
    }
}

class Quiz {
    private List<Question> questions;
    private int score;
    private List<Boolean> answers;
    private static final int QUESTIONS_PER_QUIZ = 10;

    public Quiz() {
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        initializeQuestions();
        randomizeQuestions();
    }

    private void initializeQuestions() {
        // Add more questions
        questions.add(new Question(
            "What is the capital of France?",
            Arrays.asList("London", "Paris", "Berlin", "Madrid"),
            1, // Paris is at index 1
            10
        ));

        questions.add(new Question(
            "Which planet is known as the Red Planet?",
            Arrays.asList("Venus", "Mars", "Jupiter", "Saturn"),
            1, // Mars is at index 1
            15
        ));

        questions.add(new Question(
            "What is 2 + 2?",
            Arrays.asList("3", "4", "5", "6"),
            1, // 4 is at index 1
            5
        ));

        questions.add(new Question(
            "Who painted the Mona Lisa?",
            Arrays.asList("Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Michelangelo"),
            2, // Leonardo da Vinci is at index 2
            15
        ));

        questions.add(new Question(
            "What is the largest ocean on Earth?",
            Arrays.asList("Atlantic", "Indian", "Arctic", "Pacific"),
            3, // Pacific is at index 3
            10
        ));

        questions.add(new Question(
            "Which country is home to the kangaroo?",
            Arrays.asList("New Zealand", "South Africa", "Australia", "Brazil"),
            2, // Australia is at index 2
            10
        ));

        questions.add(new Question(
            "What is the chemical symbol for gold?",
            Arrays.asList("Ag", "Fe", "Au", "Cu"),
            2, // Au is at index 2
            10
        ));

        questions.add(new Question(
            "Who wrote 'Romeo and Juliet'?",
            Arrays.asList("Charles Dickens", "Jane Austen", "William Shakespeare", "Mark Twain"),
            2, // Shakespeare is at index 2
            15
        ));

        questions.add(new Question(
            "What is the largest mammal in the world?",
            Arrays.asList("African Elephant", "Blue Whale", "Giraffe", "Polar Bear"),
            1, // Blue Whale is at index 1
            10
        ));

        questions.add(new Question(
            "Which element has the atomic number 1?",
            Arrays.asList("Helium", "Hydrogen", "Oxygen", "Carbon"),
            1, // Hydrogen is at index 1
            10
        ));

        questions.add(new Question(
            "What is the capital of Japan?",
            Arrays.asList("Beijing", "Seoul", "Tokyo", "Bangkok"),
            2, // Tokyo is at index 2
            10
        ));

        questions.add(new Question(
            "Which year did World War II end?",
            Arrays.asList("1943", "1944", "1945", "1946"),
            2, // 1945 is at index 2
            15
        ));

        questions.add(new Question(
            "What is the square root of 64?",
            Arrays.asList("6", "7", "8", "9"),
            2, // 8 is at index 2
            5
        ));

        questions.add(new Question(
            "Which planet is closest to the Sun?",
            Arrays.asList("Venus", "Mars", "Mercury", "Earth"),
            2, // Mercury is at index 2
            10
        ));

        questions.add(new Question(
            "Who is known as the father of computers?",
            Arrays.asList("Bill Gates", "Steve Jobs", "Charles Babbage", "Alan Turing"),
            2, // Charles Babbage is at index 2
            15
        ));
    }

    private void randomizeQuestions() {
        Collections.shuffle(questions);
        if (questions.size() > QUESTIONS_PER_QUIZ) {
            questions = questions.subList(0, QUESTIONS_PER_QUIZ);
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getScore() {
        return score;
    }

    public List<Boolean> getAnswers() {
        return answers;
    }

    public void checkAnswer(int questionIndex, int selectedOption) {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            Question question = questions.get(questionIndex);
            boolean isCorrect = (selectedOption == question.getCorrectAnswer());
            answers.add(isCorrect);
            if (isCorrect) {
                score++;
            }
        }
    }
}

public class QuizApplicationWithTimer {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Quiz quiz = new Quiz();
        System.out.println("Welcome to the Quiz Application!");
        System.out.println("You will have a limited time to answer each question.");
        System.out.println("Let's begin!\n");

        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question question = quiz.getQuestions().get(i);
            System.out.println("\nQuestion " + (i + 1) + ": " + question.getQuestion());
            
            List<String> options = question.getOptions();
            for (int j = 0; j < options.size(); j++) {
                System.out.println((j + 1) + ". " + options.get(j));
            }

            System.out.println("\nYou have " + question.getTimeLimit() + " seconds to answer.");
            
            // Start timer in a separate thread
            Thread timerThread = new Thread(() -> {
                try {
                    for (int time = question.getTimeLimit(); time > 0; time--) {
                        System.out.print("\rTime remaining: " + time + " seconds...");
                        Thread.sleep(1000);
                    }
                    System.out.println("\rTime's up! Moving to next question.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            timerThread.start();

            System.out.print("\nEnter your choice (1-4): ");
            int selectedOption = -1;
            
            try {
                selectedOption = scanner.nextInt() - 1;
                timerThread.interrupt(); // Stop the timer when answer is received
                
                if (selectedOption >= 0 && selectedOption < question.getOptions().size()) {
                    quiz.checkAnswer(i, selectedOption);
                } else {
                    System.out.println("Invalid input! Moving to next question.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Moving to next question.");
                scanner.next(); // Clear the invalid input
                timerThread.interrupt();
            }
        }

        // Display results
        System.out.println("\nQuiz completed!");
        System.out.println("Your final score: " + quiz.getScore() + "/" + quiz.getQuestions().size());
        
        if (!quiz.getAnswers().isEmpty()) {
            System.out.println("\nQuestion Summary:");
            for (int i = 0; i < quiz.getAnswers().size(); i++) {
                Question question = quiz.getQuestions().get(i);
                System.out.println("\nQuestion " + (i + 1) + ": " + question.getQuestion());
                System.out.println("Your answer: " + (quiz.getAnswers().get(i) ? "Correct" : "Incorrect"));
                System.out.println("Correct answer: " + (question.getCorrectAnswer() + 1) + ". " + 
                    question.getOptions().get(question.getCorrectAnswer()));
            }
        }

        scanner.close();
    }
} 