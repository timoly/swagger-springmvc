package com.mangofactory.swagger.springmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.test.context.WebContextLoader;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mangofactory.swagger.ControllerDocumentation;
import com.mangofactory.swagger.springmvc.controller.DocumentationController;
import com.mangofactory.swagger.springmvc.test.TestConfiguration;
import com.wordnik.swagger.core.Documentation;
import com.wordnik.swagger.core.DocumentationEndPoint;
import com.wordnik.swagger.core.DocumentationOperation;
import com.wordnik.swagger.core.DocumentationParameter;
import org.springframework.web.servlet.HandlerMapping;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		loader=WebContextLoader.class,
		classes=TestConfiguration.class)
public class MvcApiReaderTest {

	@Autowired
	private DocumentationController controller;
	
	@Test
	public void findsDeclaredHandlerMethods()
	{
		Documentation resourceListing = controller.getResourceListing();
		assertThat(resourceListing.getApis(),hasSize(1));
        MockHttpServletRequest request = createRequestMock("pets");
        ControllerDocumentation petsDocumentation = controller.getApiDocumentation(request );
		assertThat(petsDocumentation, is(notNullValue()));
		DocumentationEndPoint documentationEndPoint = resourceListing.getApis().get(0);
		assertEquals("resources/pets" ,documentationEndPoint.getPath());
	}
	
	@Test
	public void findsExpectedMethods()
	{
        MockHttpServletRequest request = createRequestMock("pets");
		ControllerDocumentation petsDocumentation = controller.getApiDocumentation(request );
		DocumentationOperation operation = petsDocumentation.getEndPoint("/pets/{petId}",RequestMethod.GET);
		assertThat(operation, is(notNullValue()));
		assertThat(operation.getParameters(),hasSize(1));
		DocumentationParameter parameter = operation.getParameters().get(0);

		operation = petsDocumentation.getEndPoint("/pets/allMethodsAllowed", RequestMethod.GET);
		assertThat(operation, is(notNullValue()));
		operation = petsDocumentation.getEndPoint("/pets/allMethodsAllowed", RequestMethod.POST);
		assertThat(operation, is(notNullValue()));
		operation = petsDocumentation.getEndPoint("/pets/allMethodsAllowed", RequestMethod.DELETE);
		assertThat(operation, is(notNullValue()));
		operation = petsDocumentation.getEndPoint("/pets/allMethodsAllowed", RequestMethod.PUT);
		assertThat(operation, is(notNullValue()));
	}

    private MockHttpServletRequest createRequestMock(String path){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, "/" + DocumentationController.CONTROLLER_ENDPOINT + "/**");
        request.setServletPath("/" + DocumentationController.CONTROLLER_ENDPOINT + "/" + path);
        return request;
    }
}
