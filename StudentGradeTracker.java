import java.util.ArrayList;
import java.util.Scanner;

/**
 * Student Grade Tracker (console-based)
 * CodeAlpha Java Programming Internship - Task 1
 *
 * Features:
 *  - Add, view, update and delete students
 *  - Calculate average, highest and lowest scores
 *  - Summary report of all students
 *  - Data stored in an ArrayList
 */
public class StudentGradeTracker {

    // Simple model class for one student
    static class Student {
        private String name;
        private double grade;

        Student(String name, double grade) {
            this.name = name;
            this.grade = grade;
        }

        String getName() { return name; }
        double getGrade() { return grade; }
        void setName(String name) { this.name = name; }
        void setGrade(double grade) { this.grade = grade; }

        // Letter grade based on score
        String getLetterGrade() {
            if (grade >= 90) return "A";
            if (grade >= 80) return "B";
            if (grade >= 70) return "C";
            if (grade >= 60) return "D";
            return "F";
        }
    }

    private static final ArrayList<Student> students = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: addStudent(); break;
                case 2: viewStudents(); break;
                case 3: updateStudent(); break;
                case 4: deleteStudent(); break;
                case 5: showSummaryReport(); break;
                case 0: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("===== STUDENT GRADE TRACKER =====");
        System.out.println("1. Add student");
        System.out.println("2. View all students");
        System.out.println("3. Update student grade");
        System.out.println("4. Delete student");
        System.out.println("5. Show summary report");
        System.out.println("0. Exit");
        System.out.println("=================================");
    }

    // ---------- Operations ----------

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        double grade = readGrade("Enter grade (0-100): ");
        students.add(new Student(name, grade));
        System.out.println("Student added successfully.");
    }

    private static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No students recorded yet.");
            return;
        }
        System.out.println();
        System.out.printf("%-4s %-20s %-8s %-6s%n", "No.", "Name", "Grade", "Letter");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("%-4d %-20s %-8.2f %-6s%n",
                    i + 1, s.getName(), s.getGrade(), s.getLetterGrade());
        }
    }

    private static void updateStudent() {
        if (students.isEmpty()) {
            System.out.println("No students to update.");
            return;
        }
        viewStudents();
        int index = readInt("Enter student number to update: ") - 1;
        if (!isValidIndex(index)) {
            System.out.println("Invalid student number.");
            return;
        }
        double newGrade = readGrade("Enter new grade (0-100): ");
        students.get(index).setGrade(newGrade);
        System.out.println("Grade updated successfully.");
    }

    private static void deleteStudent() {
        if (students.isEmpty()) {
            System.out.println("No students to delete.");
            return;
        }
        viewStudents();
        int index = readInt("Enter student number to delete: ") - 1;
        if (!isValidIndex(index)) {
            System.out.println("Invalid student number.");
            return;
        }
        Student removed = students.remove(index);
        System.out.println("Deleted: " + removed.getName());
    }

    private static void showSummaryReport() {
        if (students.isEmpty()) {
            System.out.println("No data available for a report.");
            return;
        }

        double total = 0;
        Student highest = students.get(0);
        Student lowest = students.get(0);

        for (Student s : students) {
            total += s.getGrade();
            if (s.getGrade() > highest.getGrade()) highest = s;
            if (s.getGrade() < lowest.getGrade()) lowest = s;
        }
        double average = total / students.size();

        System.out.println();
        System.out.println("========== SUMMARY REPORT ==========");
        System.out.printf("%-4s %-20s %-8s %-6s%n", "No.", "Name", "Grade", "Letter");
        System.out.println("------------------------------------");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("%-4d %-20s %-8.2f %-6s%n",
                    i + 1, s.getName(), s.getGrade(), s.getLetterGrade());
        }
        System.out.println("------------------------------------");
        System.out.println("Total students : " + students.size());
        System.out.printf("Average score  : %.2f%n", average);
        System.out.printf("Highest score  : %.2f (%s)%n", highest.getGrade(), highest.getName());
        System.out.printf("Lowest score   : %.2f (%s)%n", lowest.getGrade(), lowest.getName());
        System.out.println("====================================");
    }

    // ---------- Helpers ----------

    private static boolean isValidIndex(int index) {
        return index >= 0 && index < students.size();
    }

    // Reads an integer safely (re-asks until valid)
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // Reads a grade between 0 and 100 safely
    private static double readGrade(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double grade = Double.parseDouble(line);
                if (grade >= 0 && grade <= 100) {
                    return grade;
                }
                System.out.println("Grade must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
