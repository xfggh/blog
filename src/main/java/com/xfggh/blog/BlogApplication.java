package com.xfggh.blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class BlogApplication {

	private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BlogApplication.class);
		Environment env = app.run(args).getEnvironment();
		LOG.info("Blog 启动成功");
		LOG.info("地址：http://127.0.0.1:{}", env.getProperty("server.port"));

		//		SpringApplication.run(BlogApplication.class, args);
	}

}
