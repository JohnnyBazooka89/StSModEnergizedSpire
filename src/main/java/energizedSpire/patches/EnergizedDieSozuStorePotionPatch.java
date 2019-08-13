package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.StorePotion;
import energizedSpire.enums.EnergizedDieEffect;
import energizedSpire.relics.EnergizedDie;
import javassist.CtBehavior;

@SpirePatch(clz = StorePotion.class, method = "purchasePotion")
public class EnergizedDieSozuStorePotionPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn Insert(StorePotion storePotion) {
        EnergizedDie energizedDieRelic = (EnergizedDie) AbstractDungeon.player.getRelic(EnergizedDie.ID);
        if (energizedDieRelic != null && energizedDieRelic.isCurrentEffectEqualTo(EnergizedDieEffect.SOZU)) {
            energizedDieRelic.flash();
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return LineFinder.findInOrder(method, matcher);
        }
    }

}
