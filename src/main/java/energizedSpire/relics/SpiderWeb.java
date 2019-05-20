package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.EntanglePower;
import energizedSpire.EnergizedSpireMod;

public class SpiderWeb extends CustomRelic {

    public static final String ID = "energizedSpire:SpiderWeb";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int ENTANGLED_TURNS = 1;

    private static final PowerStrings ENTANGLED_POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(EntanglePower.POWER_ID);

    public SpiderWeb() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EntanglePower(AbstractDungeon.player)));
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c) {
        super.updateDescription(c);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(ENTANGLED_POWER_STRINGS.NAME, ENTANGLED_POWER_STRINGS.DESCRIPTIONS[0]));
        this.initializeTips();
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
        return DESCRIPTIONS[0] + ENTANGLED_TURNS + DESCRIPTIONS[1];
    }
}