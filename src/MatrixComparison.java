import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MatrixComparison extends JFrame {
    private int[][] A;
    private int[][] B;
    private int[] X;
    private int n;

    private JTextField sizeField;
    private JTable tableA;
    private JTable tableB;
    private JTextArea resultArea;

    public MatrixComparison() {
        setTitle("Matrix Comparison");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель введення розміру матриці
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel sizeLabel = new JLabel("Matrix size: ");
        sizeField = new JTextField(5);
        JButton loadButton = new JButton("Load from file");
        inputPanel.add(sizeLabel);
        inputPanel.add(sizeField);
        inputPanel.add(loadButton);

        add(inputPanel, BorderLayout.NORTH);

        // Таблиці для матриць A і B
        tableA = new JTable();
        tableB = new JTable();
        JPanel matrixPanel = new JPanel(new GridLayout(1, 2));
        matrixPanel.add(new JScrollPane(tableA));
        matrixPanel.add(new JScrollPane(tableB));
        add(matrixPanel, BorderLayout.CENTER);

        // Панель для результату
        resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        // Обробник натискання кнопки "Load from file"
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile();
            }
        });
    }

    private void loadFromFile() {
        try {
            n = Integer.parseInt(sizeField.getText());
            A = new int[n][n];
            B = new int[n][n];
            X = new int[n];

            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Scanner scanner = new Scanner(file);

                // Зчитування матриць A і B з файлу
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (scanner.hasNextInt()) {
                            A[i][j] = scanner.nextInt();
                        }
                    }
                }

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (scanner.hasNextInt()) {
                            B[i][j] = scanner.nextInt();
                        }
                    }
                }

                scanner.close();

                // Оновлення таблиць
                updateTable(tableA, A);
                updateTable(tableB, B);

                // Порівняння матриць та обчислення вектора X
                compareMatrices();
            }
        } catch (NumberFormatException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(JTable table, int[][] matrix) {
        DefaultTableModel model = new DefaultTableModel(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                model.setValueAt(matrix[i][j], i, j);
            }
        }
        table.setModel(model);
    }

    private void compareMatrices() {
        StringBuilder result = new StringBuilder("Vector X:\n");

        for (int i = 0; i < n; i++) {
            int negativeA = 0;
            int negativeB = 0;

            for (int j = 0; j < n; j++) {
                if (A[i][j] < 0) {
                    negativeA++;
                }
                if (B[i][j] < 0) {
                    negativeB++;
                }
            }

            // Порівнюємо кількість і присвоюємо значення в вектор X
            X[i] = (negativeA == negativeB) ? 1 : 0;
            result.append("X[").append(i).append("] = ").append(X[i]).append("\n");
        }

        resultArea.setText(result.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MatrixComparison app = new MatrixComparison();
            app.setVisible(true);
        });
    }
}
