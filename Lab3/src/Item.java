public class Item implements Comparable<Item>
{
    private String description;
    private int partNumber;

    public Item(String aDescription, int aPartNumber)
    {
        description = aDescription;
        partNumber = aPartNumber;
    }

    public String getDescription() {
        return description;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public String toString()
    {
        return "[no=" + partNumber + ", desc=" + description + "]";
    }

    public int compareTo(Item other)
    {
        int diff = Integer.compare(partNumber, other.getPartNumber());
        return diff != 0 ? diff : description.compareTo(other.getDescription());
    }
}
