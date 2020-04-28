package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EntanglePower;
import energizedSpire.relics.SpiderWeb;

@SpirePatch(clz = EntanglePower.class, method = "atEndOfTurn")
public class SpiderWebEntanglePowerPatch {

    public static SpireReturn<Void> Prefix(EntanglePower entanglePower, boolean isPlayer) {
        if (AbstractDungeon.player.hasRelic(SpiderWeb.ID) && isPlayer && entanglePower.amount > 1) {
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, EntanglePower.POWER_ID, 1));
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

}
