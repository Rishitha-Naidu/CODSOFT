import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Course {
    private String courseCode;
    private String title;
    private String description;
    private int capacity;
    private String schedule;
    private ArrayList<String> registeredStudents;

    public Course(String courseCode, String title, String description, int capacity, String schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.registeredStudents = new ArrayList<>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getSchedule() {
        return schedule;
    }

    public int getAvailableSlots() {
        return capacity - registeredStudents.size();
    }

    public boolean registerStudent(String studentId) {
        if (registeredStudents.size() < capacity) {
            registeredStudents.add(studentId);
            return true;
        }
        return false;
    }

    public boolean removeStudent(String studentId) {
        return registeredStudents.remove(studentId);
    }

    @Override
    public String toString() {
        return "Course Code: " + courseCode + "\n" +
               "Title: " + title + "\n" +
               "Description: " + description + "\n" +
               "Capacity: " + capacity + "\n" +
               "Available Slots: " + getAvailableSlots() + "\n" +
               "Schedule: " + schedule;
    }
}

class Student {
    private String studentId;
    private String name;
    private ArrayList<String> registeredCourses;

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getRegisteredCourses() {
        return registeredCourses;
    }

    public boolean registerCourse(String courseCode) {
        if (!registeredCourses.contains(courseCode)) {
            registeredCourses.add(courseCode);
            return true;
        }
        return false;
    }

    public boolean dropCourse(String courseCode) {
        return registeredCourses.remove(courseCode);
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId + "\n" +
               "Name: " + name + "\n" +
               "Registered Courses: " + registeredCourses;
    }
}

class RegistrationSystem {
    private Map<String, Course> courses;
    private Map<String, Student> students;

    public RegistrationSystem() {
        this.courses = new HashMap<>();
        this.students = new HashMap<>();
    }

    public void addCourse(Course course) {
        courses.put(course.getCourseCode(), course);
    }

    public void addStudent(Student student) {
        students.put(student.getStudentId(), student);
    }

    public boolean registerStudentForCourse(String studentId, String courseCode) {
        if (students.containsKey(studentId) && courses.containsKey(courseCode)) {
            Student student = students.get(studentId);
            Course course = courses.get(courseCode);
            
            if (course.registerStudent(studentId) && student.registerCourse(courseCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean dropCourse(String studentId, String courseCode) {
        if (students.containsKey(studentId) && courses.containsKey(courseCode)) {
            Student student = students.get(studentId);
            Course course = courses.get(courseCode);
            
            if (course.removeStudent(studentId) && student.dropCourse(courseCode)) {
                return true;
            }
        }
        return false;
    }

    public void displayAvailableCourses() {
        System.out.println("\nAvailable Courses:");
        for (Course course : courses.values()) {
            System.out.println(course);
            System.out.println("-------------------");
        }
    }

    public void displayStudentCourses(String studentId) {
        if (students.containsKey(studentId)) {
            Student student = students.get(studentId);
            System.out.println("\nRegistered Courses for " + student.getName() + ":");
            for (String courseCode : student.getRegisteredCourses()) {
                if (courses.containsKey(courseCode)) {
                    System.out.println(courses.get(courseCode));
                    System.out.println("-------------------");
                }
            }
        } else {
            System.out.println("Student not found!");
        }
    }
}

public class StudentCourseRegistrationSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RegistrationSystem system = new RegistrationSystem();

        // Add some sample courses
        system.addCourse(new Course("CS101", "Introduction to Programming", "Basic programming concepts", 30, "Mon/Wed 10:00-11:30"));
        system.addCourse(new Course("CS102", "Data Structures", "Advanced data structures", 25, "Tue/Thu 13:00-14:30"));
        system.addCourse(new Course("MATH101", "Calculus I", "Basic calculus concepts", 40, "Mon/Wed/Fri 09:00-10:00"));

        while (true) {
            System.out.println("\nStudent Course Registration System");
            System.out.println("1. Register a new student");
            System.out.println("2. Display available courses");
            System.out.println("3. Register for a course");
            System.out.println("4. Drop a course");
            System.out.println("5. Display student's registered courses");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter student ID: ");
                    String studentId = scanner.nextLine();
                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();
                    system.addStudent(new Student(studentId, studentName));
                    System.out.println("Student registered successfully!");
                    break;

                case 2:
                    system.displayAvailableCourses();
                    break;

                case 3:
                    System.out.print("Enter student ID: ");
                    String regStudentId = scanner.nextLine();
                    System.out.print("Enter course code: ");
                    String regCourseCode = scanner.nextLine();
                    if (system.registerStudentForCourse(regStudentId, regCourseCode)) {
                        System.out.println("Registration successful!");
                    } else {
                        System.out.println("Registration failed! Course might be full or student already registered.");
                    }
                    break;

                case 4:
                    System.out.print("Enter student ID: ");
                    String dropStudentId = scanner.nextLine();
                    System.out.print("Enter course code: ");
                    String dropCourseCode = scanner.nextLine();
                    if (system.dropCourse(dropStudentId, dropCourseCode)) {
                        System.out.println("Course dropped successfully!");
                    } else {
                        System.out.println("Failed to drop course!");
                    }
                    break;

                case 5:
                    System.out.print("Enter student ID: ");
                    String viewStudentId = scanner.nextLine();
                    system.displayStudentCourses(viewStudentId);
                    break;

                case 6:
                    System.out.println("Thank you for using the system!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
} 