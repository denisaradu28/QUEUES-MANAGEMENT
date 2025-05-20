package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server selectedServer = servers.get(0);
        int minWaitingTime = selectedServer.getWaitingPeriod();

        for(int i = 1; i < servers.size(); i++) {
            Server server = servers.get(i);
            if(server.getWaitingPeriod() < minWaitingTime) {
                minWaitingTime = server.getWaitingPeriod();
                selectedServer = server;
            }
        }

        selectedServer.addTask(task);
    }
}
