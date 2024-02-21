public class Item {

    private String name;  //example "Key"
    private String rawValue; //"K3" the value on the item in the map array
    private int experienceValue;

    public Item(
            String name,
            String rawValue,
            int experienceValue
    ) {

        this.name = name;
        this.rawValue = rawValue;
        this.experienceValue = experienceValue;
    }

    public static Item getItemInfo(String rawValue) {

        if(rawValue.startsWith("K")) {
            return new Item("Key", rawValue, 10);
        }

        if(rawValue.equals("$")) {
            return new Item("Gold", rawValue, 100);
        }

        if(rawValue.equals("AN")) {
            return new Item("Ankh", rawValue, 50);
        }

        return null;

    }

    public String getName() {
        return this.name;
    }

    public String getRawValue() {
        return this.rawValue;
    }

    public int getExperienceValue() {
        return this.experienceValue;
    }

}