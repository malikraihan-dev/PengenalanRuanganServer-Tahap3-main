package com.askrida.web.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.askrida.web.service.model.ChatMessage;
import com.askrida.web.service.repository.ChatRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/chatbot")
public class ChatController {

    @Autowired
    private ChatRepository chatRepo;

    // Page
    @GetMapping("")
    public String chatPage(Model model) {
        return "chatbot";
    }

    // API: Send message and get bot reply
    @PostMapping("/api/send")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            String sessionId = body.get("sessionId");
            String message = body.get("message");

            if (sessionId == null || sessionId.isEmpty()) {
                sessionId = UUID.randomUUID().toString().substring(0, 8);
            }

            // Save user message
            chatRepo.saveMessage(sessionId, "user", message);

            // Generate and save bot reply
            String botReply = chatRepo.generateBotReply(message);
            chatRepo.saveMessage(sessionId, "bot", botReply);

            result.put("success", true);
            result.put("sessionId", sessionId);
            result.put("userMessage", message);
            result.put("botReply", botReply);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("botReply", "Maaf, terjadi kesalahan pada sistem. Silakan coba lagi.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // API: Get chat history
    @GetMapping("/api/history/{sessionId}")
    @ResponseBody
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable String sessionId) {
        return ResponseEntity.ok(chatRepo.getMessagesBySession(sessionId));
    }

    // API: Clear chat
    @PostMapping("/api/clear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearChat(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            chatRepo.deleteSessionMessages(body.get("sessionId"));
            result.put("success", true);
            result.put("message", "Chat berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal menghapus chat");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // API: Get all sessions
    @GetMapping("/api/sessions")
    @ResponseBody
    public ResponseEntity<List<String>> getSessions() {
        return ResponseEntity.ok(chatRepo.getAllSessions());
    }
}
