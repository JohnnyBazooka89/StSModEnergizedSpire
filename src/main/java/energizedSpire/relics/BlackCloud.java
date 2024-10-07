package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.stances.CalmStance;
import energizedSpire.EnergizedSpireMod;
import energizedSpire.powers.LoseEnergyNextTurnPower;

public class BlackCloud extends CustomRelic {

    public static final String ID = "energizedSpire:BlackCloud";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    private static final int ENERGY_TO_LOSE_NEXT_TURN = 1;

    public BlackCloud() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
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
    public void onPlayerEndTurn() {
        if (AbstractDungeon.player.stance.ID.equals(CalmStance.STANCE_ID)) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseEnergyNextTurnPower(AbstractDungeon.player, ENERGY_TO_LOSE_NEXT_TURN), ENERGY_TO_LOSE_NEXT_TURN));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
