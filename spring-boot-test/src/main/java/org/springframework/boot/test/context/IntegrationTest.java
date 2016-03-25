/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.test.context;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class annotation signifying that the tests are "integration tests" for a
 * {@link org.springframework.boot.SpringApplication Spring Boot Application}. By default
 * will load nested {@code @Configuration} classes, or fallback an
 * {@link SpringApplicationConfiguration @SpringApplicationConfiguration} search. Unless
 * otherwise configured, a {@link SpringApplicationContextLoader} will be used to load the
 * {@link ApplicationContext}. Use
 * {@link SpringApplicationConfiguration @SpringApplicationConfiguration} or
 * {@link ContextConfiguration @ContextConfiguration} if custom configuration is required.
 * <p>
 * It's recommended that {@code @IntegrationTest} is used only for non-web applications
 * (i.e. not combined with {@link WebAppConfiguration @WebAppConfiguration}). If you want
 * to start a real embedded servlet container in the same way as a production application
 * (listening on normal ports) use
 * {@link org.springframework.boot.test.context.web.WebIntegrationTest @WebIntegrationTest}
 * instead. If you are testing a web application and want to mock the servlet environment
 * (for example so that you can use {@link MockMvc}) you should switch to the
 * {@link SpringApplicationTest @SpringApplicationTest} annotation.
 *
 * @author Dave Syer
 * @author Phillip Webb
 * @see SpringApplicationTest
 * @see org.springframework.boot.test.context.web.WebIntegrationTest
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(IntegrationTestContextBootstrapper.class)
public @interface IntegrationTest {

	/**
	 * Properties in form {@literal key=value} that should be added to the Spring
	 * {@link Environment} before the test runs.
	 * @return the environment properties
	 */
	String[] value() default {};

}
