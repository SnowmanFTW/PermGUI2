package me.snowman.permgui2.objects;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MenuItem {
    private final ItemStack item;
    private List<String> actions;
    private int slot;

    public MenuItem(Material material){
        this(material, 1);
    }

    public MenuItem(ItemStack item){
        this.item = item;
    }

    public MenuItem(Material material, int amount){
        if (amount == 0) amount = 1;
        item = new ItemStack(material, amount);
    }

    public MenuItem clone(){
        return new MenuItem(item);
    }

    @Deprecated
    public MenuItem setDurability(short dur){
        item.setDurability(dur);
        return this;
    }

    public MenuItem setName(String name){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return this;
    }

    public MenuItem addUnsafeEnchantment(Enchantment ench, int level){
        item.addUnsafeEnchantment(ench, level);
        return this;
    }

    public MenuItem removeEnchantment(Enchantment ench){
        item.removeEnchantment(ench);
        return this;
    }

    @Deprecated
    public MenuItem setSkullOwner(String owner){
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        item.setItemMeta(meta);
        return this;
    }

    public MenuItem addEnchant(Enchantment ench, int level){
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(ench, level, true);
        item.setItemMeta(meta);
        return this;
    }

    public MenuItem addEnchantments(Map<Enchantment, Integer> enchantments){
        item.addEnchantments(enchantments);
        return this;
    }

    public MenuItem setLore(String... lore){
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return this;
    }

    public MenuItem setLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public MenuItem hideItemFlags(){
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return this;
    }

    public MenuItem setActions(List<String> actions) {
        this.actions = actions;
        return this;
    }

    public MenuItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public MenuItem build() {
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    public Integer getSlot() {
        return slot;
    }

    public List<String> getActions() {
        return actions;
    }

    public String getName() {
        if (!item.hasItemMeta()) return "";
        if (!item.getItemMeta().hasDisplayName()) return "";
        return item.getItemMeta().getDisplayName();
    }
}
