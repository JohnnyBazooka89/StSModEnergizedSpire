package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import energizedSpire.EnergizedSpireMod;

public class DeadBattery extends CustomRelic {

    public static final String ID = "energizedSpire:DeadBattery";
    public static final Texture IMG = ImageMaster.loadImage((EnergizedSpireMod.getRelicImagePath(ID)));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int POWER_CARDS_TO_PLAY = 1;

    public DeadBattery() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (this.counter < POWER_CARDS_TO_PLAY && card.type == AbstractCard.CardType.POWER) {
            ++this.counter;
            if (this.counter >= POWER_CARDS_TO_PLAY) {
                this.flash();
            }
        }
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (this.counter >= POWER_CARDS_TO_PLAY && card.type == AbstractCard.CardType.POWER) {
            card.cantUseMessage =  DESCRIPTIONS[2] + POWER_CARDS_TO_PLAY + DESCRIPTIONS[3];
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
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
        return DESCRIPTIONS[0] + POWER_CARDS_TO_PLAY + DESCRIPTIONS[1];
    }

}
