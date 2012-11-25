package fi.testbed2;

import android.app.Application;
import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import fi.testbed2.app.MainApplication;
import org.junit.runners.model.InitializationError;
import roboguice.application.RoboApplication;
import roboguice.inject.ContextScope;

/**
 * Use this test runner to for activity testing. This injects the
 * beans in the activities defined in the TestModule class.
 */
public class InjectedTestRunner extends RobolectricTestRunner {

    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public void prepareTest(Object test) {
        RoboApplication sampleApplication = (RoboApplication) Robolectric.application;
        Injector injector = sampleApplication.getInjector();
        ContextScope scope = injector.getInstance(ContextScope.class);
        scope.enter(sampleApplication);
        injector.injectMembers(test);
    }

    @Override
    protected Application createApplication() {
        MainApplication application =
                (MainApplication) super.createApplication();
        application.setModule(new TestModule());
        return application;
    }

}