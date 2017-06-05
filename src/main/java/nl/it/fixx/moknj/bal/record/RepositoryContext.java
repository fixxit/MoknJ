package nl.it.fixx.moknj.bal.record;

import nl.it.fixx.moknj.properties.ApplicationProperties;
import nl.it.fixx.moknj.repository.AccessRepository;
import nl.it.fixx.moknj.repository.AssetLinkRepository;
import nl.it.fixx.moknj.repository.AssetRepository;
import nl.it.fixx.moknj.repository.EmployeeLinkRepository;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import nl.it.fixx.moknj.repository.FieldDetailRepository;
import nl.it.fixx.moknj.repository.GraphRepository;
import nl.it.fixx.moknj.repository.MenuRepository;
import nl.it.fixx.moknj.repository.TemplateRepository;
import nl.it.fixx.moknj.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The idea behind this class is a single service class which is used on every
 * controller. This removes the need to have custom service classes for each
 * controller as most of the times the service class becomes the business layer
 * which is in all accounts correct but code and logic is then repeated over
 * different services. This makes it a tedious job to maintain when new features
 * are added. So I decided away with that bs and enter the Single Service Layer
 * which if implement right would allow you change one BAL and it will reflect
 * its changes across the all points where this BAL is implemented. Layering is
 * top to down: REST Controller -- Business Layers -- Single context service --
 * Repository layer access.
 *
 * @author adriaan
 */
@Service
public class RepositoryContext {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryContext.class);

    private final UserRepository userRep;
    private final AssetRepository assetRep;
    private final EmployeeRepository employeeRep;
    private final AssetLinkRepository assetLinkRep;
    private final EmployeeLinkRepository employeeLinkRep;
    private final AccessRepository accessRep;
    private final MenuRepository menuRep;
    private final TemplateRepository templateRep;
    private final FieldDetailRepository fieldDetailRep;
    private final GraphRepository graphRep;
    private final ApplicationProperties properties;

    @Autowired
    public RepositoryContext(
            UserRepository userRep,
            AssetRepository assetRep,
            EmployeeRepository employeeRep,
            AssetLinkRepository assetLinkRep,
            EmployeeLinkRepository employeeLinkRep,
            AccessRepository accessRep,
            MenuRepository menuRep,
            TemplateRepository templateRep,
            FieldDetailRepository fieldDetailRep,
            GraphRepository graphRep,
            ApplicationProperties properties) {
        this.userRep = userRep;
        this.assetRep = assetRep;
        this.employeeRep = employeeRep;
        this.assetLinkRep = assetLinkRep;
        this.employeeLinkRep = employeeLinkRep;
        this.accessRep = accessRep;
        this.menuRep = menuRep;
        this.templateRep = templateRep;
        this.fieldDetailRep = fieldDetailRep;
        this.graphRep = graphRep;
        this.properties = properties;
    }

    public enum RepositoryCall {
        USER,
        ASSET,
        EMPLOYEE,
        ASSET_LINK,
        EMPLOYEE_LINK,
        ACCESS,
        MENU,
        TEMPLATE,
        FIELD,
        GRAPH;
    }

    /**
     * Gets the repository for specific class. Can only return class if the
     * class is auto wired.
     *
     * @param <T>
     * @param clazz
     * @return Object
     */
    public <T> T getRepository(Class<T> clazz) {
        try {
            if (clazz != null) {
                T rep = null;
                if (UserRepository.class.equals(clazz)) {
                    rep = (T) userRep;
                } else if (AssetRepository.class.equals(clazz)) {
                    rep = (T) assetRep;
                } else if (EmployeeRepository.class.equals(clazz)) {
                    rep = (T) employeeRep;
                } else if (AssetLinkRepository.class.equals(clazz)) {
                    rep = (T) assetLinkRep;
                } else if (EmployeeLinkRepository.class.equals(clazz)) {
                    rep = (T) employeeLinkRep;
                } else if (AccessRepository.class.equals(clazz)) {
                    rep = (T) accessRep;
                } else if (MenuRepository.class.equals(clazz)) {
                    rep = (T) menuRep;
                } else if (TemplateRepository.class.equals(clazz)) {
                    rep = (T) templateRep;
                } else if (FieldDetailRepository.class.equals(clazz)) {
                    rep = (T) fieldDetailRep;
                } else if (GraphRepository.class.equals(clazz)) {
                    rep = (T) graphRep;
                }

                if (rep != null) {
                    return rep;
                } else {
                    throw new RuntimeException("Error trying to get repository for call " + clazz);
                }
            } else {
                throw new RuntimeException("No call provide factory unable to get repository");
            }
        } catch (RuntimeException e) {
            LOG.error("Error on finding base repository", e);
            throw e;
        }
    }

    /**
     * @return the properties
     */
    public ApplicationProperties getProperties() {
        return properties;
    }
}
