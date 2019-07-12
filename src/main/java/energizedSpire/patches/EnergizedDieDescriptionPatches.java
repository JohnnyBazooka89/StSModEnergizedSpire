package energizedSpire.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import energizedSpire.relics.EnergizedDie;
import javassist.CtBehavior;

import java.lang.reflect.Method;

public class EnergizedDieDescriptionPatches {

    @SpirePatch(clz = FontHelper.class, method = "initialize", paramtypez = {})
    public static class FontHelperPatch {

        private static BitmapFont smallerCardDescriptionFont;

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert() throws Exception {
            Method prepFontMethod = FontHelper.class.getDeclaredMethod("prepFont", float.class, boolean.class);
            prepFontMethod.setAccessible(true);
            smallerCardDescriptionFont = (BitmapFont) prepFontMethod.invoke(null, 20.0f, false);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(FontHelper.class, "cardDescFont_N");
                return LineFinder.findInOrder(method, matcher);
            }
        }
    }

    private static final float DESC_LINE_SPACING = 30.0F * Settings.scale;
    private static final float DESC_LINE_WIDTH = 418.0F * Settings.scale;

    @SpirePatch(clz = SingleRelicViewPopup.class, method = "renderDescription", paramtypez = {SpriteBatch.class})
    public static class RenderDescriptionFixPatch {

        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> Insert(SingleRelicViewPopup singleRelicViewPopup, SpriteBatch sb) {
            AbstractRelic relic = (AbstractRelic) ReflectionHacks.getPrivate(singleRelicViewPopup, SingleRelicViewPopup.class, "relic");
            if (EnergizedDie.ID.equals(relic.relicId)) {
                FontHelper.renderSmartText(sb, FontHelperPatch.smallerCardDescriptionFont, relic.description, Settings.WIDTH / 2.0F - 200.0F * Settings.scale, Settings.HEIGHT / 2.0F - 140.0F * Settings.scale -
                        FontHelper.getSmartHeight(FontHelperPatch.smallerCardDescriptionFont, relic.description, DESC_LINE_WIDTH, DESC_LINE_SPACING) / 2.0F, DESC_LINE_WIDTH, DESC_LINE_SPACING, Settings.CREAM_COLOR);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderSmartText");
                return LineFinder.findInOrder(method, matcher);
            }
        }
    }

}