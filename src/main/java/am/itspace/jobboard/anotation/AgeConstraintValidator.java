package am.itspace.jobboard.anotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Date;
import java.util.Calendar;

public class AgeConstraintValidator implements ConstraintValidator<MinAge, Date> {
    public boolean isValid(Date birthDate, ConstraintValidatorContext context) {
        if (birthDate != null) {

            Calendar dob = Calendar.getInstance();
            dob.setTime(birthDate);
            Calendar now = Calendar.getInstance();

            int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (now.get(Calendar.MONTH) < dob.get(Calendar.MONTH) ||
                    (now.get(Calendar.MONTH) == dob.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH))) {
                age--;
            }
            return age >= 18;

        }
        return false;
    }
}
