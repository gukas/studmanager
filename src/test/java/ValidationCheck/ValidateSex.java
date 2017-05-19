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
public class ValidateSex {

    @Parameterized.Parameter(0)
    public String sex;
    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Parameterized.Parameters(name = "sex = {0}, expectedResult = {1}")
    public static Collection data() {
        Object[][] data = new Object[][]{
                {"Male", true},
                {"Female", true},
                {"", false},
                {"something else", false},
        };
        return Arrays.asList(data);
    }

    @Test
    public void CheckValidateName() {
        boolean result = Validation.validateSex(sex);
        assertThat(sex + " is a " + (expectedResult ? "valid" : "invalid") + " sex", result, is(expectedResult));
    }
}
