import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Esquaretions extends JFrame {
    private JPanel esquaretions;
    private JLabel heading;
    private JLabel type;
    private JTextField equation;
    private JLabel solutionLabel;
    private JTextArea solution;
    private JLabel answerLabel;
    private JTextField answer;
    private JButton solve;
    static String request = "http://localhost:8080/Math/QuadraticEquations?equation=";

    public Esquaretions() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(esquaretions);
        this.setPreferredSize(new Dimension(800, 450));
        this.setResizable(false);
        this.setTitle("Решение квадратных уравнений");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Esquaretions.png")));
        this.pack();

        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String requestEquation = equation.getText()
                        .replaceAll("\\s+", "")
                        .replaceAll("\\+", "%2B")
                        .replaceAll("\\^", "%5E");

                if (requestEquation.isEmpty() || requestEquation == null) {
                    JOptionPane.showMessageDialog(solve, "Введено пустое уравнение!");
                }
                else if (requestEquation.equals("Введите уравнение здесь")) {
                    JOptionPane.showMessageDialog(solve, "Введите уравнение!");
                }
                else {
                    try {
                        String solvedEquation = Request(requestEquation).replaceAll("<br>", "\n");
                        solution.setText(solvedEquation);
                        answer.setText(solvedEquation.substring(solvedEquation.lastIndexOf("\n")));
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(solve, "Ошибка запроса! \nОписание: " + e);
                    }
                }
            }
        });

        equation.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (equation.getText().equals("Введите уравнение здесь"))
                    equation.setText("");
            }
        });
        answer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                StringSelection selection = new StringSelection(answer.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                JOptionPane.showMessageDialog(null, "Ответ \"" + answer.getText() + "\" успешно скопирован!");
                answer.setFocusable(false);
                answer.setFocusable(true);
            }
        });
        answer.addFocusListener(new FocusAdapter() {
        });
    }

    public static String Request(String requestEquation) throws IOException {

        String answer = "";

        URL address = new URL(request + requestEquation);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.connect();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            answer += line;
        }
        bufferedReader.close();

        return answer;
    }

    public static void main(String[] args) {
        JFrame application = new Esquaretions();
        application.setVisible(true);
    }
}
