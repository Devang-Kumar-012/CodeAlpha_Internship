import java.util.ArrayList;
import java.util.Scanner;

class Student {
    String name;
    ArrayList<Integer> grades;

    Student(String name) {
        this.name = name;
        grades = new ArrayList<>();
    }

    void addGrade(int grade) {
        grades.add(grade);
    }

    double average() {
        if (grades.isEmpty()) return 0;
        int sum = 0;
        for (int g : grades) sum += g;
        return (double) sum / grades.size();
    }

    int highest() {
        if (grades.isEmpty()) return 0;
        int max = grades.get(0);
        for (int g : grades)
            if (g > max) max = g;
        return max;
    }

    int lowest() {
        if (grades.isEmpty()) return 0;
        int min = grades.get(0);
        for (int g : grades)
            if (g < min) min = g;
        return min;
    }
}

public class Student_Grade_Tracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        System.out.print("Enter number of students: ");
        int n = sc.nextInt();
        sc.nextLine(); // consume newline

        for (int i = 0; i < n; i++) {
            System.out.print("\nEnter student " + (i+1) + " name: ");
            String name = sc.nextLine();
            Student student = new Student(name);

            System.out.print("Enter number of grades for " + name + ": ");
            int gCount = sc.nextInt();

            System.out.println("Enter grades (0-100):");
            for (int j = 0; j < gCount; j++) {
                int grade = sc.nextInt();
                student.addGrade(grade);
            }
            sc.nextLine(); // consume newline
            students.add(student);
        }

        System.out.println("\n==== Summary Report ====");
        for (Student s : students) {
            System.out.println("Student: " + s.name);
            System.out.printf("Average: %.2f\n", s.average());
            System.out.println("Highest: " + s.highest());
            System.out.println("Lowest: " + s.lowest());
            System.out.println("-----------------------");
        }
        sc.close();
    }
}
