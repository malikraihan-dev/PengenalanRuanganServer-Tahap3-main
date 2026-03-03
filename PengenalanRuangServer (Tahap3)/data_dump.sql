--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: divisi; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.divisi (id_divisi, nama_divisi) FROM stdin;
1	Divisi IT
2	Divisi Administrasi
3	Divisi Keuangan
4	Divisi Umum
\.


--
-- Data for Name: anggota; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.anggota (nim, nama_lengkap, kelas, id_divisi) FROM stdin;
12345	Ahmad Malik	TI-1A	1
12346	Budi Santoso	TI-1B	2
12347	Citra Dewi	TI-2A	1
\.


--
-- Data for Name: ruangan; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.ruangan (id_ruangan, nama_ruangan) FROM stdin;
1	Ruang TU 1
2	Ruang TU 2
3	Ruang TU 3
4	Ruang TU 4
\.


--
-- Data for Name: absensi_divisi; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.absensi_divisi (id_absensi, nim_anggota, id_ruangan, jam_masuk, jam_keluar) FROM stdin;
\.


--
-- Data for Name: chat_messages; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.chat_messages (id_message, session_id, sender, message, created_at) FROM stdin;
\.


--
-- Data for Name: chatbot_responses; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.chatbot_responses (id_response, keyword, response, kategori) FROM stdin;
1	halo	Halo! Selamat datang. Ada yang bisa saya bantu?	greeting
2	hai	Hai! Ada yang bisa saya bantu hari ini?	greeting
3	selamat pagi	Selamat pagi! Semoga hari Anda menyenangkan. Ada yang bisa dibantu?	greeting
4	selamat siang	Selamat siang! Ada yang bisa saya bantu?	greeting
5	selamat sore	Selamat sore! Ada yang bisa saya bantu?	greeting
6	selamat malam	Selamat malam! Ada yang bisa saya bantu?	greeting
7	absensi	Untuk melakukan absensi, silakan kunjungi halaman Dashboard dan gunakan fitur Absensi Cepat dengan memasukkan NIM Anda.	info
8	ruangan	Terdapat 4 ruangan yang tersedia untuk absensi. Anda bisa melihat detail ruangan di halaman Dashboard.	info
9	properti	Untuk melihat daftar properti, silakan kunjungi halaman Real Estate Listing di sidebar menu.	info
10	project	Untuk mengelola project, silakan kunjungi halaman Project Management di sidebar menu.	info
11	bantuan	Saya bisa membantu Anda dengan informasi tentang: absensi, ruangan, properti, project, dan lainnya. Silakan tanyakan!	help
12	help	Berikut yang bisa saya bantu:\\n1. Informasi Absensi\\n2. Info Ruangan\\n3. Info Properti\\n4. Info Project Management\\n5. Bantuan Umum\\nSilakan ketik topik yang ingin ditanyakan!	help
13	terima kasih	Sama-sama! Jangan ragu untuk bertanya lagi ya.	greeting
14	makasih	Sama-sama! Semoga membantu.	greeting
15	bye	Sampai jumpa! Semoga hari Anda menyenangkan.	greeting
16	jam	Jam operasional kampus adalah Senin-Jumat pukul 07:00 - 17:00 WIB dan Sabtu pukul 07:00 - 12:00 WIB.	info
17	kontak	Untuk informasi kontak, silakan hubungi bagian TU di ext. 123 atau email tu@kampus.ac.id	info
\.


--
-- Data for Name: expense_reports; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.expense_reports (id_report, judul_report, deskripsi, periode, status_report, created_at, updated_at) FROM stdin;
1	Operasional Januari 2026	Pengeluaran operasional bulan Januari	Januari 2026	Approved	2026-02-24 13:04:08.433481	2026-02-24 13:04:08.433481
2	Perjalanan Dinas Surabaya	Biaya perjalanan dinas ke Surabaya	Februari 2026	Submitted	2026-02-24 13:04:08.433481	2026-02-24 13:04:08.433481
3	Kebutuhan ATK Q1	Pembelian alat tulis kantor kuartal pertama	Q1 2026	Draft	2026-02-24 13:04:08.433481	2026-02-24 13:04:08.433481
\.


