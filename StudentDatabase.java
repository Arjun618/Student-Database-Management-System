import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

class Student {
    String name;
    int rollNumber;
    String course;
    int totalMarks;

    public Student(String name, int rollNumber, String course, int totalMarks) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.course = course;
        this.totalMarks = totalMarks;
    }
}

class Node {
    Student student;
    Node next;

    public Node(Student student) {
        this.student = student;
        this.next = null;
    }
}

class StudentRecordManagement {
    private static final String FILE_PATH = "C:\\Users\\preti\\Desktop\\Record.txt";

    Node head;

    public StudentRecordManagement() {
        this.head = null;
    }

    public void insertRecord(Student student) {
        Node newNode = new Node(student);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        saveRecordsToFile();
        System.out.println("Record inserted successfully.");
    }

    public void deleteRecord(int rollNumber) {
        if (head == null) {
            System.out.println("Record not found.");
            return;
        }

        if (head.student.rollNumber == rollNumber) {
            head = head.next;
            saveRecordsToFile();
            System.out.println("Record deleted successfully.");
            return;
        }

        Node prev = head;
        Node current = head.next;

        while (current != null) {
            if (current.student.rollNumber == rollNumber) {
                prev.next = current.next;
                saveRecordsToFile();
                System.out.println("Record deleted successfully.");
                return;
            }
            prev = current;
            current = current.next;
        }

        System.out.println("Record not found.");
    }

    public Student searchRecord(int rollNumber) {
        Node current = head;
        while (current != null) {
            if (current.student.rollNumber == rollNumber) {
                return current.student;
            }
            current = current.next;
        }
        return null; // Record not found
    }

    private void saveRecordsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            Node current = head;
            while (current != null) {
                writer.write("Name: " + current.student.name);
                writer.newLine();
                writer.write("Roll Number: " + current.student.rollNumber);
                writer.newLine();
                writer.write("Course: " + current.student.course);
                writer.newLine();
                writer.write("Total Marks: " + current.student.totalMarks);
                writer.newLine();
                writer.write("----------------------");
                writer.newLine();
                current = current.next;
            }
            System.out.println("Records saved to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving records to file: " + e.getMessage());
        }
    }
}

class StudentRecordGUI extends JFrame implements ActionListener {
    private StudentRecordManagement recordManagement;
    private JTextField nameField, rollNumberField, courseField, marksField;
    private JTextArea outputArea;

    public StudentRecordGUI() {
        recordManagement = new StudentRecordManagement();

        setTitle("Student Record Management System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(nameLabel, constraints);

        nameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(nameField, constraints);

        JLabel rollNumberLabel = new JLabel("Roll Number:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(rollNumberLabel, constraints);

        rollNumberField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(rollNumberField, constraints);

        JLabel courseLabel = new JLabel("Course:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(courseLabel, constraints);

        courseField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(courseField, constraints);

        JLabel marksLabel = new JLabel("Total Marks:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(marksLabel, constraints);

        marksField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(marksField, constraints);

        JButton addButton = new JButton("Add Record");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(addButton, constraints);

        addButton.addActionListener(this);

        JLabel outputLabel = new JLabel("Output:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        panel.add(outputLabel, constraints);

        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        panel.add(scrollPane, constraints);

        getContentPane().add(panel);

        JButton searchButton = new JButton("Search Record");
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        panel.add(searchButton, constraints);

        searchButton.addActionListener(this);

        JButton deleteButton = new JButton("Delete Record");
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        panel.add(deleteButton, constraints);

        deleteButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add Record")) {
            String name = nameField.getText();
            int rollNumber = Integer.parseInt(rollNumberField.getText());
            String course = courseField.getText();
            int totalMarks = Integer.parseInt(marksField.getText());

            Student student = new Student(name, rollNumber, course, totalMarks);
            recordManagement.insertRecord(student);

            nameField.setText("");
            rollNumberField.setText("");
            courseField.setText("");
            marksField.setText("");

            outputArea.append("Record added successfully.\n");
        } else if (e.getActionCommand().equals("Search Record")) {
            int rollNumber = Integer.parseInt(rollNumberField.getText());
            Student student = recordManagement.searchRecord(rollNumber);
            if (student != null) {
                outputArea.append("Record found:\n");
                outputArea.append("Name: " + student.name + "\n");
                outputArea.append("Roll Number: " + student.rollNumber + "\n");
                outputArea.append("Course: " + student.course + "\n");
                outputArea.append("Total Marks: " + student.totalMarks + "\n");
            } else {
                outputArea.append("Record not found.\n");
            }
        } else if (e.getActionCommand().equals("Delete Record")) {
            int rollNumber = Integer.parseInt(rollNumberField.getText());
            recordManagement.deleteRecord(rollNumber);
            outputArea.append("Record deleted successfully.\n");
        }
    }
}

public class Project {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                StudentRecordGUI gui = new StudentRecordGUI();
                gui.setVisible(true);
            }
        });
    }
}
