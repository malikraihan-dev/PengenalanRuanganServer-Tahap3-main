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
-- Data for Name: restexamplecrud; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.restexamplecrud (id, key, value, rand, nama, waktu_input) FROM stdin;
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
-- Name: project_tasks_id_task_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.project_tasks_id_task_seq', 1, false);


--
-- Name: projects_id_project_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.projects_id_project_seq', 1, false);


--
-- Name: restexamplecrud_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.restexamplecrud_id_seq', 1, false);


--
-- Name: ruangan_id_ruangan_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.ruangan_id_ruangan_seq', 4, true);


--
-- PostgreSQL database dump complete
--

