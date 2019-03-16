/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.personage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.xslt.XsltView;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;

/**
 *
 * @author lian
 */
@Configuration
@ComponentScan(basePackages = "com.mycompany.personage")
@EnableWebMvc
public class MvcPesonageConfig extends WebMvcConfigurerAdapter{
        
        //XSL T视图解析器
        @Bean
	public ViewResolver getXSLTViewResolver() {

		XsltViewResolver xsltResolover = new XsltViewResolver();
		xsltResolover.setOrder(1);
		xsltResolover.setSourceKey("xmlSource");
		xsltResolover.setViewClass(XsltView.class);
		//ViewName
		xsltResolover.setViewNames(
			new String[]{
                                "XSLTViewXml"
			});
		xsltResolover.setPrefix("/WEB-INF/xsl/");
		xsltResolover.setSuffix(".xsl");

		return xsltResolover;
	}
        
        //视图解析器
        @Bean
	public ViewResolver getJSPViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(2);
		return resolver;
	}

        //实现WebMvcConfigurerAdapter的addResourceHandlers方法
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
        
}
