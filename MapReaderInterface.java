import java.io.FileNotFoundException;

public interface MapReaderInterface {
  public DijkstraGraph<BuildingInterface, Double> read(String filename) throws FileNotFoundException;
}
