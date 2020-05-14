package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import energizedSpire.relics.NeverEndingSparkler;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import static energizedSpire.patches.NeverEndingSparklerPatches.HasEmeraldKeyPatch.hasEmeraldKeySpireField;

public class NeverEndingSparklerPatches {

    public static class HasEmeraldKeySpireFieldChecker {

        public static Boolean assignIfNullAndGetValue(MapRoomNode mapRoomNode) {
            if (hasEmeraldKeySpireField.get(mapRoomNode) == null) {
                if (mapRoomNode.getRoom() instanceof MonsterRoom || mapRoomNode.getRoom() instanceof MonsterRoomElite) {
                    hasEmeraldKeySpireField.set(mapRoomNode, AbstractDungeon.cardRandomRng.randomBoolean());
                } else {
                    hasEmeraldKeySpireField.set(mapRoomNode, false);
                }
            }
            return hasEmeraldKeySpireField.get(mapRoomNode);
        }
    }

    @SpirePatch(clz = MapRoomNode.class, method = SpirePatch.CLASS)
    public static class HasEmeraldKeyPatch {
        public static SpireField<Boolean> hasEmeraldKeySpireField = new SpireField<Boolean>(() -> null);
    }

    @SpirePatch(clz = MapRoomNode.class, method = "updateEmerald")
    @SpirePatch(clz = MapRoomNode.class, method = "renderEmeraldVfx")
    public static class MapRoomNodePatches {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getFieldName().equals("isFinalActAvailable") || fieldAccess.getFieldName().equals("hasEmeraldKey")) {
                        fieldAccess.replace(
                                "$_ = ($proceed() || " + NeverEndingSparklerPatches.class.getName() + ".hasEmeraldKeySpireField(this));"
                        );
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    @SpirePatch(clz = MonsterRoomElite.class, method = "applyEmeraldEliteBuff")
    public static class MonsterRoomElitePatches {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getFieldName().equals("isFinalActAvailable") || fieldAccess.getFieldName().equals("hasEmeraldKey")) {
                        fieldAccess.replace(
                                "$_ = ($proceed() || " + NeverEndingSparklerPatches.class.getName() + ".hasEmeraldKeySpireField(" + AbstractDungeon.class.getName() + ".getCurrMapNode()));"
                        );
                    }
                }
            };
        }
    }

    @SpirePatch(clz = MonsterRoomElite.class, method = "addEmeraldKey")
    public static class MonsterRoomEliteAddEmeraldKeyPatch {
        public static ExprEditor Instrument() {
            final int[] counter = {0};
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getFieldName().equals("hasEmeraldKey")) {
                        if (counter[0] == 1) {
                            fieldAccess.replace(
                                    "$_ = ($proceed() || " + NeverEndingSparklerPatches.class.getName() + ".hasEmeraldKeySpireField(" + AbstractDungeon.class.getName() + ".getCurrMapNode()));"
                            );
                        }
                        counter[0]++;
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "applyEmeraldEliteBuff")
    public static class AbstractRoomPatch {
        public static void Prefix(AbstractRoom abstractRoom) {
            if (hasEmeraldKeySpireField(AbstractDungeon.getCurrMapNode())) {
                switch (AbstractDungeon.mapRng.random(0, 3)) {
                    case 0: {
                        for (final AbstractMonster m : abstractRoom.monsters.monsters) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, AbstractDungeon.actNum + 1), AbstractDungeon.actNum + 1));
                        }
                        break;
                    }
                    case 1: {
                        for (final AbstractMonster m : abstractRoom.monsters.monsters) {
                            AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpAction(m, 0.25f, true));
                        }
                        break;
                    }
                    case 2: {
                        for (final AbstractMonster m : abstractRoom.monsters.monsters) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new MetallicizePower(m, AbstractDungeon.actNum * 2 + 2), AbstractDungeon.actNum * 2 + 2));
                        }
                        break;
                    }
                    case 3: {
                        for (final AbstractMonster m : abstractRoom.monsters.monsters) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new RegenerateMonsterPower(m, 1 + AbstractDungeon.actNum * 2), 1 + AbstractDungeon.actNum * 2));
                        }
                        break;
                    }
                }
            }
        }
    }

    @SpirePatch(clz = MonsterRoom.class, method = "dropReward")
    public static class MonsterRoomPatch {

        public static void Postfix(MonsterRoom monsterRoom) {
            if (Settings.isFinalActAvailable && !Settings.hasEmeraldKey && hasEmeraldKeySpireField(AbstractDungeon.getCurrMapNode())) {
                monsterRoom.rewards.add(new RewardItem(null, RewardItem.RewardType.EMERALD_KEY));
            }
        }

    }


    public static boolean hasEmeraldKeySpireField(MapRoomNode mapRoomNode) {
        return AbstractDungeon.player.hasRelic(NeverEndingSparkler.ID) && HasEmeraldKeySpireFieldChecker.assignIfNullAndGetValue(mapRoomNode);
    }

}