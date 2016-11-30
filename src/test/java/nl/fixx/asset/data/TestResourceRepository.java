package nl.fixx.asset.data;

import nl.it.fixx.moknj.builders.UserBuilder;
import nl.it.fixx.moknj.config.MongoConfiguration;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.repository.UserRepository;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoConfiguration.class})
public class TestResourceRepository {

    @Autowired
    UserRepository repository;

    @Before
    public void init() {
        saveTest();
    }

    @Test
    public void saveTest() {
        User resource = new UserBuilder()
                .contactNumber("0644010101")
                .email("junit_test@fixx.it")
                .firstName("Courage")
                .surname("Cowardly Dog")
                .build();

        User resourceRet = this.repository.save(resource);

        assertNotNull(resourceRet.getId());
    }

    @Test
    public void updateTest() {
        User resource = this.repository.findByEmail("junit_test@fixx.it");
        String id = resource.getId();
        assertNotNull(resource);

        // update
        resource.setSurname("Uber Dog");
        resource = this.repository.save(resource);
        assertSame(id, resource.getId());
        assertSame("Uber Dog", resource.getSurname());

        // restore
        resource.setSurname("Cowardly Dog");
        resource = this.repository.save(resource);
        assertSame(id, resource.getId());
        assertSame("Cowardly Dog", resource.getSurname());
    }

    @Test
    public void findByIdEmailTest() {
        // first find without id
        User resource = this.repository.findByEmail("junit_test@fixx.it");
        assertNotNull(resource);

        resource = this.repository.findById(resource.getId());
        assertEquals("0644010101", resource.getContactNumber());
        assertEquals("junit_test@fixx.it", resource.getEmail());
        assertEquals("Courage", resource.getFirstName());
        assertEquals("Cowardly Dog", resource.getSurname());
    }

    @Test
    public void deleteTest() {
        User resource = this.repository.findByEmail("junit_test@fixx.it");
        assertNotNull(resource);

        this.repository.findById(resource.getId());
        this.repository.delete(resource);

        assertNull(this.repository.findById(resource.getId()));
    }

    @After
    public void cleanup() {
        while (true) {
            User resource = this.repository.findByEmail("junit_test@fixx.it");
            if (resource != null) {
                // System.out.println("******************************************deleting:
                // " + resource.toString());
                this.repository.delete(resource);
            } else {
                break;
            }
        }
    }

    /*
     * { "_id" : ObjectId("58073ae0194e922778916055"), "_class" :
     * "nl.fixx.asset.data.domain.User", "firstName" : "Courage", "surname"
     * : "Cowardly Dog", "email" : "junit_test@fixx.it", "contactNumber" :
     * "0644010101", "assetList" : [] }
     */
}
