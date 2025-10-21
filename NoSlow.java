package today.candy.features.modules.impl.movement;

import net.minecraft.item.*;
import today.candy.event.annotations.EventTarget;
import today.candy.event.impl.other.MoveInputEvent;
import today.candy.event.impl.player.SlowDownEvent;
import today.candy.features.modules.Category;
import today.candy.features.modules.Module;
import today.candy.features.settings.impl.BoolValue;
import today.candy.utils.player.MovementUtils;

public class NoSlow extends Module {

    private final BoolValue food = new BoolValue("Food",true);
    private final BoolValue potion = new BoolValue("Potion",true);
    private final BoolValue bow = new BoolValue("Bow",true);

    public NoSlow() {
        super("NoSlow", Category.MOVEMENT, "No Slow");
        addSettings(food,potion,bow);
    }

    @EventTarget
    public void onSlow(SlowDownEvent e) {
        // 这个是 基础判断
        if (mc.theWorld == null || mc.thePlayer == null || mc.thePlayer.getHeldItem() == null) return;
        // 这个是 1.8 使用物品疾跑. 高版本可以去掉.
        if (!mc.thePlayer.isSprinting() && !mc.thePlayer.isSneaking() && MovementUtils.isMoving() && mc.gameSettings.keyBindForward.isKeyDown()) mc.thePlayer.setSprinting(true);
        // 这个是 食物 减速部分
        if (mc.thePlayer.onGroundTicks % 3 != 0 && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood && food.isEnabled() && mc.thePlayer.isUsingItem()) e.cancel();
        // 这个是 药水 减速部分
        if (mc.thePlayer.onGroundTicks % 3 != 0 && (mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion || mc.thePlayer.getHeldItem().getItem() instanceof ItemBucketMilk) && potion.isEnabled() && mc.thePlayer.isUsingItem()) e.cancel();
        // 这个是 弓驽 减速部分
        if (mc.thePlayer.onGroundTicks % 3 != 0 && mc.thePlayer.getHeldItem().getItem() instanceof ItemBow && bow.isEnabled() && mc.thePlayer.isUsingItem()) e.cancel();
        // 这个是 1.8 剑无减速. 高版本可以去掉.
        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.thePlayer.isUsingItem()) e.cancel();
    }

    @EventTarget
    public void onMoveInput(MoveInputEvent e) {
        // 这个是 强制玩家跳跃来绕过检测
        if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.isUsingItem() && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) e.setJump(true);
    }
}