--
-- Data for Name: expense_items; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.expense_items (id_item, id_report, nama_item, kategori, jumlah, tanggal, keterangan, bukti_url, created_at) FROM stdin;
1	1	Listrik Kantor	Supplies	2500000.00	2026-01-05	Tagihan listrik bulan Januari	\N	2026-02-24 13:04:08.438182
2	1	Internet	Supplies	1500000.00	2026-01-05	Tagihan internet kantor	\N	2026-02-24 13:04:08.438182
3	1	Makan Siang Rapat	Makan	750000.00	2026-01-15	Catering rapat bulanan 30 orang	\N	2026-02-24 13:04:08.438182
4	1	Transport Kurir	Transport	350000.00	2026-01-20	Biaya antar dokumen	\N	2026-02-24 13:04:08.438182
5	2	Tiket Pesawat PP	Transport	2800000.00	2026-02-01	Jakarta-Surabaya pulang pergi	\N	2026-02-24 13:04:08.438182
6	2	Hotel 2 Malam	Akomodasi	1600000.00	2026-02-02	Hotel bintang 3 dekat kantor cabang	\N	2026-02-24 13:04:08.438182
7	2	Makan 3 Hari	Makan	450000.00	2026-02-02	Uang makan selama dinas	\N	2026-02-24 13:04:08.438182
8	2	Taxi Bandara	Transport	250000.00	2026-02-01	Taxi dari dan ke bandara	\N	2026-02-24 13:04:08.438182
9	3	Kertas A4 10 Rim	Supplies	650000.00	2026-02-10	Kertas HVS A4 80gsm	\N	2026-02-24 13:04:08.438182
10	3	Tinta Printer	Supplies	1200000.00	2026-02-10	Tinta printer HP 4 warna	\N	2026-02-24 13:04:08.438182
\.


--
-- Data for Name: face_absensi_log; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.face_absensi_log (id_log, nim, confidence, method, created_at) FROM stdin;
\.


--
-- Data for Name: face_data; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.face_data (id_face, nim, label, face_descriptor, image_data, created_at) FROM stdin;
\.


--
-- Data for Name: language_courses; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.language_courses (id_course, bahasa, level, deskripsi, icon_emoji, created_at) FROM stdin;
1	English	Beginner	Belajar bahasa Inggris dasar untuk pemula.	ðŸ‡¬ðŸ‡§	2026-02-24 13:04:08.522724
2	English	Intermediate	Bahasa Inggris level menengah.	ðŸ‡¬ðŸ‡§	2026-02-24 13:04:08.522724
3	Japanese	Beginner	Belajar bahasa Jepang dasar.	ðŸ‡¯ðŸ‡µ	2026-02-24 13:04:08.522724
4	Korean	Beginner	Belajar bahasa Korea dasar.	ðŸ‡°ðŸ‡·	2026-02-24 13:04:08.522724
5	Arabic	Beginner	Belajar bahasa Arab dasar.	ðŸ‡¸ðŸ‡¦	2026-02-24 13:04:08.522724
6	Mandarin	Beginner	Belajar bahasa Mandarin dasar.	ðŸ‡¨ðŸ‡³	2026-02-24 13:04:08.522724
7	French	Beginner	Belajar bahasa Prancis dasar.	ðŸ‡«ðŸ‡·	2026-02-24 13:04:08.522724
8	German	Beginner	Belajar bahasa Jerman dasar.	ðŸ‡©ðŸ‡ª	2026-02-24 13:04:08.522724
9	Spanish	Beginner	Belajar bahasa Spanyol dasar.	ðŸ‡ªðŸ‡¸	2026-02-24 13:04:08.522724
10	Thai	Beginner	Belajar bahasa Thailand dasar.	ðŸ‡¹ðŸ‡­	2026-02-24 13:04:08.522724
\.


