public class GridCells {

    // ******************** VARIABLES ***********************
    private boolean plant;
    private boolean carnivore;
    private boolean herbivore;
    // Pointers to reference animals/plants at this location
    private Plant plantPntr;
    private Carnivore carnPntr;
    private Herbivore herbPntr;

    // ******************** CONSTRUCTOR ***********************
    public GridCells(){
        this.plant = false;
        this.carnivore = false;
        this.herbivore = false;
    }

    // ******************** METHODS ***********************
    public boolean isPlant(){
        return this.plant;
    }

    public boolean isCarnivore(){
        return this.carnivore;
    }

    public boolean isHerbivore(){
        return this.herbivore;
    }

    public boolean isEmpty(){
        return (!(isHerbivore() || isPlant() || isCarnivore()));
    }

    public void setPlant(boolean isPlant, Plant pntr){
        this.plant = isPlant;
        if(isPlant){
            // if we're placing a plant, we need a pntr to the plant obj
            // so we can ref this plant obj if needed later
            plantPntr = pntr;
        }
        else{
            plantPntr = null;
        }
    }

    // If turning off a plant, we don't need a pointer since we're erasing it.
    public void setPlant(boolean isPlant){
        this.plant = isPlant;
        if(isPlant){
            System.out.println("ERROR: NO Plant Pointer Passed into GridCells.setPlant() function.\nEXITING PROGRAM...");
            System.exit(-1);
        }
        plantPntr = null;
    }

    public void setCarnivore(boolean isCarnivore, Carnivore pntr){
        this.carnivore = isCarnivore;
        if(isCarnivore){
            carnPntr = pntr;
        }
        else{
            carnPntr = null;
        }
    }

    public void setCarnivore(boolean isCarnivore){
        this.carnivore = isCarnivore;
        if(isCarnivore){
            System.out.println("ERROR: NO Carnivore Pointer Passed into GridCells.setCarnivore() function.\nEXITING PROGRAM...");
            System.exit(-1);
        }
        carnPntr = null;
    }

    public void setHerbivore(boolean isHerbivore, Herbivore pntr){
        this.herbivore = isHerbivore;
        if (isHerbivore){
            herbPntr = pntr;
        }
        else{
            herbPntr = null;
        }
    }

    // Overloaded: if turning off Herbivore, don't need a pointer passed in
    public void setHerbivore(boolean isHerbivore){
        this.herbivore = isHerbivore;
        if (isHerbivore){
            System.out.println("ERROR: NO Herbivore Pointer Passed into GridCells.setHerbivore() function.\nEXITING PROGRAM...");
            System.exit(-1);
        }
        herbPntr = null;
    }

    public Carnivore getCarnivore(){
        return carnPntr;
    }

    public Herbivore getHerbivore(){
        return herbPntr;
    }

    public Plant getPlant(){
        return plantPntr;
    }

    @Override
    public String toString(){
        return "GridCells Info:\tPlant: " +plant+ "\tCarnivore: " +carnivore+ "\tHerbivore: " +herbivore+ " (" +herbPntr+")";
    }
}
