package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardDisappearEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import energizedSpire.EnergizedSpireMod;
import energizedSpire.effects.AbstractGameEffectDelayedByAnotherOneEffect;

import java.util.ArrayList;
import java.util.List;

public class HugeHouse extends CustomRelic {

    public static final String ID = "energizedSpire:HugeHouse";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int POTIONS_TO_LOSE = 1;
    public static final int GOLD_TO_LOSE = 50;
    public static final int MAX_HP_TO_LOSE = 5;
    public static final int CURSES_TO_OBTAIN = 1;
    public static final int CARDS_TO_DOWNGRADE = 1;

    public HugeHouse() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;

        loseARandomPotion();
        loseGold();
        lowerMaxHP();
        obtainACurse();
        transformARandomCard();
    }

    private void loseARandomPotion() {
        AbstractPotion randomPotion = AbstractDungeon.player.getRandomPotion();
        if(randomPotion != null){
            AbstractDungeon.player.removePotion(randomPotion);
        }
    }

    private void loseGold() {
        AbstractDungeon.player.loseGold(GOLD_TO_LOSE);
    }

    private void lowerMaxHP() {
        AbstractDungeon.player.decreaseMaxHealth(MAX_HP_TO_LOSE);
    }

    private void obtainACurse() {
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.returnRandomCurse(), Settings.WIDTH  / 3.0F, Settings.HEIGHT / 2.0F));
    }

    private void transformARandomCard() {
        List<AbstractCard> transformableCards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.getPurgeableCards().group) {
            if (c.type != AbstractCard.CardType.CURSE) {
                transformableCards.add(c);
            }
        }
        if (!transformableCards.isEmpty()) {
            AbstractCard cardToTransform = transformableCards.get(AbstractDungeon.miscRng.random(transformableCards.size() - 1));
            AbstractDungeon.player.masterDeck.removeCard(cardToTransform);
            AbstractDungeon.transformCard(cardToTransform, false, AbstractDungeon.miscRng);
            if (AbstractDungeon.transformedCard != null) {
                AbstractGameEffect purgeCardEffect = new CardDisappearEffect(
                        cardToTransform, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F);
                AbstractDungeon.topLevelEffectsQueue.add(new AbstractGameEffectDelayedByAnotherOneEffect(new ShowCardAndObtainEffect(
                        AbstractDungeon.getTransformedCard(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, false), purgeCardEffect));
            }
        }
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + POTIONS_TO_LOSE + DESCRIPTIONS[1] + GOLD_TO_LOSE + DESCRIPTIONS[2] + MAX_HP_TO_LOSE + DESCRIPTIONS[3] + CURSES_TO_OBTAIN + DESCRIPTIONS[4] + CARDS_TO_DOWNGRADE + DESCRIPTIONS[5];
    }
}
