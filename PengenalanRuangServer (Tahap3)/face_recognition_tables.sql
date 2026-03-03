-- ================================================================
-- TABEL UNTUK FITUR FACE RECOGNITION ABSENSI
-- Jalankan SQL ini di database PostgreSQL "ruanganTU"
-- ================================================================

-- Tabel untuk menyimpan data wajah (face descriptor) setiap anggota
-- Setiap anggota bisa punya beberapa foto wajah untuk meningkatkan akurasi
CREATE TABLE IF NOT EXISTS face_data (
    id_face SERIAL PRIMARY KEY,
    nim VARCHAR(100) NOT NULL,
    label VARCHAR(250) NOT NULL,           -- nama anggota
    face_descriptor TEXT NOT NULL,          -- face descriptor array (128 float) disimpan sebagai JSON string
    image_data TEXT,                        -- base64 encoded image (opsional, untuk preview)
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_face_anggota FOREIGN KEY (nim) REFERENCES anggota(nim) ON DELETE CASCADE
);

-- Index untuk pencarian cepat berdasarkan NIM
CREATE INDEX IF NOT EXISTS idx_face_data_nim ON face_data(nim);

-- Tabel log absensi wajah (mencatat setiap kali absen pakai wajah)
CREATE TABLE IF NOT EXISTS face_absensi_log (
    id_log SERIAL PRIMARY KEY,
    nim VARCHAR(100) NOT NULL,
    confidence DECIMAL(5,4),               -- tingkat kepercayaan pengenalan (0-1)
    method VARCHAR(50) DEFAULT 'face_recognition',
    created_at TIMESTAMP DEFAULT NOW()
);
