package repoll.entities;

/**
 * @author Mikhail Golubev
 */
public abstract class DomainObject {

    public abstract long getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObject)) return false;
        return getId() == ((DomainObject) o).getId();
    }

    @Override
    public int hashCode() {
        return (int)getId();
    }

}
