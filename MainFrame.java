import jdk.nashorn.internal.scripts.JO;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;


public class MainFrame extends JFrame {
    // ****************** Variables ***********************
    public static String[] earthStrings;
    private static int ITERATIONS;
    private static int GRID_SIZE;
    private int i_nextBtn = -1; //counter for next button
    private int n_radioBtn = 1; // value for radio button
    private final JLabel yearLabel, speedLabel;                     // display icons
    private static final String[] names = {"carnivore.gif", "herbivore.gif", "plant.gif", "empty.gif"};
    private final Icon carn = new ImageIcon(getClass().getResource(names[0]));
    private final Icon herb = new ImageIcon(getClass().getResource(names[1]));
    private final Icon plant = new ImageIcon(getClass().getResource(names[2]));
    private final Icon empty = new ImageIcon(getClass().getResource(names[3]));
    private final JLabel[][] lab_grid;
    private final JRadioButton radioBtn1, radioBtn2, radioBtn3, radioBtn4, radioBtn5, radioBtn10;
    private final ButtonGroup radioGroup;
    private final static int imageSize = 50;
    private final char CARNIVORE = '@';
    private final char HERBIVORE = '&';
    private final char PLANT = '*';
    private final char EMPTY = '.';

    // ****************** CONSTRUCTOR ***********************
    public MainFrame(String title) {
        super(title);

        setLayout(new FlowLayout(FlowLayout.CENTER,25,15));

        // Current Cycle LABEL
        yearLabel = new JLabel("Current Cycle: #");
        
        // Grid Row Boxes
        Box[] box = new Box[GRID_SIZE];
        for (int i=0; i < GRID_SIZE; i++){
            box[i] = Box.createHorizontalBox();
        }

        // Vertical Box to hold Grid Rows
        Box gridRowsBox = Box.createVerticalBox();

        // Animal Icon JLabels 
        lab_grid = new JLabel[GRID_SIZE][GRID_SIZE];
        for (int i=0; i < GRID_SIZE; i++){
            for (int j=0; j < GRID_SIZE; j++){
                lab_grid[i][j] = new JLabel(empty);
                box[i].add(lab_grid[i][j]);
            }
        }

        // Add Grid Rows to One Vertically stacked box
        for (int i=0; i < GRID_SIZE; i++){
            gridRowsBox.add(box[i]);
        }

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

        Box radioBox = Box.createHorizontalBox();
        radioBox.add(radioBtn1);
        radioBox.add(radioBtn2);
        radioBox.add(radioBtn3);
        radioBox.add(radioBtn4);
        radioBox.add(radioBtn5);
        radioBox.add(radioBtn10);

        // Regular Buttons
        JButton prev_button = new JButton("<- prev");
        JButton next_button = new JButton("next ->");
        JButton exit_button = new JButton("EXIT");

        // Put Buttons in Box
        Box buttonsBox = Box.createHorizontalBox();

        buttonsBox.add(prev_button);
        buttonsBox.add(Box.createRigidArea(new Dimension(15,5)));
        buttonsBox.add(next_button);
        buttonsBox.add(Box.createRigidArea(new Dimension(15,5)));

        // Exit Button BOX
        Box exitBox = Box.createHorizontalBox();
        exitBox.add(exit_button);

        // ***** Add Swing Components to Frame  ******
        add(yearLabel);
        add(gridRowsBox);
        add(speedLabel);
        add(radioBox);
        add(buttonsBox);
        add(exitBox);


        //***********************************
        // ******** Action Listeners ********
        //***********************************

        // Radio Buttons behavior
        radioBtn1.addItemListener(new RadioButtonHandler(1));
        radioBtn2.addItemListener(new RadioButtonHandler(2));
        radioBtn3.addItemListener(new RadioButtonHandler(3));
        radioBtn4.addItemListener(new RadioButtonHandler(4));
        radioBtn5.addItemListener(new RadioButtonHandler(5));
        radioBtn10.addItemListener(new RadioButtonHandler(10));

        // "PREV" button behavior
        prev_button.addActionListener(

                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        i_nextBtn -= n_radioBtn;
                        if(i_nextBtn < 0) {
                            JOptionPane.showMessageDialog(null,"You are at the 1st Cycle.\n\nThe last cycle will now be displayed.");
                            i_nextBtn = (ITERATIONS-1);
                            drawEarth();
                        }
                        else if(i_nextBtn < ITERATIONS) {
                            drawEarth();
                        }
                    }
                }
        );

        // "NEXT" button behavior
        next_button.addActionListener(

                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        i_nextBtn += n_radioBtn;

                        if(i_nextBtn < ITERATIONS) {
                            drawEarth();
                        }
                        else if(i_nextBtn >= ITERATIONS){
                            JOptionPane.showMessageDialog(null,"You are at the last Cycle.\n\nThe 1st cycle will now be displayed.");
                            i_nextBtn = 0;
                            drawEarth();
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
            n_radioBtn = k;
        }
    }

    private void drawEarth(){
        yearLabel.setText("Current Cycle: " + (i_nextBtn+1));
        // Loop through that year's iteration and activate appropriate image for animal
        // located on that grid cell
        for (int m = 0; m < (GRID_SIZE*GRID_SIZE); m++){
            if (earthStrings[i_nextBtn].charAt(m) == EMPTY) {
                lab_grid[m/GRID_SIZE][m%GRID_SIZE].setIcon(empty);
            }
            else if (earthStrings[i_nextBtn].charAt(m) == CARNIVORE) {
                lab_grid[m/GRID_SIZE][m%GRID_SIZE].setIcon(carn);
            }
            else if (earthStrings[i_nextBtn].charAt(m) == HERBIVORE) {
                lab_grid[m/GRID_SIZE][m%GRID_SIZE].setIcon(herb);
            }
            else if (earthStrings[i_nextBtn].charAt(m) == PLANT) {
                lab_grid[m/GRID_SIZE][m%GRID_SIZE].setIcon(plant);
            }
        }
    }


    // ************************ MAIN FUNCTION ****************************
    public static void main(String [] args){
        ITERATIONS = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter Simulation Length","100"));
        GRID_SIZE = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter Simulation Grid Size","15"));

        Grid earth = new Grid(GRID_SIZE, ITERATIONS);
        earth.initializeWorld();
        earthStrings = earth.startSimulation();

        MainFrame mainframe = new MainFrame("A Land Before Time");
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize((imageSize*GRID_SIZE)+30,(imageSize*GRID_SIZE)+235);
        mainframe.setVisible(true);
    }
}