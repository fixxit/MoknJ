package nl.fixx.asset.data.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by Colin on 10/12/2016 1:57 PM
 */
public class Resource {
    @Id
    private String id;

    private String firstName;
    private String surname;
    private String email;
    private String contactNumber; //decided on string so we can save dashes, +country code and leading zeros if required
    private String placedAtClient; //current client where resource is working

    @Override
    public String toString() {
        return "Resource{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", placedAtClient='" + placedAtClient + '\'' +
                '}';
    }

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

    public String getPlacedAtClient() {
        return placedAtClient;
    }

    public void setPlacedAtClient(String placedAtClient) {
        this.placedAtClient = placedAtClient;
    }
}