--
-- Data for Name: music_tracks; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.music_tracks (id_track, judul, artist, album, genre, durasi, file_url, cover_url, play_count, created_at) FROM stdin;
1	Bohemian Rhapsody	Queen	A Night at the Opera	Rock	354	\N	\N	0	2026-02-24 13:04:08.375633
2	Hotel California	Eagles	Hotel California	Rock	391	\N	\N	0	2026-02-24 13:04:08.375633
3	Shape of You	Ed Sheeran	Divide	Pop	234	\N	\N	0	2026-02-24 13:04:08.375633
4	Blinding Lights	The Weeknd	After Hours	Pop	200	\N	\N	0	2026-02-24 13:04:08.375633
5	Take Five	Dave Brubeck	Time Out	Jazz	324	\N	\N	0	2026-02-24 13:04:08.375633
6	Fly Me to the Moon	Frank Sinatra	It Might as Well Be Swing	Jazz	149	\N	\N	0	2026-02-24 13:04:08.375633
7	Dynamite	BTS	BE	K-Pop	199	\N	\N	0	2026-02-24 13:04:08.375633
8	Lemon	Kenshi Yonezu	Lemon	J-Pop	254	\N	\N	0	2026-02-24 13:04:08.375633
9	Tak Ingin Usai	Keisya Levronka	Tak Ingin Usai	Pop	237	\N	\N	0	2026-02-24 13:04:08.375633
10	Aku Milikmu Malam Ini	Pongki Barata	Somewhere Out There	Pop	285	\N	\N	0	2026-02-24 13:04:08.375633
11	Bengawan Solo	Gesang	Keroncong Klasik	Keroncong	240	\N	\N	0	2026-02-24 13:04:08.375633
12	Sekali Lagi	Isyana Sarasvati	LEXICON	Pop	221	\N	\N	0	2026-02-24 13:04:08.375633
13	Butter	BTS	Butter	K-Pop	165	\N	\N	0	2026-02-24 13:04:08.375633
14	Pesan Terakhir	Lyodra	Pesan Terakhir	Pop	268	\N	\N	0	2026-02-24 13:04:08.375633
15	Night Changes	One Direction	Four	Pop	226	\N	\N	0	2026-02-24 13:04:08.375633
\.


--
-- Data for Name: playlists; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.playlists (id_playlist, nama_playlist, deskripsi, cover_url, created_at) FROM stdin;
1	Top Hits	Kumpulan lagu hits terpopuler	\N	2026-02-24 13:04:08.381547
2	Chill Vibes	Lagu santai untuk relaksasi	\N	2026-02-24 13:04:08.381547
3	Workout Mix	Lagu semangat untuk olahraga	\N	2026-02-24 13:04:08.381547
\.


--
-- Data for Name: playlist_tracks; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.playlist_tracks (id, id_playlist, id_track, track_order) FROM stdin;
1	1	3	1
2	1	4	2
3	1	7	3
4	1	13	4
5	2	5	1
6	2	6	2
7	2	11	3
8	3	1	1
9	3	4	2
10	3	7	3
\.


--
-- Data for Name: projects; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.projects (id_project, nama_project, deskripsi, status_project, prioritas, tanggal_mulai, tanggal_selesai, created_at, updated_at) FROM stdin;
\.


--
-- Data for Name: project_tasks; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.project_tasks (id_task, id_project, judul_task, deskripsi, status_task, prioritas, assignee, tanggal_deadline, created_at, updated_at) FROM stdin;
\.


--
-- Data for Name: property_listing; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.property_listing (id_property, judul, deskripsi, tipe_properti, harga, luas_tanah, luas_bangunan, jumlah_kamar, jumlah_kamar_mandi, alamat, kota, provinsi, status_listing, nama_pemilik, telepon_pemilik, gambar_url, created_at, updated_at) FROM stdin;
\.


--
-- Data for Name: quiz_results; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.quiz_results (id_quiz, id_course, total_questions, correct_answers, score, taken_at) FROM stdin;
\.


