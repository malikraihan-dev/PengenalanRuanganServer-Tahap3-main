package com.askrida.web.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.ProjectModel;
import com.askrida.web.service.model.TaskItem;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProjectRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    // ===== PROJECT CRUD =====
    public List<ProjectModel> getAllProjects() {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM project_tasks t WHERE t.id_project = p.id_project) AS total_tasks, " +
                "(SELECT COUNT(*) FROM project_tasks t WHERE t.id_project = p.id_project AND t.status_task = 'Done') AS completed_tasks " +
                "FROM projects p ORDER BY p.created_at DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            ProjectModel m = new ProjectModel();
            m.setIdProject(rs.getInt("id_project"));
            m.setNamaProject(rs.getString("nama_project"));
            m.setDeskripsi(rs.getString("deskripsi"));
            m.setStatusProject(rs.getString("status_project"));
            m.setPrioritas(rs.getString("prioritas"));
            m.setTanggalMulai(rs.getString("tanggal_mulai"));
            m.setTanggalSelesai(rs.getString("tanggal_selesai"));
            m.setCreatedAt(rs.getTimestamp("created_at"));
            m.setTotalTasks(rs.getInt("total_tasks"));
            m.setCompletedTasks(rs.getInt("completed_tasks"));
            return m;
        });
    }

    public ProjectModel getProjectById(int id) {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM project_tasks t WHERE t.id_project = p.id_project) AS total_tasks, " +
                "(SELECT COUNT(*) FROM project_tasks t WHERE t.id_project = p.id_project AND t.status_task = 'Done') AS completed_tasks " +
                "FROM projects p WHERE p.id_project = ?";
        return jdbcTemplate1.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            ProjectModel m = new ProjectModel();
            m.setIdProject(rs.getInt("id_project"));
            m.setNamaProject(rs.getString("nama_project"));
            m.setDeskripsi(rs.getString("deskripsi"));
            m.setStatusProject(rs.getString("status_project"));
            m.setPrioritas(rs.getString("prioritas"));
            m.setTanggalMulai(rs.getString("tanggal_mulai"));
            m.setTanggalSelesai(rs.getString("tanggal_selesai"));
            m.setCreatedAt(rs.getTimestamp("created_at"));
            m.setTotalTasks(rs.getInt("total_tasks"));
            m.setCompletedTasks(rs.getInt("completed_tasks"));
            return m;
        });
    }

    public void addProject(ProjectModel p) throws SQLException {
        String sql = "INSERT INTO projects (nama_project, deskripsi, status_project, prioritas, tanggal_mulai, tanggal_selesai) VALUES (?, ?, ?, ?, ?::DATE, ?::DATE)";
        Object[] params = new Object[]{
            p.getNamaProject(), p.getDeskripsi(),
            p.getStatusProject() != null ? p.getStatusProject() : "Planning",
            p.getPrioritas() != null ? p.getPrioritas() : "Medium",
            p.getTanggalMulai(), p.getTanggalSelesai()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void updateProject(ProjectModel p) throws SQLException {
        String sql = "UPDATE projects SET nama_project=?, deskripsi=?, status_project=?, prioritas=?, tanggal_mulai=?::DATE, tanggal_selesai=?::DATE, updated_at=? WHERE id_project=?";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Object[] params = new Object[]{
            p.getNamaProject(), p.getDeskripsi(), p.getStatusProject(), p.getPrioritas(),
            p.getTanggalMulai(), p.getTanggalSelesai(), now, p.getIdProject()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void deleteProject(int id) throws SQLException {
        String sql = "DELETE FROM projects WHERE id_project=?";
        jdbcTemplate1.update(sql, new Object[]{id});
        jdbcTemplate1.commit();
    }

    // ===== TASK CRUD =====
    public List<TaskItem> getTasksByProject(int projectId) {
        String sql = "SELECT t.*, p.nama_project FROM project_tasks t JOIN projects p ON t.id_project = p.id_project WHERE t.id_project = ? ORDER BY t.created_at DESC";
        return jdbcTemplate1.query(sql, new Object[]{projectId}, (rs, rowNum) -> {
            TaskItem task = new TaskItem();
            task.setIdTask(rs.getInt("id_task"));
            task.setIdProject(rs.getInt("id_project"));
            task.setJudulTask(rs.getString("judul_task"));
            task.setDeskripsi(rs.getString("deskripsi"));
            task.setStatusTask(rs.getString("status_task"));
            task.setPrioritas(rs.getString("prioritas"));
            task.setAssignee(rs.getString("assignee"));
            task.setTanggalDeadline(rs.getString("tanggal_deadline"));
            task.setCreatedAt(rs.getTimestamp("created_at"));
            task.setNamaProject(rs.getString("nama_project"));
            return task;
        });
    }

    public List<TaskItem> getAllTasks() {
        String sql = "SELECT t.*, p.nama_project FROM project_tasks t JOIN projects p ON t.id_project = p.id_project ORDER BY t.created_at DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            TaskItem task = new TaskItem();
            task.setIdTask(rs.getInt("id_task"));
            task.setIdProject(rs.getInt("id_project"));
            task.setJudulTask(rs.getString("judul_task"));
            task.setDeskripsi(rs.getString("deskripsi"));
            task.setStatusTask(rs.getString("status_task"));
            task.setPrioritas(rs.getString("prioritas"));
            task.setAssignee(rs.getString("assignee"));
            task.setTanggalDeadline(rs.getString("tanggal_deadline"));
            task.setCreatedAt(rs.getTimestamp("created_at"));
            task.setNamaProject(rs.getString("nama_project"));
            return task;
        });
    }

    public void addTask(TaskItem task) throws SQLException {
        String sql = "INSERT INTO project_tasks (id_project, judul_task, deskripsi, status_task, prioritas, assignee, tanggal_deadline) VALUES (?, ?, ?, ?, ?, ?, ?::DATE)";
        Object[] params = new Object[]{
            task.getIdProject(), task.getJudulTask(), task.getDeskripsi(),
            task.getStatusTask() != null ? task.getStatusTask() : "To Do",
            task.getPrioritas() != null ? task.getPrioritas() : "Medium",
            task.getAssignee(), task.getTanggalDeadline()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void updateTask(TaskItem task) throws SQLException {
        String sql = "UPDATE project_tasks SET judul_task=?, deskripsi=?, status_task=?, prioritas=?, assignee=?, tanggal_deadline=?::DATE, updated_at=? WHERE id_task=?";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Object[] params = new Object[]{
            task.getJudulTask(), task.getDeskripsi(), task.getStatusTask(),
            task.getPrioritas(), task.getAssignee(), task.getTanggalDeadline(), now, task.getIdTask()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void updateTaskStatus(int taskId, String status) throws SQLException {
        String sql = "UPDATE project_tasks SET status_task=?, updated_at=? WHERE id_task=?";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate1.update(sql, new Object[]{status, now, taskId});
        jdbcTemplate1.commit();
    }

    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM project_tasks WHERE id_task=?";
        jdbcTemplate1.update(sql, new Object[]{id});
        jdbcTemplate1.commit();
    }

    // ===== STATISTICS =====
    public int countProjects() {
        return jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM projects", Integer.class);
    }

    public int countProjectsByStatus(String status) {
        return jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM projects WHERE status_project=?", new Object[]{status}, Integer.class);
    }

    public int countTasks() {
        return jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM project_tasks", Integer.class);
    }

    public int countTasksByStatus(String status) {
        return jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM project_tasks WHERE status_task=?", new Object[]{status}, Integer.class);
    }
}
