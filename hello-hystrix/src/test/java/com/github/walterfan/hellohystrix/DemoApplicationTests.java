package com.github.walterfan.hellohystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

class RemoteServiceTestSimulator {

	private long wait;

	RemoteServiceTestSimulator(long wait) throws InterruptedException {
		this.wait = wait;
	}

	String execute() throws InterruptedException {
		Thread.sleep(wait);
		return "Success";
	}
}

class RemoteServiceTestCommand extends HystrixCommand<String> {

	private RemoteServiceTestSimulator remoteService;

	RemoteServiceTestCommand(Setter config, RemoteServiceTestSimulator remoteService) {
		super(config);
		this.remoteService = remoteService;
	}

	@Override
	protected String run() throws Exception {
		return remoteService.execute();
	}
}

public class DemoApplicationTests {

	@Test
	public void givenSvcTimeoutOf100AndDefaultSettings_whenRemoteSvcExecuted_thenReturnSuccess()
			throws InterruptedException {

		HystrixCommand.Setter config = HystrixCommand
				.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroup"));

		String result = new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(100)).execute();
		assertEquals(result, "Success");
	}

	@Test(expected = HystrixRuntimeException.class)
	public void givenSvcTimeoutOf15000AndExecTimeoutOf5000_whenRemoteSvcExecuted_thenExpectHre()
			throws InterruptedException {

		HystrixCommand.Setter config = HystrixCommand
				.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest5"));

		HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
		commandProperties.withExecutionTimeoutInMilliseconds(5_000);
		config.andCommandPropertiesDefaults(commandProperties);

		new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(15_000)).execute();
	}
}
