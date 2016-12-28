import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;


public class MainFrame extends JFrame {
    // ****************** Variables ***********************
    private int i = -1; //counter for next button
    private int n = 1; // value for radio button
    private final GridBagLayout layout;
    private final GridBagConstraints constraints;
    private final JLabel carn, plant, herb, speedLabel;                     // display icons
    private static final String[] names = {"carnivore1.gif", "herbivore.gif", "plant2.gif", "empty.gif"};
    private final Icon[] icons = {
            new ImageIcon(getClass().getResource(names[0])),
            new ImageIcon(getClass().getResource(names[1])),
            new ImageIcon(getClass().getResource(names[2])),
            new ImageIcon(getClass().getResource(names[3]))};
    private final JRadioButton radioBtn1, radioBtn2, radioBtn3, radioBtn4, radioBtn5, radioBtn10;
    private final ButtonGroup radioGroup;
    // ****************** CONSTRUCTOR ***********************
    public MainFrame(String title) {
        super(title);

        // Set Layout Manager, layout, and constraints
        layout = new GridBagLayout();
        setLayout(layout);
        constraints = new GridBagConstraints();

        // Images
        carn = new JLabel(icons[0]);
        herb = new JLabel(icons[1]);
        plant = new JLabel(icons[2]);

        // Text Area
        JTextArea textArea = new JTextArea("\n Click \"next\" to begin your simulation...");
        textArea.setColumns(25);
        textArea.setRows(22);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Label for radio buttons
        speedLabel = new JLabel("Please Select a Simulation Speed:");

        // Radio Buttons
        radioBtn1 = new JRadioButton("1", true);
        radioBtn2 = new JRadioButton("2", false);
        radioBtn3 = new JRadioButton("3", false);
        radioBtn4 = new JRadioButton("4", false);
        radioBtn5 = new JRadioButton("5", false);
        radioBtn10 = new JRadioButton("10", false);

        radioGroup = new ButtonGroup();
        radioGroup.add(radioBtn1);
        radioGroup.add(radioBtn2);
        radioGroup.add(radioBtn3);
        radioGroup.add(radioBtn4);
        radioGroup.add(radioBtn5);
        radioGroup.add(radioBtn10);

        // Regular Buttons
        JButton next_button = new JButton("next");
        JButton exit_button = new JButton("exit");

        // Add Swing Components to Frame (w/ constraints)
        constraints.insets = new Insets(20,20,10,20);

        addComponent(carn);
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        addComponent(plant);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        addComponent(herb);
        constraints.fill = GridBagConstraints.NONE;

        constraints.insets = new Insets(20,20,10,20);
        addComponent(textArea);

        addComponent(speedLabel);

        addComponent(radioBtn1,22,0,3,1,1,1);
        addComponent(radioBtn2,22,4,3,1,1,1);
        addComponent(radioBtn3,22,8,3,1,1,1);
        addComponent(radioBtn4,22,12,3,1,1,1);
        addComponent(radioBtn5,22,16,3,1,1,1);
        addComponent(radioBtn10,22,20,3,1,1,1);

        constraints.fill = GridBagConstraints.NONE;
        addComponent(next_button,23,10,4,2,1,1);
        addComponent(exit_button,23,17,4,2,1,1);

        //
        // ******** Action Listeners ********
        //
        // Radio Buttons behavior
        radioBtn1.addItemListener(new RadioButtonHandler(1));
        radioBtn2.addItemListener(new RadioButtonHandler(2));
        radioBtn3.addItemListener(new RadioButtonHandler(3));
        radioBtn4.addItemListener(new RadioButtonHandler(4));
        radioBtn5.addItemListener(new RadioButtonHandler(5));
        radioBtn10.addItemListener(new RadioButtonHandler(10));

        // "NEXT" button behavior
        next_button.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        i += n;
                        if(i < Grid.ITERATIONS) {
                            textArea.setText(Grid.earthStrings[i]);
                        }
                        else if(i >= Grid.ITERATIONS){
                            JOptionPane.showMessageDialog(null,"Simulation has ended.\nA new simulation will begin now");
                            dispose();
                            MainFrame myGUI = new MainFrame("A Land Before Time");
                            myGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            myGUI.setSize(700,700);
                            myGUI.setVisible(true);
                        }
                    }
                }
        );

        // Close Program when pressing the "exit" button
        exit_button.addActionListener(e -> System.exit(0));
    }

    private class RadioButtonHandler implements ItemListener {
        private int k;

        public RadioButtonHandler(int k){
            this.k = k;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            n = k;
        }
    }

    // ************ HELPER FUNCTIONS ***********************

    // Helper Functions for Adding Components
    private void addComponent(Component component, int row, int column, int width, int height, int weightx, int weighty){
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        layout.setConstraints(component, constraints);
        add(component);
    }

    private void addComponent(Component component){
        layout.setConstraints(component, constraints);
        add(component);
    }

}
