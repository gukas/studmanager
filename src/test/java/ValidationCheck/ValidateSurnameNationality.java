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
public class ValidateSurnameNationality {

    @Parameterized.Parameter(0)
    public String value;
    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Parameterized.Parameters(name = "value = {0}, expectedResult = {1}")
    public static Collection data() {
        Object[][] data = new Object[][]{
                {"Йо", true},
                {"Kim Дот.com ------------------ тоже фамилия годная", true},
                {"Ю", false},
                {"3,14", false},
                {"Очень Длинннннннннннннннннннннннннннннннная Фамилия", false},
        };
        return Arrays.asList(data);
    }

    @Test
    public void CheckValidateName() {
        boolean result = Validation.validateSurname(value);
        assertThat(value + " is a " + (expectedResult ? "valid" : "invalid") + " surname", result, is(expectedResult));
    }

    @Test
    public void CheckValidateNationality() {
        boolean result = Validation.validateNationality(value);
        assertThat(value + " is a " + (expectedResult ? "valid" : "invalid") + " nationality", result, is(expectedResult));
    }
}
