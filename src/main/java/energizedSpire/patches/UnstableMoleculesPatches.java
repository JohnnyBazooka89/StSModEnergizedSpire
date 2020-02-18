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
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.vfx.BobEffect;
import energizedSpire.actions.UnstableMoleculesDecreaseValueInOrbAction;
import energizedSpire.relics.UnstableMolecules;

public class UnstableMoleculesPatches {

    @SpirePatch(clz = AbstractOrb.class, method = SpirePatch.CLASS)
    public static class UnstableMoleculesFieldPatch {
        public static SpireField<Integer> value = new SpireField<>(() -> null);
    }

    public static class ConstructorPatches {
        @SpirePatch(clz = Lightning.class, method = SpirePatch.CONSTRUCTOR)
        public static class LightningPatch {
            public static void Prefix(Lightning orb) {
                setDefaultValueForOrb(orb);
            }
        }

        @SpirePatch(clz = com.megacrit.cardcrawl.orbs.Frost.class, method = SpirePatch.CONSTRUCTOR)
        public static class FrostPatch {
            public static void Prefix(Frost orb) {
                setDefaultValueForOrb(orb);
            }
        }

        @SpirePatch(clz = com.megacrit.cardcrawl.orbs.Dark.class, method = SpirePatch.CONSTRUCTOR)
        public static class DarkPatch {
            public static void Prefix(Dark orb) {
                setDefaultValueForOrb(orb);
            }
        }

        @SpirePatch(clz = com.megacrit.cardcrawl.orbs.Plasma.class, method = SpirePatch.CONSTRUCTOR)
        public static class PlasmaPatch {
            public static void Prefix(Plasma orb) {
                setDefaultValueForOrb(orb);
            }
        }

        private static void setDefaultValueForOrb(AbstractOrb orb) {
            if (AbstractDungeon.player.hasRelic(UnstableMolecules.ID)) {
                UnstableMoleculesFieldPatch.value.set(orb, UnstableMolecules.AMOUNT_OF_PASSIVE_ABILITY_TRIGGERS);
            }
        }
    }


    public static class RenderPatches {
        @SpirePatch(clz = Lightning.class, method = "render")
        public static class LightningPatch {
            public static void Postfix(Lightning orb, SpriteBatch sb) {
                renderAdditionalValue(orb, sb, false);
            }
        }

        @SpirePatch(clz = Frost.class, method = "render")
        public static class FrostPatch {
            public static void Postfix(Frost orb, SpriteBatch sb) {
                renderAdditionalValue(orb, sb, false);
            }
        }

        @SpirePatch(clz = Dark.class, method = "render")
        public static class DarkPatch {
            public static void Postfix(Dark orb, SpriteBatch sb) {
                renderAdditionalValue(orb, sb, true);
            }
        }

        @SpirePatch(clz = Plasma.class, method = "render")
        public static class PlasmaPatch {
            public static void Postfix(Plasma orb, SpriteBatch sb) {
                renderAdditionalValue(orb, sb, false);
            }
        }

        private static void renderAdditionalValue(AbstractOrb orb, SpriteBatch sb, boolean isDarkOrb) {
            if (AbstractDungeon.player.hasRelic(UnstableMolecules.ID)) {
                Integer value = UnstableMoleculesFieldPatch.value.get(orb);
                if (value != null) {
                    float NUM_X_OFFSET = (float) ReflectionHacks.getPrivate(orb, AbstractOrb.class, "NUM_X_OFFSET");
                    float NUM_Y_OFFSET = (float) ReflectionHacks.getPrivate(orb, AbstractOrb.class, "NUM_Y_OFFSET");
                    BobEffect bobEffect = (BobEffect) ReflectionHacks.getPrivate(orb, AbstractOrb.class, "bobEffect");
                    float fontScale = (float) ReflectionHacks.getPrivate(orb, AbstractOrb.class, "fontScale");
                    FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, value.toString(), orb.cX + NUM_X_OFFSET - 40.0F * Settings.scale,
                            orb.cY + bobEffect.y / 2.0F + NUM_Y_OFFSET + (isDarkOrb ? -4.0F * Settings.scale : 0), Color.RED, fontScale);
                }
            }
        }
    }

    public static class TriggerPassiveAbility {
        @SpirePatch(clz = Lightning.class, method = "onEndOfTurn")
        public static class LightningPatch {
            public static void Postfix(Lightning orb) {
                decreaseValueInOrb(orb);
            }
        }

        @SpirePatch(clz = Frost.class, method = "onEndOfTurn")
        public static class FrostPatch {
            public static void Postfix(Frost orb) {
                decreaseValueInOrb(orb);
            }
        }

        @SpirePatch(clz = Dark.class, method = "onEndOfTurn")
        public static class DarkPatch {
            public static void Postfix(Dark orb) {
                decreaseValueInOrb(orb);
            }
        }

        @SpirePatch(clz = Plasma.class, method = "onStartOfTurn")
        public static class PlasmaPatch {
            public static void Postfix(Plasma orb) {
                decreaseValueInOrb(orb);
            }
        }

        private static void decreaseValueInOrb(AbstractOrb orb) {
            if (AbstractDungeon.player.hasRelic(UnstableMolecules.ID)) {
                AbstractDungeon.actionManager.addToBottom(new UnstableMoleculesDecreaseValueInOrbAction(orb));
            }
        }
    }

    public static class PreventPassiveAbilityAtZeroValue {
        @SpirePatch(clz = Lightning.class, method = "onEndOfTurn")
        public static class LightningPatch {
            public static SpireReturn<Void> Prefix(Lightning orb) {
                return preventPassiveAbilityAtZeroValue(orb);
            }
        }

        @SpirePatch(clz = Frost.class, method = "onEndOfTurn")
        public static class FrostPatch {
            public static SpireReturn<Void> Prefix(Frost orb) {
                return preventPassiveAbilityAtZeroValue(orb);
            }
        }

        @SpirePatch(clz = Dark.class, method = "onEndOfTurn")
        public static class DarkPatch {
            public static SpireReturn<Void> Prefix(Dark orb) {
                return preventPassiveAbilityAtZeroValue(orb);
            }
        }

        @SpirePatch(clz = Plasma.class, method = "onStartOfTurn")
        public static class PlasmaPatch {
            public static SpireReturn<Void> Prefix(Plasma orb) {
                return preventPassiveAbilityAtZeroValue(orb);
            }
        }

        private static SpireReturn<Void> preventPassiveAbilityAtZeroValue(AbstractOrb orb) {
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