package BP.Tools;

import java.util.Enumeration;
import java.util.Properties;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import BP.Sys.Glo;

public class GvtvPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) {
		Enumeration<?> keys = props.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = props.getProperty(key);
			if (key.endsWith(".encryption") && null != value) {
				props.remove(key);

				key = key.substring(0, key.length() - 11);

				try {
					value = Glo.String_JieMi(value.trim());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				props.setProperty(key, value);
			}
			System.setProperty(key, value);
		}

		super.processProperties(beanFactoryToProcess, props);
	}

}
