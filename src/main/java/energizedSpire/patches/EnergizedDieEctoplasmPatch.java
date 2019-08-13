package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import energizedSpire.enums.EnergizedDieEffect;
import energizedSpire.relics.EnergizedDie;

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
