package com.askrida.web.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.askrida.web.service.model.LanguageCourse;
import com.askrida.web.service.model.Vocabulary;
import com.askrida.web.service.model.QuizResult;
import com.askrida.web.service.repository.LanguageRepository;

import java.util.*;

@Controller
@RequestMapping("/language")
public class LanguageController {

    @Autowired
    private LanguageRepository langRepo;

    // Page
    @GetMapping("")
    public String languagePage(Model model) {
        try {
            List<LanguageCourse> courses = langRepo.getAllCourses();
            model.addAttribute("courses", courses);
            model.addAttribute("totalCourses", courses.size());
            model.addAttribute("totalVocab", langRepo.countVocab());
            model.addAttribute("totalLearned", langRepo.countLearnedVocab());
            model.addAttribute("quizHistory", langRepo.getAllQuizHistory());
        } catch (Exception e) {
            model.addAttribute("courses", java.util.Collections.emptyList());
            model.addAttribute("totalCourses", 0);
            model.addAttribute("totalVocab", 0);
            model.addAttribute("totalLearned", 0);
            model.addAttribute("quizHistory", java.util.Collections.emptyList());
        }
        return "language";
    }

    // ===== COURSE API =====
    @GetMapping("/api/courses")
    @ResponseBody
    public ResponseEntity<List<LanguageCourse>> getAllCourses() {
        return ResponseEntity.ok(langRepo.getAllCourses());
    }

    @GetMapping("/api/courses/{id}")
    @ResponseBody
    public ResponseEntity<LanguageCourse> getCourseById(@PathVariable int id) {
        return ResponseEntity.ok(langRepo.getCourseById(id));
    }

    @PostMapping("/api/courses/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addCourse(@RequestBody LanguageCourse course) {
        Map<String, Object> result = new HashMap<>();
        try {
            langRepo.addCourse(course);
            result.put("success", true);
            result.put("message", "Kursus bahasa berhasil ditambahkan");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/courses/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCourse(@RequestBody LanguageCourse course) {
        Map<String, Object> result = new HashMap<>();
        try {
            langRepo.updateCourse(course);
            result.put("success", true);
            result.put("message", "Kursus berhasil diperbarui");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/courses/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCourse(@RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            langRepo.deleteCourse(body.get("idCourse"));
            result.put("success", true);
            result.put("message", "Kursus berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // ===== VOCABULARY API =====
    @GetMapping("/api/vocab/{courseId}")
    @ResponseBody
    public ResponseEntity<List<Vocabulary>> getVocab(@PathVariable int courseId) {
        return ResponseEntity.ok(langRepo.getVocabByCourse(courseId));
    }

    @PostMapping("/api/vocab/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addVocab(@RequestBody Vocabulary vocab) {
        Map<String, Object> result = new HashMap<>();
        try {
            langRepo.addVocab(vocab);
            result.put("success", true);
            result.put("message", "Kosakata berhasil ditambahkan");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/vocab/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateVocab(@RequestBody Vocabulary vocab) {
        Map<String, Object> result = new HashMap<>();
        try {
            langRepo.updateVocab(vocab);
            result.put("success", true);
            result.put("message", "Kosakata berhasil diperbarui");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/vocab/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteVocab(@RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            langRepo.deleteVocab(body.get("idVocab"));
            result.put("success", true);
            result.put("message", "Kosakata berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/vocab/mark-learned")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> markLearned(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            int idVocab = (Integer) body.get("idVocab");
            boolean learned = (Boolean) body.get("learned");
            langRepo.markLearned(idVocab, learned);
            result.put("success", true);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // ===== QUIZ API =====
    @GetMapping("/api/quiz/{courseId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getQuiz(@PathVariable int courseId, @RequestParam(defaultValue = "10") int count) {
        List<Vocabulary> vocabs = langRepo.getQuizVocab(courseId, count);
        List<Map<String, Object>> questions = new ArrayList<>();
        
        for (Vocabulary v : vocabs) {
            Map<String, Object> q = new HashMap<>();
            q.put("idVocab", v.getIdVocab());
            q.put("kata", v.getKata());
            q.put("correctAnswer", v.getTerjemahan());
            q.put("pelafalan", v.getPelafalan());
            
            // Get wrong options
            List<String> wrongOptions = langRepo.getWrongOptions(courseId, v.getIdVocab(), 3);
            List<String> options = new ArrayList<>(wrongOptions);
            options.add(v.getTerjemahan());
            Collections.shuffle(options);
            q.put("options", options);
            
            questions.add(q);
        }
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/api/quiz/submit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitQuiz(@RequestBody QuizResult quizResult) {
        Map<String, Object> result = new HashMap<>();
        try {
            int score = (int) Math.round((double) quizResult.getCorrectAnswers() / quizResult.getTotalQuestions() * 100);
            quizResult.setScore(score);
            langRepo.saveQuizResult(quizResult);
            result.put("success", true);
            result.put("score", score);
            result.put("message", score >= 80 ? "Luar biasa! Nilai Anda sangat bagus!" : 
                        score >= 60 ? "Bagus! Terus berlatih ya!" : "Tetap semangat! Coba lagi untuk hasil lebih baik.");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/api/quiz/history/{courseId}")
    @ResponseBody
    public ResponseEntity<List<QuizResult>> getQuizHistory(@PathVariable int courseId) {
        return ResponseEntity.ok(langRepo.getQuizHistory(courseId));
    }
}
