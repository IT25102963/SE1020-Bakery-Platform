
package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.model.Cake;
import lk.sliit.it25.bakeryweb.service.CakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cakes")
@CrossOrigin(origins = "*")
public class CakeController {

    @Autowired
    private CakeService cakeService;

    // GET all cakes
    @GetMapping
    public ResponseEntity<List<Cake>> getAllCakes() {
        return ResponseEntity.ok(cakeService.getAllCakes());
    }

    // POST add a new cake
    @PostMapping
    public ResponseEntity<?> addCake(@RequestBody Map<String, String> body) {
        String name     = body.get("name");
        String desc     = body.get("description");
        String category = body.get("category");
        String priceStr = body.get("price");

        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Name is required");
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid price");
        }

        Cake cake = cakeService.addCake(name, desc, price, category);
        return ResponseEntity.ok(cake);
    }

    // DELETE a cake by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCake(@PathVariable String id) {
        boolean deleted = cakeService.deleteCake(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Cake deleted successfully"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
