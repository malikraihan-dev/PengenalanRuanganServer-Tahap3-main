package com.askrida.web.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.askrida.web.service.model.ProjectModel;
import com.askrida.web.service.model.TaskItem;
import com.askrida.web.service.repository.ProjectRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepo;

    // Page
    @GetMapping("")
    public String projectPage(Model model) {
        try {
            List<ProjectModel> projects = projectRepo.getAllProjects();
            model.addAttribute("projects", projects);
            model.addAttribute("totalProjects", projects.size());
            model.addAttribute("totalInProgress", projectRepo.countProjectsByStatus("In Progress"));
            model.addAttribute("totalCompleted", projectRepo.countProjectsByStatus("Completed"));
            model.addAttribute("totalTasks", projectRepo.countTasks());
        } catch (Exception e) {
            model.addAttribute("projects", java.util.Collections.emptyList());
            model.addAttribute("totalProjects", 0);
            model.addAttribute("totalInProgress", 0);
            model.addAttribute("totalCompleted", 0);
            model.addAttribute("totalTasks", 0);
        }
        return "project";
    }

    // ===== PROJECT API =====
    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<List<ProjectModel>> getAllProjects() {
        return ResponseEntity.ok(projectRepo.getAllProjects());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ProjectModel> getProjectById(@PathVariable int id) {
        return ResponseEntity.ok(projectRepo.getProjectById(id));
    }

    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addProject(@RequestBody ProjectModel project) {
        Map<String, Object> result = new HashMap<>();
        try {
            projectRepo.addProject(project);
            result.put("success", true);
            result.put("message", "Project berhasil ditambahkan");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProject(@RequestBody ProjectModel project) {
        Map<String, Object> result = new HashMap<>();
        try {
            projectRepo.updateProject(project);
            result.put("success", true);
            result.put("message", "Project berhasil diperbarui");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteProject(@RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            projectRepo.deleteProject(body.get("idProject"));
            result.put("success", true);
            result.put("message", "Project berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // ===== TASK API =====
    @GetMapping("/api/tasks/{projectId}")
    @ResponseBody
    public ResponseEntity<List<TaskItem>> getTasksByProject(@PathVariable int projectId) {
        return ResponseEntity.ok(projectRepo.getTasksByProject(projectId));
    }

    @GetMapping("/api/tasks")
    @ResponseBody
    public ResponseEntity<List<TaskItem>> getAllTasks() {
        return ResponseEntity.ok(projectRepo.getAllTasks());
    }

    @PostMapping("/api/task/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addTask(@RequestBody TaskItem task) {
        Map<String, Object> result = new HashMap<>();
        try {
            projectRepo.addTask(task);
            result.put("success", true);
            result.put("message", "Task berhasil ditambahkan");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/task/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTask(@RequestBody TaskItem task) {
        Map<String, Object> result = new HashMap<>();
        try {
            projectRepo.updateTask(task);
            result.put("success", true);
            result.put("message", "Task berhasil diperbarui");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/task/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTaskStatus(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            int taskId = (int) body.get("idTask");
            String status = (String) body.get("statusTask");
            projectRepo.updateTaskStatus(taskId, status);
            result.put("success", true);
            result.put("message", "Status task diperbarui");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/task/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteTask(@RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            projectRepo.deleteTask(body.get("idTask"));
            result.put("success", true);
            result.put("message", "Task berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
