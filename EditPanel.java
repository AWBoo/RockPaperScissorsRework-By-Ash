import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class EditPanel extends JPanel implements ActionListener {
    private static JLabel RedCount;
    private static JLabel GreenCount;
    private static JLabel BlueCount;

    public EditPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 10, 5, 10); // Add padding


        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel rockLabel = new JLabel("Rock (Red):");
        rockLabel.setForeground(Color.WHITE);
        add(rockLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField rockInput = new JTextField(10);
        add(rockInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel paperLabel = new JLabel("Paper (Green):");
        paperLabel.setForeground(Color.WHITE);
        add(paperLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField paperInput = new JTextField(10);
        add(paperInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel ScissorsLabel = new JLabel("Scissors (Blue):");
        ScissorsLabel.setForeground(Color.WHITE);
        add(ScissorsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JTextField ScissorsInput = new JTextField(10);
        add(ScissorsInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton UpdateSimButton = new JButton("Update");
        UpdateSimButton.addActionListener(this);
        add(UpdateSimButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        JLabel Rule = new JLabel("Only Numbers");
        Rule.setForeground(Color.WHITE);
        add(Rule, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel RedCountLabel = new JLabel("Red Count");
        RedCountLabel.setForeground(Color.WHITE);
        add(RedCountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        RedCount = new JLabel(""); // Initialize RedCount JLabel
        RedCount.setForeground(Color.WHITE);
        add(RedCount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel GreenCountLabel = new JLabel("Green Count: ");
        GreenCountLabel.setForeground(Color.WHITE);
        add(GreenCountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        GreenCount = new JLabel(""); // Using the class-level variable here
        GreenCount.setForeground(Color.WHITE);
        add(GreenCount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel BlueCountLabel = new JLabel("Blue Count: ");
        BlueCountLabel.setForeground(Color.WHITE);
        add(BlueCountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        BlueCount = new JLabel(""); // Using the class-level variable here
        BlueCount.setForeground(Color.WHITE);
        add(BlueCount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel RadiusButtonLabel = new JLabel("Ball Radius:");
        RadiusButtonLabel.setForeground(Color.WHITE);
        add(RadiusButtonLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        JTextField RadiusButtonInput = new JTextField(10);
        add(RadiusButtonInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        JButton StopSimButton = new JButton("Pause");
        StopSimButton.addActionListener(this);
        add(StopSimButton, gbc);

        UpdateSimButton.addActionListener(e -> {
            try {
                SimPanel.cleanBallArray();

                String paperInputText = paperInput.getText();
                String scissorsInputText = ScissorsInput.getText();
                String rockInputText = rockInput.getText();
                String RadiusButtonText = RadiusButtonInput.getText();

                if (paperInputText != null && scissorsInputText != null && rockInputText != null && RadiusButtonText != null) {
                    int Paper = Integer.parseInt(paperInputText);
                    int Scissors = Integer.parseInt(scissorsInputText);
                    int Rock = Integer.parseInt(rockInputText);
                    int radius = Integer.parseInt(RadiusButtonText);

                    if (Paper >= -1 && Scissors >= -1 && Rock >= -1) {
                        SimPanel.changeRadius(radius);
                        SimPanel.SpawnBallForType(Scissors, "scissors");
                        SimPanel.SpawnBallForType(Paper, "paper");
                        SimPanel.SpawnBallForType(Rock, "rock");

                        rockInput.setText("0");
                        paperInput.setText("0");
                        ScissorsInput.setText("0");


                        System.out.println("Button Clicked");
                    } else {
                        showErrorDialog("Invalid input: All input values must be non-negative.");
                    }
                } else {
                    showErrorDialog("Invalid input: Please enter valid integer values.");
                }
            } catch (NumberFormatException ex) {
                showErrorDialog("Input is null");
            }
        });

        StopSimButton.addActionListener(e -> System.out.println("Clcik"));
    }

    private static void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void updateRedCount(int count) {
        if (RedCount != null) {
            RedCount.setText(Integer.toString(count));
        }
    }

    public static void updateGreenCount(int count) {
        if (GreenCount != null) {
            GreenCount.setText(Integer.toString(count));
        }
    }

    public static void updateBlueCount(int count) {
        if (BlueCount != null) {
            BlueCount.setText(Integer.toString(count));
        }
    }

    public static void UpdateNumberOfBalls() {
        int red = 0;
        int green = 0;
        int blue = 0;

        for (Ball ball : SimPanel.BallArray) {
            if (Objects.equals(ball.getType(), "rock")) {
                red++;
                CheckWin(red, "rock");
            }
            if (Objects.equals(ball.getType(), "paper")) {
                green++;
                CheckWin(green, "paper");

            }
            if (Objects.equals(ball.getType(), "scissors")) {
                blue++;
                CheckWin(blue, "scissors");

            }
        }
        updateRedCount(red);    // Call the public method to update the RedCount label
        updateGreenCount(green);
        updateBlueCount(blue);
    }

    public static void CheckWin(int color, String type){
        int Size = SimPanel.BallArray.size();
        if(color == Size){
            showErrorDialog(type + "\nWon the game");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
