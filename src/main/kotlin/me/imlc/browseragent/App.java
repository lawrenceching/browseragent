package me.imlc.browseragent;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ServiceManager;
import me.imlc.browseragent.logger.Logger;

import java.util.Objects;

public class App {

	private ServiceManager serviceManager;
	private BilibiliMonitorService bilibiliMonitorService;
	private Logger logger = new Logger(App.class);
	private Browser browser = new Browser();
	private ElasticSearch es;
	private Config config;


	public App(Config config) {

		this.config = config;
		this.es = new ElasticSearch(
				config.getEsHost(),
				config.getEsIndex()
		);

		bilibiliMonitorService = new BilibiliMonitorService(
				Lists.newArrayList(
						"https://www.bilibili.com/video/BV1WE411q7TA",
						"https://www.bilibili.com/video/BV1Xt411h7Zy",
						"https://www.bilibili.com/video/BV1oA411e7wE",
						"https://www.bilibili.com/video/BV1zJ411U7UM",
						"https://www.bilibili.com/video/BV13b411u7Z1"
				),
				browser,
				es
		);

		serviceManager = new ServiceManager(
				Lists.newArrayList(
						bilibiliMonitorService
				)
		);

	}


	public void start() {

		es.connect();

		serviceManager.startAsync();
		serviceManager.awaitHealthy();
	}

	public void stop() {
		serviceManager.stopAsync();
		serviceManager.awaitStopped();

		browser.quit();
		logger.info("Closed browser");
	}

	public static void main(String[] args) throws InterruptedException {

		Logger logger = new Logger("Main");

		String esHost = System.getProperty("elasticsearch.host");
		String esIndex = System.getProperty("elasticsearch.index");
		Objects.nonNull(esHost);
		Objects.nonNull(esIndex);

		Config config = new Config(
				true,
				esHost,
				esIndex
		);

		logger.info("Start up application with config: " + config.toString());


		final App app = new App(config);
		app.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				app.stop();
			}
		});
	}

}
