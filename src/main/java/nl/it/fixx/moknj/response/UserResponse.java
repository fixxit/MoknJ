package nl.it.fixx.moknj.response;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.domain.core.user.User;

public class UserResponse extends Response {

    private User resource;
    private List<User> resources = new ArrayList<>();
    private List<String> authorities = new ArrayList<>();

    public User getResource() {
        return resource;
    }

    public void setResource(User resource) {
        this.resource = resource;
    }

    public List<User> getResources() {
        return resources;
    }

    public void setResources(List<User> resources) {
        this.resources = resources;
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
        return "ResourceResponse{"
                + "resource=" + resource
                + ", resources=" + resources
                + ", authorities=" + authorities + '}';
    }
}
