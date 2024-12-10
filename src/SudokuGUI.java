import javax.swing.*;
import java.awt.*;

public class SudokuGUI extends JFrame {
    private Sudoku sudoku;
    private JTextField[][] cells;
    private JComboBox<String> difficultyComboBox;

    public SudokuGUI() {
        sudoku = new Sudoku();
        cells = new JTextField[9][9];

        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel boardPanel = createBoard();
        JPanel controlPanel = createControls();

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setSize(500, 500);
        setResizable(false);

        startNewGame("Easy");

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createBoard() {
        JPanel boardPanel = new JPanel(new GridLayout(9, 9));
        Font font = new Font("Arial", Font.BOLD, 18);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JTextField(1);
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(font);
                boardPanel.add(cells[i][j]);
            }
        }

        return boardPanel;
    }

    private JPanel createControls() {
        JPanel controlPanel = new JPanel();

        difficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        JButton newGameButton = new JButton("New Game");
        JButton checkButton = new JButton("Check");
        JButton solveButton = new JButton("Solve");

        newGameButton.addActionListener(e -> startNewGame(difficultyComboBox.getSelectedItem().toString()));
        checkButton.addActionListener(e -> checkSolution());
        solveButton.addActionListener(e -> solvePuzzle());

        controlPanel.add(new JLabel("Difficulty:"));
        controlPanel.add(difficultyComboBox);
        controlPanel.add(newGameButton);
        controlPanel.add(checkButton);
        controlPanel.add(solveButton);

        return controlPanel;
    }

    private void startNewGame(String difficulty) {
        int diff = switch (difficulty) {
            case "Easy" -> 20;
            case "Medium" -> 35;
            case "Hard" -> 50;
            default -> 20;
        };

        SudokuGenerator generator = new SudokuGenerator();
        sudoku.newGame(generator.generate(diff));
        updateBoard();
    }

    private void updateBoard() {
        int[][] board = sudoku.getBoard();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
            }
        }
    }

    private void updateSudokuBoard() {
        int[][] board = sudoku.getBoard();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText();
                try {
                    int num = Integer.parseInt(text);
                    if (num >= 1 && num <= 9) {
                        board[i][j] = num;
                    } else {
                        board[i][j] = 0; // Nếu nhập sai, coi như ô trống
                    }
                } catch (NumberFormatException ex) {
                    board[i][j] = 0; // Nếu nhập sai, coi như ô trống
                }
            }
        }
    }


    private void checkSolution() {
        updateSudokuBoard();
        JOptionPane.showMessageDialog(this, sudoku.checkResult() ? "Correct!" : "Incorrect!");
    }

    private void solvePuzzle() {
        if (sudoku.solve()) {
            updateBoard();
        } else {
            JOptionPane.showMessageDialog(this, "No solution found!");
        }
    }
}
