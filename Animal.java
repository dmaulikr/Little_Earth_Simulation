import java.util.LinkedList;
import java.util.Queue;

public abstract class Animal extends Life implements Movable {
    // ********************  VARIABLES  *************************
    int age;
    int birthEnergyCost;
    int hungerThreshold;
    int minBirthAge;
    int minBirthEnergy;
    int deathAge;

    Queue<int[]> memory = new LinkedList<int[]>();

    // ********************  METHODS  ***************************
    public Animal(int initialEnergy, int[] location, int hungerThreshold, int minBirthAge, int minBirthEnergy, int deathAge){
        super(initialEnergy, location);
        age = 0;
        birthEnergyCost = initialEnergy;
        this.hungerThreshold = hungerThreshold;
        this.minBirthAge = minBirthAge;
        this.minBirthEnergy = minBirthEnergy;
        this.deathAge = deathAge;
        // Fill Queue w/ current location 4 times
        memory.add(location);
        memory.add(location);
        memory.add(location);
        memory.add(location);
    }

    public Animal(int initialEnergy, int hungerThreshold, int minBirthAge, int minBirthEnergy, int deathAge){
        super(initialEnergy);
        age = 0;
        birthEnergyCost = initialEnergy;
        this.hungerThreshold = hungerThreshold;
        this.minBirthAge = minBirthAge;
        this.minBirthEnergy = minBirthEnergy;
        this.deathAge = deathAge;
        // Fill Queue w/ current location 4 times
        memory.add(location);
        memory.add(location);
        memory.add(location);
    }

    // ********************  METHODS  ***************************
    public abstract void act(Grid world); // MUST DEFINE IN SUBCLASSES

    public int getAge(){
        return age;
    }

    public boolean isHungry(){
        return ((energy <= hungerThreshold));
    }

    public boolean isPreggers(){
        return (energy >= minBirthEnergy && age >= minBirthAge && !(isDead()) );
    }

    public boolean isDead(){
        return ((age >= deathAge || energy <= 0));
    }

    public boolean inMemory(int [] move){
        // Check to see if a move is in the recent list of moves
        for(int[] i : memory){
            if(move[0] == i[0] && move[1] == i[1]){
                return true;
            }
        }
        return false;
    }

    // Used in tandem w/ each animals specific "move" function
    // where it will further analyze its surroundings and
    // decide where to move.
    public int[][] getMoves(Grid world){
        int[][] possibleMoves = new int[8][2];

        // find all 8 coordinates available to move to
        // had to use Math.floor.Mod here when subtracting to avoid
        // any negative results after modulo
        possibleMoves[0][0] = Math.floorMod((getXlocation() - 1), world.getGridSize());
        possibleMoves[0][1] = Math.floorMod((getYlocation() - 1), world.getGridSize());
        possibleMoves[1][0] = Math.floorMod((getXlocation() - 1), world.getGridSize());
        possibleMoves[1][1] = getYlocation();
        possibleMoves[2][0] = Math.floorMod((getXlocation() - 1), world.getGridSize());
        possibleMoves[2][1] = (getYlocation() + 1) % world.getGridSize();
        possibleMoves[3][0] = getXlocation();
        possibleMoves[3][1] = Math.floorMod((getYlocation() - 1), world.getGridSize());
        possibleMoves[4][0] = getXlocation();
        possibleMoves[4][1] = (getYlocation() + 1) % world.getGridSize();
        possibleMoves[5][0] = (getXlocation() + 1) % world.getGridSize();
        possibleMoves[5][1] = Math.floorMod((getYlocation() - 1), world.getGridSize());
        possibleMoves[6][0] = (getXlocation() + 1) % world.getGridSize();
        possibleMoves[6][1] = getYlocation();
        possibleMoves[7][0] = (getXlocation() + 1) % world.getGridSize();
        possibleMoves[7][1] = (getYlocation() + 1) % world.getGridSize();
        // return 2D array of all possible coordinates
        return possibleMoves;
    }
}
