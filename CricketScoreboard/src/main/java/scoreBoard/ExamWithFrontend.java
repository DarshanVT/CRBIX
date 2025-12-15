package scoreBoard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class ExamWithFrontend {

    
    static class Question {
        String text;
        String a, b, c, d;
        int correct; 

        Question(String text, String a, String b, String c, String d, int correct) {
            this.text = text;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.correct = correct;
        }

        
        String toLine() {
            return escape(text) + "|" + escape(a) + "|" + escape(b) + "|" + escape(c) + "|" + escape(d) + "|" + correct;
        }

        static Question fromLine(String line) {
            try {
                String[] parts = line.split("\\|", 6);
                if (parts.length < 6) return null;
                String text = unescape(parts[0]);
                String a = unescape(parts[1]);
                String b = unescape(parts[2]);
                String c = unescape(parts[3]);
                String d = unescape(parts[4]);
                int corr = Integer.parseInt(parts[5]);
                return new Question(text, a, b, c, d, corr);
            } catch (Exception ex) {
                return null;
            }
        }

        static String escape(String s) {
            return s.replace("\n", " ").replace("|", " ");
        }

        static String unescape(String s) {
            return s;
        }
    }

    
    private final ArrayList<Question> questions = new ArrayList<>();
    private String currentStudent = "";
    private int lastStudentScore = -1;

    
    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel cards;

    
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"#", "Question", "Opt1", "Opt2", "Opt3", "Opt4", "Correct"}, 0);
    private final JTable questionTable = new JTable(tableModel);
    private final JTextField searchField = new JTextField();
    private final JLabel studentWelcome = new JLabel("", SwingConstants.CENTER);

    
    private ArrayList<Integer> studentAnswers;
    private int examIndex;

    
    private final File questionsFile = new File("questions.dat");

    
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "123";

    
    public ExamWithFrontend() {
        
        loadQuestionsFromFile();

        
        frame = new JFrame("Online Exam System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 640);
        frame.setLocationRelativeTo(null);

        
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        
        cards.add(buildHomePanel(), "home");
        cards.add(buildTeacherPanel(), "teacher");
        cards.add(buildStudentPanel(), "student");
        cards.add(buildExamPanel(), "exam");
        cards.add(buildResultPanel(), "result");

        frame.setContentPane(cards);

        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        frame.setVisible(true);
    }

    

    private JPanel buildHomePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Online Exam System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(15, 75, 130));
        p.add(title, BorderLayout.NORTH);

        
        JPanel center = new JPanel(new GridLayout(1, 2, 30, 0));
        center.setBorder(new EmptyBorder(30, 30, 30, 30));

        
        JPanel studentBox = new JPanel(new BorderLayout(10, 10));
        studentBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(16, 16, 16, 16)
        ));
        JLabel sTitle = new JLabel("Student", SwingConstants.CENTER);
        sTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        studentBox.add(sTitle, BorderLayout.NORTH);

        JPanel sCenter = new JPanel(new GridLayout(3, 1, 8, 8));
        JTextField studentNameInput = new JTextField();
        studentNameInput.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sCenter.add(new JLabel("Enter your name:"));
        sCenter.add(studentNameInput);

        JButton sLogin = new JButton("Login as Student");
        sLogin.setBackground(new Color(70, 130, 180)); sLogin.setForeground(Color.white);
        sLogin.addActionListener(e -> {
            String nm = studentNameInput.getText().trim();
            if (nm.length() < 1) {
                JOptionPane.showMessageDialog(frame, "Please enter your name.", "Input required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            currentStudent = nm;
            studentWelcome.setText("Welcome, " + currentStudent);
            cardLayout.show(cards, "student");
        });
        sCenter.add(sLogin);
        studentBox.add(sCenter, BorderLayout.CENTER);

        
        JPanel teacherBox = new JPanel(new BorderLayout(10,10));
        teacherBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(16, 16, 16, 16)
        ));
        JLabel tTitle = new JLabel("Teacher", SwingConstants.CENTER);
        tTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        teacherBox.add(tTitle, BorderLayout.NORTH);

        JPanel tCenter = new JPanel(new GridLayout(3,1,8,8));
        tCenter.add(new JLabel("<html><i>Teacher login is secured. Enter credentials in the dialog .</i></html>"));
        JButton tLogin = new JButton("Login as Teacher");
        tLogin.setBackground(new Color(34, 139, 34)); tLogin.setForeground(Color.white);
        tLogin.addActionListener(e -> showTeacherLoginDialog());
        tCenter.add(new JLabel(""));
        tCenter.add(tLogin);
        teacherBox.add(tCenter, BorderLayout.CENTER);

        center.add(studentBox);
        center.add(teacherBox);

        p.add(center, BorderLayout.CENTER);

       
        JLabel footer = new JLabel("", SwingConstants.CENTER);
        footer.setForeground(Color.DARK_GRAY);
        p.add(footer, BorderLayout.SOUTH);

        return p;
    }

    private void showTeacherLoginDialog() {
        JPasswordField pwd = new JPasswordField();
        JTextField user = new JTextField();

        Object[] obj = {
                "Username:", user,
                "Password:", pwd
        };
        int result = JOptionPane.showConfirmDialog(frame, obj, "Teacher Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String u = user.getText().trim();
            String p = new String(pwd.getPassword());
            if (ADMIN_USER.equals(u) && ADMIN_PASS.equals(p)) {
                refreshQuestionTable();
                cardLayout.show(cards, "teacher");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    private JPanel buildTeacherPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(new EmptyBorder(12,12,12,12));

        JLabel title = new JLabel("Teacher Panel", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(40, 100, 60));
        p.add(title, BorderLayout.NORTH);

        
        JPanel left = new JPanel(new GridBagLayout());
        left.setPreferredSize(new Dimension(380, 0));
        left.setBorder(BorderFactory.createTitledBorder("Add New Question"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        left.add(new JLabel("Question:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        JTextArea qText = new JTextArea(3, 20);
        qText.setLineWrap(true); qText.setWrapStyleWord(true);
        JScrollPane qScroll = new JScrollPane(qText);
        left.add(qScroll, gbc);

        gbc.weightx = 0; gbc.gridx = 0; gbc.gridy++;
        left.add(new JLabel("Option A:"), gbc);
        gbc.gridx = 0; gbc.gridy++; left.add(new JLabel("Option B:"), gbc);
        gbc.gridx = 0; gbc.gridy++; left.add(new JLabel("Option C:"), gbc);
        gbc.gridx = 0; gbc.gridy++; left.add(new JLabel("Option D:"), gbc);

        // Options fields
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1;
        JTextField optA = new JTextField();
        left.add(optA, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField optB = new JTextField();
        left.add(optB, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JTextField optC = new JTextField();
        left.add(optC, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        JTextField optD = new JTextField();
        left.add(optD, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        left.add(new JLabel("Correct Option (1-4):"), gbc);
        gbc.gridx = 1;
        JComboBox<String> correctCombo = new JComboBox<>(new String[]{"1","2","3","4"});
        left.add(correctCombo, gbc);

        JButton addQBtn = new JButton("Add Question");
        addQBtn.setBackground(new Color(70,130,180)); addQBtn.setForeground(Color.white);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        left.add(addQBtn, gbc);

        addQBtn.addActionListener(e -> {
            String qt = qText.getText().trim();
            String a = optA.getText().trim();
            String b = optB.getText().trim();
            String c = optC.getText().trim();
            String d = optD.getText().trim();
            int corr = correctCombo.getSelectedIndex() + 1;
            if (qt.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Missing Data", JOptionPane.WARNING_MESSAGE);
                return;
            }
            questions.add(new Question(qt, a, b, c, d, corr));
            JOptionPane.showMessageDialog(frame, "Question added.");
            qText.setText(""); optA.setText(""); optB.setText(""); optC.setText(""); optD.setText("");
            refreshQuestionTable();
        });

        
        JPanel right = new JPanel(new BorderLayout(6,6));
        right.setBorder(BorderFactory.createTitledBorder("Questions"));

        
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        top.add(new JLabel("Search:"));
        searchField.setColumns(18);
        top.add(searchField);
        JButton searchBtn = new JButton("Search");
        top.add(searchBtn);
        JButton refreshBtn = new JButton("Refresh");
        top.add(refreshBtn);
        JButton saveBtn = new JButton("Save Questions");
        saveBtn.setBackground(new Color(34, 139, 34)); saveBtn.setForeground(Color.white);
        top.add(saveBtn);
        JButton loadBtn = new JButton("Load Questions");
        top.add(loadBtn);
        JButton backBtn = new JButton("Back to Home");
        top.add(backBtn);

        right.add(top, BorderLayout.NORTH);

        refreshBtn.addActionListener(e -> refreshQuestionTable());
        searchBtn.addActionListener(e -> searchQuestions());
        saveBtn.addActionListener(e -> {
            saveQuestionsToFile();
            JOptionPane.showMessageDialog(frame, "Questions saved.");
        });
        loadBtn.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(frame, "This will replace current unsaved questions. Continue?", "Load", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                loadQuestionsFromFile();
                refreshQuestionTable();
            }
        });
        backBtn.addActionListener(e -> cardLayout.show(cards, "home"));

        
        questionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        JScrollPane tableScroll = new JScrollPane(questionTable);
        right.add(tableScroll, BorderLayout.CENTER);

        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(220, 50, 50)); deleteBtn.setForeground(Color.white);
        bottom.add(deleteBtn);
        right.add(bottom, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> deleteSelectedQuestion());

        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(380);
        p.add(split, BorderLayout.CENTER);

        return p;
    }

    private void refreshQuestionTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            tableModel.addRow(new Object[]{i+1, q.text, q.a, q.b, q.c, q.d, q.correct});
        }
    }

    private void searchQuestions() {
        String key = searchField.getText().trim().toLowerCase();
        if (key.isEmpty()) {
            refreshQuestionTable();
            return;
        }
        tableModel.setRowCount(0);
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            if (q.text.toLowerCase().contains(key) ||
                    q.a.toLowerCase().contains(key) ||
                    q.b.toLowerCase().contains(key) ||
                    q.c.toLowerCase().contains(key) ||
                    q.d.toLowerCase().contains(key)) {
                tableModel.addRow(new Object[]{i+1, q.text, q.a, q.b, q.c, q.d, q.correct});
            }
        }
    }

    private void deleteSelectedQuestion() {
        int sel = questionTable.getSelectedRow();
        if (sel == -1) {
            JOptionPane.showMessageDialog(frame, "Select a question to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelIndex = sel; // our table rows align with list rows
        if (modelIndex >= 0 && modelIndex < questions.size()) {
            int ans = JOptionPane.showConfirmDialog(frame, "Delete selected question?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ans == JOptionPane.YES_OPTION) {
                questions.remove(modelIndex);
                refreshQuestionTable();
            }
        }
    }

    
    private JPanel buildStudentPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(new EmptyBorder(12,12,12,12));

        
        JPanel head = new JPanel(new BorderLayout());
        studentWelcome.setFont(new Font("SansSerif", Font.BOLD, 20));
        studentWelcome.setForeground(new Color(18, 90, 130));
        head.add(studentWelcome, BorderLayout.CENTER);

        JButton backHome = new JButton("Back to Home");
        backHome.addActionListener(e -> cardLayout.show(cards, "home"));
        head.add(backHome, BorderLayout.EAST);

        p.add(head, BorderLayout.NORTH);

        
        JTextArea instr = new JTextArea();
        instr.setEditable(false);
        instr.setLineWrap(true);
        instr.setWrapStyleWord(true);
        instr.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instr.setText("Exam Instructions:\n\n - You will see one question at a time.\n - Choose the correct option and press NEXT to go forward.\n - On the final question press SUBMIT to finish the exam.\n - No negative marking.\n - Good luck!");
        instr.setBorder(BorderFactory.createTitledBorder("Instructions"));
        p.add(instr, BorderLayout.CENTER);

        // bottom buttons
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 10));
        JButton start = new JButton("Start Exam");
        start.setPreferredSize(new Dimension(140, 40));
        JButton lastScoreBtn = new JButton("Last Score");
        JButton logout = new JButton("Logout");

        bottom.add(start);
        bottom.add(lastScoreBtn);
        bottom.add(logout);
        p.add(bottom, BorderLayout.SOUTH);

        start.addActionListener(e -> {
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No questions available. Ask teacher to add questions.");
                return;
            }
            startExamForStudent();
            cardLayout.show(cards, "exam");
        });

        lastScoreBtn.addActionListener(e -> {
            if (lastStudentScore == -1) JOptionPane.showMessageDialog(frame, "No previous score found.");
            else JOptionPane.showMessageDialog(frame, "Last Score: " + lastStudentScore + "%");
        });

        logout.addActionListener(e -> {
            currentStudent = "";
            lastStudentScore = lastStudentScore; 
            cardLayout.show(cards, "home");
        });

        return p;
    }

   
    private JPanel examPanel;
    private JLabel examQLabel;
    private final JRadioButton opt1 = new JRadioButton();
    private final JRadioButton opt2 = new JRadioButton();
    private final JRadioButton opt3 = new JRadioButton();
    private final JRadioButton opt4 = new JRadioButton();
    private final ButtonGroup answerGroup = new ButtonGroup();
    private final JButton nextOrSubmit = new JButton("Next");

    private JPanel buildExamPanel() {
        examPanel = new JPanel(new BorderLayout(10,10));
        examPanel.setBorder(new EmptyBorder(12,12,12,12));

        JLabel header = new JLabel("Exam in progress", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        examPanel.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(8,8));
        examQLabel = new JLabel("Question goes here");
        examQLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        examQLabel.setBorder(new EmptyBorder(8,8,8,8));
        center.add(examQLabel, BorderLayout.NORTH);

        JPanel opts = new JPanel(new GridLayout(4,1,6,6));
        opt1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        opt2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        opt3.setFont(new Font("SansSerif", Font.PLAIN, 14));
        opt4.setFont(new Font("SansSerif", Font.PLAIN, 14));
        answerGroup.add(opt1); answerGroup.add(opt2); answerGroup.add(opt3); answerGroup.add(opt4);
        opts.add(opt1); opts.add(opt2); opts.add(opt3); opts.add(opt4);
        center.add(opts, BorderLayout.CENTER);

        examPanel.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nextOrSubmit.setPreferredSize(new Dimension(120, 36));
        JButton cancel = new JButton("Cancel");
        bottom.add(cancel);
        bottom.add(nextOrSubmit);
        examPanel.add(bottom, BorderLayout.SOUTH);

        cancel.addActionListener(e -> {
            int ans = JOptionPane.showConfirmDialog(frame, "Cancel exam and return to student menu?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ans == JOptionPane.YES_OPTION) {
                cardLayout.show(cards, "student");
            }
        });

        nextOrSubmit.addActionListener(e -> onNextOrSubmitExam());

        return examPanel;
    }

    private void startExamForStudent() {
        studentAnswers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) studentAnswers.add(0);
        examIndex = 0;
        populateExamQuestion(examIndex);
    }

    private void populateExamQuestion(int idx) {
        Question q = questions.get(idx);
        examQLabel.setText("<html><b>Q" + (idx+1) + ".</b> " + q.text + "</html>");
        opt1.setText("1) " + q.a);
        opt2.setText("2) " + q.b);
        opt3.setText("3) " + q.c);
        opt4.setText("4) " + q.d);
        answerGroup.clearSelection();
        int prev = studentAnswers.get(idx);
        if (prev == 1) opt1.setSelected(true);
        if (prev == 2) opt2.setSelected(true);
        if (prev == 3) opt3.setSelected(true);
        if (prev == 4) opt4.setSelected(true);
        if (idx == questions.size() - 1) nextOrSubmit.setText("Submit");
        else nextOrSubmit.setText("Next");
    }

    private void onNextOrSubmitExam() {
        int selected = 0;
        if (opt1.isSelected()) selected = 1;
        if (opt2.isSelected()) selected = 2;
        if (opt3.isSelected()) selected = 3;
        if (opt4.isSelected()) selected = 4;

        studentAnswers.set(examIndex, selected);

        if (examIndex == questions.size() - 1) {
            int ans = JOptionPane.showConfirmDialog(frame, "Submit exam now?", "Confirm Submit", JOptionPane.YES_NO_OPTION);
            if (ans == JOptionPane.YES_OPTION) {
                calculateAndShowResult();
            }
            return;
        }
        examIndex++;
        populateExamQuestion(examIndex);
    }

    
    private JPanel buildResultPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(new EmptyBorder(12,12,12,12));

        JLabel title = new JLabel("Exam Result", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(80, 30, 100));
        p.add(title, BorderLayout.NORTH);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(resultArea);
        p.add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backToStudent = new JButton("Back to Student Menu");
        backToStudent.addActionListener(e -> cardLayout.show(cards, "student"));
        bottom.add(backToStudent);
        p.add(bottom, BorderLayout.SOUTH);

        
        p.putClientProperty("resultArea", resultArea);
        return p;
    }

    private void calculateAndShowResult() {
        int correct = 0;
        StringBuilder details = new StringBuilder();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int ans = studentAnswers.get(i);
            boolean ok = ans == q.correct;
            if (ok) correct++;
            details.append("Q").append(i+1).append(": ").append(q.text).append("\n");
            details.append("  Your: ").append(ans == 0 ? "Not answered" : ans).append("   Correct: ").append(q.correct)
                    .append("\n");
            details.append(ok ? "   Correct\n\n" : "   Incorrect\n\n");
        }
        int percent = Math.round((correct * 100f) / questions.size());
        lastStudentScore = percent;

        
        JPanel resultCard = (JPanel) cards.getComponent(4);
        JTextArea ra = (JTextArea) resultCard.getClientProperty("resultArea");
        ra.setText("");
        ra.append("Student: " + currentStudent + "\n");
        ra.append("Score: " + correct + " / " + questions.size() + "    (" + percent + "%)\n\n");
        ra.append("Details:\n\n");
        ra.append(details.toString());

        cardLayout.show(cards, "result");
    }

    
    private void saveQuestionsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFile))) {
            for (Question q : questions) {
                bw.write(q.toLine());
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error saving questions: " + ex.getMessage());
        }
    }

    private void loadQuestionsFromFile() {
        questions.clear();
        if (!questionsFile.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(questionsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                Question q = Question.fromLine(line);
                if (q != null) questions.add(q);
            }
        } catch (IOException ex) {
            
            System.err.println("Failed to load questions: " + ex.getMessage());
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExamWithFrontend::new);
    }
}
