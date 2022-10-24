/**
 * 
 */
package cn.jflow.boot;

import cn.jflow.boot.config.JFlowConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * JFlow Boot版本应用
 * @author Bryce Han
 *
 */
@SpringBootApplication
public class JFlowApplication  {
	
	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = SpringApplication.run(JFlowApplication.class, args);
		JFlowConfig.setApplicationContext(applicationContext);	
	}


}
