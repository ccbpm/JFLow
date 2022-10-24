package bp.difference;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class Helper {

	/**
	 * @return
	 * @throws IOException
	 */
	public static InputStream loadResource() throws IOException {
		InputStream is;
		ResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource("jflow.properties");
		is = resource.getInputStream();
		return is;
	}
}
