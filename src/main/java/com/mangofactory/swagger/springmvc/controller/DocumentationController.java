package com.mangofactory.swagger.springmvc.controller;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.mangofactory.swagger.ControllerDocumentation;
import com.mangofactory.swagger.SwaggerConfiguration;
import com.mangofactory.swagger.springmvc.MvcApiReader;
import com.wordnik.swagger.core.Documentation;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequestMapping('/' + DocumentationController.CONTROLLER_ENDPOINT)
public class DocumentationController implements InitializingBean {

	public static final String CONTROLLER_ENDPOINT = "resources";
	
	@Getter @Setter
	private String apiVersion = "1.0";
	@Getter @Setter
	private String swaggerVersion = "1.1";

    // By default all the packages are scanned for documentation, if set only the specified packages are read
    // e.g. Lists.newArrayList("com.mangofactory.swagger.springmvc.test");
    @Getter @Setter
    private List<String> swaggerPackages = null;

    @Getter @Setter
	private String basePath = "/";
	
	@Autowired
	private WebApplicationContext wac;
	
	@Getter
	private MvcApiReader apiReader;
	 
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Documentation getResourceListing()
	{
        Documentation documentation = apiReader.getResourceListing();
        String path = basePath + "/" + CONTROLLER_ENDPOINT + "/";
        documentation.setBasePath(path);
		return documentation;
	}

    @RequestMapping(value="/**",method=RequestMethod.GET, produces="application/json")
    public @ResponseBody ControllerDocumentation getApiDocumentation(HttpServletRequest request)
	{
        String pattern = (String)request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String path = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());

		return apiReader.getDocumentation(path);
	}

	// TODO : 
	// Initializing apiReader here so that consumers only have
	// to declare a single bean, rather than many.
	// A better approach would be to use a custom xml declaration
	// and parser - like <swagger:documentation ... />
	public void afterPropertiesSet() throws Exception {
		String documentationBasePath = basePath;
		if (!basePath.endsWith("/"))
			documentationBasePath += "/";
//		documentationBasePath += CONTROLLER_ENDPOINT;
		
		SwaggerConfiguration config = new SwaggerConfiguration(apiVersion,swaggerVersion,documentationBasePath, swaggerPackages);
		apiReader = new MvcApiReader(wac, config);
	}
	
}
