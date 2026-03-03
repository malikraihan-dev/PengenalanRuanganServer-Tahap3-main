-- ================================================================
-- SERVER ROOM MONITORING SYSTEM - DATABASE SCHEMA
-- Face Recognition Based Access Control
-- ================================================================

-- 1. TABEL SERVER USERS (User dengan Role-Based Access)
CREATE TABLE IF NOT EXISTS server_users (
    id SERIAL PRIMARY KEY,
    nim VARCHAR(100) NOT NULL UNIQUE,
    nama VARCHAR(250) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    password_hash VARCHAR(255),
    face_embedding TEXT,
    photo_url TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_server_users_nim ON server_users(nim);
CREATE INDEX IF NOT EXISTS idx_server_users_role ON server_users(role);

-- 2. TABEL ACCESS LOG (Log Aktivitas Akses Ruang Server)
CREATE TABLE IF NOT EXISTS access_log (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES server_users(id) ON DELETE SET NULL,
    nim VARCHAR(100),
    nama VARCHAR(250),
    role VARCHAR(20),
    status VARCHAR(20) NOT NULL CHECK (status IN ('GRANTED', 'DENIED')),
    confidence DECIMAL(5,4),
    method VARCHAR(50) DEFAULT 'face_recognition',
    ip_address VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_access_log_created ON access_log(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_access_log_status ON access_log(status);
CREATE INDEX IF NOT EXISTS idx_access_log_nim ON access_log(nim);

-- 3. TABEL SERVER FACE DATA (Embedding Wajah untuk Monitoring)
CREATE TABLE IF NOT EXISTS server_face_data (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES server_users(id) ON DELETE CASCADE,
    nim VARCHAR(100) NOT NULL,
    label VARCHAR(250) NOT NULL,
    face_descriptor TEXT NOT NULL,
    image_data TEXT,
    quality_score DECIMAL(5,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_server_face_nim ON server_face_data(nim);
CREATE INDEX IF NOT EXISTS idx_server_face_user ON server_face_data(user_id);

-- 4. TABEL NOTIFICATIONS (Notifikasi Sistem)
CREATE TABLE IF NOT EXISTS monitor_notifications (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(20) DEFAULT 'INFO' CHECK (type IN ('INFO', 'WARNING', 'DANGER', 'SUCCESS')),
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================================================
-- SAMPLE DATA
-- ================================================================

INSERT INTO server_users (nim, nama, role, is_active) VALUES
('12345', 'Ahmad Malik', 'ADMIN', true),
('12346', 'Budi Santoso', 'USER', true),
('12347', 'Citra Dewi', 'USER', true)
ON CONFLICT (nim) DO NOTHING;

-- Sample access logs
INSERT INTO access_log (nim, nama, role, status, confidence, method, notes) VALUES
('12345', 'Ahmad Malik', 'ADMIN', 'GRANTED', 0.9500, 'face_recognition', 'Akses rutin pagi'),
('12346', 'Budi Santoso', 'USER', 'GRANTED', 0.8900, 'face_recognition', 'Maintenance server'),
('12347', 'Citra Dewi', 'USER', 'GRANTED', 0.9200, 'face_recognition', 'Pengecekan jaringan'),
('12345', 'Ahmad Malik', 'ADMIN', 'GRANTED', 0.9700, 'face_recognition', 'Update sistem'),
(NULL, 'Unknown', NULL, 'DENIED', 0.3500, 'face_recognition', 'Wajah tidak dikenali');

-- Sample notifications
INSERT INTO monitor_notifications (title, message, type) VALUES
('Sistem Aktif', 'Server Room Monitoring System telah aktif', 'SUCCESS'),
('User Baru', 'Ahmad Malik terdaftar sebagai ADMIN', 'INFO'),
('Akses Ditolak', 'Percobaan akses oleh wajah tidak dikenal', 'DANGER');
