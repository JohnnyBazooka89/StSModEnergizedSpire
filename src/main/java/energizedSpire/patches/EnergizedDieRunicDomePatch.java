package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.RunicDome;
import energizedSpire.enums.EnergizedDieEffect;
import energizedSpire.relics.EnergizedDie;

@SpirePatch(clz = AbstractPlayer.class, method = "hasRelic")
public class EnergizedDieRunicDomePatch {

    public static SpireReturn Prefix(AbstractPlayer player, String targetID) {
        EnergizedDie energizedDieRelic = (EnergizedDie) player.getRelic(EnergizedDie.ID);
        if (energizedDieRelic != null && energizedDieRelic.isCurrentEffectEqualTo(EnergizedDieEffect.RUNIC_DOME) && targetID.equals(RunicDome.ID)) {
            return SpireReturn.Return(Boolean.TRUE);
        }
        return SpireReturn.Continue();
    }

}
