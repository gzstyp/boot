package com.fwtai.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan({"com.fwtai"})
@EnableTransactionManagement
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)//排除,移开
@EnableAsync
public class Launch extends SpringBootServletInitializer{

	public static void main(final String[] args){
		SpringApplication.run(Launch.class,args);
        System.out.println("--应用启动成功--");
	}

	/**打包war时能正常运行*/
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Launch.class);
    }
}