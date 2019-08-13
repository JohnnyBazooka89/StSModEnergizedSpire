package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import energizedSpire.EnergizedSpireMod;

public class CursedPentagram extends CustomRelic {

    public static final String ID = "energizedSpire:CursedPentagram";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int CARDS_TO_ADD = 5;

    public CursedPentagram() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        if(c.type != AbstractCard.CardType.CURSE){
            this.counter++;
        }
        if(this.counter == CARDS_TO_ADD){
            this.counter = 0;
            this.flash();
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.returnRandomCurse(), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
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
        return DESCRIPTIONS[0] + CARDS_TO_ADD + DESCRIPTIONS[1];
    }
}
