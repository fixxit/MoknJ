package nl.fixx.asset.data;

import nl.it.fixx.moknj.Application;
import nl.it.fixx.moknj.properties.ApplicationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Adriaan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class PropertyLoadingTest {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyLoadingTest.class);

    @Autowired
    private ApplicationProperties sampleProperty;

    @Test
    public void testLoadingOfProperties() {
        LOG.debug("security : " + sampleProperty.getSecurity());
        LOG.debug("System : " + sampleProperty.getSystem());
        LOG.debug("Admin : " + sampleProperty.getAdmin());
        LOG.debug("Environment : " + sampleProperty.getEnvironment());
        assertThat(sampleProperty.getSecurity(), notNullValue());
        assertThat(sampleProperty.getSystem(), notNullValue());
        assertThat(sampleProperty.getAdmin(), notNullValue());
    }

}
