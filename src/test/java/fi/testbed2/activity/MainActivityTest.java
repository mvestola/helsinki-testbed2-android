package fi.testbed2.activity;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import fi.testbed2.AbstractTestCase;
import fi.testbed2.R;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest extends AbstractTestCase {

    @Test
    public void shouldReturnCorrectName() throws Exception {
        String appName = new MainActivity().getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("Helsinki Testbed Viewer 2.0"));
    }

}
