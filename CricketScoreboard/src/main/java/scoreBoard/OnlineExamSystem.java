package scoreBoard;

import java.util.*;

class Question {
    String question;
    String opt1, opt2, opt3, opt4;
    int correct;

    Question(String question, String opt1, String opt2, String opt3, String opt4, int correct) {
        this.question = question;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.correct = correct;
    }
}

public class OnlineExamSystem {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Question> list = new ArrayList<>();
    static int lastScore = -1;  

    public static void main(String[] args) {
        loginMenu();
    }

    static void loginMenu() {
        while (true) {
            System.out.println("\n===== LOGIN MENU =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1: adminLogin(); break;
                case 2: studentMenu(); break;
                case 3: System.out.println("Exiting..."); return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    
    static void adminLogin() {
        System.out.print("Enter Admin Username: ");
        String user = sc.nextLine();

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        if (user.equals("admin") && pass.equals("123")) {
            System.out.println("Login Successful!");
            adminMenu();
        } else {
            System.out.println("Invalid Credentials!");
        }
    }

   
    static void adminMenu() {
        int choice;

        do {
            System.out.println("\n===== ADMIN PANEL =====");
            System.out.println("1. Add Question");
            System.out.println("2. View Questions");
            System.out.println("3. Search Question");
            System.out.println("4. Delete Question");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: addQuestion(); break;
                case 2: viewQuestions(); break;
                case 3: searchQuestion(); break;
                case 4: deleteQuestion(); break;
                case 5: System.out.println("Logged Out."); break;
                default: System.out.println("Invalid Choice!");
            }

        } while (choice != 5);
    }

    
    static void studentMenu() {
        System.out.print("\nEnter Student Name: ");
        String name = sc.nextLine();

        int ch;

        do {
            System.out.println("\n===== STUDENT PANEL =====");
            System.out.println("Welcome " + name);
            System.out.println("1. View Exam Instructions");
            System.out.println("2. Start Exam");
            System.out.println("3. Check Last Score");
            System.out.println("4. Logout");

            System.out.print("Enter choice: ");
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1: showInstructions(); break;
                case 2: startExam(); break;
                case 3: viewLastScore(); break;
                case 4: System.out.println("Logged out."); break;
                default: System.out.println("Invalid choice!");
            }
        } while (ch != 4);
    }

    

    static void showInstructions() {
        System.out.println("\n===== EXAM INSTRUCTIONS =====");
        System.out.println("1. Each question has 4 options.");
        System.out.println("2. Choose 1 correct answer (1-4).");
        System.out.println("3. No negative marking.");
        System.out.println("4. At the end, press SUBMIT to finish exam.");
    }

    static void viewLastScore() {
        if (lastScore == -1)
            System.out.println("No exam attempted yet.");
        else
            System.out.println("Last Score: " + lastScore + "%");
    }

    
    static void addQuestion() {
        System.out.print("Enter Question: ");
        String q = sc.nextLine();

        System.out.print("Option 1: ");
        String o1 = sc.nextLine();

        System.out.print("Option 2: ");
        String o2 = sc.nextLine();

        System.out.print("Option 3: ");
        String o3 = sc.nextLine();

        System.out.print("Option 4: ");
        String o4 = sc.nextLine();

        System.out.print("Correct Option (1-4): ");
        int c = sc.nextInt();

        list.add(new Question(q, o1, o2, o3, o4, c));
        System.out.println("Question Added!");
    }

    
    static void viewQuestions() {
        if (list.isEmpty()) {
            System.out.println("No questions available!");
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            Question q = list.get(i);

            System.out.println("\nQ" + (i+1) + ": " + q.question);
            System.out.println("1) " + q.opt1);
            System.out.println("2) " + q.opt2);
            System.out.println("3) " + q.opt3);
            System.out.println("4) " + q.opt4);
            System.out.println("Correct: " + q.correct);
        }
    }

    
    static void searchQuestion() {
        System.out.print("Enter keyword: ");
        String key = sc.nextLine().toLowerCase();

        boolean found = false;

        for (Question q : list) {
            if (q.question.toLowerCase().contains(key)) {
                System.out.println("Found: " + q.question);
                found = true;
            }
        }

        if (!found) System.out.println("Not found.");
    }

   
    static void deleteQuestion() {
        viewQuestions();
        if (list.isEmpty()) return;

        System.out.print("Enter question number to delete: ");
        int n = sc.nextInt();

        if (n <= 0 || n > list.size()) {
            System.out.println("Invalid number!");
            return;
        }

        list.remove(n - 1);
        System.out.println("Deleted Successfully!");
    }

    
    static void startExam() {
        if (list.isEmpty()) {
            System.out.println("No questions available!");
            return;
        }

        ArrayList<Integer> answers = new ArrayList<>();

        System.out.println("\n===== EXAM STARTED =====");

        for (int i = 0; i < list.size(); i++) {
            Question q = list.get(i);

            System.out.println("\nQ" + (i+1) + ": " + q.question);
            System.out.println("1) " + q.opt1);
            System.out.println("2) " + q.opt2);
            System.out.println("3) " + q.opt3);
            System.out.println("4) " + q.opt4);
            System.out.print("Your Answer: ");

            answers.add(sc.nextInt());
        }

        
        System.out.println("\nPress 1 to SUBMIT EXAM");
        int sub = sc.nextInt();

        if (sub == 1) calculateResult(answers);
        else System.out.println("Exam not submitted!");
    }

    static void calculateResult(ArrayList<Integer> answers) {
        int score = 0;

        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i) == list.get(i).correct) score++;
        }

        int percentage = (score * 100 / list.size());
        lastScore = percentage;

        System.out.println("\n===== RESULT =====");
        System.out.println("Total Questions: " + list.size());
        System.out.println("Correct: " + score);
        System.out.println("Score: " + percentage + "%");
    }
}
