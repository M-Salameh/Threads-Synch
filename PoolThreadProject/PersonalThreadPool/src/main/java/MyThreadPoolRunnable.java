public class MyThreadPoolRunnable<RunnableType extends Runnable> implements Runnable
{
    private TasksQueue<RunnableType> tasksQueue;

    private boolean isTerminated;

    private Thread thread;

    public MyThreadPoolRunnable(TasksQueue<RunnableType> tasksQueue)
    {
        this.tasksQueue = tasksQueue;
        this.isTerminated = false;
    }

    public void forceStop()
    {
        this.isTerminated = true;
        this.thread.interrupt();
    }

    @Override
    public void run()
    {
        thread = Thread.currentThread();

        while(!terminated())
        {
            try
            {
                RunnableType job = tasksQueue.removeTask();
                if (job == null) continue;
                job.run();
            }
            catch (InterruptedException e)
            {
                //ignore
            }
        }
    }

    private synchronized boolean terminated()
    {
        return this.isTerminated;
    }
}
