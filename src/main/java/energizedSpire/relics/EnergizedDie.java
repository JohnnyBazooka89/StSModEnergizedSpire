package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.VelvetChoker;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import energizedSpire.EnergizedSpireMod;
import energizedSpire.enums.EnergizedDieEffect;

public class EnergizedDie extends CustomRelic {

    public static final String ID = "energizedSpire:EnergizedDie";
    public static final String IMG_ID_CURSED_KEY = "EnergizedDieCursedKey";
    public static final String IMG_ID_SOZU = "EnergizedDieSozu";
    public static final String IMG_ID_ECTOPLASM = "EnergizedDieEctoplasm";
    public static final String IMG_ID_RUNIC_DOME = "EnergizedDieRunicDome";
    public static final String IMG_ID_PHILOSOPHERS_STONE = "EnergizedDiePhilosophersStone";
    public static final String IMG_ID_VELVET_CHOKER = "EnergizedDieVelvetChoker";
    public static final Texture IMG_CURSED_KEY = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(IMG_ID_CURSED_KEY));
    public static final Texture IMG_SOZU = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(IMG_ID_SOZU));
    public static final Texture IMG_ECTOPLASM = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(IMG_ID_ECTOPLASM));
    public static final Texture IMG_RUNIC_DOME = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(IMG_ID_RUNIC_DOME));
    public static final Texture IMG_PHILOSOPHER_STONE = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(IMG_ID_PHILOSOPHERS_STONE));
    public static final Texture IMG_VELVET_CHOKER = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(IMG_ID_VELVET_CHOKER));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    private static final RelicStrings velvetChokerStrings = CardCrawlGame.languagePack.getRelicStrings(VelvetChoker.ID);

    private static EnergizedDieEffect currentEffect;

    public EnergizedDie() {
        super(ID, IMG_CURSED_KEY, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        currentEffect = EnergizedDieEffect.CURSED_KEY;
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if (isCurrentEffectEqualTo(EnergizedDieEffect.CURSED_KEY)) {
            if ((room instanceof TreasureRoom)) {
                flash();
                this.pulse = true;
            } else {
                this.pulse = false;
            }
        }
    }

    @Override
    public void onChestOpen(boolean bossChest) {
        if (isCurrentEffectEqualTo(EnergizedDieEffect.CURSED_KEY)) {
            if (!bossChest) {
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(AbstractDungeon.returnRandomCurse(), Settings.WIDTH / 2, Settings.HEIGHT / 2));
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            }
        }
    }

    @Override
    public void atBattleStart() {
        if (isCurrentEffectEqualTo(EnergizedDieEffect.PHILOSOPHERS_STONE)) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(m, this));
                m.addPower(new StrengthPower(m, 1));
            }
            AbstractDungeon.onModifyPower();
        }
        if (isCurrentEffectEqualTo(EnergizedDieEffect.VELVET_CHOKER)) {
            this.counter = 0;
        }
    }

    @Override
    public void atTurnStart() {
        if (isCurrentEffectEqualTo(EnergizedDieEffect.VELVET_CHOKER)) {
            this.counter = 0;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (isCurrentEffectEqualTo(EnergizedDieEffect.VELVET_CHOKER)) {
            if ((this.counter < 6) && (card.type != AbstractCard.CardType.CURSE)) {
                this.counter += 1;
                if (this.counter >= 6) {
                    flash();
                }
            }
        }
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (isCurrentEffectEqualTo(EnergizedDieEffect.VELVET_CHOKER)) {
            if ((this.counter >= 6) && (card.type != AbstractCard.CardType.CURSE)) {
                card.cantUseMessage = (velvetChokerStrings.DESCRIPTIONS[3] + 6 + velvetChokerStrings.DESCRIPTIONS[1]);
                return false;
            }
            return true;
        }
        return super.canPlay(card);
    }

    @Override
    public void onVictory() {
        if (isCurrentEffectEqualTo(EnergizedDieEffect.VELVET_CHOKER)) {
            this.counter = -1;
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        flash();

        int rolledNumber = AbstractDungeon.cardRandomRng.random(1, 6);

        switch (rolledNumber) {
            case 1:
                currentEffect = EnergizedDieEffect.CURSED_KEY;
                break;
            case 2:
                currentEffect = EnergizedDieEffect.SOZU;
                break;
            case 3:
                currentEffect = EnergizedDieEffect.ECTOPLASM;
                break;
            case 4:
                currentEffect = EnergizedDieEffect.RUNIC_DOME;
                break;
            case 5:
                currentEffect = EnergizedDieEffect.PHILOSOPHERS_STONE;
                break;
            case 6:
                currentEffect = EnergizedDieEffect.VELVET_CHOKER;
                break;
        }
        setImageBasedOnCurrentEffect();
    }

    private void setImageBasedOnCurrentEffect() {
        switch (currentEffect) {
            case CURSED_KEY:
                setTextureOutline(IMG_CURSED_KEY, OUTLINE);
                break;
            case SOZU:
                setTextureOutline(IMG_SOZU, OUTLINE);
                break;
            case ECTOPLASM:
                setTextureOutline(IMG_ECTOPLASM, OUTLINE);
                break;
            case RUNIC_DOME:
                setTextureOutline(IMG_RUNIC_DOME, OUTLINE);
                break;
            case PHILOSOPHERS_STONE:
                setTextureOutline(IMG_PHILOSOPHER_STONE, OUTLINE);
                break;
            case VELVET_CHOKER:
                setTextureOutline(IMG_VELVET_CHOKER, OUTLINE);
                break;
        }
    }

    public boolean isCurrentEffectEqualTo(EnergizedDieEffect effect) {
        return currentEffect == effect;
    }
}
