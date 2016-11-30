package nl.it.fixx.moknj.domain.core.user;

import java.util.List;
import org.springframework.data.annotation.Id;

/**
 * Created by Colin on 10/12/2016 1:57 PM
 */
public class User {

    @Id
    private String id;
    private String firstName;
    private String surname;
    private String email;
    private String contactNumber; // decided on string so we can save dashes,
    // +country code and leading zeros if required
    private boolean systemUser;
    private String userName;
    private String password;
    private boolean hidden;
    private List<String> authorities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * @return the systemUser
     */
    public boolean isSystemUser() {
        return systemUser;
    }

    /**
     * @param systemUser the systemUser to set
     */
    public void setSystemUser(boolean systemUser) {
        this.systemUser = systemUser;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the authorities
     */
    public List<String> getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "Resource{" + "id=" + id
                + ", firstName=" + firstName
                + ", surname=" + surname
                + ", email=" + email
                + ", contactNumber=" + contactNumber
                + ", systemUser=" + systemUser
                + ", userName=" + userName
                + ", password=" + password
                + ", hidden=" + hidden
                + ", authorities=" + authorities + '}';
    }

}
