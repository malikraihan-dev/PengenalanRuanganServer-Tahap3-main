-- =============================================
-- SQL Tables for New Features
-- 5. Real Estate Listing
-- 6. Project Management Tool
-- 7. Chatbot Interface
-- =============================================

-- ===== 5. REAL ESTATE LISTING =====
CREATE TABLE IF NOT EXISTS property_listing (
    id_property SERIAL PRIMARY KEY,
    judul VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    tipe_properti VARCHAR(50) NOT NULL, -- 'Rumah', 'Apartemen', 'Ruko', 'Tanah', 'Kos'
    harga BIGINT NOT NULL,
    luas_tanah INT, -- m2
    luas_bangunan INT, -- m2
    jumlah_kamar INT DEFAULT 0,
    jumlah_kamar_mandi INT DEFAULT 0,
    alamat TEXT NOT NULL,
    kota VARCHAR(100) NOT NULL,
    provinsi VARCHAR(100),
    status_listing VARCHAR(20) DEFAULT 'Tersedia', -- 'Tersedia', 'Terjual', 'Disewa'
    nama_pemilik VARCHAR(255),
    telepon_pemilik VARCHAR(20),
    gambar_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== 6. PROJECT MANAGEMENT TOOL =====
CREATE TABLE IF NOT EXISTS projects (
    id_project SERIAL PRIMARY KEY,
    nama_project VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    status_project VARCHAR(30) DEFAULT 'Planning', -- 'Planning', 'In Progress', 'Review', 'Completed', 'On Hold'
    prioritas VARCHAR(20) DEFAULT 'Medium', -- 'Low', 'Medium', 'High', 'Critical'
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
    status_task VARCHAR(30) DEFAULT 'To Do', -- 'To Do', 'In Progress', 'Done'
    prioritas VARCHAR(20) DEFAULT 'Medium',
    assignee VARCHAR(255),
    tanggal_deadline DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== 7. CHATBOT INTERFACE =====
CREATE TABLE IF NOT EXISTS chat_messages (
    id_message SERIAL PRIMARY KEY,
    session_id VARCHAR(100) NOT NULL,
    sender VARCHAR(10) NOT NULL, -- 'user' or 'bot'
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS chatbot_responses (
    id_response SERIAL PRIMARY KEY,
    keyword VARCHAR(100) NOT NULL,
    response TEXT NOT NULL,
    kategori VARCHAR(50)
);

-- Insert default chatbot responses
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

-- =============================================
-- 8. MUSIC STREAMING SERVICE
-- =============================================
CREATE TABLE IF NOT EXISTS music_tracks (
    id_track SERIAL PRIMARY KEY,
    judul VARCHAR(255) NOT NULL,
    artist VARCHAR(255) NOT NULL,
    album VARCHAR(255),
    genre VARCHAR(50), -- Pop, Rock, Jazz, R&B, Hip Hop, Electronic, Classical, Dangdut, Keroncong, Indie, K-Pop, J-Pop
    durasi INT NOT NULL DEFAULT 0, -- duration in seconds
    file_url VARCHAR(500),
    cover_url VARCHAR(500),
    play_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS playlists (
    id_playlist SERIAL PRIMARY KEY,
    nama_playlist VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    cover_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS playlist_tracks (
    id SERIAL PRIMARY KEY,
    id_playlist INT NOT NULL REFERENCES playlists(id_playlist) ON DELETE CASCADE,
    id_track INT NOT NULL REFERENCES music_tracks(id_track) ON DELETE CASCADE,
    track_order INT DEFAULT 0,
    UNIQUE(id_playlist, id_track)
);

-- Sample music data
INSERT INTO music_tracks (judul, artist, album, genre, durasi) VALUES
('Bohemian Rhapsody', 'Queen', 'A Night at the Opera', 'Rock', 354),
('Hotel California', 'Eagles', 'Hotel California', 'Rock', 391),
('Shape of You', 'Ed Sheeran', 'Divide', 'Pop', 234),
('Blinding Lights', 'The Weeknd', 'After Hours', 'Pop', 200),
('Take Five', 'Dave Brubeck', 'Time Out', 'Jazz', 324),
('Fly Me to the Moon', 'Frank Sinatra', 'It Might as Well Be Swing', 'Jazz', 149),
('Dynamite', 'BTS', 'BE', 'K-Pop', 199),
('Lemon', 'Kenshi Yonezu', 'Lemon', 'J-Pop', 254),
('Tak Ingin Usai', 'Keisya Levronka', 'Tak Ingin Usai', 'Pop', 237),
('Aku Milikmu Malam Ini', 'Pongki Barata', 'Somewhere Out There', 'Pop', 285),
('Bengawan Solo', 'Gesang', 'Keroncong Klasik', 'Keroncong', 240),
('Sekali Lagi', 'Isyana Sarasvati', 'LEXICON', 'Pop', 221),
('Butter', 'BTS', 'Butter', 'K-Pop', 165),
('Pesan Terakhir', 'Lyodra', 'Pesan Terakhir', 'Pop', 268),
('Night Changes', 'One Direction', 'Four', 'Pop', 226);

INSERT INTO playlists (nama_playlist, deskripsi) VALUES
('Top Hits', 'Kumpulan lagu hits terpopuler'),
('Chill Vibes', 'Lagu santai untuk relaksasi'),
('Workout Mix', 'Lagu semangat untuk olahraga');

INSERT INTO playlist_tracks (id_playlist, id_track, track_order) VALUES
(1, 3, 1), (1, 4, 2), (1, 7, 3), (1, 13, 4),
(2, 5, 1), (2, 6, 2), (2, 11, 3),
(3, 1, 1), (3, 4, 2), (3, 7, 3);

-- =============================================
-- 9. EXPENSE REPORT GENERATOR
-- =============================================
CREATE TABLE IF NOT EXISTS expense_reports (
    id_report SERIAL PRIMARY KEY,
    judul_report VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    periode VARCHAR(50),
    status_report VARCHAR(20) DEFAULT 'Draft', -- Draft, Submitted, Approved, Rejected
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS expense_items (
    id_item SERIAL PRIMARY KEY,
    id_report INT NOT NULL REFERENCES expense_reports(id_report) ON DELETE CASCADE,
    nama_item VARCHAR(255) NOT NULL,
    kategori VARCHAR(50) NOT NULL, -- Transport, Makan, Akomodasi, Supplies, Entertainment, Lainnya
    jumlah DECIMAL(15,2) NOT NULL,
    tanggal DATE,
    keterangan TEXT,
    bukti_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample expense data
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

-- =============================================
-- 10. LANGUAGE LEARNING (MULTI-LANGUAGE)
-- =============================================
CREATE TABLE IF NOT EXISTS language_courses (
    id_course SERIAL PRIMARY KEY,
    bahasa VARCHAR(50) NOT NULL, -- English, Japanese, Korean, Mandarin, Arabic, French, German, Spanish, etc.
    level VARCHAR(20) NOT NULL DEFAULT 'Beginner', -- Beginner, Intermediate, Advanced
    deskripsi TEXT,
    icon_emoji VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vocabulary (
    id_vocab SERIAL PRIMARY KEY,
    id_course INT NOT NULL REFERENCES language_courses(id_course) ON DELETE CASCADE,
    kata VARCHAR(255) NOT NULL,
    terjemahan VARCHAR(255) NOT NULL, -- translation to Indonesian
    pelafalan VARCHAR(255), -- pronunciation guide
    contoh_kalimat TEXT,
    kategori_kata VARCHAR(30), -- Noun, Verb, Adjective, Adverb, Phrase, Greeting, Number, Question
    learned BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS quiz_results (
    id_quiz SERIAL PRIMARY KEY,
    id_course INT NOT NULL REFERENCES language_courses(id_course) ON DELETE CASCADE,
    total_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    score INT NOT NULL, -- percentage
    taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample Language Courses
INSERT INTO language_courses (bahasa, level, deskripsi, icon_emoji) VALUES
('English', 'Beginner', 'Belajar bahasa Inggris dasar untuk pemula. Mencakup sapaan, angka, dan kosakata sehari-hari.', '🇬🇧'),
('English', 'Intermediate', 'Bahasa Inggris level menengah. Kosakata bisnis dan percakapan formal.', '🇬🇧'),
('Japanese', 'Beginner', 'Belajar bahasa Jepang dasar (日本語). Hiragana, sapaan, dan kosakata umum.', '🇯🇵'),
('Korean', 'Beginner', 'Belajar bahasa Korea dasar (한국어). Hangul, sapaan, dan ekspresi sehari-hari.', '🇰🇷'),
('Arabic', 'Beginner', 'Belajar bahasa Arab dasar (العربية). Huruf Arab, sapaan, dan kosakata dasar.', '🇸🇦'),
('Mandarin', 'Beginner', 'Belajar bahasa Mandarin dasar (中文). Pinyin, sapaan, dan kosakata umum.', '🇨🇳'),
('French', 'Beginner', 'Belajar bahasa Prancis dasar (Français). Sapaan, angka, dan frasa sehari-hari.', '🇫🇷'),
('German', 'Beginner', 'Belajar bahasa Jerman dasar (Deutsch). Sapaan dan kosakata dasar.', '🇩🇪'),
('Spanish', 'Beginner', 'Belajar bahasa Spanyol dasar (Español). Sapaan, angka, dan frasa umum.', '🇪🇸'),
('Thai', 'Beginner', 'Belajar bahasa Thailand dasar (ภาษาไทย). Sapaan dan kosakata sehari-hari.', '🇹🇭');

-- English Beginner Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(1, 'Hello', 'Halo', 'he-LO', 'Hello, how are you?', 'Greeting'),
(1, 'Good morning', 'Selamat pagi', 'gud MOR-ning', 'Good morning, teacher!', 'Greeting'),
(1, 'Thank you', 'Terima kasih', 'thangk yoo', 'Thank you very much!', 'Phrase'),
(1, 'Please', 'Tolong / Silakan', 'pleez', 'Please sit down.', 'Phrase'),
(1, 'Water', 'Air', 'WO-ter', 'Can I have some water?', 'Noun'),
(1, 'Food', 'Makanan', 'food', 'The food is delicious.', 'Noun'),
(1, 'House', 'Rumah', 'haus', 'This is my house.', 'Noun'),
(1, 'Book', 'Buku', 'buk', 'I read a book every day.', 'Noun'),
(1, 'Go', 'Pergi', 'go', 'Let''s go to school.', 'Verb'),
(1, 'Eat', 'Makan', 'eet', 'I eat rice for lunch.', 'Verb'),
(1, 'Beautiful', 'Cantik / Indah', 'BYOO-ti-ful', 'The garden is beautiful.', 'Adjective'),
(1, 'Big', 'Besar', 'big', 'That is a big house.', 'Adjective'),
(1, 'One', 'Satu', 'wun', 'I have one book.', 'Number'),
(1, 'Two', 'Dua', 'too', 'I have two cats.', 'Number'),
(1, 'Where', 'Di mana', 'wer', 'Where is the library?', 'Question');

-- English Intermediate Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(2, 'Achievement', 'Pencapaian', 'uh-CHEEV-ment', 'This is a great achievement.', 'Noun'),
(2, 'Deadline', 'Batas waktu', 'DED-lain', 'The deadline is tomorrow.', 'Noun'),
(2, 'Negotiate', 'Bernegosiasi', 'ni-GO-shi-eit', 'We need to negotiate the terms.', 'Verb'),
(2, 'Efficient', 'Efisien', 'i-FI-shent', 'The new system is more efficient.', 'Adjective'),
(2, 'Furthermore', 'Selain itu', 'FUR-ther-mor', 'Furthermore, we need more data.', 'Adverb');

-- Japanese Beginner Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(3, 'こんにちは', 'Halo / Selamat siang', 'Konnichiwa', 'こんにちは、元気ですか？', 'Greeting'),
(3, 'ありがとう', 'Terima kasih', 'Arigatou', 'ありがとうございます。', 'Phrase'),
(3, 'すみません', 'Permisi / Maaf', 'Sumimasen', 'すみません、トイレはどこですか？', 'Phrase'),
(3, '水', 'Air', 'Mizu', '水をください。', 'Noun'),
(3, '食べる', 'Makan', 'Taberu', 'ご飯を食べる。', 'Verb'),
(3, '大きい', 'Besar', 'Ookii', 'この家は大きいです。', 'Adjective'),
(3, '一', 'Satu', 'Ichi', '一つください。', 'Number'),
(3, '二', 'Dua', 'Ni', '二人です。', 'Number'),
(3, '美しい', 'Cantik / Indah', 'Utsukushii', '花が美しい。', 'Adjective'),
(3, 'おはよう', 'Selamat pagi', 'Ohayou', 'おはようございます。', 'Greeting');

-- Korean Beginner Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(4, '안녕하세요', 'Halo', 'Annyeonghaseyo', '안녕하세요, 만나서 반갑습니다.', 'Greeting'),
(4, '감사합니다', 'Terima kasih', 'Gamsahamnida', '도와주셔서 감사합니다.', 'Phrase'),
(4, '물', 'Air', 'Mul', '물 주세요.', 'Noun'),
(4, '밥', 'Nasi', 'Bap', '밥을 먹었어요.', 'Noun'),
(4, '먹다', 'Makan', 'Meokda', '같이 먹자!', 'Verb'),
(4, '가다', 'Pergi', 'Gada', '학교에 가다.', 'Verb'),
(4, '예쁘다', 'Cantik', 'Yeppeuda', '꽃이 예쁘다.', 'Adjective'),
(4, '하나', 'Satu', 'Hana', '하나만 주세요.', 'Number'),
(4, '사랑', 'Cinta', 'Sarang', '사랑해요.', 'Noun'),
(4, '좋아요', 'Bagus / Suka', 'Joayo', '이 노래 좋아요!', 'Adjective');

-- Arabic Beginner Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(5, 'مرحبا', 'Halo', 'Marhaba', 'مرحبا، كيف حالك؟', 'Greeting'),
(5, 'شكرا', 'Terima kasih', 'Syukran', 'شكرا جزيلا.', 'Phrase'),
(5, 'ماء', 'Air', 'Maa''', 'أريد ماء من فضلك.', 'Noun'),
(5, 'كتاب', 'Buku', 'Kitab', 'هذا كتاب جديد.', 'Noun'),
(5, 'جميل', 'Cantik / Indah', 'Jamiil', 'هذا المكان جميل.', 'Adjective'),
(5, 'واحد', 'Satu', 'Waahid', 'واحد من فضلك.', 'Number'),
(5, 'أكل', 'Makan', 'Akala', 'أكلت الطعام.', 'Verb'),
(5, 'ذهب', 'Pergi', 'Dzahaba', 'ذهبت إلى المدرسة.', 'Verb');

-- Mandarin Beginner Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(6, '你好', 'Halo', 'Nǐ hǎo', '你好，你叫什么名字？', 'Greeting'),
(6, '谢谢', 'Terima kasih', 'Xiè xiè', '非常谢谢你！', 'Phrase'),
(6, '水', 'Air', 'Shuǐ', '我要喝水。', 'Noun'),
(6, '书', 'Buku', 'Shū', '这是我的书。', 'Noun'),
(6, '吃', 'Makan', 'Chī', '我们一起吃饭吧。', 'Verb'),
(6, '去', 'Pergi', 'Qù', '我要去学校。', 'Verb'),
(6, '漂亮', 'Cantik', 'Piàoliang', '这个地方很漂亮。', 'Adjective'),
(6, '一', 'Satu', 'Yī', '给我一个。', 'Number'),
(6, '大', 'Besar', 'Dà', '这个房子很大。', 'Adjective'),
(6, '早上好', 'Selamat pagi', 'Zǎoshang hǎo', '早上好，老师！', 'Greeting');

-- French Beginner Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(7, 'Bonjour', 'Halo / Selamat pagi', 'bon-ZHOOR', 'Bonjour, comment allez-vous?', 'Greeting'),
(7, 'Merci', 'Terima kasih', 'mer-SEE', 'Merci beaucoup!', 'Phrase'),
(7, 'Eau', 'Air', 'oh', 'Je voudrais de l''eau.', 'Noun'),
(7, 'Livre', 'Buku', 'LEE-vruh', 'C''est un bon livre.', 'Noun'),
(7, 'Manger', 'Makan', 'mon-ZHAY', 'Je vais manger.', 'Verb'),
(7, 'Beau', 'Tampan / Indah', 'boh', 'C''est un beau jardin.', 'Adjective'),
(7, 'Un', 'Satu', 'uhn', 'Un café, s''il vous plaît.', 'Number'),
(7, 'Grand', 'Besar', 'gron', 'C''est une grande maison.', 'Adjective');

-- German Beginner Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(8, 'Hallo', 'Halo', 'HA-loh', 'Hallo, wie geht es Ihnen?', 'Greeting'),
(8, 'Danke', 'Terima kasih', 'DUNG-keh', 'Danke schön!', 'Phrase'),
(8, 'Wasser', 'Air', 'VA-ser', 'Ich möchte Wasser.', 'Noun'),
(8, 'Buch', 'Buku', 'bookh', 'Das ist ein gutes Buch.', 'Noun'),
(8, 'Essen', 'Makan', 'E-sen', 'Wir essen zusammen.', 'Verb'),
(8, 'Schön', 'Cantik / Indah', 'shurn', 'Der Garten ist schön.', 'Adjective'),
(8, 'Eins', 'Satu', 'ains', 'Eins, zwei, drei!', 'Number'),
(8, 'Groß', 'Besar', 'grohs', 'Das Haus ist groß.', 'Adjective');

-- Spanish Beginner Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(9, 'Hola', 'Halo', 'OH-lah', '¡Hola! ¿Cómo estás?', 'Greeting'),
(9, 'Gracias', 'Terima kasih', 'GRAH-thee-ahs', '¡Muchas gracias!', 'Phrase'),
(9, 'Agua', 'Air', 'AH-gwah', 'Quiero agua, por favor.', 'Noun'),
(9, 'Libro', 'Buku', 'LEE-broh', 'Es un buen libro.', 'Noun'),
(9, 'Comer', 'Makan', 'koh-MER', 'Vamos a comer.', 'Verb'),
(9, 'Hermoso', 'Cantik / Indah', 'er-MOH-soh', 'El jardín es hermoso.', 'Adjective'),
(9, 'Uno', 'Satu', 'OO-noh', 'Dame uno, por favor.', 'Number'),
(9, 'Grande', 'Besar', 'GRAN-deh', 'La casa es grande.', 'Adjective');

-- Thai Beginner Vocabulary
INSERT INTO vocabulary (id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata) VALUES
(10, 'สวัสดี', 'Halo', 'Sawatdee', 'สวัสดีครับ/ค่ะ', 'Greeting'),
(10, 'ขอบคุณ', 'Terima kasih', 'Khob khun', 'ขอบคุณมากครับ', 'Phrase'),
(10, 'น้ำ', 'Air', 'Nam', 'ขอน้ำหนึ่งแก้ว', 'Noun'),
(10, 'กิน', 'Makan', 'Kin', 'ไปกินข้าวกัน', 'Verb'),
(10, 'สวย', 'Cantik', 'Suay', 'ดอกไม้สวยมาก', 'Adjective'),
(10, 'หนึ่ง', 'Satu', 'Nueng', 'ขอหนึ่งชิ้น', 'Number'),
(10, 'ไป', 'Pergi', 'Pai', 'ไปโรงเรียน', 'Verb'),
(10, 'ใหญ่', 'Besar', 'Yai', 'บ้านหลังนี้ใหญ่มาก', 'Adjective');
