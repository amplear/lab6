package com.example.AsyncScheduledApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
public class AsyncScheduledAppApplication {
	private static final Instant startTime = Instant.now();
	private static boolean periodicTaskEnabled = false;

	public static void main(String[] args) {
		SpringApplication.run(AsyncScheduledAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner startInteractiveTaskControl() {
		return args -> {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Do you want to enable a task that runs every 10 seconds? (Y/N)");
			String input = scanner.nextLine().trim().toUpperCase();

			if ("Y".equals(input)) {
				periodicTaskEnabled = true;
				System.out.println("Task enabled. It will run every 10 seconds.");
			} else {
				System.out.println("Task disabled. The periodic task will not run.");
			}

			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			executorService.scheduleWithFixedDelay(() -> {
				int delay = ThreadLocalRandom.current().nextInt(1, 11);
				try {
					System.out.println("Random delay task executed after " + delay + " seconds. Elapsed time since start: " +
							Duration.between(startTime, Instant.now()).getSeconds() + " seconds.");
					Thread.sleep(delay * 1000L);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}, 0, 1, TimeUnit.SECONDS);
		};
	}
	@Scheduled(fixedRate = 10000)
	public void runPeriodicTask() {
		if (periodicTaskEnabled) {
			System.out.println("Periodic task executed at: " + Instant.now());
		} else {
			System.out.println("Periodic task skipped at: " + Instant.now());
		}
	}

}
