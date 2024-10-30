package collection.deque.test.queue;

public class SchedulerTest {
    public static void main(String[] args) {
        //낮에 작업을 저장
        TaskScheduler scheduler = new TaskScheduler();
        scheduler.addTask(new CompressionTask());
        scheduler.addTask(new BackupTask());
        scheduler.addTask(new CleanTask());

        //새벽 시간에 실행
        System.out.println("==작업 시작==");
        run(scheduler);
        System.out.println("==작업 완료==");
    }

    private static void run(TaskScheduler scheduler) {
        while (scheduler.getRemainingTasks() > 0) {
            scheduler.processNextTask();
        }
    }
}