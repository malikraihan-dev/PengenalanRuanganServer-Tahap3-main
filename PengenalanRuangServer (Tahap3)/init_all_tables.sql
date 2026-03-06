-- ================================================================
-- CORE TABLES (Attendance System)
-- ================================================================

CREATE TABLE IF NOT EXISTS divisi (
    id_divisi SERIAL PRIMARY KEY,
    nama_divisi VARCHAR(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS anggota (
    nim VARCHAR(100) PRIMARY KEY,
    nama_lengkap VARCHAR(250) NOT NULL,
    kelas VARCHAR(100),
    id_divisi INT REFERENCES divisi(id_divisi)
);

CREATE TABLE IF NOT EXISTS ruangan (
    id_ruangan SERIAL PRIMARY KEY,
    nama_ruangan VARCHAR(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS absensi_divisi (
    id_absensi SERIAL PRIMARY KEY,
    nim_anggota VARCHAR(100) NOT NULL REFERENCES anggota(nim),
    id_ruangan INT NOT NULL REFERENCES ruangan(id_ruangan),
    jam_masuk TIMESTAMP,
    jam_keluar TIMESTAMP
);

-- Sample divisi
INSERT INTO divisi (nama_divisi) VALUES
('Divisi IT'),
('Divisi Administrasi'),
('Divisi Keuangan'),
('Divisi Umum');

-- Sample ruangan (4 rooms as referenced by random 1-4)
INSERT INTO ruangan (nama_ruangan) VALUES
('Ruang TU 1'),
('Ruang TU 2'),
('Ruang TU 3'),
('Ruang TU 4');

-- Sample anggota
INSERT INTO anggota (nim, nama_lengkap, kelas, id_divisi) VALUES
('12345', 'Ahmad Malik', 'TI-1A', 1),
('12346', 'Budi Santoso', 'TI-1B', 2),
('12347', 'Citra Dewi', 'TI-2A', 1);

-- ================================================================
-- LEGACY TABLE (restexamplecrud)
-- ================================================================
CREATE TABLE IF NOT EXISTS restexamplecrud 
(id serial PRIMARY KEY NOT NULL,
key varchar(100) NOT NULL,
value varchar(250) NOT NULL,
rand smallint NOT NULL,
nama varchar(250) NOT NULL,
waktu_input varchar (250) NOT NULL 
);

-- ================================================================
-- FACE RECOGNITION TABLES
-- ================================================================
CREATE TABLE IF NOT EXISTS face_data (
    id_face SERIAL PRIMARY KEY,
    nim VARCHAR(100) NOT NULL,
    label VARCHAR(250) NOT NULL,
    face_descriptor TEXT NOT NULL,
    image_data TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_face_anggota FOREIGN KEY (nim) REFERENCES anggota(nim) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_face_data_nim ON face_data(nim);

CREATE TABLE IF NOT EXISTS face_absensi_log (
    id_log SERIAL PRIMARY KEY,
    nim VARCHAR(100) NOT NULL,
    confidence DECIMAL(5,4),
    method VARCHAR(50) DEFAULT 'face_recognition',
    created_at TIMESTAMP DEFAULT NOW()
);

-- ================================================================
-- FEATURE TABLES
-- ================================================================

-- 6. PROJECT MANAGEMENT
CREATE TABLE IF NOT EXISTS projects (
    id_project SERIAL PRIMARY KEY,
    nama_project VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    status_project VARCHAR(30) DEFAULT 'Planning',
    prioritas VARCHAR(20) DEFAULT 'Medium',
    tanggal_mulai DATE,
    tanggal_selesai DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS project_tasks (
    id_task SERIAL PRIMARY KEY,
    id_project INT NOT NULL REFERENCES projects(id_project) ON DELETE CASCADE,
    judul_task VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    status_task VARCHAR(30) DEFAULT 'To Do',
    prioritas VARCHAR(20) DEFAULT 'Medium',
    assignee VARCHAR(255),
    tanggal_deadline DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. CHATBOT
CREATE TABLE IF NOT EXISTS chat_messages (
    id_message SERIAL PRIMARY KEY,
    session_id VARCHAR(100) NOT NULL,
    sender VARCHAR(10) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS chatbot_responses (
    id_response SERIAL PRIMARY KEY,
    keyword VARCHAR(100) NOT NULL,
    response TEXT NOT NULL,
    kategori VARCHAR(50)
);

INSERT INTO chatbot_responses (keyword, response, kategori) VALUES
('halo', 'Halo! Selamat datang. Ada yang bisa saya bantu?', 'greeting'),
('hai', 'Hai! Ada yang bisa saya bantu hari ini?', 'greeting'),
('selamat pagi', 'Selamat pagi! Semoga hari Anda menyenangkan. Ada yang bisa dibantu?', 'greeting'),
('selamat siang', 'Selamat siang! Ada yang bisa saya bantu?', 'greeting'),
('selamat sore', 'Selamat sore! Ada yang bisa saya bantu?', 'greeting'),
('selamat malam', 'Selamat malam! Ada yang bisa saya bantu?', 'greeting'),
('absensi', 'Untuk melakukan absensi, silakan kunjungi halaman Dashboard dan gunakan fitur Absensi Cepat dengan memasukkan NIM Anda.', 'info'),
('ruangan', 'Terdapat 4 ruangan yang tersedia untuk absensi. Anda bisa melihat detail ruangan di halaman Dashboard.', 'info'),
('properti', 'Untuk melihat daftar properti, silakan kunjungi halaman Real Estate Listing di sidebar menu.', 'info'),
('project', 'Untuk mengelola project, silakan kunjungi halaman Project Management di sidebar menu.', 'info'),
('bantuan', 'Saya bisa membantu Anda dengan informasi tentang: absensi, ruangan, properti, project, dan lainnya. Silakan tanyakan!', 'help'),
('help', 'Berikut yang bisa saya bantu:\n1. Informasi Absensi\n2. Info Ruangan\n3. Info Properti\n4. Info Project Management\n5. Bantuan Umum\nSilakan ketik topik yang ingin ditanyakan!', 'help'),
('terima kasih', 'Sama-sama! Jangan ragu untuk bertanya lagi ya.', 'greeting'),
('makasih', 'Sama-sama! Semoga membantu.', 'greeting'),
('bye', 'Sampai jumpa! Semoga hari Anda menyenangkan.', 'greeting'),
('jam', 'Jam operasional kampus adalah Senin-Jumat pukul 07:00 - 17:00 WIB dan Sabtu pukul 07:00 - 12:00 WIB.', 'info'),
('kontak', 'Untuk informasi kontak, silakan hubungi bagian TU di ext. 123 atau email tu@kampus.ac.id', 'info');

-- 9. EXPENSE REPORT
CREATE TABLE IF NOT EXISTS expense_reports (
    id_report SERIAL PRIMARY KEY,
    judul_report VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    periode VARCHAR(50),
    status_report VARCHAR(20) DEFAULT 'Draft',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS expense_items (
    id_item SERIAL PRIMARY KEY,
    id_report INT NOT NULL REFERENCES expense_reports(id_report) ON DELETE CASCADE,
    nama_item VARCHAR(255) NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    jumlah DECIMAL(15,2) NOT NULL,
    tanggal DATE,
    keterangan TEXT,
    bukti_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO expense_reports (judul_report, deskripsi, periode, status_report) VALUES
('Operasional Januari 2026', 'Pengeluaran operasional bulan Januari', 'Januari 2026', 'Approved'),
('Perjalanan Dinas Surabaya', 'Biaya perjalanan dinas ke Surabaya', 'Februari 2026', 'Submitted'),
('Kebutuhan ATK Q1', 'Pembelian alat tulis kantor kuartal pertama', 'Q1 2026', 'Draft');

INSERT INTO expense_items (id_report, nama_item, kategori, jumlah, tanggal, keterangan) VALUES
(1, 'Listrik Kantor', 'Supplies', 2500000, '2026-01-05', 'Tagihan listrik bulan Januari'),
(1, 'Internet', 'Supplies', 1500000, '2026-01-05', 'Tagihan internet kantor'),
(1, 'Makan Siang Rapat', 'Makan', 750000, '2026-01-15', 'Catering rapat bulanan 30 orang'),
(1, 'Transport Kurir', 'Transport', 350000, '2026-01-20', 'Biaya antar dokumen'),
(2, 'Tiket Pesawat PP', 'Transport', 2800000, '2026-02-01', 'Jakarta-Surabaya pulang pergi'),
(2, 'Hotel 2 Malam', 'Akomodasi', 1600000, '2026-02-02', 'Hotel bintang 3 dekat kantor cabang'),
(2, 'Makan 3 Hari', 'Makan', 450000, '2026-02-02', 'Uang makan selama dinas'),
(2, 'Taxi Bandara', 'Transport', 250000, '2026-02-01', 'Taxi dari dan ke bandara'),
(3, 'Kertas A4 10 Rim', 'Supplies', 650000, '2026-02-10', 'Kertas HVS A4 80gsm'),
(3, 'Tinta Printer', 'Supplies', 1200000, '2026-02-10', 'Tinta printer HP 4 warna');
