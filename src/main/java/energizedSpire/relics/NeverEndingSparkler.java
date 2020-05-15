package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import energizedSpire.EnergizedSpireMod;
import energizedSpire.Keyword;

public class NeverEndingSparkler extends CustomRelic {

    public static final String ID = "energizedSpire:NeverEndingSparkler";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public NeverEndingSparkler() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        refreshTips();
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
        return DESCRIPTIONS[0];
    }

    public void refreshTips() {
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        Keyword burningRoomsKeyword = EnergizedSpireMod.keywords.get(EnergizedSpireMod.BURNING_ROOMS_KEYWORD_ID);
        this.tips.add(new PowerTip(burningRoomsKeyword.PROPER_NAME, burningRoomsKeyword.DESCRIPTION));
    }

}
