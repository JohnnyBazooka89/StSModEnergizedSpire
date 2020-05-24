package energizedSpire.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.BobEffect;
import energizedSpire.actions.UnstableMoleculesDecreaseValueInOrbAction;
import energizedSpire.relics.UnstableMolecules;

public class UnstableMoleculesPatches {

    @SpirePatch(clz = AbstractOrb.class, method = SpirePatch.CLASS)
    public static class UnstableMoleculesFieldPatch {
        public static SpireField<Integer> value = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = Frost.class, method = SpirePatch.CONSTRUCTOR)
    public static class ConstructorPatch {
        public static void Prefix(Frost orb) {
            if (AbstractDungeon.player.hasRelic(UnstableMolecules.ID)) {
                UnstableMoleculesFieldPatch.value.set(orb, UnstableMolecules.AMOUNT_OF_PASSIVE_ABILITY_TRIGGERS);
            }
        }
    }

    @SpirePatch(clz = Frost.class, method = "render")
    public static class RenderPatch {
        public static void Postfix(Frost orb, SpriteBatch sb) {
            if (AbstractDungeon.player.hasRelic(UnstableMolecules.ID)) {
                Integer value = UnstableMoleculesFieldPatch.value.get(orb);
                if (value != null) {
                    BobEffect bobEffect = (BobEffect) ReflectionHacks.getPrivate(orb, AbstractOrb.class, "bobEffect");
                    FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, value.toString(), orb.cX,
                            orb.cY + (bobEffect.y / 2.0F) + 20.0F * Settings.scale, Color.RED, 0.5f);
                }
            }
        }
    }

    @SpirePatch(clz = Frost.class, method = "onEndOfTurn")
    public static class TriggerPassiveAbilityPatch {
        public static void Postfix(Frost orb) {
            if (AbstractDungeon.player.hasRelic(UnstableMolecules.ID)) {
                AbstractDungeon.actionManager.addToBottom(new UnstableMoleculesDecreaseValueInOrbAction(orb));
            }
        }
    }

    @SpirePatch(clz = Frost.class, method = "onEndOfTurn")
    public static class PreventPassiveAbilityAtZeroValuePatch {
        public static SpireReturn<Void> Prefix(Frost orb) {
            if (AbstractDungeon.player.hasRelic(UnstableMolecules.ID)) {
                Integer value = UnstableMoleculesFieldPatch.value.get(orb);
                if (value != null && value == 0) {
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }

}