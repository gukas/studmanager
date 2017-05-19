package dbservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Validation {
    public static boolean validateName(String name) {
        return validateString(name, "[A-Za-zА-Яа-я.\\-\\s]{2,30}");
    }

    public static boolean validateSurname(String surname) {
        return validateString(surname, "[A-Za-zА-Яа-я.\\-\\s]{2,50}");
    }

    public static boolean validateSex(String sex) {
        return validateString(sex, "(Male|Female)");
    }

    public static boolean validateBirthday(String birthday) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            df.parse(birthday);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateNationality(String nationality) {
        return validateString(nationality, "[A-Za-zА-Яа-я.\\-\\s]{2,50}");
    }

    public static boolean validateAddress(String adress) {
        return validateString(adress, "[0-9A-Za-zА-Яа-я.\\,;\\-\\s]{1,50}");
    }

    public static boolean validateGroupId(String groupId) {
        try {
            Integer.parseInt(groupId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validateMark(String mark) {
        try
        {
            double result = Double.parseDouble(mark);
            return (result >= 2.0 && result <= 5.0)? true : false;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

    private static boolean validateString(String str, String regex) {
        if(str == null || str.isEmpty() || !str.matches(regex)) {
            return false;
        }
        return true;
    }
 }
