package hu.Pdani.FMCSkript.disguise;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Before calling this, make sure that you constructed a new Disguise using "disguise type %string%"
 */
public class DisguiseExpression extends SimpleExpression<String> {
    static {
        Skript.registerExpression(DisguiseExpression.class, String.class, ExpressionType.COMBINED,"disguise of %entity%");
    }

    private Expression<Entity> entity;
    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
    @Override
    public boolean isSingle() {
        return true;
    }
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int arg1, Kleenean arg2, SkriptParser.ParseResult arg3) {
        entity = (Expression<Entity>) e[0];
        return true;
    }
    @Override
    public String toString(Event e, boolean arg1) {
        return "disguise of %entity%";
    }
    @Override
    protected String[] get(Event e) {
        Entity target = entity.getSingle(e);
        Disguise disguise = DisguiseAPI.getDisguise(target);
        if(disguise == null){
            if(!(target instanceof Player))
                return new String[]{target.getName()};
            else
                return new String[]{((Player) target).getDisplayName()};
        } else {
            return new String[]{disguise.getDisguiseName()};
        }
    }
    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode){
        Entity target = entity.getSingle(e);
        if (mode == Changer.ChangeMode.SET) {
            if(!(delta[0] instanceof Disguise)) {
                return;
            }
            Disguise disguise = (Disguise) delta[0];
            DisguiseAPI.disguiseToAll(target,disguise);
        }
        if (mode == Changer.ChangeMode.RESET) {
            DisguiseAPI.undisguiseToAll(target);
        }
    }
    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Disguise.class);
        }
        return null;
    }
}
