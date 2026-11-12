import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class StudentGradeTracker extends JFrame {

    ArrayList<Student> students = new ArrayList<>();
    int nextId = 1;

    JTextField nameField, marksField, searchField;
    JTable table;
    DefaultTableModel model;

    JLabel total, average, highest, lowest, passed, failed, topper;

    Color navy = new Color(20, 31, 52);
    Color blue = new Color(42, 99, 210);
    Color bg = new Color(243, 246, 251);

    public StudentGradeTracker() {

        setTitle("Student Grade Management System");
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(bg);

        // HEADER
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(navy);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 20));

        JLabel title = new JLabel("STUDENT GRADE TRACKER");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Manage Records | Track Performance | Generate Reports"
        );
        subtitle.setForeground(Color.LIGHT_GRAY);

        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        main.add(header, BorderLayout.NORTH);

        // CONTENT
        JPanel content = new JPanel();
        content.setBackground(bg);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // INPUT SECTION
        JPanel input = createPanel();
        input.setLayout(new GridLayout(2, 2, 10, 10));

        nameField = new JTextField();
        marksField = new JTextField();

        input.add(new JLabel("Student Name"));
        input.add(nameField);
        input.add(new JLabel("Marks (0-100)"));
        input.add(marksField);

        content.add(input);
        content.add(Box.createVerticalStrut(15));

        // BUTTONS
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBackground(bg);

        JButton add = button("Add Student", blue);
        JButton edit = button("Edit Selected", blue);
        JButton delete = button("Delete Selected", new Color(190, 65, 65));
        JButton export = button("Export CSV", blue);
        JButton clear = button("Clear All", blue);

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(export);
        buttons.add(clear);

        content.add(buttons);

        // SEARCH
        JPanel search = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        search.setBackground(bg);

        searchField = new JTextField(18);
        JButton searchBtn = button("Search", blue);
        JButton showAll = button("Show All", blue);

        search.add(new JLabel("Search:"));
        search.add(searchField);
        search.add(searchBtn);
        search.add(showAll);

        content.add(search);
        content.add(Box.createVerticalStrut(15));

        // STATISTICS
        JPanel stats = new JPanel(new GridLayout(2, 3, 12, 12));
        stats.setBackground(bg);

        total = addCard(stats, "TOTAL STUDENTS");
        average = addCard(stats, "AVERAGE MARKS");
        highest = addCard(stats, "HIGHEST MARKS");
        lowest = addCard(stats, "LOWEST MARKS");
        passed = addCard(stats, "PASSED STUDENTS");
        failed = addCard(stats, "FAILED STUDENTS");

        content.add(stats);
        content.add(Box.createVerticalStrut(15));

        // TOPPER
        JPanel topperPanel = createPanel();
        topper = new JLabel("TOPPER: No records available");
        topper.setFont(new Font("Arial", Font.BOLD, 15));
        topper.setForeground(blue);
        topperPanel.add(topper);

        content.add(topperPanel);
        content.add(Box.createVerticalStrut(15));

        // STUDENT TABLE
        JPanel tablePanel = createPanel();
        tablePanel.setLayout(new BorderLayout());

        JLabel tableTitle = new JLabel("STUDENT RECORDS");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 17));

        String[] columns = {
            "ID", "STUDENT NAME", "MARKS", "GRADE", "STATUS"
        };

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        content.add(tablePanel);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        main.add(scroll, BorderLayout.CENTER);
        add(main);

        // BUTTON ACTIONS
        add.addActionListener(e -> addStudent());
        edit.addActionListener(e -> editStudent());
        delete.addActionListener(e -> deleteStudent());
        export.addActionListener(e -> exportCSV());
        clear.addActionListener(e -> clearAll());

        searchBtn.addActionListener(e -> refreshTable());

        showAll.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        searchField.addActionListener(e -> refreshTable());

        refreshDashboard();
        setVisible(true);
    }

    // CREATE PANEL
    JPanel createPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 240)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return p;
    }

    // CREATE BUTTON
    JButton button(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }

    // STATISTICS CARD
    JLabel addCard(JPanel panel, String heading) {

        JPanel card = createPanel();
        card.setLayout(new BorderLayout());

        JLabel name = new JLabel(heading, SwingConstants.CENTER);
        name.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel value = new JLabel("0", SwingConstants.CENTER);
        value.setForeground(blue);
        value.setFont(new Font("Arial", Font.BOLD, 27));

        card.add(name, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        panel.add(card);
        return value;
    }

    // ADD STUDENT
    void addStudent() {

        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter student name");
            return;
        }

        try {
            double marks = Double.parseDouble(
                marksField.getText().trim()
            );

            if (marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(this,
                    "Marks must be between 0 and 100");
                return;
            }

            students.add(new Student(nextId++, name, marks));

            nameField.setText("");
            marksField.setText("");

            refreshDashboard();

            JOptionPane.showMessageDialog(this,
                "Student added successfully!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Enter valid numeric marks");
        }
    }

    // EDIT STUDENT
    void editStudent() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Select a student first");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        Student s = findStudent(id);

        if (s == null) return;

        JTextField newName = new JTextField(s.name);
        JTextField newMarks = new JTextField(
            String.valueOf(s.marks)
        );

        Object[] fields = {
            "Student Name:", newName,
            "Marks:", newMarks
        };

        int result = JOptionPane.showConfirmDialog(
            this, fields, "Edit Student",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String name = newName.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Name cannot be empty");
                return;
            }

            try {
                double marks = Double.parseDouble(
                    newMarks.getText().trim()
                );

                if (marks < 0 || marks > 100) {
                    JOptionPane.showMessageDialog(this,
                        "Marks must be between 0 and 100");
                    return;
                }

                s.name = name;
                s.marks = marks;

                refreshDashboard();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Enter valid marks");
            }
        }
    }

    // DELETE STUDENT
    void deleteStudent() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Select a student first");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        Student s = findStudent(id);

        if (s != null) {
            int confirm = JOptionPane.showConfirmDialog(
                this, "Delete selected student?",
                "Confirm", JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                students.remove(s);
                refreshDashboard();
            }
        }
    }

    // SEARCH AND DISPLAY TABLE
    void refreshTable() {

        model.setRowCount(0);

        String query = searchField.getText()
            .trim().toLowerCase();

        for (Student s : students) {

            if (s.name.toLowerCase().contains(query)
                || String.valueOf(s.id).contains(query)) {

                model.addRow(new Object[] {
                    s.id,
                    s.name,
                    s.marks,
                    s.getGrade(),
                    s.getStatus()
                });
            }
        }
    }

    // FIND STUDENT BY ID
    Student findStudent(int id) {
        for (Student s : students) {
            if (s.id == id) return s;
        }
        return null;
    }

    // UPDATE DASHBOARD
    void refreshDashboard() {

        refreshTable();

        int count = students.size();

        total.setText(String.valueOf(count));

        if (count == 0) {
            average.setText("0.00");
            highest.setText("0");
            lowest.setText("0");
            passed.setText("0");
            failed.setText("0");
            topper.setText("TOPPER: No records available");
            return;
        }

        double sum = 0;
        double max = students.get(0).marks;
        double min = students.get(0).marks;

        int passCount = 0;
        int failCount = 0;

        Student top = students.get(0);

        for (Student s : students) {

            sum += s.marks;

            if (s.marks > max) max = s.marks;
            if (s.marks < min) min = s.marks;

            if (s.marks >= 35) passCount++;
            else failCount++;

            if (s.marks > top.marks) top = s;
        }

        average.setText(String.format("%.2f", sum / count));
        highest.setText(String.valueOf(max));
        lowest.setText(String.valueOf(min));
        passed.setText(String.valueOf(passCount));
        failed.setText(String.valueOf(failCount));

        topper.setText(
            "TOPPER: " + top.name + " - " + top.marks + " marks"
        );
    }

    // EXPORT CSV
    void exportCSV() {

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No records available to export");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(
            new File("StudentGradeReport.csv")
        );

        if (chooser.showSaveDialog(this)
            == JFileChooser.APPROVE_OPTION) {

            try (PrintWriter out = new PrintWriter(
                chooser.getSelectedFile())) {

                out.println(
                    "ID,Student Name,Marks,Grade,Status"
                );

                for (Student s : students) {
                    out.println(
                        s.id + ",\"" +
                        s.name.replace("\"", "\"\"") + "\"," +
                        s.marks + "," +
                        s.getGrade() + "," +
                        s.getStatus()
                    );
                }

                JOptionPane.showMessageDialog(this,
                    "CSV exported successfully!");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting file");
            }
        }
    }

    // CLEAR ALL RECORDS
    void clearAll() {

        int confirm = JOptionPane.showConfirmDialog(
            this, "Delete all student records?",
            "Confirm", JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            students.clear();
            refreshDashboard();
        }
    }

    // STUDENT CLASS
    class Student {

        int id;
        String name;
        double marks;

        Student(int id, String name, double marks) {
            this.id = id;
            this.name = name;
            this.marks = marks;
        }

        String getGrade() {

            if (marks >= 90) return "A+";
            if (marks >= 80) return "A";
            if (marks >= 70) return "B";
            if (marks >= 60) return "C";
            if (marks >= 50) return "D";
            if (marks >= 35) return "E";

            return "F";
        }

        String getStatus() {
            return marks >= 35 ? "Pass" : "Fail";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new StudentGradeTracker()
        );
    }
}
