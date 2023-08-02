package automobile.project.VehicleController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import automobile.project.VehicleRepository.VehicleRepository;
import automobile.project.vehicleModel.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleRepository vehicleRepository;
    private final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @PostMapping
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        logger.info("User added a new vehicle. Model: {}, Name: {}, Price: {}, Visibility: {}",
                vehicle.getModel(), vehicle.getName(), vehicle.getPrice(), vehicle.getIsVisible());
        return vehicleRepository.save(vehicle);
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        logger.info("User requested vehicle with ID: {}", id);

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        return optionalVehicle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle updatedVehicle) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setModel(updatedVehicle.getModel());
            vehicle.setName(updatedVehicle.getName());
            vehicle.setPrice(updatedVehicle.getPrice());
            vehicle.setIsVisible(updatedVehicle.getIsVisible());
            vehicleRepository.save(vehicle);
            return ResponseEntity.ok(vehicle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        logger.info("User deleted vehicle with ID: {}", id);

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isPresent()) {
            vehicleRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<Vehicle> searchVehicles(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Float price
    ) {
        if (model != null) {
            return vehicleRepository.findByModel(model);
        } else if (name != null) {
            return vehicleRepository.findByName(name);
        } else if (price != null) {
            return vehicleRepository.findByPrice(price);
        } else {
            return vehicleRepository.findAll();
        }
    }
}

