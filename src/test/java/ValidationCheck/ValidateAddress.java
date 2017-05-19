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
public class ValidateAddress {

    @Parameterized.Parameter(0)
    public String address;
    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Parameterized.Parameters(name = "address = {0}, expectedResult = {1}")
    public static Collection data() {
        Object[][] data = new Object[][]{
                {"Ю", true},
                {"Kim Дот.com -------------------- тоже адрес годный", true},
                {"3,14", true},
                {"", false},
                {"Очень Длинннннннннннннннннннннннннннннннннный Адрес", false},
        };
        return Arrays.asList(data);
    }

    @Test
    public void CheckValidateName() {
        boolean result = Validation.validateAddress(address);
        assertThat(address + " is a " + (expectedResult ? "valid" : "invalid") + " address", result, is(expectedResult));
    }
}
