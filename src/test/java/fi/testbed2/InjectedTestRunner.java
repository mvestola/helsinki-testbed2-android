package fi.testbed2;

import org.junit.runners.model.InitializationError;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.TestLifecycle;

import javax.annotation.Nonnull;

import roboguice.RoboGuice;

/**
 * Use this test runner to for activity testing. This automatically injects the
 * beans in the activities defined in the TestModule class.
 */
@SuppressWarnings("rawtypes")
public class InjectedTestRunner extends RobolectricTestRunner {

    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Nonnull
    @Override
    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        return TestLifeCycleWithInjection.class;
    }

    public static class TestLifeCycleWithInjection extends DefaultTestLifecycle {

        @SuppressWarnings("deprecation")
        @Override
        public void prepareTest(Object test) {
            RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new TestModule());
            RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(test);
        }
    }

}