package permission;

import java.util.List;

public abstract class RoleComponent {

    public void add(RoleComponent role) {
        throw new UnsupportedOperationException();
    }

    public void remove(RoleComponent role) {
        throw new UnsupportedOperationException();
    }

    public List<RoleComponent> getChildren() {
        throw new UnsupportedOperationException();
    }

    public abstract String getName();

    public abstract boolean hasPermission(String permission);
}
