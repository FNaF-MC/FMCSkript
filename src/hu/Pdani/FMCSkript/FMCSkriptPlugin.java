package hu.Pdani.FMCSkript;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.nicknamer.NickNamerPlugin;
import org.inventivetalent.nicknamer.api.NickManager;

import java.util.logging.Level;

public class FMCSkriptPlugin extends JavaPlugin {
    private static FMCSkriptPlugin instance;
    private static SkriptAddon addon;
    private static NickManager nmapi = null;

    @Override
    public void onEnable() {
        instance = this;
        if(!hasSkript()){
            getLogger().log(Level.SEVERE,"Skript plugin not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        nmapi = getNickNamer();
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("hu.Pdani.FMCSkript", "elements");
        } catch (Exception e) {
            e.printStackTrace();
        }
        getLogger().log(Level.INFO,"The addon has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO,"The addon has been disabled!");
    }

    private boolean hasSkript() {
        Plugin pl = getServer().getPluginManager().getPlugin("Skript");
        if (pl == null) {
            return false;
        }
        return (pl instanceof Skript);
    }

    private NickManager getNickNamer(){
        Plugin pl = getServer().getPluginManager().getPlugin("NickNamer");
        if(pl == null)
            return null;
        return ((NickNamerPlugin) pl).getAPI();
    }

    public static NickManager getNmapi() {
        return nmapi;
    }

    public static FMCSkriptPlugin getInstance() {
        return instance;
    }

    public static SkriptAddon getAddonInstance() {
        return addon;
    }

    public static boolean isDoor(Block b){
        return b.getType().toString().endsWith("DOOR")
                || b.getType().toString().equalsIgnoreCase("IRON_DOOR_BLOCK");
    }
}
