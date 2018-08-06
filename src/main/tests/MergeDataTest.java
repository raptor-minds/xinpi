import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MergeDataTest {

    @Before
    public  void prepareForTest() {
        ExcelWriter.deleteExcel("./resources/test2.xls");

    }

    @Test
    public void assertStringNull() {
        Assert.assertNotEquals("", null);
    }
}
