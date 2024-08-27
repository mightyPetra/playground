package org.spend.manager;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.spend.model.User;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserManager {

    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public void register(User userToRegister) throws InvalidParameterException {
        if (anyUserFieldEmpty(userToRegister)) {
            throw new InvalidParameterException("User can not be null or have missing fields");
        } else if (users.containsValue(userToRegister)) {
            throw new IllegalStateException("User is already registered");
        } else {
            users.put(userToRegister.getEmail(), userToRegister);
        }
    }

    public User loginUser(User userToLogin) throws InvalidParameterException {
        if (anyLoginDetailsEmpty(userToLogin)) {
            throw new InvalidParameterException("User can not be null or have missing login details");
        } else if (!users.containsValue(userToLogin)) {
            throw new IllegalArgumentException("User does not exist");
        } else if (!userDetailsMatchStored(userToLogin)) {
            throw new IllegalStateException("Incorrect username or password");
        } else {
            return users.get(userToLogin.getEmail());
        }
    }

    private boolean anyUserFieldEmpty(User user) {
        return anyLoginDetailsEmpty(user)
            || isNullOrEmpty(user.getFirstName())
            || isNullOrEmpty(user.getLastName());
    }

    private boolean anyLoginDetailsEmpty(User user) {
        return Objects.isNull(user) || (isNullOrEmpty(user.getEmail()) || isNullOrEmpty(user.getPassword()));
    }

    private boolean isNullOrEmpty(String string) {
        return Objects.isNull(string) || string.isEmpty();
    }

    private boolean userDetailsMatchStored(User user) {
        return user.equals(users.get(user.getEmail()));
    }
}
