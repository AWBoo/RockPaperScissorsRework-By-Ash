import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    static int WindowWidth = 1400;
    static int WindowHeight = (int) (WindowWidth * (0.5555));
    JFrame frame = new JFrame();

    public MainFrame(){
        frame.setTitle("Rock Paper Scissors. A Rework");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel containerPanel = new JPanel(new BorderLayout()); // Use BorderLayout
        containerPanel.setBackground(Color.BLACK);

        SimPanel panel1 = new SimPanel();
        panel1.setPreferredSize(new Dimension((int) (WindowWidth * 0.75), WindowHeight)); // Adjusted width

        EditPanel panel2 = new EditPanel();
        panel2.setPreferredSize(new Dimension((int) (WindowWidth * 0.25), WindowHeight)); // Adjusted width

        containerPanel.add(panel1, BorderLayout.WEST); // Add panel1 to the left (WEST) side
        containerPanel.add(panel2, BorderLayout.CENTER); // Add panel2 to the center (CENTER)

        frame.add(containerPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
