package BusinessLogic;

import GUI.AnimationFrame;
import GUI.SimulationFrame;
import Model.Server;
import Model.Task;

import java.io.*;
import java.util.*;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int numberOfServers;
    private int numberOfClients;
    private int minArrivalTime;
    private int maxArrivalTime;
    private SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;

    private Scheduler scheduler;
    private AnimationFrame frame;
    private List<Task> tasks;

    private int peakHour = 0;
    private int maxClientsInQueue = 0;
    private int totalWaitingTime = 0;
    private int totalServiceTime = 0;
    private int clientsProcessed = 0;

    private BufferedWriter logWriter;

    public SimulationManager(int timeLimit, int numberOfServers, int numberOfClients,
                             int minArrivalTime, int maxArrivalTime,
                             int minProcessingTime, int maxProcessingTime,
                             String strategy, AnimationFrame frame) {
        this.timeLimit = timeLimit;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.selectionPolicy = SelectionPolicy.valueOf(strategy);
        this.frame = frame;

        scheduler = new Scheduler(numberOfServers, numberOfClients / numberOfServers);
        scheduler.changeStrategy(selectionPolicy);
        generateRandomTasks();
        initLogFile();
    }

    private void initLogFile() {
        try {
            int index = 1;
            File indexFile = new File("log_index.txt");

            if (indexFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(indexFile))) {
                    index = Integer.parseInt(reader.readLine().trim()) + 1;
                } catch (NumberFormatException ignored) {}
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile))) {
                writer.write(String.valueOf(index));
            }

            String fileName = "log_" + index + ".txt";
            logWriter = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateRandomTasks() {
        Random rand = new Random();
        tasks = new ArrayList<>();

        for (int i = 0; i < numberOfClients; i++) {
            int arrivalTime = rand.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = rand.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;
            Task task = new Task(i + 1, arrivalTime, serviceTime);
            tasks.add(task);
        }

        tasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }

    @Override
    public void run() {
        try {
            int crtTime = 0;
            while (crtTime < timeLimit) {
                List<Task> taskToDispatch = new ArrayList<>();
                for (Task task : tasks) {
                    if (task.getArrivalTime() == crtTime) {
                        taskToDispatch.add(task);
                    }
                }
                tasks.removeAll(taskToDispatch);
                for (Task task : taskToDispatch) {
                    scheduler.dispatchTask(task);
                    int minWaiting = Integer.MAX_VALUE;
                    for (Server server : scheduler.getServers()) {
                        if (server.getWaitingPeriod() < minWaiting) {
                            minWaiting = server.getWaitingPeriod();
                        }
                    }
                    totalWaitingTime += (minWaiting == Integer.MAX_VALUE) ? 0 : minWaiting;
                    totalServiceTime += task.getInitialServiceTime();
                    clientsProcessed++;
                }
                updateGUI();
                writeLog(crtTime);
                int totalClientsInQueue = 0;
                for (Server server : scheduler.getServers()) {
                    totalClientsInQueue += server.getTasks().size();
                }
                if (totalClientsInQueue > maxClientsInQueue) {
                    maxClientsInQueue = totalClientsInQueue;
                    peakHour = crtTime;
                }
                crtTime++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean allServersEmpty = true;
                for (Server server : scheduler.getServers()) {
                    if (!server.isEmpty()) {
                        allServersEmpty = false;
                        break;
                    }
                }
                if (tasks.isEmpty() && allServersEmpty) {
                    break;
                }
            }
            scheduler.stopScheduler();
            logSimulationResults();
            closeLogFile();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (logWriter != null) {
                    logWriter.write("Exception occurred: " + e.getMessage() + "\n");
                    logWriter.flush();
                    logWriter.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void writeLog(int crtTime) {
        try {
            logWriter.write("Time " + crtTime + "\n");

            logWriter.write("Waiting clients: ");
            if (tasks.isEmpty()) {
                logWriter.write("none\n");
            } else {
                for (Task task : tasks) {
                    logWriter.write(task.toString() + ";");
                }
                logWriter.write("\n");
            }

            List<Server> servers = scheduler.getServers();
            for (int i = 0; i < servers.size(); i++) {
                logWriter.write("Queue " + (i + 1) + ": " + servers.get(i).toString() + "\n");
            }
            logWriter.write("\n");

            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logSimulationResults() {
        if (logWriter == null) {
            System.err.println("logWriter is null!");
            return;
        }
        try {
            logWriter.write("Simulation finished\n");

            logWriter.write("clientsProcessed = " + clientsProcessed + "\n");

            double avgWait = clientsProcessed == 0 ? 0 : (double) totalWaitingTime / clientsProcessed;
            double avgService = clientsProcessed == 0 ? 0 : (double) totalServiceTime / clientsProcessed;

            logWriter.write("Average waiting time: " + avgWait + "\n");
            logWriter.write("Average service time: " + avgService + "\n");
            logWriter.write("Peak hour: " + peakHour + "\n");

            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeLogFile() {
        try {
            if (logWriter != null) {
                logWriter.close();
                System.out.println("Log file closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateGUI() {
        if (frame == null) return;

        List<Server> servers = scheduler.getServers();

        for (int i = 0; i < servers.size(); i++) {
            List<String> taskLabels = new ArrayList<>();
            for (Task task : servers.get(i).getTasks()) {
                taskLabels.add("(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")");
            }
            frame.updateQueue(i, taskLabels);
        }
        frame.refresh();
    }

    public static void main(String[] args) {
        new SimulationFrame().setVisible(true);
    }
}
