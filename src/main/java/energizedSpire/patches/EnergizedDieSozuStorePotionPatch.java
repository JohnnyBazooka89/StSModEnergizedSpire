package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.shop.StorePotion;
import energizedSpire.enums.EnergizedDieEffect;
import energizedSpire.relics.EnergizedDie;
import javassist.CtBehavior;

import java.lang.reflect.Method;

@SpirePatch(clz = StorePotion.class, method = "update")
public class EnergizedDieSozuStorePotionPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn Insert(StorePotion storePotion, float rugY) {
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
