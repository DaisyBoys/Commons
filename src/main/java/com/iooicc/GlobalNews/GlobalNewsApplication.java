package com.iooicc.GlobalNews;

import com.iooicc.GlobalNews.common.GlobalNewsSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@EnableConfigurationProperties({GlobalNewsSettings.class})
@SpringBootApplication
public class GlobalNewsApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GlobalNewsApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(GlobalNewsApplication.class, args);
	}
}
