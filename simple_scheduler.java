// This is a messy simple_scheduler using Preemptive Priority
// With RR as a secondary

import java.util.*;
import java.io.*;

// Arrival, Check for complete, Preemption
class Process {
  int id;
  int arrival;
  int priority;
  int cpuTime;

  public int getId() {return id;}
  public int getArrival() { return arrival; }
  public int getPriority() { return priority; }
  public int getTime() { return cpuTime; }

  void setTime(int cpuTime) {
    this.cpuTime = cpuTime;
  }

  void setId(int id) {
    this.id = id;
  }

  void setArrival(int arrival) {
    this.arrival = arrival;
  }

  void setPriority(int priority) {
    this.priority = priority;
  }
}

public class simple_scheduler {
  public static void main(String [] args) throws IOException {
    Scanner kb = new Scanner(System.in);
    System.out.println("Enter file name");
    String filename = kb.nextLine();
    System.out.println("Enter quantum");
    int timeQuantum = kb.nextInt();
    // max number of processes that can be on the readyQ at one time will be the number of processes.
    Scanner infile = new Scanner(new File(filename));
    int processNumber = infile.nextInt();
    Process[] processArray = new Process[processNumber];
    Process[] readyQueue = new Process[processNumber+1];
    int[] completionTime = new int[processNumber];
    int[] ganttChart = new int[9999];
    infile.nextLine();
    System.out.println("Reading processes");
    for(int i = 0; i < processNumber; i++) {
      String line = infile.nextLine();
      Scanner input = new Scanner (line);
      int arrival = input.nextInt();
      int priority = input.nextInt();
      int cpu_time = input.nextInt();
      processArray[i] = new Process();
      processArray[i].setId(i);
      processArray[i].setArrival(arrival);
      processArray[i].setPriority(priority);
      processArray[i].setTime(cpu_time);
    }
    int cpuIter = 0;
    int currentProcess = 0;
    int arrivalProcess = currentProcess + 1;
    int readyPos = 0;
    int remainingProcesses = processNumber;

    while ( processArray[currentProcess].getArrival() != cpuIter ) {
        ganttChart[cpuIter] = -1;
        cpuIter++;
    }
    // Our first process has arrived.
    System.out.println("Process " + currentProcess + " arrived at time: " + cpuIter);
    // Stick it on the readyQ at 0
    readyQueue[readyPos] = processArray[currentProcess];

    // Make the readyQueue ready for the next process
    readyPos++;

////////////////////////////////////////////////////////////////////////////////////////////

    while ( remainingProcesses > 0 ) {
      if (arrivalProcess != processNumber) {
        if ( processArray[arrivalProcess].getArrival() == cpuIter ) {
          System.out.println("Process " + arrivalProcess + " arrived at time: " + cpuIter);
          readyQueue[readyPos] = processArray[arrivalProcess];
          readyQueue[readyPos+1] = null;
          arrivalProcess++;
          readyPos++;
        }
      }

      if (readyQueue[1] != null) {
        // sort the readyQ ITO priority using a simple bubble sort
        //System.out.println("Sorting");
        for (int i = 0; i < readyPos; i++) {
          for (int j = i+1; j < readyPos; j++) {
            if (readyQueue[i] != null && readyQueue[j] != null) {
              if (readyQueue[i].getPriority() > readyQueue[j].getPriority()) {
                Process temp = readyQueue[i];
                readyQueue[i] = readyQueue[j];
                readyQueue[j] = temp;
              }
            }
          }
        }
        // Let's check for Round-Robin
        if(readyQueue[0].getId() != readyQueue[1].getId()) {
          while(readyQueue[0].getPriority() == readyQueue[1].getPriority()) {
            currentProcess = readyQueue[0].getId();

            for (int i = 0; i < timeQuantum; i++) {
              if (processArray[currentProcess].getTime() < 1) {
                break;
              }

              processArray[currentProcess].setTime(processArray[currentProcess].getTime()-1);
              if(readyQueue[0] != null) {
                ganttChart[cpuIter] = processArray[currentProcess].getId();
              } else {
                ganttChart[cpuIter] = -1;
              }

              if (processArray[currentProcess].getTime() < 1) {
                break;
              }


              if(readyQueue[0] != null){
              System.out.print("ReadyQ: ");
                for (int j = 0; j < readyPos; j++) {
                  System.out.print(readyQueue[j].getId()+" ");
                }
              }

              System.out.println(" ");
              System.out.println("Process " +processArray[currentProcess].getId() +" ran at time: " + cpuIter);
              System.out.println("Burst remaining: " +processArray[currentProcess].getTime());
              System.out.println(" ");

              cpuIter++;
            }

            if (processArray[currentProcess].getTime() < 1) {
              System.out.println("Process " + currentProcess + " has finished at time: " + cpuIter);

              completionTime[currentProcess] = cpuIter;
              remainingProcesses--;

              for(int i = 1; i < readyPos; i++) {
                  readyQueue[i-1] = readyQueue[i];
              }

              readyPos--;

            } else {

              for(int i = 0; i < readyPos-1; i++) {
                readyQueue[i] = readyQueue[i+1];
              }

              readyQueue[readyPos-1] = processArray[currentProcess];

              for (int i = 0; i < readyPos; i++) {
                for (int j = i+1; j < readyPos; j++) {
                  if (readyQueue[i] != null && readyQueue[j] != null) {
                    if (readyQueue[i].getPriority() > readyQueue[j].getPriority()) {
                      Process temp = readyQueue[i];
                      readyQueue[i] = readyQueue[j];
                      readyQueue[j] = temp;
                    }
                  }
                }
              }
            }
          }
        }
      }

      currentProcess = readyQueue[0].getId();
      processArray[currentProcess].setTime(processArray[currentProcess].getTime()-1);
      if(readyQueue[0] != null) {
        ganttChart[cpuIter] = processArray[currentProcess].getId();
      } else {
        ganttChart[cpuIter] = -1;
      }
      System.out.println(" ");
      System.out.println("Process " +processArray[currentProcess].getId() +" ran at time: " + cpuIter);
      System.out.println("Burst remaining: " +processArray[currentProcess].getTime());
      System.out.println(' ');

      cpuIter++;

      if (processArray[currentProcess].getTime() < 1) {
        System.out.println("Process " + currentProcess + " has finished at time: " + cpuIter);

        completionTime[currentProcess] = cpuIter;
        remainingProcesses--;

        for(int i = 1; i < readyPos; i++) {
            readyQueue[i-1] = readyQueue[i];
        }

        readyPos--;
        currentProcess = readyQueue[0].getId();
      }

      if(readyQueue[0] != null) {
        System.out.print("ReadyQ: ");
        for (int i = 0; i < readyPos; i++) {
          System.out.print(readyQueue[i].getId()+" ");
        }
      }
      System.out.println(" ");
    }
    System.out.println("Gantt Chart:");
    System.out.println(" ");
    System.out.print('|');

    for(int i=0; i < cpuIter+1; i++) {
      if(ganttChart[i]==-1) {
        System.out.print("x|");
      } else {
        System.out.print(ganttChart[i] + "|");
      }
    }

    System.out.println(" ");

    int[] turnAroundTime = new int[processNumber];
    int totalTurnAroundTime = 0;

    for(int i = 0; i < processNumber; i++) {
      turnAroundTime[i] = completionTime[i] - processArray[i].getArrival();
      totalTurnAroundTime += turnAroundTime[i];
      System.out.println("Turn around time for process " + i + " was: " + turnAroundTime[i]);
    }

    int avgTT = totalTurnAroundTime/processNumber;
    System.out.println("Average turnaround time: " + avgTT);
    System.out.println("BINKY!!!");
  }
}
