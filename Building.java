/**
 * This class represents the building(node) in the map. 
 * 
 */
public class Building implements BuildingInterface {
  String name; // name of building

  public Building(String name) {
    this.name = name;
  }

  /**
   * Returns name of the building
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Compares name of building, if it's similar then it's equal
   */
  @Override
  public boolean equals(Object o) {
    Building b = (Building) o;
    if (this.name.equals(b.getName())) {
      return true;
    }
    return false;
  }
}
