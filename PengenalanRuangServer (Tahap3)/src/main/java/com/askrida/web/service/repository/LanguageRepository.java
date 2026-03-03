package com.askrida.web.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.LanguageCourse;
import com.askrida.web.service.model.Vocabulary;
import com.askrida.web.service.model.QuizResult;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LanguageRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    // ===== COURSES =====
    public List<LanguageCourse> getAllCourses() {
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM vocabulary v WHERE v.id_course = c.id_course) AS total_vocab " +
                     "FROM language_courses c ORDER BY c.bahasa, c.level";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            LanguageCourse c = new LanguageCourse();
            c.setIdCourse(rs.getInt("id_course"));
            c.setBahasa(rs.getString("bahasa"));
            c.setLevel(rs.getString("level"));
            c.setDeskripsi(rs.getString("deskripsi"));
            c.setIconEmoji(rs.getString("icon_emoji"));
            c.setTotalVocab(rs.getInt("total_vocab"));
            c.setCreatedAt(rs.getTimestamp("created_at"));
            return c;
        });
    }

    public LanguageCourse getCourseById(int id) {
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM vocabulary v WHERE v.id_course = c.id_course) AS total_vocab " +
                     "FROM language_courses c WHERE c.id_course = ?";
        return jdbcTemplate1.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            LanguageCourse c = new LanguageCourse();
            c.setIdCourse(rs.getInt("id_course"));
            c.setBahasa(rs.getString("bahasa"));
            c.setLevel(rs.getString("level"));
            c.setDeskripsi(rs.getString("deskripsi"));
            c.setIconEmoji(rs.getString("icon_emoji"));
            c.setTotalVocab(rs.getInt("total_vocab"));
            c.setCreatedAt(rs.getTimestamp("created_at"));
            return c;
        });
    }

    public void addCourse(LanguageCourse c) throws SQLException {
        String sql = "INSERT INTO language_courses (bahasa, level, deskripsi, icon_emoji) VALUES (?, ?, ?, ?)";
        jdbcTemplate1.update(sql, new Object[]{c.getBahasa(), c.getLevel(), c.getDeskripsi(), c.getIconEmoji()});
        jdbcTemplate1.commit();
    }

    public void updateCourse(LanguageCourse c) throws SQLException {
        String sql = "UPDATE language_courses SET bahasa=?, level=?, deskripsi=?, icon_emoji=? WHERE id_course=?";
        jdbcTemplate1.update(sql, new Object[]{c.getBahasa(), c.getLevel(), c.getDeskripsi(), c.getIconEmoji(), c.getIdCourse()});
        jdbcTemplate1.commit();
    }

    public void deleteCourse(int id) throws SQLException {
        jdbcTemplate1.update("DELETE FROM language_courses WHERE id_course=?", new Object[]{id});
        jdbcTemplate1.commit();
    }

    public int countCourses() {
        try {
            Integer count = jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM language_courses", Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) { return 0; }
    }

    // ===== VOCABULARY =====
    public List<Vocabulary> getVocabByCourse(int courseId) {
        String sql = "SELECT v.*, c.bahasa AS bahasa_course FROM vocabulary v JOIN language_courses c ON v.id_course = c.id_course WHERE v.id_course = ? ORDER BY v.kategori_kata, v.kata";
        return jdbcTemplate1.query(sql, new Object[]{courseId}, (rs, rowNum) -> {
            Vocabulary v = new Vocabulary();
            v.setIdVocab(rs.getInt("id_vocab"));
            v.setIdCourse(rs.getInt("id_course"));
            v.setKata(rs.getString("kata"));
            v.setTerjemahan(rs.getString("terjemahan"));
            v.setPelafalan(rs.getString("pelafalan"));
            v.setContohKalimat(rs.getString("contoh_kalimat"));
            v.setKategoriKata(rs.getString("kategori_kata"));
            v.setLearned(rs.getBoolean("learned"));
            v.setCreatedAt(rs.getTimestamp("created_at"));
            v.setBahasaCourse(rs.getString("bahasa_course"));
            return v;
        });
    }

    public void addVocab(Vocabulary v) throws SQLException {
        String sql = "INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate1.update(sql, new Object[]{v.getIdCourse(), v.getKata(), v.getTerjemahan(), v.getPelafalan(), v.getContohKalimat(), v.getKategoriKata()});
        jdbcTemplate1.commit();
    }

    public void updateVocab(Vocabulary v) throws SQLException {
        String sql = "UPDATE vocabulary SET kata=?, terjemahan=?, pelafalan=?, contoh_kalimat=?, kategori_kata=? WHERE id_vocab=?";
        jdbcTemplate1.update(sql, new Object[]{v.getKata(), v.getTerjemahan(), v.getPelafalan(), v.getContohKalimat(), v.getKategoriKata(), v.getIdVocab()});
        jdbcTemplate1.commit();
    }

    public void deleteVocab(int id) throws SQLException {
        jdbcTemplate1.update("DELETE FROM vocabulary WHERE id_vocab=?", new Object[]{id});
        jdbcTemplate1.commit();
    }

    public void markLearned(int id, boolean learned) throws SQLException {
        jdbcTemplate1.update("UPDATE vocabulary SET learned=? WHERE id_vocab=?", new Object[]{learned, id});
        jdbcTemplate1.commit();
    }

    public int countVocab() {
        try {
            Integer count = jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM vocabulary", Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) { return 0; }
    }

    public int countLearnedVocab() {
        try {
            Integer count = jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM vocabulary WHERE learned = true", Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) { return 0; }
    }

    // Get random vocab for quiz
    public List<Vocabulary> getQuizVocab(int courseId, int limit) {
        String sql = "SELECT v.*, c.bahasa AS bahasa_course FROM vocabulary v JOIN language_courses c ON v.id_course = c.id_course WHERE v.id_course = ? ORDER BY RANDOM() LIMIT ?";
        return jdbcTemplate1.query(sql, new Object[]{courseId, limit}, (rs, rowNum) -> {
            Vocabulary v = new Vocabulary();
            v.setIdVocab(rs.getInt("id_vocab"));
            v.setIdCourse(rs.getInt("id_course"));
            v.setKata(rs.getString("kata"));
            v.setTerjemahan(rs.getString("terjemahan"));
            v.setPelafalan(rs.getString("pelafalan"));
            v.setContohKalimat(rs.getString("contoh_kalimat"));
            v.setKategoriKata(rs.getString("kategori_kata"));
            v.setLearned(rs.getBoolean("learned"));
            v.setCreatedAt(rs.getTimestamp("created_at"));
            v.setBahasaCourse(rs.getString("bahasa_course"));
            return v;
        });
    }

    // Get random wrong options for quiz
    public List<String> getWrongOptions(int courseId, int vocabId, int limit) {
        String sql = "SELECT terjemahan FROM vocabulary WHERE id_course = ? AND id_vocab != ? ORDER BY RANDOM() LIMIT ?";
        return jdbcTemplate1.query(sql, new Object[]{courseId, vocabId, limit}, (rs, rowNum) -> rs.getString("terjemahan"));
    }

    // ===== QUIZ RESULTS =====
    public void saveQuizResult(QuizResult qr) throws SQLException {
        String sql = "INSERT INTO quiz_results (id_course, total_questions, correct_answers, score) VALUES (?, ?, ?, ?)";
        jdbcTemplate1.update(sql, new Object[]{qr.getIdCourse(), qr.getTotalQuestions(), qr.getCorrectAnswers(), qr.getScore()});
        jdbcTemplate1.commit();
    }

    public List<QuizResult> getQuizHistory(int courseId) {
        String sql = "SELECT qr.*, c.bahasa AS bahasa_course FROM quiz_results qr JOIN language_courses c ON qr.id_course = c.id_course WHERE qr.id_course = ? ORDER BY qr.taken_at DESC LIMIT 20";
        return jdbcTemplate1.query(sql, new Object[]{courseId}, (rs, rowNum) -> {
            QuizResult qr = new QuizResult();
            qr.setIdQuiz(rs.getInt("id_quiz"));
            qr.setIdCourse(rs.getInt("id_course"));
            qr.setTotalQuestions(rs.getInt("total_questions"));
            qr.setCorrectAnswers(rs.getInt("correct_answers"));
            qr.setScore(rs.getInt("score"));
            qr.setTakenAt(rs.getTimestamp("taken_at"));
            qr.setBahasaCourse(rs.getString("bahasa_course"));
            return qr;
        });
    }

    public List<QuizResult> getAllQuizHistory() {
        String sql = "SELECT qr.*, c.bahasa AS bahasa_course FROM quiz_results qr JOIN language_courses c ON qr.id_course = c.id_course ORDER BY qr.taken_at DESC LIMIT 50";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            QuizResult qr = new QuizResult();
            qr.setIdQuiz(rs.getInt("id_quiz"));
            qr.setIdCourse(rs.getInt("id_course"));
            qr.setTotalQuestions(rs.getInt("total_questions"));
            qr.setCorrectAnswers(rs.getInt("correct_answers"));
            qr.setScore(rs.getInt("score"));
            qr.setTakenAt(rs.getTimestamp("taken_at"));
            qr.setBahasaCourse(rs.getString("bahasa_course"));
            return qr;
        });
    }
}
