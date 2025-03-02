package cn.ksmcbrigade.paunch.entity;

import cn.ksmcbrigade.paunch.Config;
import cn.ksmcbrigade.paunch.Paunch;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StoneProjectile extends ThrowableItemProjectile {

    public Item BLOCK = Items.STONE;

    public StoneProjectile(EntityType<? extends StoneProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
    }

    public StoneProjectile(Level p_37443_,LivingEntity owner,Item BLOCK) {
        super(Paunch.STONE_PROJECTILE.get(), p_37443_);
        this.setOwner(owner);
        this.setBLOCK(BLOCK);
        this.setPos(owner.position().add(0,0.2,0));
    }

    public StoneProjectile setBLOCK(Item BLOCK){
        this.BLOCK = BLOCK;
        return this;
    }

    public StoneProjectile owner(Entity entity){
        this.setOwner(entity);
        return this;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.STONE;
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity p_37250_) {
        return p_37250_ instanceof LivingEntity && p_37250_ != this.getOwner();
    }

    @Override
    public void setOwner(@Nullable Entity p_37263_) {
        if(p_37263_ instanceof LivingEntity){
            super.setOwner(p_37263_);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        this.level().setBlockAndUpdate(p_37258_.getBlockPos().above(), Block.byItem(BLOCK).defaultBlockState());
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult p_37259_) {
        if(p_37259_.getEntity() instanceof LivingEntity livingEntity){
            livingEntity.hurt(this.getOwner()==null?livingEntity.damageSources().generic():livingEntity.damageSources().mobAttack((LivingEntity) this.getOwner()), Config.HURT.get());
        }
    }
}
