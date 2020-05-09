package energizedSpire.patches;

import com.esotericsoftware.spine.SkeletonJson;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import energizedSpire.relics.MagnifyingGlass;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractCreature.class, method = "loadAnimation")
public class MagnifyingGlassPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static void Prefix(AbstractCreature abstractCreature, String atlasUrl, String skeletonUrl, @ByRef float[] scale) {

        if (CardCrawlGame.dungeon != null && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MagnifyingGlass.ID) && !abstractCreature.isPlayer) {
            scale[0] *= 0.8F;
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(SkeletonJson.class, "setScale");
            return LineFinder.findInOrder(method, matcher);
        }
    }

}
