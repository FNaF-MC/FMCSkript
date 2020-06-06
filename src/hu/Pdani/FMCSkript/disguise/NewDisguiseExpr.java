package hu.Pdani.FMCSkript.disguise;

import ch.njol.skript.Skript;
import hu.Pdani.FMCSkript.FMCSkriptPlugin;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;
import org.bukkit.Material;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/**
 * Skript is the biggest bullshit ever and you need to construct a new Disguise before setting it to the entity, because you can't send two or more objects to an expression
 */
public class NewDisguiseExpr extends SimpleExpression<Disguise> {
    static {
        Skript.registerExpression(NewDisguiseExpr.class, Disguise.class, ExpressionType.COMBINED,"[new] disguise [with] type %-string% [with block [id] %-itemstack%] [with armor %-string%] [(and|with) data [id] %number%] [with [user[ ]]name %-string%] [(and|with) adult [state] %-boolean%] [(and|with) visible [state] %-boolean%]");
    }

    private Expression<Long> data;
    private Expression<ItemStack> block;
    private Expression<String> name, typeString, armor;
    private Expression<Boolean> baby, visible;
    public Class<? extends Disguise> getReturnType() {
        return Disguise.class;
    }
    @Override
    public boolean isSingle() {
        return true;
    }
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        typeString = (Expression<String>) e[0];
        block = (Expression<ItemStack>) e[1];
        armor = (Expression<String>) e[2];
        data = (Expression<Long>) e[3];
        name = (Expression<String>) e[4];
        baby = (Expression<Boolean>) e[5];
        visible = (Expression<Boolean>) e[6];
        return true;
    }
    @Override
    public String toString(Event e, boolean arg1) {
        return "[new] disguise [with] type %-string% [with block [id] %-itemstack%] [with armor %-string%] [(and|with) data [id] %number%] [with [user[ ]]name %-string%] [(and|with) adult [state] %-boolean%] [(and|with) visible [state] %-boolean%]";
    }

    @Override
    protected Disguise[] get(Event e) {
        String type = typeString.getSingle(e);
        DisguiseType disguiseType = null;
        for (DisguiseType dtype : DisguiseType.values()) {
            if (type.equalsIgnoreCase(dtype.name())) {
                disguiseType = dtype;
                break;
            }
        }
        if (disguiseType != null) {
            if (disguiseType.isMisc()) {
                if (block != null && block.getSingle(e) != null) {
                    Long bd = 0L;
                    if(data.getSingle(e) != null){
                        bd = data.getSingle(e);
                    }
                    ItemStack stack = block.getSingle(e);
                    return new Disguise[]{new MiscDisguise(disguiseType, stack.getType(), bd.intValue()).setViewSelfDisguise(false)};
                } else {
                    return new Disguise[]{new MiscDisguise(disguiseType).setViewSelfDisguise(false)};
                }
            } else if (disguiseType.isMob()) {
                MobDisguise disguise;
                if (baby != null && baby.getSingle(e) != null) {
                    disguise = new MobDisguise(disguiseType, baby.getSingle(e));
                } else {
                    disguise = new MobDisguise(disguiseType);
                }
                LivingWatcher watcher = disguise.getWatcher();
                if (name != null && name.getSingle(e) != null) {
                    String pname = name.getSingle(e);
                    if(pname.contains("\\n")){
                        watcher.setCustomName(pname.replace("\\n","\n"));
                    } else {
                        disguise.setMultiName(pname.split("[,;]+"));
                    }
                }
                if(armor != null && armor.getSingle(e) != null) {
                    String[] armorlist = armor.getSingle(e).split("[,;]+");
                    ItemStack[] armors = new ItemStack[armorlist.length];
                    for(int i = 0; i < armorlist.length;i++){
                        try {
                            FMCSkriptPlugin.getInstance().getLogger().info(armorlist[i]);
                            armors[i] = new ItemStack(Material.valueOf(armorlist[i].toUpperCase()));
                        } catch (IllegalArgumentException ex){
                            FMCSkriptPlugin.getInstance().getLogger().info("wrong material");
                            armors[i] = new ItemStack(Material.STONE);
                        }
                    }
                    if(armors.length >= 4) {
                        watcher.setHelmet(armors[0]);
                        watcher.setChestplate(armors[1]);
                        watcher.setLeggings(armors[2]);
                        watcher.setBoots(armors[3]);
                    }
                }
                if(visible != null && visible.getSingle(e) != null) {
                    watcher.setInvisible(!visible.getSingle(e));
                    if(!visible.getSingle(e))
                        watcher.addPotionEffect(PotionEffectType.INVISIBILITY);
                }
                return new Disguise[]{disguise.setViewSelfDisguise(false)};
            } else if (disguiseType.isPlayer()) {
                if (name != null && name.getSingle(e) != null) {
                    String pname = name.getSingle(e);
                    return new Disguise[]{new PlayerDisguise(pname).setViewSelfDisguise(false)};
                } else {
                    return new Disguise[]{new PlayerDisguise("Notch").setViewSelfDisguise(false)};
                }
            } else {
                FMCSkriptPlugin.getInstance().getLogger().warning("Unknown DisguiseType: " + disguiseType);
            }
        } else {
            FMCSkriptPlugin.getInstance().getLogger().warning("Unknown DisguiseType: " + type);
        }
        return null;
    }
}