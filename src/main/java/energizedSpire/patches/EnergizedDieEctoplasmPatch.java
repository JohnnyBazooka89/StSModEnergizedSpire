package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import energizedSpire.enums.EnergizedDieEffect;
import energizedSpire.relics.EnergizedDie;
import energizedSpire.relics.OgreHead;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(clz = AbstractPlayer.class, method = "gainGold")
public class EnergizedDieEctoplasmPatch {

    public static SpireReturn Prefix(AbstractPlayer player, int amount) {
        EnergizedDie energizedDieRelic = (EnergizedDie) player.getRelic(EnergizedDie.ID);
        if (energizedDieRelic != null && energizedDieRelic.isCurrentEffectEqualTo(EnergizedDieEffect.ECTOPLASM)) {
            energizedDieRelic.flash();
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

}
