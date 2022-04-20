package mx.com.udemy.encuestasBackend;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
/**
 * Clase que permite leer las bean en el proyecto de forma global
 * @author pnolasco
 *
 */
public class SpringApplicationContext implements ApplicationContextAware {

	public static ApplicationContext CONTEXT;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CONTEXT = applicationContext;

	}

	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}

}
