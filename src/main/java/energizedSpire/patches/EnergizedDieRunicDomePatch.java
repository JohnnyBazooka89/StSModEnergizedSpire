package energizedSpire.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.RunicDome;
import energizedSpire.enums.EnergizedDieEffect;
import energizedSpire.relics.EnergizedDie;
import javassist.CtBehavior;
import sun.security.provider.ConfigFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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
