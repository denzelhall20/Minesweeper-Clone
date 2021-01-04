package ms;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class MinesweeperCloneSpringApplication {

	public static void main(String[] args) {
		//SpringApplication.run(MinesweeperCloneSpringApplication.class, args);
		Application.launch(MinesweeperFxApplication.class, args);
	}

}
