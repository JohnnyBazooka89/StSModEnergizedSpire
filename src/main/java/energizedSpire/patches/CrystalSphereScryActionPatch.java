package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import energizedSpire.relics.CrystalSphere;
import javassist.CtBehavior;

@SpirePatch(clz = ScryAction.class, method = "update")
public class CrystalSphereScryActionPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(ScryAction scryAction) {
        CrystalSphere crystalSphereRelic = (CrystalSphere) AbstractDungeon.player.getRelic(CrystalSphere.ID);
        if (crystalSphereRelic != null && !crystalSphereRelic.triggeredThisTurn) {
            crystalSphereRelic.triggeredThisTurn = true;
            crystalSphereRelic.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, crystalSphereRelic));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            return LineFinder.findInOrder(method, matcher);
        }
    }

}
