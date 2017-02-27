package nl.it.fixx.moknj.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
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
    @Autowired
    private UserRepository userRep;
    @Autowired
    private AssetRepository assetRep;
    @Autowired
    private EmployeeRepository employeeRep;
    @Autowired
    private AssetLinkRepository assetLinkRep;
    @Autowired
    private EmployeeLinkRepository employeeLinkRep;
    @Autowired
    private AccessRepository accessRep;
    @Autowired
    private MenuRepository menuRep;
    @Autowired
    private TemplateRepository templateRep;
    @Autowired
    private FieldDetailRepository fieldDetailRep;
    @Autowired
    private GraphRepository graphRep;

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
     * @throws Exception
     */
    public <T extends Object> T getRepository(Class<T> clazz) throws Exception {
        return getRepositoryFromAutoWiredClazz(clazz);
    }

    /**
     * Searches context class for matching repository. Can only return class if
     * the class is auto wired to RepositoryContext.
     *
     * @param <T>
     * @param clazz
     * @return Object
     * @throws Exception
     */
    private <T extends Object> T getRepositoryFromAutoWiredClazz(Class<T> clazz) throws Exception {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mods = field.getModifiers();
                if (!Modifier.isFinal(mods)
                        && !Modifier.isStatic(mods)) {
                    field.setAccessible(true);
                    if (field.getType().equals(clazz)) {
                        return (T) field.get(this);
                    }
                }
            }
            throw new Exception("Could not find repository, "
                    + "repository not autowired to "
                    + RepositoryContext.class.getName() + "!");
        } catch (Exception e) {
            LOG.error("Error on finding repository", e);
            throw e;
        }
    }

    /**
     * Gets the Repository for a RepositoryCall, keep in mind this returns basic
     * Mongo Repository and its full contracted repository.
     *
     * @param call
     * @return
     * @throws Exception
     */
    public MongoRepository getBaseRepository(RepositoryCall call) throws Exception {
        try {
            if (call != null) {
                MongoRepository rep = null;
                switch (call) {
                    case USER:
                        rep = userRep;
                        break;
                    case ASSET:
                        rep = assetRep;
                        break;
                    case EMPLOYEE:
                        rep = employeeRep;
                        break;
                    case ASSET_LINK:
                        rep = assetLinkRep;
                        break;
                    case EMPLOYEE_LINK:
                        rep = employeeLinkRep;
                        break;
                    case ACCESS:
                        rep = accessRep;
                        break;
                    case MENU:
                        rep = menuRep;
                        break;
                    case TEMPLATE:
                        rep = templateRep;
                        break;
                    case FIELD:
                        rep = fieldDetailRep;
                        break;
                    case GRAPH:
                        rep = graphRep;
                        break;
                    default:
                        break;
                }
                if (rep != null) {
                    return rep;
                } else {
                    throw new Exception("Error trying to get repository for call " + call.name());
                }
            } else {
                throw new Exception("No call provide factory unable to get repository");
            }
        } catch (Exception e) {
            LOG.error("Error on finding base repository", e);
            throw e;
        }
    }
}
