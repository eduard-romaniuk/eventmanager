package com.example.eventmanager.validator;

import com.example.eventmanager.dao.UsersRepository;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User userRegistration = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "username", "NotEmpty", null, "Empty or whitespaces");
        if (userRegistration.getUsername().contains(" ")) {
            errors.rejectValue("username", "UsernameWithWhitespaces", null,
                    "Cannot contain whitespaces");
        } else if (userRegistration.getUsername().length() < 6 || userRegistration.getUsername().length() > 45) {
            errors.rejectValue("username", "UsernameSize", null,
                    "Invalid username size (6 - 45 symbols)");
        } else if (usersRepository.findByUsername(userRegistration.getUsername()) != null) {
            errors.rejectValue("username", "UsernameDuplicate", null, "Username already used");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "password", "NotEmpty", null, "Empty or whitespaces");
        if (userRegistration.getPassword().contains(" ")) {
            errors.rejectValue("password", "PasswordWithWhitespaces", null,
                    "Cannot contain whitespaces");
        } else if (userRegistration.getPassword().length() < 8 || userRegistration.getPassword().length() > 32) {
            errors.rejectValue("password", "PasswordSize", null,
                    "Invalid password size (8 - 32 symbols)");
        }  else if (!userRegistration.getPassword().toUpperCase().matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
            errors.rejectValue("password", "PasswordWeak", null,
                    "Password must contains letters and numbers");
        } else if (!userRegistration.getPassword().equals(userRegistration.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "PasswordConfirm", null, "Password confirm error");
        }
    }
}
