import javax.swing.*;
import java.awt.*;
import java.io.*;


public class demo extends Component {

    public JButton dosyaSec1;
    private JPanel panel1;

    private JTextField field1;
    private JButton dosyaSec2;
    private JTextField field2;

    private JButton button3;
    private JButton button4;
    private JTextField textFieldHiza1;
    private JTextField textFieldScore;
    private JTextField textFieldHiza2;

    private JTextField lenField1;
    private JTextField lenField2;
    private JButton export;


    private final int GAP = -2;
    private final int MATCH = 1;
    private final int MISMATCH = -1;

    private int matchValue = 0;

    private int score = 0;
    int[][] matrix;
    private String rowSequence;
    private String colSequence;
    private int rowLen;
    private int colLen;
    JTable table;
    static JFrame frame = new JFrame("demo");

    public demo() {


        button3.addActionListener(actionEvent -> {
            matrix = new int[rowLen + 1][colLen + 1];
            table = new JTable(matrix.length + 1, matrix.length + 1);

            // iç matris değerlerimizi algortimazmıdaki 3 farklı fonksiyona göre max değerleriyle doldurduk
            for (int i = 1; i < rowLen + 1; i++) {
                for (int j = 1; j < colLen + 1; j++) {
                    //0 0 sıfır matrisini 0 olarak belirledik
                    table.setValueAt(0, 0, 0);
                    table.setValueAt(i * GAP, i, 0);
                    table.setValueAt(j * GAP, 0, j);
                    matrix[0][0] = 0;
                    matrix[i][0] = matrix[i - 1][0] + GAP;
                    matrix[0][j] = matrix[0][j - 1] + GAP;
                    if (rowSequence.charAt(i - 1) == colSequence.charAt(j - 1)) {
                        matchValue = MATCH;
                    } else {
                        matchValue = MISMATCH;
                    }
                    // max değerleri bulmak için 3 farklı fonksiyonu kullanıyoruz
                    matrix[i][j] = Math.max(Math.max(matrix[i][j - 1] + GAP, matrix[i - 1][j - 1] + matchValue), matrix[i - 1][j] + GAP);
                    table.setValueAt(matrix[i][j], i, j);
                }
            }


            Font font = new Font("Verdana", Font.PLAIN, 12);
            table.setFont(font);
            table.setRowHeight(30);
            JFrame frame = new JFrame();
            frame.setSize(600, 400);
            frame.add(new JScrollPane(table));
            frame.setVisible(true);
        });


        dosyaSec1.addActionListener(actionEvent -> {

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                    int value = Integer.parseInt(br.readLine());
                    String stringValue = br.readLine();
                    lenField1.setText(String.valueOf(value));
                    field1.setText(stringValue);
                    rowSequence = stringValue;
                    rowLen = value;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        dosyaSec2.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                    int value = Integer.parseInt(br.readLine());
                    String stringValue = br.readLine();
                    lenField2.setText(String.valueOf(value));
                    colLen = value;
                    field2.setText(stringValue);
                    colSequence = stringValue;
                    colLen = value;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // traceback işlemi gerçekleştirir ve hızaları ve score değerini ekrana basar
        button4.addActionListener(actionEvent -> {

            StringBuilder hiza1Builder = new StringBuilder();
            StringBuilder hiza2Builder = new StringBuilder();

            int i = rowLen;
            int j = colLen;

            while (i > 0 && j > 0) {
                int score = matrix[i][j];
                int scoreDiag = matrix[i - 1][j - 1];
                int scoreUp = matrix[i - 1][j];

                if (score == scoreDiag + (rowSequence.charAt(i - 1) == colSequence.charAt(j - 1) ? MATCH : MISMATCH)) {
                    hiza1Builder.append(rowSequence.charAt(i - 1));
                    hiza2Builder.append(colSequence.charAt(j - 1));
                    i--;
                    j--;
                } else if (score == scoreUp + GAP) {
                    hiza1Builder.append(rowSequence.charAt(i - 1));
                    hiza2Builder.append("-");
                    i--;
                } else {
                    hiza1Builder.append("-");
                    hiza2Builder.append(colSequence.charAt(j - 1));
                    j--;
                }
            }

            while (i > 0) {
                hiza1Builder.append(rowSequence.charAt(i - 1));
                hiza2Builder.append("-");
                i--;
            }

            while (j > 0) {
                hiza1Builder.append("-");
                hiza2Builder.append(colSequence.charAt(j - 1));
                j--;
            }

            String hiza1 = hiza1Builder.reverse().toString();
            String hiza2 = hiza2Builder.reverse().toString();


            // score hesaplar

            for (int k = 0; k < hiza1.length(); k++) {
                if (hiza1.charAt(k) == hiza2.charAt(k)) {
                    score += MATCH;
                } else if (hiza1.charAt(k) == '-' || hiza2.charAt(k) == '-') {
                    score += GAP;
                } else {
                    score += MISMATCH;
                }
            }

            String scoreText = String.valueOf(score);

            textFieldHiza1.setText(hiza1);
            textFieldHiza2.setText(hiza2);
            textFieldScore.setText(scoreText);


        });

        export.addActionListener(actionEvent -> {
            try {
                FileOutputStream fileOut = new FileOutputStream("/home/tamerb/Downloads/needleman.xlsx");
                PrintWriter printWriter = new PrintWriter(fileOut);
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        printWriter.print(table.getValueAt(i, j) + "\t");
                    }
                    printWriter.println();
                }
                printWriter.close();
                JOptionPane.showMessageDialog(null, "Dosya başarıyla kaydedildi.");
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Dosya kaydedilemedi." + e);
            }
        });
    }


    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame.setTitle("needleman wunsch algoritması");
        frame.setContentPane(new demo().panel1);
        frame.getContentPane().setPreferredSize(new Dimension(1100, 600));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

}

