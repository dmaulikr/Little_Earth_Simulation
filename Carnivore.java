import java.util.concurrent.ThreadLocalRandom;

public class Carnivore extends Animal {
    // ********************  VARIABLES  *************************
    static int speed = 2;
    private Life[] agents = Earth.getLifePointer();

    // ********************  CONSTRUCTOR  ***********************
    // Constructor for initial random setup
    public Carnivore(Earth world){
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
        memory.add(location);       // since constructor initialized memory w/ location (0,0) 3 times, we'll push the new one once.

        // mark the grid that there's a Carnivore @ this location
        world.setCarnivore(x,y,true, this);
    }

    // Constructor for specifying birth location BUT w/ array location
    public Carnivore(Earth world, int [] xy){
        super(3, xy,2,4,5,8);
        location = xy;

        // mark the grid that there's a Carnivore @ this location
        world.setCarnivore(xy[0],xy[1],true, this);
    }

    // ********************  METHODS  ***************************
    public int[] move(Earth world){
        // pick from that, those that aren't occupied by other Carnivores
        int[][] moves = getMoves(world);
        int choice;
        int counter = 0;

        // Pop the Q
        memory.remove();
        // Add current location to the Q
        memory.add(location);

        // check if any of the viable moves have Herbivores in them
        for (int i=0; i < 8; i++){
            if(world.isHerbivore(moves[i][0],moves[i][1]) && !(world.isCarnivore(moves[i][0],moves[i][1]))){
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
        }while (world.isCarnivore(moves[choice][0], moves[choice][1]) && counter < 30);
        if (counter >= 30){
            //System.out.println("ERROR: NO AVAILABLE MOVES for " +this+ "\n(Animal surrounded on all sides), EXITING PROGRAM...");
            //System.exit(-1);
            // Instead of ending program if grid gets full, decided to let animal stay still
            return getLocation();
        }
        return moves[choice];
    }

    @Override
    public void act(Earth world){
        if (world.getSimulationLength() % speed == 0 && !isDead()) {
            --energy;   // costs 1 energy unit per turn
            ++age;      // animal ages each move

            // check if dead
            if (isDead()) {
                // turn off "carnivore" at this location cell
                world.setCarnivore(getXlocation(), getYlocation(), false, this);
                //########## LOG OUTPUT #############
                //System.out.println("DIED:\t" +this);
                // reset location
                setLocation(-1, -1);
            }
            else {
                // un-mark carnivore at current location
                world.setCarnivore(getXlocation(), getYlocation(), false, this);
                // set Carnivore's location to new spot moving to
                setLocation(move(world));
                // mark Carnivore at new location
                world.setCarnivore(getXlocation(), getYlocation(), true, this);

                // see if hungry & herbivore in space
                if (isHungry() && world.isHerbivore(getXlocation(), getYlocation())) {
                    eat(world, location);
                }

                // check if ready to birth
                if (isPreggers()){
                    //########## LOG OUTPUT #############
                    //System.out.println("HAD A BABY:\t" +this);
                    agents[world.agentCount++] = new Carnivore(world, this.move(world));
                    setEnergy(getEnergy() - initialEnergy);
                }
            }
        }
    }

    public void eat(Earth world, int[] location){
        // get energy from Herbivore at our location
        setEnergy(getEnergy() + world.getHerbivoreEnergy(location));
        // "erase" herbivore obj & turn it off on grid location
        world.herbivoreEaten(location);
        //########## LOG OUTPUT #############
        //System.out.println("HERBIVORE EATEN BY:\t" +this);
    }

    @Override
    public String toString(){
        return "Carnivore @ (" + location[0] + ", " + location[1] + "), Energy: " +getEnergy()+ ", Age: " +getAge()+ ", Dead: " +isDead();
    }
}
