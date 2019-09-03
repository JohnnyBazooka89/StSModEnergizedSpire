package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import energizedSpire.relics.TabascoSauce;
import javassist.CtBehavior;

public class TabascoSaucePatches {

    private static boolean isHealingCalledByIncreasingMaxHP;

    @SpirePatch(clz = AbstractCreature.class, method = "heal", paramtypez = {int.class, boolean.class})
    public static class HealPatch {
        public static void Prefix(AbstractCreature creature, @ByRef int[] healAmount, boolean showEffect) {
            if (AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && creature instanceof AbstractPlayer && !isHealingCalledByIncreasingMaxHP) {
                AbstractPlayer player = (AbstractPlayer) creature;
                if (player.hasRelic(TabascoSauce.ID)) {
                    healAmount[0] /= 2;
                }
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "increaseMaxHp")
    public static class IncreaseMaxHPPartOnePatch {

        @SpireInsertPatch(locator = PartOneLocator.class, localvars = "amount")
        public static void Insert(AbstractCreature creature, int amount_, boolean showEffect, @ByRef int[] amount) {
            if (creature instanceof AbstractPlayer) {
                AbstractPlayer player = (AbstractPlayer) creature;
                if (player.hasRelic(TabascoSauce.ID)) {
                    amount[0] /= 2;
                }
            }
        }

        private static class PartOneLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "maxHealth");
                return LineFinder.findInOrder(method, matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "increaseMaxHp")
    public static class IncreaseMaxHPPartTwoPatch {

        @SpireInsertPatch(locator = PartTwoLocator.class)
        public static void Insert(AbstractCreature creature, int amount, boolean showEffect) {
            isHealingCalledByIncreasingMaxHP = true;
        }

        private static class PartTwoLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "heal");
                return LineFinder.findInOrder(method, matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "increaseMaxHp")
    public static class IncreaseMaxHPPartThreePatch {

        @SpireInsertPatch(locator = PartThreeLocator.class)
        public static void Insert(AbstractCreature creature, int amount, boolean showEffect) {
            isHealingCalledByIncreasingMaxHP = false;
        }

        private static class PartThreeLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "healthBarUpdatedEvent");
                return LineFinder.findInOrder(method, matcher);
            }
        }
    }
}