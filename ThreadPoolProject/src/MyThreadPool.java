
import java.util.ArrayList;
import java.util.List;

public class MyThreadPool <RunnableType extends Runnable>
{
    private TasksQueue<RunnableType> tasksQueue;

    private List<MyThreadPoolRunnable> workingThreads;

    private boolean isTerminated;
    private boolean allowInserting;

    private int queueSize;
    private int workingThreadsNumber;

    public MyThreadPool(int queueSizeInput, int workingThreadsNumberInput) throws Exception
    {
        if (queueSizeInput <=0 || workingThreadsNumberInput <= 0)
        {
            throw new Exception("Queue Size and Threads Number must be Strictly Larger than zero!!");
        }
        this.queueSize = queueSizeInput;
        this.workingThreadsNumber = workingThreadsNumberInput;
        this.isTerminated = false;
        this.allowInserting = true;
        initiateThreadPool();
        startWorking();
    }

    public void execute (RunnableType runnableType) throws Exception
    {
        if (isTerminated || !allowInserting) return;
        this.tasksQueue.addTask(runnableType);
    }

    public void stop()
    {
        synchronized (this)
        {
            this.isTerminated = true;
            this.allowInserting = false;
            for (MyThreadPoolRunnable runnable : workingThreads)
                runnable.forceStop();
        }
    }

    public synchronized void waitUntilAllTasksFinished(long timeOut) throws InterruptedException
    {
        if (this.isTerminated) return;
        allowInserting = false;
        this.isTerminated = true;
        tasksQueue.softFinish(timeOut);

    }

    private void initiateThreadPool()
    {
        this.tasksQueue = new TasksQueue<>(this.queueSize);

        this.workingThreads = new ArrayList<>();

        for (int i=1 ; i<= this.workingThreadsNumber ; i++)
        {
            workingThreads.add(new MyThreadPoolRunnable(tasksQueue));
        }
    }

    private void startWorking()
    {
        for (MyThreadPoolRunnable myThreadPoolRunnable : workingThreads)
            new Thread(myThreadPoolRunnable).start();
    }
}
