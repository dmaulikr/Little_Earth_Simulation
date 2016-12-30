// #####################################################################################################
// Small GridCells Simulator by Mike Garcia
//
// NOTES:   The gridCells does not cut off at the edges, it wraps around.
// NOTES:   Animals can see/move/eat in all directions (8 available moves).
// NOTES:   Plants grow randomly w/ random energy.
// NOTES:   Animals acquire the energy of the animal or plant they eat.
// NOTES:   Animals look for things to eat in adjacent spaces in order to decide where to move.
//          They also keep a memory of the last 4 moves and avoid those spaces unless no other moves available.
// NOTES:   You can toggle detailed output if you search "LOG OUTPUT" and (un)comment system.out statements.
// #####################################################################################################

import javax.swing.*;
import static java.lang.Math.toIntExact;

public class Grid {
    // ******************** VARIABLES ***********************
    private int gridSize;   // used to create NxN 2-dimensional array
    private int simulationLength;
    //public static int ITERATIONS = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter Simulation Length","50"));
    private int initNumOfPlants;
    private int initNumOfHerbs;
    private int initNumOfCarns;
    private GridCells[][] gridCells = new GridCells[gridSize][gridSize];    // create 2D array w/ 4 data nodes in each cell.
    private static Life[] agents = new Life[1000];
    public int agentCount = 0;
    private String[] earthStrings;
    private final String CARNIVORE = "@";
    private final String HERBIVORE = "&";
    private final String PLANT = "*";
    private final String EMPTY = ".";
    // ******************** CONSTRUCTOR ***********************
    public Grid(int gridSize, int simulationLength){
        this.simulationLength = simulationLength;
        this.gridSize = gridSize;
        initNumOfPlants = toIntExact(Math.round((gridSize*gridSize*0.75)));
        initNumOfHerbs = gridSize;
        initNumOfCarns = (initNumOfHerbs/3);
        gridCells = new GridCells[gridSize][gridSize];

        // Initialize all the cells to be gridCells objects
        for (int i=0; i < gridSize; i++) {       // cycles through rows
            for (int j = 0; j < gridSize; j++) {   // cycles through columns
                gridCells[i][j] = new GridCells();
            }
        }
    }

    // ******************** METHODS ***********************
//    public static void main(String [] args){
//        Grid earth = new Grid(15,ITERATIONS);
//        earth.initializeWorld();
//        earthStrings = earth.startSimulation();
//
//        MainFrame mainframe = new MainFrame("A Land Before Time");
//        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        mainframe.setSize(700,700);
//        mainframe.setVisible(true);
//    }

    public void initializeWorld(){
        int i=0;
        for (; i < initNumOfPlants; i++){
            agents[i] = new Plant(this);
        }
        for (int j=0; j < initNumOfHerbs; j++){
            agents[i++] = new Herbivore(this);
        }
        for (int k=0; k < initNumOfCarns; k++){
            agents[i++] = new Carnivore(this);
        }
        agentCount = i;
    }

    public String[] startSimulation(){
        // "j" is current year of simulation
        int j = 0;
        String[] earthStrings = new String[getSimulationLength()];
        // Print the initialized gridCells
        earthStrings[j++] = printWorld();
        decrementSimLength();
        //printConsoleWorld();                            //########## LOG OUTPUT #############
        // Run simulation for desired length
        while(getSimulationLength() > 0){
            for (int i = 0; i < agents.length && agents[i] != null; i++) {
                // Only make animals "move/act"
                if (agents[i] instanceof Movable){
                    Animal a = (Animal) agents[i];
                    a.act(this);
                }
            }
            // Generate more plants if applicable
            if (getSimulationLength() % Plant.plantGrowthRate == 0){
                agents[agentCount++] = new Plant(this);
            }
            decrementSimLength();
            earthStrings[j++] = printWorld();
            //printConsoleWorld();                        //########## LOG OUTPUT #############
        }
        return earthStrings;
    }

    public void startConsoleSimulation(){
        // "j" is current year of simulation
        int j = 0;
        // Print the initialized gridCells
        System.out.print("\nYear " + j++);
        printConsoleWorld();
        // Run simulation for desired length
        while(getSimulationLength() > 0){
            for (int i = 0; i < agents.length && agents[i] != null; i++) {
                // Only make animals "move/act"
                if (agents[i] instanceof Movable){
                    Animal a = (Animal) agents[i];
                    a.act(this);
                }
            }
            // Generate more plants if applicable
            if (getSimulationLength() % Plant.plantGrowthRate == 0){
                agents[agentCount++] = new Plant(this);
            }
            decrementSimLength();
            System.out.print("\nYear " + j++);
            printConsoleWorld();
        }
    }

