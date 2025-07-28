package net.mcreator.chishimod; // 使用你的实际包名

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation; // 添加ResourceLocation导入
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries; // 确保正确导入ForgeRegistries

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class FearEffectHandler {
    
    // 使用你的模组ID和附魔注册名
    private static final String ENCHANTMENT_ID = "chishimod:fear_enchantment";
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Player player = event.player;
        Level world = player.level;
        
        // 检查玩家是否持有附魔物品
        if (!hasEnchantedStick(player)) return;
        
        // 获取周围生物
        List<Entity> entities = world.getEntities(player, player.getBoundingBox().inflate(12.0));
        for (Entity entity : entities) {
            if (entity instanceof Mob && entity != player && !isHoldingEnchantedStick(entity)) {
                scareEntity((Mob) entity, player);
            }
        }
    }
    
    private static boolean hasEnchantedStick(Player player) {
        return isEnchantedStick(player.getMainHandItem()) || 
               isEnchantedStick(player.getOffhandItem());
    }
    
    private static boolean isHoldingEnchantedStick(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            return isEnchantedStick(living.getMainHandItem()) ||
                   isEnchantedStick(living.getOffhandItem());
        }
        return false;
    }
    
    private static boolean isEnchantedStick(ItemStack stack) {
        // 检查是否为木棍
        if (stack.getItem() != Items.STICK) {
            return false;
        }
        
        // 通过注册表获取附魔实例
        ResourceLocation rl = new ResourceLocation(ENCHANTMENT_ID);
        Enchantment enchant = ForgeRegistries.ENCHANTMENTS.getValue(rl);
        
        if (enchant == null) {
            // 附魔未注册，可能是加载顺序问题
            return false;
        }
        
        // 检查物品是否附有该附魔
        return EnchantmentHelper.getItemEnchantmentLevel(enchant, stack) > 0;
    }
    
    private static void scareEntity(Mob entity, Player source) {
        Random rand = entity.getRandom();
        
        // 设置随机速度 (1.5-2.5倍正常速度)
        double speedMultiplier = 1.5 + rand.nextDouble() * 1.0;
        
        // 计算逃跑方向 (远离玩家)
        double dx = entity.getX() - source.getX();
        double dz = entity.getZ() - source.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);
        
        if (distance > 0) {
            dx /= distance;
            dz /= distance;
            
            // 设置移动目标 (当前方向10格外)
            double targetX = entity.getX() + dx * 10;
            double targetZ = entity.getZ() + dz * 10;
            
            // 清除当前目标并逃跑
            entity.setTarget(null);
            entity.getNavigation().moveTo(targetX, entity.getY(), targetZ, speedMultiplier);
        }
    }
}