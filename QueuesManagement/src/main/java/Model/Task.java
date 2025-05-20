package Model;

public class Task {

    private int ID;
    private int arrivalTime;
    private int serviceTime;
    private int initialServiceTime;
    private boolean justArrived = true;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.initialServiceTime = serviceTime;
    }

    public int getID() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime() {
        if(serviceTime > 0)
        {
            serviceTime --;
        }
    }

    public int getInitialServiceTime() {
        return initialServiceTime;
    }

    public boolean isJustArrived() {
        return justArrived;
    }

    public void setJustArrived(boolean justArrived) {
        this.justArrived = justArrived;
    }

    @Override
    public String toString() {
        return "(" + ID + "," + arrivalTime + "," + serviceTime + ")";
    }

}