    public int getSimulationLength(){
        return simulationLength;
    }

    public void decrementSimLength(){
        simulationLength--;
    }

    public int getGridSize() {
        return gridSize;
    }

    public boolean isPlant(int x, int y){
        return gridCells[x][y].isPlant();
    }

    // There is an animal in the space
    public boolean isOccupied(int x, int y){
        return (gridCells[x][y].isCarnivore() || gridCells[x][y].isHerbivore()) ? true : false;
    }

    public boolean isCarnivore(int x, int y){
        return gridCells[x][y].isCarnivore();
    }

    public boolean isHerbivore(int x, int y){
        return gridCells[x][y].isHerbivore();
    }

    public void setPlant(int x, int y, boolean isPlant, Plant pntr){
        gridCells[x][y].setPlant(isPlant, pntr);
    }

    public void setCarnivore(int x, int y, boolean isCarnivore, Carnivore pntr){
        gridCells[x][y].setCarnivore(isCarnivore, pntr);
    }

    public void setHerbivore(int x, int y, boolean isHerbivore, Herbivore pntr){
        gridCells[x][y].setHerbivore(isHerbivore, pntr);
    }

    public int getHerbivoreEnergy(int[] location){
        return gridCells[location[0]][location[1]].getHerbivore().getEnergy();
    }

    public int getPlantEnergy(int[] location){
        return gridCells[location[0]][location[1]].getPlant().getEnergy();
    }

    // action to take if plant is eaten
    public void plantEaten(int[] location){
        // Update Plant Object's status to eaten
        gridCells[location[0]][location[1]].getPlant().setEnergy(0);
        gridCells[location[0]][location[1]].getPlant().setEaten(true);
        // Change plant's internal location to -1,-1
        gridCells[location[0]][location[1]].getPlant().setLocation(-1,-1);
        // Update Grid location to reflect no plant either
        gridCells[location[0]][location[1]].setPlant(false);
    }

    // action to take if herbivore is eaten
    public void herbivoreEaten(int[] location){
        // Update Herb Object status to eaten
        gridCells[location[0]][location[1]].getHerbivore().setEnergy(0);
        // Change Herb's location to -1,-1
        gridCells[location[0]][location[1]].getHerbivore().setLocation(-1,-1);
        // Update Grid location to reflect no Herb present
        gridCells[location[0]][location[1]].setHerbivore(false);
    }

    // prints NxN matrix of gridCells and plants/animals inhabiting it
    public void printConsoleWorld(){
        for (int i=0; i < this.gridSize; i++){       // cycles through rows
            //########## LOG OUTPUT #############
//            if(i==0) {
//                System.out.print("\n  ");
//                for (int k=0; k < this.gridSize; k++) {
//                    System.out.print(" " + k + " ");
//                }
//            }
            System.out.print("\n");
            for (int j=0; j < this.gridSize; j++){   // cycles through columns
                //########## LOG OUTPUT #############
//                if(j==0) {
//                    System.out.print(i + " ");
//                }
                if (gridCells[i][j].isEmpty()){
                    System.out.print(" . ");
                }
                else if (gridCells[i][j].isCarnivore()){
                    System.out.print(" @ ");
                }
                else if (gridCells[i][j].isHerbivore()){
                    System.out.print(" & ");
                }
                else if (gridCells[i][j].isPlant()){
                    System.out.print(" * ");
                }
            }
        }
        System.out.print("\n");
    }

    public String printWorld(){
        String earthString = "";
        for (int i=0; i < this.gridSize; i++){       // cycles through rows
            for (int j=0; j < this.gridSize; j++){   // cycles through columns
                if (gridCells[i][j].isEmpty()){
                    earthString += EMPTY;
                }
                else if (gridCells[i][j].isCarnivore()){
                    earthString += CARNIVORE;
                }
                else if (gridCells[i][j].isHerbivore()){
                    earthString += HERBIVORE;
                }
                else if (gridCells[i][j].isPlant()){
                    earthString += PLANT;
                }
            }
        }
        return earthString;
    }

    public static Life[] getLifePointer(){
        return agents;
    }

}
