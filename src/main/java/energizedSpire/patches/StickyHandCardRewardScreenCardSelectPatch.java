package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import energizedSpire.relics.StickyHand;
import javassist.CtBehavior;

@SpirePatch(clz = CardRewardScreen.class, method = "cardSelectUpdate")
public class StickyHandCardRewardScreenCardSelectPatch {

    private static boolean anyHovered;

    @SpireInsertPatch(locator = Locator.class, localvars = "hoveredCard")
    public static void Insert(CardRewardScreen cardRewardScreen, AbstractCard hoveredCard) {
        if (AbstractDungeon.player.hasRelic(StickyHand.ID)) {
            for (AbstractCard card : cardRewardScreen.rewardGroup) {
                if (hoveredCard != null) {
                    card.drawScale = 1.0f;
                    card.targetDrawScale = 1.0f;
                } else {
                    card.drawScale = MathHelper.cardScaleLerpSnap(card.drawScale, card.targetDrawScale);
                    card.targetDrawScale = 0.75f;
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(InputHelper.class, "justClickedLeft");
            return LineFinder.findInOrder(method, matcher);
        }
    }

}
