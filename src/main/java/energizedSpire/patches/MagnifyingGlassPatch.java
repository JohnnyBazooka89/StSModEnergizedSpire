package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import energizedSpire.relics.MagnifyingGlass;

@SpirePatch(clz = AbstractCreature.class, method = "loadAnimation")
public class MagnifyingGlassPatch {

    public static void Prefix(AbstractCreature abstractCreature, String atlasUrl, String skeletonUrl, @ByRef float[] scale) {

        if (CardCrawlGame.dungeon != null && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MagnifyingGlass.ID) && !abstractCreature.isPlayer) {
            scale[0] -= 0.2F;
        }
    }

}
