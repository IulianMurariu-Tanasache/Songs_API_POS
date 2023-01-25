package com.pos.idm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class SoapWebServiceConfig extends WsConfigurerAdapter {

    @Value("${soap.endpoint}")
    private String soapEndpoint;
    @Value("${soap.namespace}")
    private String soapNamespace;
    @Value("${soap.xsd}")
    private String xsd;

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/" + soapEndpoint + "/*");
    }


    @Bean
    public XsdSchema userSchema() {
        return new SimpleXsdSchema(new ClassPathResource(xsd));
    }

    @Bean(name = "users")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema userSchema) {

        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();

        definition.setSchema(userSchema);
        definition.setLocationUri("/" + soapEndpoint);
        definition.setPortTypeName("myServicePort");
        definition.setTargetNamespace(soapNamespace);
        return definition;
    }


}
