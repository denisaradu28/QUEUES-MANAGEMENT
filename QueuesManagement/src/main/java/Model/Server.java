package Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int serverId;
    private boolean running;
    private int totalTasksProcessed;
    private int totalServiceTime;


    public Server(int serverId) {
        this.serverId = serverId;
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
        running = false;
        totalTasksProcessed = 0;
        totalServiceTime = 0;

    }


    public void addTask(Task task) {
        try {
            task.setJustArrived(true);
            tasks.put(task);
            waitingPeriod.addAndGet(task.getServiceTime());
            totalServiceTime += task.getInitialServiceTime();
            totalTasksProcessed++;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Task crtTask = tasks.peek();
                if (crtTask != null) {
                    if (crtTask.isJustArrived()) {
                        crtTask.setJustArrived(false);
                    } else {
                        Thread.sleep(1000);
                        crtTask.decrementServiceTime();
                        waitingPeriod.decrementAndGet();

                        if (crtTask.getServiceTime() == 0) {
                            tasks.take();
                        }
                    }
                } else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public boolean isEmpty(){
        return tasks.isEmpty();
    }


    @Override
    public String toString() {
        if (tasks.isEmpty())
            return "closed";
        else{
            StringBuilder string = new StringBuilder();
            for(Task task : tasks){
                string.append(task.toString()).append("; ");
            }
            return string.toString();
        }
    }
}