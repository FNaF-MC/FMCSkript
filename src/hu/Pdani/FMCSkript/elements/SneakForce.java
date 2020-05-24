package hu.Pdani.FMCSkript.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class SneakForce extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(SneakForce.class, Boolean.class, ExpressionType.COMBINED,"sneak[ing] [state] of %player%");
    }

    private Expression<Player> player;
    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public boolean init(Expression<?>[] e, int arg1, Kleenean arg2, SkriptParser.ParseResult arg3) {
        player = (Expression<Player>) e[0];
        return true;
    }
    @Override
    public String toString(Event e, boolean arg1) {
        return "sneak[ing] [state] of %player%";
    }
    @Override
    protected Boolean[] get(Event e) {
        return new Boolean[]{player.getSingle(e).isSneaking()};
    }
    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode){
        if (mode == Changer.ChangeMode.SET) {
            player.getSingle(e).setSneaking((Boolean)delta[0]);
        }
        if (mode == Changer.ChangeMode.RESET) {
            player.getSingle(e).setSneaking(false);
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
