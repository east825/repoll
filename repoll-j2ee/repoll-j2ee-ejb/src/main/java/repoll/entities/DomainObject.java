package repoll.entities;

import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Golubev
 */
public abstract class DomainObject {

    @Nullable
    public abstract Integer getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObject)) return false;
        return getId() != null && getId().equals(((DomainObject) o).getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

}
