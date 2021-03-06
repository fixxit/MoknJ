package nl.it.fixx.moknj.domain.core.field;

import java.util.Objects;
import nl.it.fixx.moknj.domain.core.global.GlobalFieldType;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class FieldDetail {

    @Id
    private String id;
    private GlobalFieldType type;
    private String name;
    private boolean unique;
    private boolean mandatory;
    private boolean display;

    @Override
    public String toString() {
        return "FieldDetail{" + "id=" + getId() + ", type=" + getType() + ", name=" + getName() + ", unique=" + isUnique() + ", mandatory=" + isMandatory() + ", display=" + isDisplay() + '}';
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the type
     */
    public GlobalFieldType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(GlobalFieldType type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the unique
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * @param unique the unique to set
     */
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    /**
     * @return the mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * @param mandatory the mandatory to set
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * @return the display
     */
    public boolean isDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(boolean display) {
        this.display = display;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.type);
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + (this.unique ? 1 : 0);
        hash = 89 * hash + (this.mandatory ? 1 : 0);
        hash = 89 * hash + (this.display ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FieldDetail other = (FieldDetail) obj;
        if (this.unique != other.unique) {
            return false;
        }
        if (this.mandatory != other.mandatory) {
            return false;
        }
        if (this.display != other.display) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

}
