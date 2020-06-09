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
import me.libraryaddict.disguise.disguisetypes.Disguise;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

public class ViewSelfDisgExpr extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ViewSelfDisgExpr.class, Boolean.class, ExpressionType.COMBINED,"self disguise [visible] of %entity%");
    }
    private Expression<Entity> entity;
    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
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
        return "self disguise [visible] of %entity%";
    }
    @Override
    protected Boolean[] get(Event e) {
        Entity target = entity.getSingle(e);
        Disguise disguise = DisguiseAPI.getDisguise(target);
        if(disguise == null){
            return new Boolean[]{false};
        } else {
            return new Boolean[]{disguise.isSelfDisguiseVisible()};
        }
    }
    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode){
        Entity target = entity.getSingle(e);
        if (mode == Changer.ChangeMode.SET) {
            if(!(delta[0] instanceof Boolean)) {
                return;
            }
            boolean value = (boolean) delta[0];
            Disguise disguise = DisguiseAPI.getDisguise(target);
            if(disguise != null)
                disguise.setSelfDisguiseVisible(value);
        }
        if (mode == Changer.ChangeMode.RESET) {
            Disguise disguise = DisguiseAPI.getDisguise(target);
            if(disguise != null)
                disguise.setSelfDisguiseVisible(false);
        }
    }
    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Boolean.class);
        }
        return null;
    }
}
