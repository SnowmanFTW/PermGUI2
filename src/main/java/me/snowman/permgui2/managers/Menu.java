package me.snowman.permgui2.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class Menu {

    private String title;
    private int size;
    private List<String> commands;
    private InventoryType inventoryType = InventoryType.CHEST;
    private String listType;
    private int page = 1;
    private List<MenuItem> items;
    private Inventory inventory;

    public Menu(){
    }

    public Menu setTitle(String title){
        this.title = title;
        return this;
    }

    public Menu setSize(int size){
        this.size = size;
        return this;
    }

    public Menu setCommands(List<String> commands){
        this.commands = commands;
        return this;
    }

    public Menu setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        return this;
    }

    public Menu setListType(String listType) {
        this.listType = listType;
        return this;
    }

    public Menu build() {
        this.inventory = Bukkit.createInventory(null, getSize(), getTitle());
        return this;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public Menu setItems(List<MenuItem> items) {
        this.items = items;
        return this;
    }

    public int getSize() {
        return size;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getListType() {
        return listType;
    }

    public String getTitle() {
        return title;
    }

    public Menu setItems(MenuItem... items) {
        this.items = Arrays.asList(items);
        return this;
    }

    public int getPage() {
        return page;
    }

    public void addPage() {
        page += 1;
    }

    public void substractPage() {
        page -= 1;
    }
}
