import java.util.*;

public class Inventory {
    private ArrayList<Item> items;

    public Inventory() {
        items = new ArrayList<Item>();
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void addItem(String rawValue) {

        Item item = Item.getItemInfo(rawValue);
        if(item != null) {
            this.items.add(item);
        } else {
            System.out.println("Error in Inventory.addItem - " + rawValue +
                    " is unknown!");
        }
    }

    public void removeItem(String rawValue) {
        int removeMe = -1;

        for(int i=0; i<this.items.size(); i++) {
            Item item = items.get(i);

            if(item.getRawValue().equals(rawValue)) {
                removeMe = i;
                break;
            }
        }

        if(removeMe >= 0) {
            items.remove(removeMe);
        }

    }

    public Item getKeyForDoor(String door) {

        String keyForDoor = "K" + door.substring(1);
        Item key = new Item("Key", keyForDoor, 10);

        return key;
    }

    public boolean hasKeyForDoor(String door) {

        String keyForDoor = "K" + door.substring(1,2);

        for(Item item : items) {
            if(item.getRawValue().equals(keyForDoor)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnkh() {

        for(Item item : items) {
            if(item.getRawValue().equals("AN")) {
                return true;
            }
        }

        return false;
    }

    public String getInventory() {
        String inventory;

        if(items.size() == 0) {
            inventory = "You have nothing in your inventory.";
        } else {
            inventory = "You have: \n";
            int keyCount = 0;
            for(Item item : items) {
                if(item.getName().equals("Key")) {
                    keyCount++;
                } else {
                    inventory += item.getName() + "\n";
                }
            }
            if(keyCount > 0) {
                inventory += keyCount + " key";
                if(keyCount > 1) {
                    inventory += "s";
                }
            }

        }

        return inventory;
    }

}