package worldguardbridge.v6;

import bwillows.worldguardbridge.WorldGuardBridge;
import bwillows.worldguardbridge.model.WrappedRegion;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class WorldGuardBridge_v6 implements WorldGuardBridge {

    private final WorldGuardPlugin wg;

    public WorldGuardBridge_v6() {
        this.wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
    }

    private RegionManager getRegionManager(World world) {
        RegionContainer container = wg.getRegionContainer();
        return container.get(world);
    }

    @Override
    public WrappedRegion getRegion(String regionName) {
        for (World world : Bukkit.getWorlds()) {
            RegionManager manager = getRegionManager(world);
            if (manager == null) continue;

            ProtectedRegion region = manager.getRegion(regionName);
            if (region != null) {
                return wrapRegion(region, world);
            }
        }
        return null;
    }

    @Override
    public Set<String> getApplicableRegions(Location location) {
        RegionManager manager = getRegionManager(location.getWorld());
        if (manager == null) return Collections.emptySet();

        ApplicableRegionSet set = manager.getApplicableRegions(location);
        Set<String> regionIds = new HashSet<>();
        for (ProtectedRegion region : set) {
            regionIds.add(region.getId());
        }
        return regionIds;
    }

    private WrappedRegion wrapRegion(ProtectedRegion region, World world) {
        BlockVector min = region.getMinimumPoint();
        BlockVector max = region.getMaximumPoint();
        Vector minVec = new Vector(min.getBlockX(), min.getBlockY(), min.getBlockZ());
        Vector maxVec = new Vector(max.getBlockX(), max.getBlockY(), max.getBlockZ());

        Set<String> owners = extractNames(region.getOwners());
        Set<String> members = extractNames(region.getMembers());

        Map<String, String> flags = new HashMap<>();
        for (Map.Entry<Flag<?>, Object> entry : region.getFlags().entrySet()) {
            Flag<?> flag = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                flags.put(flag.getName(), value.toString());
            }
        }

        return new WrappedRegion(
                region.getId(),
                world,
                minVec,
                maxVec,
                region.getPriority(),
                owners,
                members,
                flags
        );
    }

    private Set<String> extractNames(DefaultDomain domain) {
        Set<String> names = new HashSet<>();
        names.addAll(domain.getPlayers());
        return names;
    }
}