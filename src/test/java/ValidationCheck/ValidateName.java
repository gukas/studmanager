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
public class ValidateName {

    @Parameterized.Parameter(0)
    public String name;
    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Parameterized.Parameters(name = "name = {0}, expectedResult = {1}")
    public static Collection data() {
        Object[][] data = new Object[][]{
                {"Иа", true},
                {"Kim Дот.com -- тоже имя годное", true},
                {"Ю", false},
                {"3,14", false},
                {"Очень Длинннннннннннннннное Имя", false},
        };
        return Arrays.asList(data);
    }

    @Test
    public void CheckValidateName() {
        boolean result = Validation.validateName(name);
        assertThat(name + " is a " + (expectedResult ? "valid" : "invalid") + " name", result, is(expectedResult));
    }
}
