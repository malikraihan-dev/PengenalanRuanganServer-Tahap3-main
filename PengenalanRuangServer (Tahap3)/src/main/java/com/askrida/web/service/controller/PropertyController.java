package com.askrida.web.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.askrida.web.service.model.PropertyListing;
import com.askrida.web.service.repository.PropertyRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/property")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepo;

    // Page
    @GetMapping("")
    public String propertyPage(Model model) {
        try {
            List<PropertyListing> properties = propertyRepo.getAll();
            model.addAttribute("properties", properties);
            model.addAttribute("totalProperty", properties.size());
            model.addAttribute("totalTersedia", propertyRepo.countByStatus("Tersedia"));
            model.addAttribute("totalTerjual", propertyRepo.countByStatus("Terjual"));
        } catch (Exception e) {
            model.addAttribute("properties", java.util.Collections.emptyList());
            model.addAttribute("totalProperty", 0);
            model.addAttribute("totalTersedia", 0);
            model.addAttribute("totalTerjual", 0);
        }
        return "property";
    }

    // API: Get all
    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<List<PropertyListing>> getAll() {
        return ResponseEntity.ok(propertyRepo.getAll());
    }

    // API: Get by ID
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<PropertyListing> getById(@PathVariable int id) {
        return ResponseEntity.ok(propertyRepo.getById(id));
    }

    // API: Search by kota
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<PropertyListing>> search(@RequestParam String kota) {
        return ResponseEntity.ok(propertyRepo.searchByKota(kota));
    }

    // API: Add property
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addProperty(@RequestBody PropertyListing property) {
        Map<String, Object> result = new HashMap<>();
        try {
            propertyRepo.addProperty(property);
            result.put("success", true);
            result.put("message", "Properti berhasil ditambahkan");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal menambahkan properti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // API: Update property
    @PostMapping("/api/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProperty(@RequestBody PropertyListing property) {
        Map<String, Object> result = new HashMap<>();
        try {
            propertyRepo.updateProperty(property);
            result.put("success", true);
            result.put("message", "Properti berhasil diperbarui");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal memperbarui properti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // API: Delete property
    @PostMapping("/api/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteProperty(@RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            propertyRepo.deleteProperty(body.get("idProperty"));
            result.put("success", true);
            result.put("message", "Properti berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal menghapus properti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
