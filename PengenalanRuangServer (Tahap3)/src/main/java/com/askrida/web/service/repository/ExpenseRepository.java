package com.askrida.web.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.ExpenseReport;
import com.askrida.web.service.model.ExpenseItem;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Repository
public class ExpenseRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    // ===== EXPENSE REPORTS =====
    public List<ExpenseReport> getAllReports() {
        String sql = "SELECT r.*, " +
                "(SELECT COALESCE(SUM(i.jumlah), 0) FROM expense_items i WHERE i.id_report = r.id_report) AS total_amount, " +
                "(SELECT COUNT(*) FROM expense_items i WHERE i.id_report = r.id_report) AS total_items " +
                "FROM expense_reports r ORDER BY r.created_at DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            ExpenseReport rpt = new ExpenseReport();
            rpt.setIdReport(rs.getInt("id_report"));
            rpt.setJudulReport(rs.getString("judul_report"));
            rpt.setDeskripsi(rs.getString("deskripsi"));
            rpt.setPeriode(rs.getString("periode"));
            rpt.setStatusReport(rs.getString("status_report"));
            rpt.setTotalAmount(rs.getBigDecimal("total_amount"));
            rpt.setTotalItems(rs.getInt("total_items"));
            rpt.setCreatedAt(rs.getTimestamp("created_at"));
            rpt.setUpdatedAt(rs.getTimestamp("updated_at"));
            return rpt;
        });
    }

    public ExpenseReport getReportById(int id) {
        String sql = "SELECT r.*, " +
                "(SELECT COALESCE(SUM(i.jumlah), 0) FROM expense_items i WHERE i.id_report = r.id_report) AS total_amount, " +
                "(SELECT COUNT(*) FROM expense_items i WHERE i.id_report = r.id_report) AS total_items " +
                "FROM expense_reports r WHERE r.id_report = ?";
        return jdbcTemplate1.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            ExpenseReport rpt = new ExpenseReport();
            rpt.setIdReport(rs.getInt("id_report"));
            rpt.setJudulReport(rs.getString("judul_report"));
            rpt.setDeskripsi(rs.getString("deskripsi"));
            rpt.setPeriode(rs.getString("periode"));
            rpt.setStatusReport(rs.getString("status_report"));
            rpt.setTotalAmount(rs.getBigDecimal("total_amount"));
            rpt.setTotalItems(rs.getInt("total_items"));
            rpt.setCreatedAt(rs.getTimestamp("created_at"));
            rpt.setUpdatedAt(rs.getTimestamp("updated_at"));
            return rpt;
        });
    }

    public void addReport(ExpenseReport r) throws SQLException {
        String sql = "INSERT INTO expense_reports (judul_report, deskripsi, periode, status_report) VALUES (?, ?, ?, ?)";
        Object[] params = new Object[]{
            r.getJudulReport(), r.getDeskripsi(), r.getPeriode(),
            r.getStatusReport() != null ? r.getStatusReport() : "Draft"
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void updateReport(ExpenseReport r) throws SQLException {
        String sql = "UPDATE expense_reports SET judul_report=?, deskripsi=?, periode=?, status_report=?, updated_at=? WHERE id_report=?";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Object[] params = new Object[]{
            r.getJudulReport(), r.getDeskripsi(), r.getPeriode(), r.getStatusReport(), now, r.getIdReport()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void deleteReport(int id) throws SQLException {
        jdbcTemplate1.update("DELETE FROM expense_reports WHERE id_report=?", new Object[]{id});
        jdbcTemplate1.commit();
    }

    public int countReports() {
        try {
            Integer count = jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM expense_reports", Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) { return 0; }
    }

    public int countByStatus(String status) {
        try {
            Integer count = jdbcTemplate1.queryForObject(
                "SELECT COUNT(*) FROM expense_reports WHERE status_report = ?",
                new Object[]{status}, Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) { return 0; }
    }

    public BigDecimal getTotalAllExpenses() {
        try {
            BigDecimal total = jdbcTemplate1.queryForObject(
                "SELECT COALESCE(SUM(jumlah), 0) FROM expense_items", BigDecimal.class);
            return total != null ? total : BigDecimal.ZERO;
        } catch (Exception e) { return BigDecimal.ZERO; }
    }

    // ===== EXPENSE ITEMS =====
    public List<ExpenseItem> getItemsByReport(int reportId) {
        String sql = "SELECT * FROM expense_items WHERE id_report = ? ORDER BY tanggal DESC, created_at DESC";
        return jdbcTemplate1.query(sql, new Object[]{reportId}, (rs, rowNum) -> {
            ExpenseItem item = new ExpenseItem();
            item.setIdItem(rs.getInt("id_item"));
            item.setIdReport(rs.getInt("id_report"));
            item.setNamaItem(rs.getString("nama_item"));
            item.setKategori(rs.getString("kategori"));
            item.setJumlah(rs.getBigDecimal("jumlah"));
            item.setTanggal(rs.getString("tanggal"));
            item.setKeterangan(rs.getString("keterangan"));
            item.setBuktiUrl(rs.getString("bukti_url"));
            item.setCreatedAt(rs.getTimestamp("created_at"));
            return item;
        });
    }

    public void addItem(ExpenseItem item) throws SQLException {
        String sql = "INSERT INTO expense_items (id_report, nama_item, kategori, jumlah, tanggal, keterangan, bukti_url) VALUES (?, ?, ?, ?, ?::DATE, ?, ?)";
        Object[] params = new Object[]{
            item.getIdReport(), item.getNamaItem(), item.getKategori(),
            item.getJumlah(), item.getTanggal(), item.getKeterangan(), item.getBuktiUrl()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void updateItem(ExpenseItem item) throws SQLException {
        String sql = "UPDATE expense_items SET nama_item=?, kategori=?, jumlah=?, tanggal=?::DATE, keterangan=?, bukti_url=? WHERE id_item=?";
        Object[] params = new Object[]{
            item.getNamaItem(), item.getKategori(), item.getJumlah(),
            item.getTanggal(), item.getKeterangan(), item.getBuktiUrl(), item.getIdItem()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void deleteItem(int id) throws SQLException {
        jdbcTemplate1.update("DELETE FROM expense_items WHERE id_item=?", new Object[]{id});
        jdbcTemplate1.commit();
    }

    // Summary by category for a report
    public List<Map<String, Object>> getSummaryByCategory(int reportId) {
        String sql = "SELECT kategori, COUNT(*) as jumlah_item, SUM(jumlah) as total FROM expense_items WHERE id_report = ? GROUP BY kategori ORDER BY total DESC";
        return jdbcTemplate1.query(sql, new Object[]{reportId}, (rs, rowNum) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("kategori", rs.getString("kategori"));
            map.put("jumlahItem", rs.getInt("jumlah_item"));
            map.put("total", rs.getBigDecimal("total"));
            return map;
        });
    }
}
