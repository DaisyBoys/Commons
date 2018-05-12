package com.iooicc.GlobalNews;

import com.iooicc.GlobalNews.common.GlobalNewsSettings;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@ConfigurationProperties(prefix="class",locations="classpath:/application.properties")
@EnableConfigurationProperties(GlobalNewsSettings.class)
@SpringBootTest
public class GlobalNewsApplicationTests {

    @Autowired
	private GlobalNewsSettings globalNewsSettings;

	@Value("${class.accessPath}")
	public  String accessPath ;

	@Test
	public void contextLoads() {
    System.out.print(accessPath);
		System.out.print(globalNewsSettings.getAccessPath());

	}

}
