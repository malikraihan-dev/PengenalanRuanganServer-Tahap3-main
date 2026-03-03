package com.askrida.web.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.ChatMessage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ChatRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    public List<ChatMessage> getMessagesBySession(String sessionId) {
        String sql = "SELECT * FROM chat_messages WHERE session_id = ? ORDER BY created_at ASC";
        return jdbcTemplate1.query(sql, new Object[]{sessionId}, (rs, rowNum) -> {
            ChatMessage msg = new ChatMessage();
            msg.setIdMessage(rs.getInt("id_message"));
            msg.setSessionId(rs.getString("session_id"));
            msg.setSender(rs.getString("sender"));
            msg.setMessage(rs.getString("message"));
            msg.setCreatedAt(rs.getTimestamp("created_at"));
            return msg;
        });
    }

    public void saveMessage(String sessionId, String sender, String message) throws SQLException {
        String sql = "INSERT INTO chat_messages (session_id, sender, message) VALUES (?, ?, ?)";
        jdbcTemplate1.update(sql, new Object[]{sessionId, sender, message});
        jdbcTemplate1.commit();
    }

    public String findBotResponse(String userMessage) {
        String lowerMsg = userMessage.toLowerCase().trim();
        String sql = "SELECT response FROM chatbot_responses WHERE LOWER(?) LIKE '%' || LOWER(keyword) || '%' ORDER BY LENGTH(keyword) DESC LIMIT 1";
        try {
            return jdbcTemplate1.queryForObject(sql, new Object[]{lowerMsg}, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String generateBotReply(String userMessage) {
        String response = findBotResponse(userMessage);
        if (response != null) {
            return response;
        }
        // Default responses based on simple pattern matching
        String lower = userMessage.toLowerCase().trim();
        if (lower.contains("apa kabar")) {
            return "Saya baik! Terima kasih sudah bertanya. Ada yang bisa saya bantu?";
        } else if (lower.contains("siapa")) {
            return "Saya adalah asisten virtual AbsensiKampus. Saya bisa membantu Anda dengan informasi tentang sistem ini.";
        } else if (lower.contains("fitur")) {
            return "Sistem kami memiliki beberapa fitur:\n1. Monitoring Absensi\n2. Kelola Anggota\n3. Real Estate Listing\n4. Project Management\n5. Chatbot (sedang Anda gunakan!)\nSilakan pilih menu di sidebar untuk mengakses fitur.";
        } else if (lower.length() < 3) {
            return "Mohon ketik pesan yang lebih lengkap agar saya bisa membantu Anda.";
        }
        return "Maaf, saya belum bisa memahami pertanyaan Anda. Coba tanyakan tentang: absensi, ruangan, properti, project, bantuan, atau ketik 'help' untuk melihat daftar topik.";
    }

    public void deleteSessionMessages(String sessionId) throws SQLException {
        String sql = "DELETE FROM chat_messages WHERE session_id=?";
        jdbcTemplate1.update(sql, new Object[]{sessionId});
        jdbcTemplate1.commit();
    }

    public List<String> getAllSessions() {
        String sql = "SELECT DISTINCT session_id FROM chat_messages ORDER BY session_id";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> rs.getString("session_id"));
    }
}
