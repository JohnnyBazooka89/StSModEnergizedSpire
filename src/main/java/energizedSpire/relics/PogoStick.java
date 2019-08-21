package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import energizedSpire.EnergizedSpireMod;

public class PogoStick extends CustomRelic {

    public static final String ID = "energizedSpire:PogoStick";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int AMOUNT_OF_FEWER_CARDS = 1;

    public PogoStick() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    public boolean active = false;

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
        return DESCRIPTIONS[0] + AMOUNT_OF_FEWER_CARDS + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStartPostDraw() {
        turnOn();
    }

    @Override
    public void onManualDiscard() {
        turnOff();
    }

    @Override
    public void onVictory() {
        turnOff();
    }

    private void turnOn() {
        this.active = true;
        this.beginLongPulse();
    }

    private void turnOff() {
        this.active = false;
        this.stopPulse();
    }
}
