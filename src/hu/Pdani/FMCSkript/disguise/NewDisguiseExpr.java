package hu.Pdani.FMCSkript.disguise;

import ch.njol.skript.Skript;
import hu.Pdani.FMCSkript.FMCSkriptPlugin;
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

/**
 * Skript is the biggest bullshit ever and you need to construct a new Disguise before setting it to the entity, because you can't send two or more objects to an expression
 */
public class NewDisguiseExpr extends SimpleExpression<Disguise> {
    static {
        Skript.registerExpression(NewDisguiseExpr.class, Disguise.class, ExpressionType.COMBINED,"[new] disguise [with] type %-string% [with block [id] %-itemstack%] [(and|with) data [id] %number%] [with [user[ ]]name %-string%] [(and|with) adult [state] %-boolean%]");
    }

    private Expression<Long> data;
    private Expression<ItemStack> block;
    private Expression<String> name, typeString;
    private Expression<Boolean> baby;
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
        data = (Expression<Long>) e[2];
        name = (Expression<String>) e[3];
        baby = (Expression<Boolean>) e[4];
        return true;
    }
    @Override
    public String toString(Event e, boolean arg1) {
        return "[new] disguise [with] type %-string% [with block [id] %-itemstack%] [(and|with) data [id] %number%] [with [user[ ]]name %-string%] [(and|with) adult [state] %-boolean%]";
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
                    return new Disguise[]{new MiscDisguise(disguiseType, stack.getType(), bd.intValue())};
                } else {
                    return new Disguise[]{new MiscDisguise(disguiseType)};
                }
            } else if (disguiseType.isMob()) {
                if (baby != null && baby.getSingle(e) != null) {
                    return new Disguise[]{new MobDisguise(disguiseType, !baby.getSingle(e))};
                } else {
                    return new Disguise[]{new MobDisguise(disguiseType)};
                }
            } else if (disguiseType.isPlayer()) {
                if (name != null && name.getSingle(e) != null) {
                    String pname = name.getSingle(e);
                    return new Disguise[]{new PlayerDisguise(pname)};
                } else {
                    return new Disguise[]{new PlayerDisguise("Notch")};
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