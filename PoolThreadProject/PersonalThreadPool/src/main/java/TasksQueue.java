
import java.util.ArrayList;
import java.util.List;

public class TasksQueue <RunnableType>
{
    private List<RunnableType> tasks;
    private int maxSize;

    private boolean allowInsertion;
    public TasksQueue(int maxCap)
    {
        this.maxSize = maxCap;
        tasks = new ArrayList<>();
        allowInsertion = true;
    }

    public void addTask(RunnableType task) throws Exception
    {
        if (task == null)
        {
            throw new Exception("Task Assigned is NULL");
        }
        synchronized (this)
        {
            if (!allowInsertion) return;
            if (tasks.size() == maxSize)
            {
                ///System.out.println("Stuck in Adding");
                this.wait();
            }
            tasks.add(task);
            this.notifyAll();
        }
    }

    public RunnableType removeTask () throws InterruptedException
    {
        RunnableType task = null;
        synchronized (this)
        {
            if (tasks.size()==0 && allowInsertion)
            {
                ///System.out.println("Stuck in Removing");
                this.wait();
            }
            if (tasks.size() ==0 && !allowInsertion) return task;
            if(tasks.size() == 0)
            {
                /*System.out.println("HHHHHHHHHHHH");
                System.out.println("!allow" + !allowInsertion);*/
            }
            task = tasks.remove(0);
            this.notifyAll();
        }
        return task;
    }

    public void softFinish(long timeOut) throws InterruptedException
    {
            allowInsertion = false;
            while (this.size() > 0)
            {
                this.wait(timeOut);
            }
    }

    private int size()
    {
        synchronized (tasks)
        {
            return tasks.size();
        }
    }
}
