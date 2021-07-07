package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import energizedSpire.EnergizedSpireMod;

public class RottingSkull extends CustomRelic {

    public static final String ID = "energizedSpire:RottingSkull";

    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public RottingSkull() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        addToBot(new AbstractGameAction() {
            public void update() {
                if (AbstractDungeon.player.isBloodied) {
                    flash();
                    startPulse();
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player.isBloodied) {
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void onBloodied() {
        flash();
        startPulse();
    }

    @Override
    public void onNotBloodied() {
        stopPulse();
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    private void startPulse() {
        this.pulse = true;
    }

}