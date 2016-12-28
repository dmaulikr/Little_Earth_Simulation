import java.util.concurrent.ThreadLocalRandom;

public class Herbivore extends Animal {
    // ********************  VARIABLES  *************************

    static int speed = 2;
    private Life[] agents = Grid.getLifePointer();

    // ********************  CONSTRUCTOR  ***********************

    // Constructor for initial random setup
    public Herbivore(Grid world){
        super(3,2,4,5,8);
        // Generate random location to put Herbivore until we find a location that isn't occupied
        int x,y;
        int counter = 0;
        do {
            x = ThreadLocalRandom.current().nextInt(0, world.getGridSize());
            y = ThreadLocalRandom.current().nextInt(0, world.getGridSize());
            counter++;
        } while(world.isOccupied(x,y) && counter < world.getGridSize()*world.getGridSize());
        if (counter >= world.getGridSize()*world.getGridSize()){
            System.out.println("ERROR: CANNOT FIND SPACE TO PLACE ANIMAL (Animal surrounded on all sides), EXITING PROGRAM...");
            System.exit(-1);
        }
        location[0] = x;
        location[1] = y;
        // Fill Queue w/ current location
        memory.add(location);       // since constructor initialized memory w/ location (0,0) 3 times, we'll push the new one once.
        // mark the grid that there's a Herbivore @ this location
        world.setHerbivore(x, y, true, this);
    }

    // Constructor for specifying birth location w/ array location
    public Herbivore(Grid world, int [] xy){
        super(3, xy,2,4,5,8);
        location = xy;

        // mark the grid that there's a Herbivore @ this location
        world.setHerbivore(xy[0],xy[1],true,this);
    }

    // ********************  METHODS  ***************************

    // Herbivore will see available spaces surrounding them in every direction
    // and move to a spot with a plant if available,
    // otherwise will select an available spot at random
    public int[] move(Grid world){
        // pick from that, those that aren't occupied
        int[][] moves = getMoves(world);
        int choice;
        int counter = 0;

        // Pop the Q (remove oldest location)
        memory.remove();
        // Add current location to the front of the Q
        memory.add(location);

        // check if any of the viable moves have Plants & No Carnivores in them
        for (int i=0; i < 8; i++){
            if(world.isPlant(moves[i][0],moves[i][1]) && !(world.isOccupied(moves[i][0],moves[i][1]))){
                return moves[i];
            }
        }

        // return any move that isn't in memory since didn't find anything to eat nearby
        for (int i=0; i<8; i++){
            if ( !inMemory(moves[i]) ){
                return moves[i];
            }
        }

        // if no such moves available...
        // keep randomly selecting a valid location until you find a non-occupied one.
        do {
            choice = ThreadLocalRandom.current().nextInt(0, 8);
            counter++;
        }while (world.isOccupied(moves[choice][0], moves[choice][1]) && counter < 30);
        if (counter >= 30){
            //System.out.println("ERROR: NO AVAILABLE MOVES for " +this+ "\n(Animal surrounded on all sides), EXITING PROGRAM...");
            //System.exit(-1);
            // Instead of ending prog if grid gets full, decided to let animal stay still
            return getLocation();
        }
        return moves[choice];
    }

    @Override
    public void act(Grid world){
        // If not dead from old age or no energy
        if( world.getSimulationLength() % speed == 0 && !isDead() ){
            --energy;   // costs 1 energy unit per turn
            ++age;      // animal ages each move

            // if this turn killed them...
            if (isDead()) {
                // turn off "herbivore" at this location cell
                world.setHerbivore(getXlocation(), getYlocation(), false, this);
                //########## LOG OUTPUT #############
                //System.out.println("DIED:\t" +this);
                // reset location
                setLocation(-1, -1);
            }
            // if still alive however...
            else {
                // un-mark herbivore at current location
                world.setHerbivore(getXlocation(), getYlocation(), false, this);
                // set herbivore's location to new spot moving to
                setLocation(move(world));
                // mark herbivore at new location on grid
                world.setHerbivore(getXlocation(), getYlocation(), true, this);

                // see if hungry and plants in the new space
                if (isHungry() && world.isPlant(getXlocation(), getYlocation())) {
                    // eat plants // get energy
                    eat(world, location);
                }

                // check if ready to birth
                if (isPreggers()) {
                    //########## LOG OUTPUT #############
                    //System.out.println("HAD A BABY:\t" + this);
                    // spawn new herbivore in an adjacent spot to pregnant herbivore
                    agents[world.agentCount++] = new Herbivore(world, this.move(world));
                    // take Herbivore baby initialEnergy away from mother
                    setEnergy(getEnergy() - initialEnergy);
                }
            }
        }
    }

    public void eat(Grid world, int[] location){
        // get energy from plant at our location
        setEnergy(getEnergy() + world.getPlantEnergy(location));
        // "erase" plant obj & turn it off on grid location
        world.plantEaten(location);
        //########## LOG OUTPUT #############
        //System.out.println("PLANT EATEN BY:\t" +this);
    }

    @Override
    public String toString(){
        return "Herbivore @ (" + location[0] + ", " + location[1] + "), Energy: " +getEnergy()+ ", Age: " +getAge()+ ", Dead: " +isDead();
    }
}
