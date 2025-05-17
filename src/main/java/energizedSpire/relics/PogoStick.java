package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardPowerTip;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import energizedSpire.EnergizedSpireMod;

public class PogoStick extends CustomRelic {

    public static final String ID = "energizedSpire:PogoStick";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int DAZED_TO_SHUFFLE = 1;

    public PogoStick() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        refreshTips();
    }

    public boolean active = false;

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
        if (AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
            turnOn();
        }
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAZED_TO_SHUFFLE + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStartPostDraw() {
        turnOn();
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.active) {
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToTop(new MakeTempCardInDrawPileAction(new Dazed(), DAZED_TO_SHUFFLE, true, true));
        }
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

    public void refreshTips() {
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        this.tips.add(new CardPowerTip(new Dazed()));
    }
}
