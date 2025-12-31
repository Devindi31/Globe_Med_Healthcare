package permission;

import java.util.ArrayList;
import java.util.List;

public class RoleGroup extends RoleComponent {
    private final String name;
    private final List<RoleComponent> children = new ArrayList<>();

    public RoleGroup(String name) {
        this.name = name;
    }

    @Override
    public void add(RoleComponent role) {
        children.add(role);
    }

    @Override
    public void remove(RoleComponent role) {
        children.remove(role);
    }

    @Override
    public List<RoleComponent> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasPermission(String permission) {
        for (RoleComponent child : children) {
            if (child.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
}
