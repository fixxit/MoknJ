package nl.fixx.asset.data.builders;


import nl.fixx.asset.data.domain.Resource;

public class ResourceBuilder {
    private Resource resource = new Resource();

    public ResourceBuilder firstName(String firstName) {
	this.resource.setFirstName(firstName);
	return this;
    }

    public ResourceBuilder surname(String surname) {
	this.resource.setSurname(surname);
	return this;
    }

    public ResourceBuilder email(String email) {
	this.resource.setEmail(email);
	return this;
    }

    public ResourceBuilder contactNumber(String contactNumber) {
	this.resource.setContactNumber(contactNumber);
	return this;
    }

    public ResourceBuilder placedAtClient(String placedAtClient) {
	this.resource.setPlacedAtClient(placedAtClient);
	return this;
    }


    public Resource build() {
	return this.resource;
    }
}
