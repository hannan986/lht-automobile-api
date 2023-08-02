package automobile.project.VehicleRepository;

import automobile.project.vehicleModel.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByModel(String model);
    List<Vehicle> findByName(String name);
    List<Vehicle> findByPrice(float price);
}
