package energized_spire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import energized_spire.enums.EnergizedDieEffect;
import energized_spire.relics.EnergizedDie;
import javassist.CtBehavior;

@SpirePatch(clz = RewardItem.class, method = "claimReward")
public class EnergizedDieSozuRewardItemPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn Insert(RewardItem rewardItem) {
        EnergizedDie energizedDieRelic = (EnergizedDie) AbstractDungeon.player.getRelic(EnergizedDie.ID);
        if (energizedDieRelic != null && energizedDieRelic.isCurrentEffectEqualTo(EnergizedDieEffect.SOZU)) {
            energizedDieRelic.flash();
            return SpireReturn.Return(Boolean.TRUE);
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
