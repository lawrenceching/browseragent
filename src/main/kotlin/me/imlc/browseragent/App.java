package me.imlc.browseragent;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ServiceManager;
import me.imlc.browseragent.logger.Logger;

public class App {

	private ServiceManager serviceManager;
	private BilibiliMonitorService bilibiliMonitorService;
	private Logger logger = new Logger(App.class);
	private Browser browser = new Browser();


	public App() {

		bilibiliMonitorService = new BilibiliMonitorService(
				Lists.newArrayList(
						"https://www.bilibili.com/video/BV1WE411q7TA"
				),
				browser
		);

		serviceManager = new ServiceManager(
				Lists.newArrayList(
						bilibiliMonitorService
				)
		);

	}


	public void start() {
		serviceManager.startAsync();
		serviceManager.awaitHealthy();
	}

	public void stop() {
		serviceManager.stopAsync();
		serviceManager.awaitStopped();

		browser.close();
		logger.info("Closed browser");
	}

	public static void main(String[] args) throws InterruptedException {
		final App app = new App();
		app.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				app.stop();
			}
		});
	}

}
