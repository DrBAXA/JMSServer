package ua.datalink.jms.client.configuration;

/**
 *
 */

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Configuration
@ComponentScan(basePackages = "ua.datalink")
@PropertySources({
        @PropertySource("classpath:properties/jms.properties"),
})
@Import({MvcConfiguration.class})
public class AppContext {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${jms.server}")
    String jmsServerURI;
    @Value("${jms.user.name}")
    String jmsUserName;
    @Value("${jms.user.password}")
    String jmsUserPassword;

    @Bean(name = "jmsConnectionFactory")
    ConnectionFactory getSession() throws JMSException {
        return new ActiveMQConnectionFactory(jmsUserName, jmsUserPassword, jmsServerURI);
    }
}
