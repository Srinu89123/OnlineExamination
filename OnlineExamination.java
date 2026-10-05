

import java.sql.*;
import java.util.Scanner;

public class OnlineExamination {

    static final String URL ="jdbc:mysql://localhost:3306/online_examination";
    static final String USERNAME = "root";
    static final String PASSWORD = "0907";
    static Scanner sc = new Scanner(System.in);

    // DATABASE CONNECTION
    
    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL,USERNAME,PASSWORD);

        }
        catch (Exception e) 
        {

            e.printStackTrace();

        }

        return con;
    }


    // STUDENT REGISTRATION
    
    public static void addStudent() {

        try {

            Connection con = getConnection();

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Username: ");
            String username = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            String sql = "INSERT INTO students " + "(student_name, username, password) " + "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);

            ps.executeUpdate();

            System.out.println("Student Registered Successfully!");

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    // STUDENT LOGIN
    
    public static int login() {

        int studentId = -1;

        try {

            Connection con = getConnection();

            System.out.print("Enter Username: ");
            String username = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            String sql = "SELECT * FROM students " + "WHERE username = ? AND password = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                studentId = rs.getInt("student_id");

                System.out.println("Login Successful!");
                System.out.println("Welcome " + rs.getString("student_name"));

            } else {

                System.out.println("Invalid Username or Password");

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return studentId;
    }


    // CREATE EXAM
    
    public static void createExam() {

        try {

            Connection con = getConnection();

            System.out.print("Enter Exam Name: ");
            String examName = sc.nextLine();

            System.out.print("Enter Subject: ");
            String subject = sc.nextLine();

            System.out.print("Enter Total Questions: ");
            int totalQuestions = sc.nextInt();
            sc.nextLine();

            String sql = "INSERT INTO exams " + "(exam_name, subject, total_questions) " + "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, examName);
            ps.setString(2, subject);
            ps.setInt(3, totalQuestions);

            ps.executeUpdate();

            System.out.println("Exam Created Successfully!");

            con.close();

        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        	
        }
        
    }


    // VIEW EXAMS
    
    public static void viewExams() {

        try {

            Connection con = getConnection();

            String sql = "SELECT * FROM exams";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\nEXAMS");

            while (rs.next()) {

                System.out.println("ID: " + rs.getInt("exam_id") + " | Name: " + rs.getString("exam_name") + " | Subject: " + rs.getString("subject") + " | Questions: " + rs.getInt("total_questions"));
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    // ADD QUESTION
    
    public static void addQuestion() {

        try {

            Connection con = getConnection();

            System.out.print("Enter Exam ID: ");
            int examId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Question: ");
            String question = sc.nextLine();

            System.out.print("Enter Option A: ");
            String a = sc.nextLine();

            System.out.print("Enter Option B: ");
            String b = sc.nextLine();

            System.out.print("Enter Option C: ");
            String c = sc.nextLine();

            System.out.print("Enter Option D: ");
            String d = sc.nextLine();

            System.out.print("Enter Correct Answer (A/B/C/D): ");
            String answer = sc.nextLine().toUpperCase();

            String sql = "INSERT INTO questions " + "(exam_id, question_text, option_a, option_b, " + "option_c, option_d, correct_answer) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, examId);
            ps.setString(2, question);
            ps.setString(3, a);
            ps.setString(4, b);
            ps.setString(5, c);
            ps.setString(6, d);
            ps.setString(7, answer);

            ps.executeUpdate();

            System.out.println("Question Added Successfully!");

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    // VIEW QUESTIONS
    
    public static void viewQuestions() {

        try {

            Connection con = getConnection();

            String sql = "SELECT * FROM questions";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {

                System.out.println(rs.getInt("question_id") + " | Exam ID: " + rs.getInt("exam_id") + " | " + rs.getString("question_text"));
            }

            con.close();

        } catch (Exception e){

            e.printStackTrace();

        }
    }


    // TAKE EXAM AND GENERATE RESUL

    public static void takeExam(int studentId) {

        try {

            Connection con = getConnection();

            System.out.print("Enter Exam ID: ");
            int examId = sc.nextInt();
            sc.nextLine();

            String sql ="SELECT * FROM questions " +"WHERE exam_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, examId);

            ResultSet rs = ps.executeQuery();

            int marks = 0;
            int totalQuestions = 0;

            while (rs.next()) {

                totalQuestions++;

                System.out.println("\nQuestion " +
                        totalQuestions + ":");

                System.out.println(
                        rs.getString("question_text")
                );

                System.out.println(
                        "A. " + rs.getString("option_a")
                );

                System.out.println(
                        "B. " + rs.getString("option_b")
                );

                System.out.println(
                        "C. " + rs.getString("option_c")
                );

                System.out.println(
                        "D. " + rs.getString("option_d")
                );

                System.out.print("Enter Answer: ");

                String answer = sc.nextLine().toUpperCase();

                String correct = rs.getString("correct_answer");

                if (answer.equals(correct)) {

                    marks++;

                }
            }

            if (totalQuestions == 0) {

                System.out.println("No questions found for this exam.");

                con.close();
                return;
            }

            double percentage =(marks * 100.0) / totalQuestions;

            String status;

            if (percentage >= 40)
            {

                status = "PASS";

            }
            else 
            {

                status = "FAIL";

            }

            String insert = "INSERT INTO results " +"(student_id, exam_id, marks, total_marks, " + "percentage, result_status) " + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps2 = con.prepareStatement(insert);

            ps2.setInt(1, studentId);
            ps2.setInt(2, examId);
            ps2.setInt(3, marks);
            ps2.setInt(4, totalQuestions);
            ps2.setDouble(5, percentage);
            ps2.setString(6, status);

            ps2.executeUpdate();

            System.out.println("\n======================");
            System.out.println("       RESULT");
            System.out.println("======================");

            System.out.println("Marks: " + marks + "/" + totalQuestions);

            System.out.println("Percentage: " + percentage + "%");

            System.out.println("Status: " + status);

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    // VIEW RESULTS USING JOIN
    
    public static void viewResults() {

        try {

            Connection con = getConnection();

            String sql = "SELECT students.student_name, " + "exams.exam_name, " + "results.marks, " + "results.total_marks, " + "results.percentage, " +
                         "results.result_status " + "FROM results " +  "JOIN students " +
                          "ON results.student_id = students.student_id " + "JOIN exams " + "ON results.exam_id = exams.exam_id";

            PreparedStatement ps =con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\nRESULTS");

            while (rs.next())
            {

                System.out.println(rs.getString("student_name") + " | " + rs.getString("exam_name") + " | Marks: " + rs.getInt("marks") + "/" +
                rs.getInt("total_marks") + " | Percentage: " + rs.getDouble("percentage") + "% | " + rs.getString("result_status"));
                         
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    // Hear Main Method ....
    
    public static void main(String[] args) {

        int choice;

        do {

            System.out.println("\n================================");
            System.out.println("   ONLINE EXAMINATION SYSTEM");
            System.out.println("================================");

            System.out.println("1. Student Registration");
            System.out.println("2. Student Login");
            System.out.println("3. Create Exam");
            System.out.println("4. View Exams");
            System.out.println("5. Add Question");
            System.out.println("6. View Questions");
            System.out.println("7. Take Exam");
            System.out.println("8. View Results");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:

                    int id = login();

                    if (id != -1) 
                    {
                        System.out.println("Student ID: " + id);
                    }

                    break;

                case 3:
                    createExam();
                    break;

                case 4:
                    viewExams();
                    break;

                case 5:
                    addQuestion();
                    break;

                case 6:
                    viewQuestions();
                    break;

                case 7:

                    System.out.print(
                            "Enter Student ID: "
                    );

                    int studentId = sc.nextInt();
                    sc.nextLine();

                    takeExam(studentId);

                    break;

                case 8:
                    viewResults();
                    break;

                case 9:
                    System.out.println("Thank You!");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 9);

        sc.close();
    }
}
