package org.escidoc.watcher.domain.internal;

import com.google.common.base.Preconditions;

import de.escidoc.core.resources.om.item.component.Component;

public class ComponentWraper {

    private final Component eComponent;

    public ComponentWraper(Component eComponent) {
        Preconditions.checkNotNull(eComponent, "eComponent is null.");
        this.eComponent = eComponent;
    }

    public String getId() {
        return eComponent.getObjid();
    }

    public String getName() {
        return eComponent.getXLinkTitle();
    }

    @Override
    public String toString() {
        return "ComponentWraper [getId()=" + getId() + ", getName()="
            + getName() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
            prime * result + ((eComponent == null) ? 0 : eComponent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ComponentWraper other = (ComponentWraper) obj;
        if (eComponent == null) {
            if (other.eComponent != null)
                return false;
        }
        else if (!eComponent.equals(other.eComponent))
            return false;
        return true;
    }

}
