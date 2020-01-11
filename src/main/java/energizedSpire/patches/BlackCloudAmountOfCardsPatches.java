package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import energizedSpire.stances.DepressionStance;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class BlackCloudAmountOfCardsPatches {

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class GameActionManagerPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr newExpr) throws CannotCompileException {
                    if (newExpr.getClassName().equals(DrawCardAction.class.getName())) {
                        newExpr.replace(
                                "$_ = $proceed($1, $2 - (" + BlackCloudAmountOfCardsPatches.class.getName() + ".getAmountOfCardsToDrawFewer()), $3);"
                        );
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class AbstractRoomPatch {
        public static ExprEditor Instrument() {
            final int[] counter = {0};
            return new ExprEditor() {
                @Override
                public void edit(NewExpr newExpr) throws CannotCompileException {
                    if (newExpr.getClassName().equals(DrawCardAction.class.getName())) {
                        if (counter[0] == 0) {
                            newExpr.replace(
                                    "$_ = $proceed($1, $2 - (" + BlackCloudAmountOfCardsPatches.class.getName() + ".getAmountOfCardsToDrawFewer()));"
                            );
                        }
                        counter[0]++;
                    }
                }
            };
        }
    }

    public static int getAmountOfCardsToDrawFewer() {
        return AbstractDungeon.player.stance.ID.equals(DepressionStance.STANCE_ID) ? 1 : 0;
    }

}