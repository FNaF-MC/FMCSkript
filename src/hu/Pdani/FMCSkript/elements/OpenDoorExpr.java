package hu.Pdani.FMCSkript.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import hu.Pdani.FMCSkript.FMCSkriptPlugin;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.material.Door;

public class OpenDoorExpr extends Condition {

    static {
        //Skript.registerExpression(DoorExpr.class, Integer.class, ExpressionType.COMBINED, "[is] open %block%");
        Skript.registerCondition(OpenDoorExpr.class, "%block% is opendoor");
    }

    private Expression<Block> block;

    @Override
    public String toString(Event event, boolean debug) {
        return block.toString(event, debug) + " is opendoor";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        block = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event event) {
        Block b = block.getSingle(event);
        if(FMCSkriptPlugin.isDoor(b)){
            return ((Door)b.getState().getData()).isOpen();
        }
        return false;
    }
}
