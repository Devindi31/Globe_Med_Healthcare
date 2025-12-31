package permission;

import java.util.HashSet;
import java.util.Set;

public class RoleLeaf extends RoleComponent {
    private final String name;
    private final Set<String> permissions = new HashSet<>();

    public RoleLeaf(String name) {
        this.name = name;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
