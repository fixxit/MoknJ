package nl.fixx.asset.data.domain;

import org.springframework.data.annotation.Id;

public class Asset {
    @Id
    public String id;
    
    public String name;
    public String description;

    public Asset() {
    }

    public Asset(String name, String description) {
	this.name = name;
	this.description = description;
    }

    @Override
    public String toString() {
	return "Asset [id=" + id + ", name=" + name + ", description=" + description + "]";
    }

}
