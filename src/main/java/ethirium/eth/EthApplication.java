package ethirium.eth;

import ethirium.eth.scraper.Crawler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.IOException;

@SpringBootApplication
public class EthApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EthApplication.class);
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		SpringApplication.run(EthApplication.class, args);
//		Crawler.initialise();
	}
}
