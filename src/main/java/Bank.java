import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Bank {

    private static final int MAX_PASSWORD = 9999;

    public static void main(String[] args) {
        Random random = new Random();
        Vault vault = new Vault(random.nextInt(MAX_PASSWORD));
        List<Thread> threads = List.of(new AscendingHackerThread(vault), new DescendingHackerThread(vault), new PoliceThread());
        threads.stream().forEach(thread -> thread.start());
    }

    private static class Vault {
        private int password;

        public Vault(int password) {
            this.password = password;
        }

        boolean isCorrectPassword(int guess) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.password == guess;
        }
    }

    private static abstract class HackerThread extends Thread {
        protected Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void start() {
            System.out.println("Starting thread " + this.getName());
            super.start();
        }
    }

    private static class AscendingHackerThread extends HackerThread {

        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            IntStream.range(0, MAX_PASSWORD)
                    .filter(guess -> vault.isCorrectPassword(guess))
                    .forEach(guess ->
                    {
                        System.out.println(this.getName() + "guessed the password " + guess);
                        System.exit(1);
                    });
        }
    }

    private static class DescendingHackerThread extends HackerThread {

        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            IntStream.range(0, MAX_PASSWORD)
                    .map(i -> 0 + (MAX_PASSWORD - 1 - i))
                    .filter(s -> vault.isCorrectPassword(s))
                    .forEach(guess ->
                    {
                        System.out.println(this.getName() + "guessed the password " + guess);
                        System.exit(1);
                    });
        }
    }

    private static class PoliceThread extends Thread {

        @Override
        public void run() {
            IntStream.rangeClosed(0, 10)
                    .map(i -> 0 + (10 - 1 - i))
                    .forEach(s ->
                    {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(s);
                    });
            System.out.println("Game over for you hackers");
            System.exit(0);
        }
    }
}
