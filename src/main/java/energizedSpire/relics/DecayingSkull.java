package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import energizedSpire.EnergizedSpireMod;

public class DecayingSkull extends CustomRelic {

    public static final String ID = "energizedSpire:DecayingSkull";
    private static final int HP_TO_LOSE = 2;

    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public DecayingSkull() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

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
            AbstractDungeon.actionManager.addToTop(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, HP_TO_LOSE, AbstractGameAction.AttackEffect.FIRE));
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

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + HP_TO_LOSE + this.DESCRIPTIONS[1];
    }

}