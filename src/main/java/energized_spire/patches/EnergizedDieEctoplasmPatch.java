package energized_spire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import energized_spire.enums.EnergizedDieEffect;
import energized_spire.relics.EnergizedDie;

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
