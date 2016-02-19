package org.camunda.bpm.extension.osgi.el;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Properties;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.osgi.JustAnotherJavaDelegate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

/**
 * Integration test to check if the OSGiELResolver finds a JavaDelegate via LDAP
 * filter.
 * 
 * 
 * @author Ronny Bräunlich
 * 
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class OSGiELResolverLdapIntegrationTest extends
		AbstractOSGiELResolverIntegrationTest {

	@Override
	protected File getProcessDefinition() {
		return new File(
				"src/test/resources/org/camunda/bpm/extension/osgi/el/ldaptestprocess.bpmn");
	}

	@Test
	public void runProcess() throws Exception {
		Properties properties = new Properties();
		properties.setProperty("processExpression", "thisIsAReallyNeatFeature");
		JustAnotherJavaDelegate service = new JustAnotherJavaDelegate();
		ctx.registerService(JavaDelegate.class.getName(), service, properties);
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey("ldap");
		assertThat(service.called, is(true));
		assertThat(processInstance.isEnded(), is(true));
	}

}