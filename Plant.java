import java.util.concurrent.ThreadLocalRandom;      // recommended to use over random in java 1.7+

public class Plant extends Life{
    // ********************  VARIABLES  *************************
    private boolean eaten;
    public static int plantGrowthRate = 2;

    // ********************  CONSTRUCTOR  ***********************
    public Plant(Grid world){
        // Generate random energy between 2 & 10 for new plants
        super(ThreadLocalRandom.current().nextInt(2, 11));
        this.eaten = false;
        // Generate random location to put a plant until we find a location that
        // doesn't already have a plant there or we've tried for the # of cells in the grid.
        int x,y;
        int counter = 0;
        do {
            x = ThreadLocalRandom.current().nextInt(0, world.getGridSize());
            y = ThreadLocalRandom.current().nextInt(0, world.getGridSize());
            counter++;
        } while(world.isPlant(x,y) && counter < world.getGridSize() * world.getGridSize());
        if (counter >= world.getGridSize()*world.getGridSize()){
            System.out.println("ERROR: CANNOT FIND SPACE TO PLACE PLANT (Surrounded on all sides), EXITING PROGRAM...");
            System.exit(-1);
        }
        this.location[0] = x;
        this.location[1] = y;
        world.setPlant(x,y,true,this);       // mark the grid that there's a plant @ this location
    }

    // ********************  METHODS  ***************************
    public boolean isEaten(){
        return eaten;
    }

    public void setEaten(boolean isEaten){
        eaten = isEaten;
    }

    @Override
    public String toString(){
        return "Plant @ (" + location[0] + ", " + location[1] + "), Energy: " +getEnergy()+ ", Eaten: " +isEaten();
    }
}
