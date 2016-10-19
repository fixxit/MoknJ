package nl.fixx.asset.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import nl.fixx.asset.data.config.MongoConfiguration;
import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.repository.ResourceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoConfiguration.class })
public class TestResourceRepository {

    @Autowired
    ResourceRepository repository;

    @Before
    public void init() {
	saveTest();
    }

    @Test
    public void saveTest() {
	Resource resource = new Resource();
	resource.setContactNumber("0644010101");
	resource.setEmail("junit_test@fixx.it");
	resource.setFirstName("Courage");
	resource.setSurname("Cowardly Dog");

	Resource resourceRet = this.repository.save(resource);

	assertNotNull(resourceRet.getId());
    }
    
    @Test
    public void updateTest(){
	Resource resource = this.repository.findByEmail("junit_test@fixx.it");
	String id = resource.getId();
	assertNotNull(resource);

	//update
	resource.setSurname("Uber Dog");
	resource = this.repository.save(resource);
	assertSame(id, resource.getId());
	assertSame("Uber Dog", resource.getSurname());
	
	//restore
	resource.setSurname("Cowardly Dog");
	resource = this.repository.save(resource);
	assertSame(id, resource.getId());
	assertSame("Cowardly Dog", resource.getSurname());
    }

    @Test
    public void findByIdEmailTest() {
	// first find without id
	Resource resource = this.repository.findByEmail("junit_test@fixx.it");
	assertNotNull(resource);

	resource = this.repository.findById(resource.getId());
	assertEquals("0644010101", resource.getContactNumber());
	assertEquals("junit_test@fixx.it", resource.getEmail());
	assertEquals("Courage", resource.getFirstName());
	assertEquals("Cowardly Dog", resource.getSurname());
    }

    @Test
    public void deleteTest() {
	Resource resource = this.repository.findByEmail("junit_test@fixx.it");
	assertNotNull(resource);

	this.repository.findById(resource.getId());
	this.repository.delete(resource);

	assertNull(this.repository.findById(resource.getId()));
    }

    @After
    public void cleanup() {
	while (true) {
	    Resource resource = this.repository.findByEmail("junit_test@fixx.it");
	    if (resource != null) {
		//System.out.println("******************************************deleting: " + resource.toString());
		this.repository.delete(resource);
	    } else {
		break;
	    }
	}
    }

    /*
     * { "_id" : ObjectId("58073ae0194e922778916055"), "_class" :
     * "nl.fixx.asset.data.domain.Resource", "firstName" : "Courage", "surname"
     * : "Cowardly Dog", "email" : "junit_test@fixx.it", "contactNumber" :
     * "0644010101", "assetList" : [] }
     */
}
