package nl.it.fixx.moknj.response;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;
import nl.it.fixx.moknj.domain.core.access.AccessRight;
import nl.it.fixx.moknj.domain.core.user.User;

public class UserResponse extends Response {

    private User resource;
    private List<User> resources = new ArrayList<>();
    private List<String> authorities = new ArrayList<>();
    private List<AccessRight> rights = new ArrayList<>();
    private List<Access> accessRules = new ArrayList<>();

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

    /**
     * @return the rights
     */
    public List<AccessRight> getRights() {
        return rights;
    }

    /**
     * @param rights the rights to set
     */
    public void setRights(List<AccessRight> rights) {
        this.rights = rights;
    }

    /**
     * @return the accessRules
     */
    public List<Access> getAccessRules() {
        return accessRules;
    }

    /**
     * @param accessRules the accessRules to set
     */
    public void setAccessRules(List<Access> accessRules) {
        this.accessRules = accessRules;
    }

    @Override
    public String toString() {
        return "UserResponse{"
                + "resource=" + resource
                + ", resources=" + resources
                + ", authorities=" + authorities
                + ", rights=" + rights
                + ", accessRules=" + accessRules
                + '}';
    }

}
