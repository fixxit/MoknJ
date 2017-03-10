package nl.it.fixx.moknj.response;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;

/**
 *
 * @author adriaan
 */
public class LinkResponse extends Response {

    private AssetLink link;
    private EmployeeLink employeeLink;

    private List<AssetLink> links;
    private List<EmployeeLink> employeeLinks;

    /**
     * @return the link
     */
    public AssetLink getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(AssetLink link) {
        this.link = link;
    }

    /**
     * @return the links
     */
    public List<AssetLink> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(List<AssetLink> links) {
        this.links = links;
    }

    /**
     * @return the employeeLink
     */
    public EmployeeLink getEmployeeLink() {
        return employeeLink;
    }

    /**
     * @param employeeLink the employeeLink to set
     */
    public void setEmployeeLink(EmployeeLink employeeLink) {
        this.employeeLink = employeeLink;
    }

    /**
     * @return the employeeLinks
     */
    public List<EmployeeLink> getEmployeeLinks() {
        return employeeLinks;
    }

    /**
     * @param employeeLinks the employeeLinks to set
     */
    public void setEmployeeLinks(List<EmployeeLink> employeeLinks) {
        this.employeeLinks = employeeLinks;
    }

    @Override
    public String toString() {
        return "LinkResponse{"
                + "link=" + link
                + ", employeeLink=" + employeeLink
                + ", links=" + links
                + ", employeeLinks=" + employeeLinks
                + '}';
    }

}
