package energized_spire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import energized_spire.enums.EnergizedDieEffect;
import energized_spire.relics.EnergizedDie;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SpirePatch(clz = ObtainPotionAction.class, method = "update")
public class EnergizedDieSozuObtainPotionActionPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn Insert(ObtainPotionAction obtainPotionAction) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        EnergizedDie energizedDieRelic = (EnergizedDie) AbstractDungeon.player.getRelic(EnergizedDie.ID);
        if (energizedDieRelic != null && energizedDieRelic.isCurrentEffectEqualTo(EnergizedDieEffect.SOZU)) {
            energizedDieRelic.flash();
            Method m = AbstractGameAction.class.getDeclaredMethod("tickDuration");
            m.setAccessible(true);
            m.invoke(obtainPotionAction);
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