--
-- Data for Name: restexamplecrud; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.restexamplecrud (id, key, value, rand, nama, waktu_input) FROM stdin;
\.


--
-- Data for Name: vocabulary; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.vocabulary (id_vocab, id_course, kata, terjemahan, pelafalan, contoh_kalimat, kategori_kata, learned, created_at) FROM stdin;
1	1	Hello	Halo	he-LO	Hello, how are you?	Greeting	f	2026-02-24 13:04:08.538104
2	1	Good morning	Selamat pagi	gud MOR-ning	Good morning, teacher!	Greeting	f	2026-02-24 13:04:08.538104
3	1	Thank you	Terima kasih	thangk yoo	Thank you very much!	Phrase	f	2026-02-24 13:04:08.538104
4	1	Please	Tolong / Silakan	pleez	Please sit down.	Phrase	f	2026-02-24 13:04:08.538104
5	1	Water	Air	WO-ter	Can I have some water?	Noun	f	2026-02-24 13:04:08.538104
6	1	Food	Makanan	food	The food is delicious.	Noun	f	2026-02-24 13:04:08.538104
7	1	House	Rumah	haus	This is my house.	Noun	f	2026-02-24 13:04:08.538104
8	1	Book	Buku	buk	I read a book every day.	Noun	f	2026-02-24 13:04:08.538104
9	1	Go	Pergi	go	Let's go to school.	Verb	f	2026-02-24 13:04:08.538104
10	1	Eat	Makan	eet	I eat rice for lunch.	Verb	f	2026-02-24 13:04:08.538104
11	1	Beautiful	Cantik / Indah	BYOO-ti-ful	The garden is beautiful.	Adjective	f	2026-02-24 13:04:08.538104
12	1	Big	Besar	big	That is a big house.	Adjective	f	2026-02-24 13:04:08.538104
13	1	One	Satu	wun	I have one book.	Number	f	2026-02-24 13:04:08.538104
14	1	Two	Dua	too	I have two cats.	Number	f	2026-02-24 13:04:08.538104
15	1	Where	Di mana	wer	Where is the library?	Question	f	2026-02-24 13:04:08.538104
16	2	Achievement	Pencapaian	uh-CHEEV-ment	This is a great achievement.	Noun	f	2026-02-24 13:04:08.545585
17	2	Deadline	Batas waktu	DED-lain	The deadline is tomorrow.	Noun	f	2026-02-24 13:04:08.545585
18	2	Negotiate	Bernegosiasi	ni-GO-shi-eit	We need to negotiate the terms.	Verb	f	2026-02-24 13:04:08.545585
19	2	Efficient	Efisien	i-FI-shent	The new system is more efficient.	Adjective	f	2026-02-24 13:04:08.545585
20	2	Furthermore	Selain itu	FUR-ther-mor	Furthermore, we need more data.	Adverb	f	2026-02-24 13:04:08.545585
21	7	Bonjour	Halo	bon-ZHOOR	Bonjour, comment allez-vous?	Greeting	f	2026-02-24 13:04:08.559174
22	7	Merci	Terima kasih	mer-SEE	Merci beaucoup!	Phrase	f	2026-02-24 13:04:08.559174
23	7	Eau	Air	oh	Je voudrais de l'eau.	Noun	f	2026-02-24 13:04:08.559174
24	7	Livre	Buku	LEE-vruh	C'est un bon livre.	Noun	f	2026-02-24 13:04:08.559174
25	7	Manger	Makan	mon-ZHAY	Je vais manger.	Verb	f	2026-02-24 13:04:08.559174
26	7	Beau	Tampan / Indah	boh	C'est un beau jardin.	Adjective	f	2026-02-24 13:04:08.559174
27	7	Un	Satu	uhn	Un cafe, s'il vous plait.	Number	f	2026-02-24 13:04:08.559174
28	7	Grand	Besar	gron	C'est une grande maison.	Adjective	f	2026-02-24 13:04:08.559174
29	8	Hallo	Halo	HA-loh	Hallo, wie geht es Ihnen?	Greeting	f	2026-02-24 13:04:08.572185
30	8	Danke	Terima kasih	DUNG-keh	Danke schon!	Phrase	f	2026-02-24 13:04:08.572185
31	8	Wasser	Air	VA-ser	Ich mochte Wasser.	Noun	f	2026-02-24 13:04:08.572185
32	8	Buch	Buku	bookh	Das ist ein gutes Buch.	Noun	f	2026-02-24 13:04:08.572185
33	8	Essen	Makan	E-sen	Wir essen zusammen.	Verb	f	2026-02-24 13:04:08.572185
34	8	Schon	Cantik / Indah	shurn	Der Garten ist schon.	Adjective	f	2026-02-24 13:04:08.572185
35	8	Eins	Satu	ains	Eins, zwei, drei!	Number	f	2026-02-24 13:04:08.572185
36	8	Gross	Besar	grohs	Das Haus ist gross.	Adjective	f	2026-02-24 13:04:08.572185
37	9	Hola	Halo	OH-lah	Hola! Como estas?	Greeting	f	2026-02-24 13:04:08.574316
38	9	Gracias	Terima kasih	GRAH-thee-ahs	Muchas gracias!	Phrase	f	2026-02-24 13:04:08.574316
39	9	Agua	Air	AH-gwah	Quiero agua, por favor.	Noun	f	2026-02-24 13:04:08.574316
40	9	Libro	Buku	LEE-broh	Es un buen libro.	Noun	f	2026-02-24 13:04:08.574316
41	9	Comer	Makan	koh-MER	Vamos a comer.	Verb	f	2026-02-24 13:04:08.574316
42	9	Hermoso	Cantik / Indah	er-MOH-soh	El jardin es hermoso.	Adjective	f	2026-02-24 13:04:08.574316
43	9	Uno	Satu	OO-noh	Dame uno, por favor.	Number	f	2026-02-24 13:04:08.574316
44	9	Grande	Besar	GRAN-deh	La casa es grande.	Adjective	f	2026-02-24 13:04:08.574316
\.


