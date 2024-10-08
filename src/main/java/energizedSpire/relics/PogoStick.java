package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import energizedSpire.EnergizedSpireMod;

public class PogoStick extends CustomRelic {

    public static final String ID = "energizedSpire:PogoStick";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int DAMAGE_TO_TAKE = 4;

    public PogoStick() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
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
        return DESCRIPTIONS[0] + DAMAGE_TO_TAKE + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStartPostDraw() {
        turnOn();
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.active) {
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, DAMAGE_TO_TAKE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
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
}
