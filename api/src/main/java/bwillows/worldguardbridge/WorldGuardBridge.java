package bwillows.worldguardbridge;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import bwillows.worldguardbridge.model.WrappedRegion;

import java.util.Set;

public interface WorldGuardBridge {
    WrappedRegion getRegion(String regionName);
    Set<String> getApplicableRegions(Location location);
}