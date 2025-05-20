package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;


public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    private List<Thread> threads;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new ArrayList<>();
        this.threads = new ArrayList<>();

        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(i);
            servers.add(server);
            Thread thread = new Thread(server);
            threads.add(thread);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        switch (policy) {
            case SHORTEST_QUEUE:
                strategy = new ShortestQueueStrategy();
                break;
            case SHORTEST_TIME:
                strategy = new TimeStrategy();
                break;
        }
    }

    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }

    public void stopScheduler(){
        for(int i = 0; i < servers.size(); i++){

            try{
                threads.get(i).join(1000);
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("Interrupted when waiting for server thread to stop: " + e.getMessage());
            }
        }
    }

}