--
-- Name: absensi_divisi_id_absensi_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.absensi_divisi_id_absensi_seq', 1, false);


--
-- Name: chat_messages_id_message_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.chat_messages_id_message_seq', 1, false);


--
-- Name: chatbot_responses_id_response_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.chatbot_responses_id_response_seq', 17, true);


--
-- Name: divisi_id_divisi_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.divisi_id_divisi_seq', 4, true);


--
-- Name: expense_items_id_item_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.expense_items_id_item_seq', 10, true);


--
-- Name: expense_reports_id_report_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.expense_reports_id_report_seq', 3, true);


--
-- Name: face_absensi_log_id_log_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.face_absensi_log_id_log_seq', 1, false);


--
-- Name: face_data_id_face_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.face_data_id_face_seq', 1, false);


--
-- Name: language_courses_id_course_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.language_courses_id_course_seq', 10, true);


--
-- Name: music_tracks_id_track_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.music_tracks_id_track_seq', 15, true);


--
-- Name: playlist_tracks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.playlist_tracks_id_seq', 10, true);


--
-- Name: playlists_id_playlist_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.playlists_id_playlist_seq', 3, true);


--
-- Name: project_tasks_id_task_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.project_tasks_id_task_seq', 1, false);


--
-- Name: projects_id_project_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.projects_id_project_seq', 1, false);


--
-- Name: property_listing_id_property_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.property_listing_id_property_seq', 1, false);


--
-- Name: quiz_results_id_quiz_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.quiz_results_id_quiz_seq', 1, false);


--
-- Name: restexamplecrud_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.restexamplecrud_id_seq', 1, false);


--
-- Name: ruangan_id_ruangan_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.ruangan_id_ruangan_seq', 4, true);


--
-- Name: vocabulary_id_vocab_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.vocabulary_id_vocab_seq', 44, true);


--
-- PostgreSQL database dump complete
--

