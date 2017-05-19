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
public class ValidateBirthday {

    @Parameterized.Parameter(0)
    public String birthday;
    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Parameterized.Parameters(name = "birthday = {0}, expectedResult = {1}")
    public static Collection data() {
        Object[][] data = new Object[][]{
                {"2016-02-29", true},
                {"2001-02-29", false},
                {"", false},
                {"0000-00-00", false},
                {"01-01-2000", false},
        };
        return Arrays.asList(data);
    }

    @Test
    public void CheckValidateName() {
        boolean result = Validation.validateBirthday(birthday);
        assertThat(birthday + " is a " + (expectedResult ? "valid" : "invalid") + " birthday", result, is(expectedResult));
    }
}
