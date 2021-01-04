package ms;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import javafx.application.Application;
//import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import ms.events.StageReadyEvent;

public class MinesweeperFxApplication extends Application{
	
	private ConfigurableApplicationContext context;
	
	@Override
	public void init() throws Exception {
		ApplicationContextInitializer<GenericApplicationContext> initializer = 
				new ApplicationContextInitializer<>() {

					@Override
					public void initialize(GenericApplicationContext applicationContext) {
						applicationContext.registerBean(Application.class, () -> MinesweeperFxApplication.this);
					}
			
		};
		this.context = new SpringApplicationBuilder()
					.sources(MinesweeperCloneSpringApplication.class)
					.initializers(initializer)
					.run(getParameters().getRaw().toArray(new String[0]));
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.context.publishEvent(new StageReadyEvent(primaryStage));
		
	}
	
	@Override
	public void stop() {
		this.context.close();
		Platform.exit();
	}
}