package co.id.kai.location;

import co.id.kai.location.SampleLocationApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleLocationApplication.class)
@WebAppConfiguration
public class SampleLocationApplicationTests {

	@Test
	public void contextLoads() {
	}

}