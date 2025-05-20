package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server selectedServer = servers.get(0);
        int minSize = selectedServer.getTasks().size();

        for(int i = 1; i < servers.size(); i++) {
            Server server = servers.get(i);
            if(server.getTasks().size() < minSize) {
                minSize = server.getTasks().size();
                selectedServer = server;
            }
        }
        selectedServer.addTask(task);
    }
}