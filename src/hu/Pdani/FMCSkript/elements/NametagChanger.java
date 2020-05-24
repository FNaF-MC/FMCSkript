package hu.Pdani.FMCSkript.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import hu.Pdani.FMCSkript.FMCSkriptPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class NametagChanger extends SimpleExpression<String> {
    static {
        Skript.registerExpression(NametagChanger.class, String.class, ExpressionType.COMBINED,"nametag of %player%");
    }

    private Expression<Player> player;
    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
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
        return "nametag of %player%";
    }
    @Override
    protected String[] get(Event e) {
        if(FMCSkriptPlugin.getNmapi() != null && FMCSkriptPlugin.getNmapi().isNicked(player.getSingle(e).getUniqueId()))
            return new String[]{FMCSkriptPlugin.getNmapi().getNick(player.getSingle(e).getUniqueId())};
        else
            return new String[]{player.getSingle(e).getDisplayName()};
    }
    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode){
        if(FMCSkriptPlugin.getNmapi() == null)
            return;
        if (mode == Changer.ChangeMode.SET) {
            FMCSkriptPlugin.getNmapi().setNick(player.getSingle(e).getUniqueId(),(String)delta[0]);
        }
        if (mode == Changer.ChangeMode.RESET) {
            if(FMCSkriptPlugin.getNmapi().isNicked(player.getSingle(e).getUniqueId()))
                FMCSkriptPlugin.getNmapi().removeNick(player.getSingle(e).getUniqueId());
        }
    }
    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(String.class);
        }
        return null;
    }
}
