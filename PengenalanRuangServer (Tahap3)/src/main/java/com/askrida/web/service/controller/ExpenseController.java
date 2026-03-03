package com.askrida.web.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.askrida.web.service.model.ExpenseReport;
import com.askrida.web.service.model.ExpenseItem;
import com.askrida.web.service.repository.ExpenseRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepo;

    // Page
    @GetMapping("")
    public String expensePage(Model model) {
        try {
            List<ExpenseReport> reports = expenseRepo.getAllReports();
            model.addAttribute("reports", reports);
            model.addAttribute("totalReports", reports.size());
            model.addAttribute("totalDraft", expenseRepo.countByStatus("Draft"));
            model.addAttribute("totalApproved", expenseRepo.countByStatus("Approved"));
            model.addAttribute("totalExpense", expenseRepo.getTotalAllExpenses());
        } catch (Exception e) {
            model.addAttribute("reports", java.util.Collections.emptyList());
            model.addAttribute("totalReports", 0);
            model.addAttribute("totalDraft", 0);
            model.addAttribute("totalApproved", 0);
            model.addAttribute("totalExpense", java.math.BigDecimal.ZERO);
        }
        return "expense";
    }

    // ===== REPORT API =====
    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<List<ExpenseReport>> getAllReports() {
        return ResponseEntity.ok(expenseRepo.getAllReports());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ExpenseReport> getReportById(@PathVariable int id) {
        return ResponseEntity.ok(expenseRepo.getReportById(id));
    }

    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addReport(@RequestBody ExpenseReport report) {
        Map<String, Object> result = new HashMap<>();
        try {
            expenseRepo.addReport(report);
            result.put("success", true);
            result.put("message", "Laporan pengeluaran berhasil ditambahkan");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateReport(@RequestBody ExpenseReport report) {
        Map<String, Object> result = new HashMap<>();
        try {
            expenseRepo.updateReport(report);
            result.put("success", true);
            result.put("message", "Laporan berhasil diperbarui");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteReport(@RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            expenseRepo.deleteReport(body.get("idReport"));
            result.put("success", true);
            result.put("message", "Laporan berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // ===== ITEMS API =====
    @GetMapping("/api/{id}/items")
    @ResponseBody
    public ResponseEntity<List<ExpenseItem>> getItemsByReport(@PathVariable int id) {
        return ResponseEntity.ok(expenseRepo.getItemsByReport(id));
    }

    @PostMapping("/api/items/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addItem(@RequestBody ExpenseItem item) {
        Map<String, Object> result = new HashMap<>();
        try {
            expenseRepo.addItem(item);
            result.put("success", true);
            result.put("message", "Item pengeluaran berhasil ditambahkan");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/items/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateItem(@RequestBody ExpenseItem item) {
        Map<String, Object> result = new HashMap<>();
        try {
            expenseRepo.updateItem(item);
            result.put("success", true);
            result.put("message", "Item berhasil diperbarui");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/items/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteItem(@RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            expenseRepo.deleteItem(body.get("idItem"));
            result.put("success", true);
            result.put("message", "Item berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // Summary by category
    @GetMapping("/api/{id}/summary")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getSummary(@PathVariable int id) {
        return ResponseEntity.ok(expenseRepo.getSummaryByCategory(id));
    }
}
