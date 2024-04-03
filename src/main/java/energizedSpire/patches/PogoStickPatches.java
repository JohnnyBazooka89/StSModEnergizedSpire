package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import energizedSpire.relics.PogoStick;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class PogoStickPatches {

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class GameActionManagerPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr newExpr) throws CannotCompileException {
                    if (newExpr.getClassName().equals(DrawCardAction.class.getName())) {
                        newExpr.replace(
                                "$_ = $proceed($1, $2 - (" + PogoStickPatches.class.getName() + ".getNumberOfCardsToDrawFewer()), $3);"
                        );
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class AbstractRoomPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                int counter = 0;
                @Override
                public void edit(NewExpr newExpr) throws CannotCompileException {
                    if (newExpr.getClassName().equals(DrawCardAction.class.getName())) {
                        if (counter == 0) {
                            newExpr.replace(
                                    "$_ = $proceed($1, $2 - (" + PogoStickPatches.class.getName() + ".getNumberOfCardsToDrawFewer()));"
                            );
                        }
                        counter++;
                    }
                }
            };
        }
    }

    public static int getNumberOfCardsToDrawFewer() {
        int counter = 0;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof PogoStick) {
                PogoStick pogoStick = (PogoStick) relic;
                if (pogoStick.active) {
                    counter++;
                }
            }
        }
        return counter;
    }

}