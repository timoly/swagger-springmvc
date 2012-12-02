package com.mangofactory.swagger;

import lombok.Data;

import com.mangofactory.swagger.springmvc.MvcApiResource;
import com.wordnik.swagger.core.Documentation;
import lombok.Getter;

import java.util.List;

@Data
public class SwaggerConfiguration {

	public SwaggerConfiguration(String apiVersion, String swaggerVersion,
			String basePath, List<String> swaggerPackages) {
		this.apiVersion = apiVersion;
		this.swaggerVersion = swaggerVersion;
		this.basePath = basePath;
        this.swaggerPackages = swaggerPackages;
	}
	private String swaggerVersion;
	private String apiVersion;
	private String basePath;
    private List<String> swaggerPackages;

	public ControllerDocumentation newDocumentation(MvcApiResource resource) {
		return new ControllerDocumentation(apiVersion, swaggerVersion, basePath, resource);
	}
	public Documentation newDocumentation()
	{
		return new Documentation(apiVersion, swaggerVersion, basePath, null);
	}
}
