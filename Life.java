public abstract class Life {
    // ********************  VARIABLES  *************************
    int initialEnergy;
    int energy;
    int[] location = new int[2];

    // ********************  CONSTRUCTOR  ***********************
    public Life(int initialEnergy, int[] location){
        this.initialEnergy = initialEnergy;
        this.location = location;
        this.energy = initialEnergy;
    }

    public Life(int initialEnergy){
        this.initialEnergy = initialEnergy;
        this.energy = initialEnergy;
        this.location[0] = 0;
        this.location[1] = 0;
    }

    // ********************  METHODS  ***************************
    public int getEnergy(){
        return energy;
    }

    public int[] getLocation(){
        return location;
    }

    public int getXlocation(){
        return location[0];
    }

    public int getYlocation(){
        return location[1];
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    public void setLocation(int x, int y){
        this.location[0] = x;
        this.location[1] = y;
    }

    public void setLocation(int[] xy){
        this.location = xy;
    }

}