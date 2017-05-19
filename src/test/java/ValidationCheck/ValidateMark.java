package ValidationCheck;

import java.util.Arrays;
import java.util.Collection;

import dbservice.Validation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class ValidateMark {

    @Parameterized.Parameter(0)
    public String mark;
    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Parameterized.Parameters(name = "mark = {0}, expectedResult = {1}")
    public static Collection data() {
        Object[][] data = new Object[][]{
                {"2", true},
                {"5", true},
                {"1.9", false},
                {"5.1", false},
                {"не число", false},
        };
        return Arrays.asList(data);
    }

    @Test
    public void CheckValidateName() {
        boolean result = Validation.validateMark(mark);
        assertThat(mark + " is a " + (expectedResult ? "valid" : "invalid") + " mark", result, is(expectedResult));
    }
}
