# Queue Simulation Project

## Overview

This project simulates a queue management system with multiple servers processing incoming tasks. Tasks have arrival times and service times, and are dispatched to servers based on different strategies. The simulation includes a graphical interface showing real-time queue states and logs detailed information about the simulation.

---

## Main Classes

### `Task` (abstract, sealed)
- Represents a generic task.
- Attributes:
  - `idTask`: unique task ID.
  - `statusTask`: current status (e.g. "New", "In Progress", "Completed").
  - `taskName`: name of the task.
- Methods:
  - `estimateDuration()`: abstract, implemented by subclasses.
  - `toString()`: user-friendly task description.

### `SimpleTask` (extends `Task`)
- Represents a simple task with a fixed estimated duration.

### `ComplexTask` (extends `Task`)
- Represents a complex task composed of multiple sub-tasks.
- Contains a list of `SimpleTask` or other `Task` objects.

### `Server` (implements `Runnable`)
- Represents a server processing tasks in its queue.
- Attributes:
  - Thread-safe queue of tasks.
  - `waitingPeriod`: total remaining service time for tasks.
  - `serverId`: unique server identifier.
- Methods:
  - `addTask(Task task)`: add a new task to the queue.
  - `decrementServiceTime()`: decrease service time of current task.
  - `isEmpty()`: check if queue is empty.
  - Runs in its own thread, processing one time unit per second.

### `Scheduler`
- Manages multiple servers and dispatches tasks based on a strategy.
- Attributes:
  - List of servers.
  - Maximum servers and max tasks per server.
  - Current dispatching strategy (`ShortestQueueStrategy` or `TimeStrategy`).
- Methods:
  - `changeStrategy(SelectionPolicy policy)`: switch dispatching strategy.
  - `dispatchTask(Task task)`: assign task to a server.
  - `stopScheduler()`: gracefully stops all server threads.

### `Strategy` (interface)
- Defines method `addTask(List<Server> servers, Task task)` for task dispatching.

### `ShortestQueueStrategy`
- Dispatches tasks to the server with the fewest tasks.

### `TimeStrategy`
- Dispatches tasks to the server with the shortest total waiting time.

### `SimulationManager` (implements `Runnable`)
- Runs the entire simulation.
- Attributes:
  - Simulation parameters (number of clients, servers, max times, strategy).
  - Task list.
  - Scheduler.
  - GUI references for animation.
  - Logs for simulation events.
- Dispatches tasks according to their arrival time.
- Updates GUI and logs every second.
- Calculates statistics such as peak hour and average times.

### GUI Classes
- `SimulationFrame`: Main window for inputting parameters and starting simulation.
- `AnimationFrame`: Displays visual queue states and tasks.
- `QueuePanel`: Visual representation of each queue.

---

## Running the Project

1. Make sure JDK 17 or newer is installed.
2. Clone the repository:
   ```bash
   git clone https://github.com/username/queue-simulation.git
