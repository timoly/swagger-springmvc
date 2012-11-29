package com.mangofactory.swagger.springmvc.controller;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

@Controller
@RequestMapping('/' + DocumentationController.CONTROLLER_ENDPOINT)
public class DocumentationController implements InitializingBean {

	public static final String CONTROLLER_ENDPOINT = "resources";
	
	@Getter @Setter
	private String apiVersion = "1.0";
	@Getter @Setter
	private String swaggerVersion = "1.1";
	
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
	
	@RequestMapping(value="/{apiName}/**",method=RequestMethod.GET, produces="application/json")
	public @ResponseBody ControllerDocumentation getApiDocumentation(@PathVariable("apiName") String apiName, HttpServletRequest request)
	{
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if(path == null){
            path = "";
        }
        else{
            StringBuilder builder = new StringBuilder(path);
            int start = path.indexOf(DocumentationController.CONTROLLER_ENDPOINT);
            //remove the start of the path including the swagger resources part
            builder = builder.delete(0, start + DocumentationController.CONTROLLER_ENDPOINT.length() + 1);
            path = builder.toString();
        }
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
		
		SwaggerConfiguration config = new SwaggerConfiguration(apiVersion,swaggerVersion,documentationBasePath);
		apiReader = new MvcApiReader(wac, config);
	}
	
}
