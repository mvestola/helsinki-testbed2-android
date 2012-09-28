package fi.testbed2.result;

import fi.testbed2.data.TestbedParsedPage;

/**
 * Results from parsing the HTML page and downloading the latest map image.
 */
public class ParseAndInitTaskResult extends AbstractTaskResult {

    private TestbedParsedPage testbedParsedPage;

    public ParseAndInitTaskResult(TaskResultType type, String message) {
        super(type, message);
    }

    public TestbedParsedPage getTestbedParsedPage() {
        return testbedParsedPage;
    }

    public void setTestbedParsedPage(TestbedParsedPage testbedParsedPage) {
        this.testbedParsedPage = testbedParsedPage;
    }

}
