public class Main
{
    public static void main(String[] args) throws Exception
    {
        MyThreadPool myThreadPool = new MyThreadPool<>(3 , 50);
        for(int i = 0; i < 100; i++)
        {
            int taskNo = i;
            myThreadPool.execute(new Runnable()
            {
                @Override
                public void run() {
                    String message = Thread.currentThread().getName() + ": Task " + (taskNo + 1) ;
                    System.out.println(message);
                }
            });
        }
       /* myThreadPool.waitUntilAllTasksFinished(1000);
        myThreadPool.stop();*/

    }
}
