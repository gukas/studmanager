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
public class ValidateGroupId {

    @Parameterized.Parameter(0)
    public String groupid;
    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Parameterized.Parameters(name = "groupid = {0}, expectedResult = {1}")
    public static Collection data() {
        Object[][] data = new Object[][]{
                {"-2147483648", true},
                {"2147483647", true},
                {"-2147483649", false},
                {"2147483648", false},
                {"не число", false},
                {"3.14", false},
        };
        return Arrays.asList(data);
    }

    @Test
    public void CheckValidateName() {
        boolean result = Validation.validateGroupId(groupid);
        assertThat(groupid + " is a " + (expectedResult ? "valid" : "invalid") + " groupid", result, is(expectedResult));
    }
}
