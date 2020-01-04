package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import energizedSpire.EnergizedSpireMod;

public class DecayingSkull extends CustomRelic {

    public static final String ID = "energizedSpire:DecayingSkull";
    private static final int DAMAGE_TO_TAKE = 3;

    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public DecayingSkull() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        if (!AbstractDungeon.player.isBloodied) {
            flash();
            beginLongPulse();
        }
    }

    @Override
    public void onBloodied() {
        stopPulse();
    }

    @Override
    public void onNotBloodied() {
        flash();
        beginLongPulse();
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    @Override
    public void onPlayerEndTurn() {
        if (!AbstractDungeon.player.isBloodied) {
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, DAMAGE_TO_TAKE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
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
        return this.DESCRIPTIONS[0] + DAMAGE_TO_TAKE + this.DESCRIPTIONS[1];
    }

}