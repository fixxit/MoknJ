package nl.it.fixx.moknj.builders;

import nl.it.fixx.moknj.domain.core.user.User;

public class UserBuilder {

    private final User resource = new User();

    public UserBuilder firstName(String firstName) {
        this.resource.setFirstName(firstName);
        return this;
    }

    public UserBuilder surname(String surname) {
        this.resource.setSurname(surname);
        return this;
    }

    public UserBuilder email(String email) {
        this.resource.setEmail(email);
        return this;
    }

    public UserBuilder contactNumber(String contactNumber) {
        this.resource.setContactNumber(contactNumber);
        return this;
    }

    public User build() {
        return this.resource;
    }
}
