package org.karn.actionstatistics;

import com.mojang.logging.LogUtils;
import eu.pb4.polymer.core.api.other.PolymerStat;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.logging.Logger;

public class ActionStatistics implements ModInitializer {
    public static final String MOD_ID = "actionstatistics";
    public static final Identifier LEFT_CLICK_STAT = Identifier.of(MOD_ID, "left_click");
    public static final Identifier HAND_SWAP_STAT = Identifier.of(MOD_ID, "press_swap");
    public static final Identifier PRESS_SNEAK_STAT = Identifier.of(MOD_ID, "press_sneak");
    public static final Identifier PRESS_SPRINT_STAT = Identifier.of(MOD_ID, "press_sprint");
    public static final Identifier PRESS_FORWARD_STAT = Identifier.of(MOD_ID, "press_forward");
    public static final Identifier PRESS_BACKWARD_STAT = Identifier.of(MOD_ID, "press_backward");
    public static final Identifier PRESS_LEFT_STAT = Identifier.of(MOD_ID, "press_left");
    public static final Identifier PRESS_RIGHT_STAT = Identifier.of(MOD_ID, "press_right");
    public static final Identifier PRESS_DROP = Identifier.of(MOD_ID, "press_drop");
    public static final Identifier PRESS_DROP_ALL = Identifier.of(MOD_ID, "press_dropall");
    public static final Identifier RELEASE_USE = Identifier.of(MOD_ID, "release_use");
    public static final Identifier PRESS_RIDING_JUMP = Identifier.of(MOD_ID, "press_riding_jump");
    public static final Identifier PRESS_FALL_FLYING = Identifier.of(MOD_ID, "press_fall_flying");
    @Override
    public void onInitialize() {
        registerStat(HAND_SWAP_STAT, "as_press_swap");
        registerStat(PRESS_SNEAK_STAT, "as_press_sneak");
        registerStat(PRESS_SPRINT_STAT, "as_press_sprint");
        registerStat(LEFT_CLICK_STAT, "as_left_click");
        registerStat(PRESS_FORWARD_STAT, "as_press_forward");
        registerStat(PRESS_BACKWARD_STAT, "as_press_backward");
        registerStat(PRESS_LEFT_STAT, "as_press_left");
        registerStat(PRESS_RIGHT_STAT, "as_press_right");
        registerStat(PRESS_DROP, "as_press_drop");
        registerStat(PRESS_DROP_ALL, "as_press_dropall");
        registerStat(RELEASE_USE, "as_release_use");
        registerStat(PRESS_RIDING_JUMP, "as_press_riding_jump");
        registerStat(PRESS_FALL_FLYING, "as_press_fall_flying");
        System.out.println("ActionStatistics is initialized!");
    }

    public static void registerStat(Identifier id, String name) {
        Registry.register(Registries.CUSTOM_STAT, name, id);
        Stats.CUSTOM.getOrCreateStat(id, StatFormatter.DEFAULT);
        RegistrySyncUtils.setServerEntry((Registry<Object>) (Object) Registries.CUSTOM_STAT, (Object) id);
    }
}
