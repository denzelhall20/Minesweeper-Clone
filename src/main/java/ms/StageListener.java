package ms;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ms.events.StageReadyEvent;

@Component
public class StageListener {

	private final String title;
	private final Resource fxml;
	private final ApplicationContext ac;
	
	public StageListener(@Value("${spring.application.ui.title}") String title,
						 @Value("classpath:/static/ui.fxml") Resource fxml,
						 ApplicationContext ac) {
		this.title = title;
		this.fxml = fxml;
		this.ac = ac;
	}
	
	@EventListener
	public void onStageReady(StageReadyEvent event) {
		
		Stage stage = event.getStage();
		
		try {
 			FXMLLoader loader = new FXMLLoader(fxml.getURL());
			loader.setControllerFactory(c -> {
				return ac.getBean(c);
			});
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle(title);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}