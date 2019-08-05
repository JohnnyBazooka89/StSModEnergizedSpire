package energized_spire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import energized_spire.relics.StickyHand;
import javassist.CtBehavior;

@SpirePatch(clz = CardRewardScreen.class, method = "acquireCard")
public class StickyHandCardRewardScreenAcquireCardsPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn Insert(CardRewardScreen cardRewardScreen, AbstractCard hoveredCard) {
        if (AbstractDungeon.player.hasRelic(StickyHand.ID)) {
            for (AbstractCard card : cardRewardScreen.rewardGroup) {
                AbstractDungeon.effectsQueue.add(new FastCardObtainEffect(card, card.current_x, card.current_y));
            }
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            Matcher matcher = new Matcher.NewExprMatcher(FastCardObtainEffect.class);
            return LineFinder.findInOrder(method, matcher);
        }
    }

}
