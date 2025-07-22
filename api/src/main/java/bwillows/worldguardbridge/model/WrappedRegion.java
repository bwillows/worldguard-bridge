package bwillows.worldguardbridge.model;

import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Set;

public class WrappedRegion {
    private final String id;
    private final World world;
    private final Vector min;
    private final Vector max;
    private final int priority;
    private final Set<String> owners;
    private final Set<String> members;
    private final Map<String, String> flags;

    public WrappedRegion(String id, World world, Vector min, Vector max, int priority,
                         Set<String> owners, Set<String> members, Map<String, String> flags) {
        this.id = id;
        this.world = world;
        this.min = min;
        this.max = max;
        this.priority = priority;
        this.owners = owners;
        this.members = members;
        this.flags = flags;
    }

    public String getId() { return id; }
    public World getWorld() { return world; }
    public Vector getMin() { return min; }
    public Vector getMax() { return max; }
    public int getPriority() { return priority; }
    public Set<String> getOwners() { return owners; }
    public Set<String> getMembers() { return members; }
}