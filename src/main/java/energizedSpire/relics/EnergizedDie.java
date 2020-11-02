package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.BeforeRenderIntentRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.VelvetChoker;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import energizedSpire.EnergizedSpireMod;
import energizedSpire.enums.EnergizedDieEffect;

public class EnergizedDie extends CustomRelic implements BeforeRenderIntentRelic {

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

    private EnergizedDieEffect currentEffect;
    private boolean obtained;

    public EnergizedDie() {
        super(ID, IMG_CURSED_KEY, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        currentEffect = EnergizedDieEffect.CURSED_KEY;
        obtained = false;
        refreshTips();
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
        obtained = true;
        refreshTips();
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
        this.pulse = false;
        if (isCurrentEffectEqualTo(EnergizedDieEffect.CURSED_KEY) && (room instanceof TreasureRoom)) {
            flash();
            this.pulse = true;
        }
    }

    @Override
    public void onChestOpen(boolean bossChest) {
        if (isCurrentEffectEqualTo(EnergizedDieEffect.CURSED_KEY) && !bossChest) {
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(AbstractDungeon.returnRandomCurse(), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
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
        if (isCurrentEffectEqualTo(EnergizedDieEffect.VELVET_CHOKER) && this.counter < 6) {
            this.counter++;
            if (this.counter >= 6) {
                flash();
            }
        }
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (isCurrentEffectEqualTo(EnergizedDieEffect.VELVET_CHOKER)) {
            if (this.counter >= 6) {
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
        refreshTips();
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

    public void refreshTips() {
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        if (obtained) {
            switch (currentEffect) {
                case CURSED_KEY:
                    this.tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[2]));
                    break;
                case SOZU:
                    this.tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[3]));
                    break;
                case ECTOPLASM:
                    this.tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[4]));
                    break;
                case RUNIC_DOME:
                    this.tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[5]));
                    break;
                case PHILOSOPHERS_STONE:
                    this.tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[6]));
                    break;
                case VELVET_CHOKER:
                    this.tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[7]));
                    break;
            }
        }
    }

    public boolean isCurrentEffectEqualTo(EnergizedDieEffect effect) {
        return currentEffect == effect;
    }

    @Override
    public boolean beforeRenderIntent(AbstractMonster abstractMonster) {
        return !this.isCurrentEffectEqualTo(EnergizedDieEffect.RUNIC_DOME);
    }
}
