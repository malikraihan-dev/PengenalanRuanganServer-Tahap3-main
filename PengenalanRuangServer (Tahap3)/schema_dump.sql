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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: absensi_divisi; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.absensi_divisi (
    id_absensi integer NOT NULL,
    nim_anggota character varying(100) NOT NULL,
    id_ruangan integer NOT NULL,
    jam_masuk timestamp without time zone,
    jam_keluar timestamp without time zone
);


--
-- Name: absensi_divisi_id_absensi_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.absensi_divisi_id_absensi_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: absensi_divisi_id_absensi_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.absensi_divisi_id_absensi_seq OWNED BY public.absensi_divisi.id_absensi;


--
-- Name: anggota; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.anggota (
    nim character varying(100) NOT NULL,
    nama_lengkap character varying(250) NOT NULL,
    kelas character varying(100),
    id_divisi integer
);


--
-- Name: chat_messages; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.chat_messages (
    id_message integer NOT NULL,
    session_id character varying(100) NOT NULL,
    sender character varying(10) NOT NULL,
    message text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: chat_messages_id_message_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.chat_messages_id_message_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: chat_messages_id_message_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.chat_messages_id_message_seq OWNED BY public.chat_messages.id_message;


--
-- Name: chatbot_responses; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.chatbot_responses (
    id_response integer NOT NULL,
    keyword character varying(100) NOT NULL,
    response text NOT NULL,
    kategori character varying(50)
);


--
-- Name: chatbot_responses_id_response_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.chatbot_responses_id_response_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: chatbot_responses_id_response_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.chatbot_responses_id_response_seq OWNED BY public.chatbot_responses.id_response;


--
-- Name: divisi; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.divisi (
    id_divisi integer NOT NULL,
    nama_divisi character varying(250) NOT NULL
);


--
-- Name: divisi_id_divisi_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.divisi_id_divisi_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: divisi_id_divisi_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.divisi_id_divisi_seq OWNED BY public.divisi.id_divisi;


--
-- Name: expense_items; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.expense_items (
    id_item integer NOT NULL,
    id_report integer NOT NULL,
    nama_item character varying(255) NOT NULL,
    kategori character varying(50) NOT NULL,
    jumlah numeric(15,2) NOT NULL,
    tanggal date,
    keterangan text,
    bukti_url character varying(500),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: expense_items_id_item_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.expense_items_id_item_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: expense_items_id_item_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.expense_items_id_item_seq OWNED BY public.expense_items.id_item;


--
-- Name: expense_reports; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.expense_reports (
    id_report integer NOT NULL,
    judul_report character varying(255) NOT NULL,
    deskripsi text,
    periode character varying(50),
    status_report character varying(20) DEFAULT 'Draft'::character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: expense_reports_id_report_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.expense_reports_id_report_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: expense_reports_id_report_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.expense_reports_id_report_seq OWNED BY public.expense_reports.id_report;


--
-- Name: face_absensi_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.face_absensi_log (
    id_log integer NOT NULL,
    nim character varying(100) NOT NULL,
    confidence numeric(5,4),
    method character varying(50) DEFAULT 'face_recognition'::character varying,
    created_at timestamp without time zone DEFAULT now()
);


--
-- Name: face_absensi_log_id_log_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.face_absensi_log_id_log_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: face_absensi_log_id_log_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.face_absensi_log_id_log_seq OWNED BY public.face_absensi_log.id_log;


--
-- Name: face_data; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.face_data (
    id_face integer NOT NULL,
    nim character varying(100) NOT NULL,
    label character varying(250) NOT NULL,
    face_descriptor text NOT NULL,
    image_data text,
    created_at timestamp without time zone DEFAULT now()
);


--
-- Name: face_data_id_face_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.face_data_id_face_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: face_data_id_face_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.face_data_id_face_seq OWNED BY public.face_data.id_face;


--
-- Name: language_courses; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.language_courses (
    id_course integer NOT NULL,
    bahasa character varying(50) NOT NULL,
    level character varying(20) DEFAULT 'Beginner'::character varying NOT NULL,
    deskripsi text,
    icon_emoji character varying(10),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: language_courses_id_course_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.language_courses_id_course_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: language_courses_id_course_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.language_courses_id_course_seq OWNED BY public.language_courses.id_course;


--
-- Name: music_tracks; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.music_tracks (
    id_track integer NOT NULL,
    judul character varying(255) NOT NULL,
    artist character varying(255) NOT NULL,
    album character varying(255),
    genre character varying(50),
    durasi integer DEFAULT 0 NOT NULL,
    file_url character varying(500),
    cover_url character varying(500),
    play_count integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: music_tracks_id_track_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.music_tracks_id_track_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: music_tracks_id_track_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.music_tracks_id_track_seq OWNED BY public.music_tracks.id_track;


--
-- Name: playlist_tracks; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.playlist_tracks (
    id integer NOT NULL,
    id_playlist integer NOT NULL,
    id_track integer NOT NULL,
    track_order integer DEFAULT 0
);


--
-- Name: playlist_tracks_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.playlist_tracks_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: playlist_tracks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.playlist_tracks_id_seq OWNED BY public.playlist_tracks.id;


--
-- Name: playlists; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.playlists (
    id_playlist integer NOT NULL,
    nama_playlist character varying(255) NOT NULL,
    deskripsi text,
    cover_url character varying(500),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: playlists_id_playlist_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.playlists_id_playlist_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: playlists_id_playlist_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.playlists_id_playlist_seq OWNED BY public.playlists.id_playlist;


--
-- Name: project_tasks; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.project_tasks (
    id_task integer NOT NULL,
    id_project integer NOT NULL,
    judul_task character varying(255) NOT NULL,
    deskripsi text,
    status_task character varying(30) DEFAULT 'To Do'::character varying,
    prioritas character varying(20) DEFAULT 'Medium'::character varying,
    assignee character varying(255),
    tanggal_deadline date,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: project_tasks_id_task_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.project_tasks_id_task_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: project_tasks_id_task_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.project_tasks_id_task_seq OWNED BY public.project_tasks.id_task;


--
-- Name: projects; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.projects (
    id_project integer NOT NULL,
    nama_project character varying(255) NOT NULL,
    deskripsi text,
    status_project character varying(30) DEFAULT 'Planning'::character varying,
    prioritas character varying(20) DEFAULT 'Medium'::character varying,
    tanggal_mulai date,
    tanggal_selesai date,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: projects_id_project_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.projects_id_project_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: projects_id_project_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.projects_id_project_seq OWNED BY public.projects.id_project;


--
-- Name: property_listing; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.property_listing (
    id_property integer NOT NULL,
    judul character varying(255) NOT NULL,
    deskripsi text,
    tipe_properti character varying(50) NOT NULL,
    harga bigint NOT NULL,
    luas_tanah integer,
    luas_bangunan integer,
    jumlah_kamar integer DEFAULT 0,
    jumlah_kamar_mandi integer DEFAULT 0,
    alamat text NOT NULL,
    kota character varying(100) NOT NULL,
    provinsi character varying(100),
    status_listing character varying(20) DEFAULT 'Tersedia'::character varying,
    nama_pemilik character varying(255),
    telepon_pemilik character varying(20),
    gambar_url character varying(500),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: property_listing_id_property_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.property_listing_id_property_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: property_listing_id_property_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.property_listing_id_property_seq OWNED BY public.property_listing.id_property;


--
-- Name: quiz_results; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.quiz_results (
    id_quiz integer NOT NULL,
    id_course integer NOT NULL,
    total_questions integer NOT NULL,
    correct_answers integer NOT NULL,
    score integer NOT NULL,
    taken_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: quiz_results_id_quiz_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.quiz_results_id_quiz_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: quiz_results_id_quiz_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.quiz_results_id_quiz_seq OWNED BY public.quiz_results.id_quiz;


--
-- Name: restexamplecrud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.restexamplecrud (
    id integer NOT NULL,
    key character varying(100) NOT NULL,
    value character varying(250) NOT NULL,
    rand smallint NOT NULL,
    nama character varying(250) NOT NULL,
    waktu_input character varying(250) NOT NULL
);


--
-- Name: restexamplecrud_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.restexamplecrud_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: restexamplecrud_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.restexamplecrud_id_seq OWNED BY public.restexamplecrud.id;


--
-- Name: ruangan; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ruangan (
    id_ruangan integer NOT NULL,
    nama_ruangan character varying(250) NOT NULL
);


--
-- Name: ruangan_id_ruangan_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ruangan_id_ruangan_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ruangan_id_ruangan_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ruangan_id_ruangan_seq OWNED BY public.ruangan.id_ruangan;


--
-- Name: vocabulary; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.vocabulary (
    id_vocab integer NOT NULL,
    id_course integer NOT NULL,
    kata character varying(255) NOT NULL,
    terjemahan character varying(255) NOT NULL,
    pelafalan character varying(255),
    contoh_kalimat text,
    kategori_kata character varying(30),
    learned boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: vocabulary_id_vocab_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.vocabulary_id_vocab_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: vocabulary_id_vocab_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.vocabulary_id_vocab_seq OWNED BY public.vocabulary.id_vocab;


--
-- Name: absensi_divisi id_absensi; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.absensi_divisi ALTER COLUMN id_absensi SET DEFAULT nextval('public.absensi_divisi_id_absensi_seq'::regclass);


--
-- Name: chat_messages id_message; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chat_messages ALTER COLUMN id_message SET DEFAULT nextval('public.chat_messages_id_message_seq'::regclass);


--
-- Name: chatbot_responses id_response; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chatbot_responses ALTER COLUMN id_response SET DEFAULT nextval('public.chatbot_responses_id_response_seq'::regclass);


--
-- Name: divisi id_divisi; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.divisi ALTER COLUMN id_divisi SET DEFAULT nextval('public.divisi_id_divisi_seq'::regclass);


--
-- Name: expense_items id_item; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expense_items ALTER COLUMN id_item SET DEFAULT nextval('public.expense_items_id_item_seq'::regclass);


--
-- Name: expense_reports id_report; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expense_reports ALTER COLUMN id_report SET DEFAULT nextval('public.expense_reports_id_report_seq'::regclass);


--
-- Name: face_absensi_log id_log; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.face_absensi_log ALTER COLUMN id_log SET DEFAULT nextval('public.face_absensi_log_id_log_seq'::regclass);


--
-- Name: face_data id_face; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.face_data ALTER COLUMN id_face SET DEFAULT nextval('public.face_data_id_face_seq'::regclass);


--
-- Name: language_courses id_course; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.language_courses ALTER COLUMN id_course SET DEFAULT nextval('public.language_courses_id_course_seq'::regclass);


--
-- Name: music_tracks id_track; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.music_tracks ALTER COLUMN id_track SET DEFAULT nextval('public.music_tracks_id_track_seq'::regclass);


--
-- Name: playlist_tracks id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.playlist_tracks ALTER COLUMN id SET DEFAULT nextval('public.playlist_tracks_id_seq'::regclass);


--
-- Name: playlists id_playlist; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.playlists ALTER COLUMN id_playlist SET DEFAULT nextval('public.playlists_id_playlist_seq'::regclass);


--
-- Name: project_tasks id_task; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.project_tasks ALTER COLUMN id_task SET DEFAULT nextval('public.project_tasks_id_task_seq'::regclass);


--
-- Name: projects id_project; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.projects ALTER COLUMN id_project SET DEFAULT nextval('public.projects_id_project_seq'::regclass);


--
-- Name: property_listing id_property; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.property_listing ALTER COLUMN id_property SET DEFAULT nextval('public.property_listing_id_property_seq'::regclass);


--
-- Name: quiz_results id_quiz; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quiz_results ALTER COLUMN id_quiz SET DEFAULT nextval('public.quiz_results_id_quiz_seq'::regclass);


--
-- Name: restexamplecrud id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.restexamplecrud ALTER COLUMN id SET DEFAULT nextval('public.restexamplecrud_id_seq'::regclass);


--
-- Name: ruangan id_ruangan; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ruangan ALTER COLUMN id_ruangan SET DEFAULT nextval('public.ruangan_id_ruangan_seq'::regclass);


--
-- Name: vocabulary id_vocab; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vocabulary ALTER COLUMN id_vocab SET DEFAULT nextval('public.vocabulary_id_vocab_seq'::regclass);


--
-- Name: absensi_divisi absensi_divisi_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.absensi_divisi
    ADD CONSTRAINT absensi_divisi_pkey PRIMARY KEY (id_absensi);


--
-- Name: anggota anggota_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.anggota
    ADD CONSTRAINT anggota_pkey PRIMARY KEY (nim);


--
-- Name: chat_messages chat_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chat_messages
    ADD CONSTRAINT chat_messages_pkey PRIMARY KEY (id_message);


--
-- Name: chatbot_responses chatbot_responses_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chatbot_responses
    ADD CONSTRAINT chatbot_responses_pkey PRIMARY KEY (id_response);


--
-- Name: divisi divisi_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.divisi
    ADD CONSTRAINT divisi_pkey PRIMARY KEY (id_divisi);


--
-- Name: expense_items expense_items_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expense_items
    ADD CONSTRAINT expense_items_pkey PRIMARY KEY (id_item);


--
-- Name: expense_reports expense_reports_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expense_reports
    ADD CONSTRAINT expense_reports_pkey PRIMARY KEY (id_report);


--
-- Name: face_absensi_log face_absensi_log_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.face_absensi_log
    ADD CONSTRAINT face_absensi_log_pkey PRIMARY KEY (id_log);


--
-- Name: face_data face_data_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.face_data
    ADD CONSTRAINT face_data_pkey PRIMARY KEY (id_face);


--
-- Name: language_courses language_courses_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.language_courses
    ADD CONSTRAINT language_courses_pkey PRIMARY KEY (id_course);


--
-- Name: music_tracks music_tracks_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.music_tracks
    ADD CONSTRAINT music_tracks_pkey PRIMARY KEY (id_track);


--
-- Name: playlist_tracks playlist_tracks_id_playlist_id_track_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.playlist_tracks
    ADD CONSTRAINT playlist_tracks_id_playlist_id_track_key UNIQUE (id_playlist, id_track);


--
-- Name: playlist_tracks playlist_tracks_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.playlist_tracks
    ADD CONSTRAINT playlist_tracks_pkey PRIMARY KEY (id);


--
-- Name: playlists playlists_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.playlists
    ADD CONSTRAINT playlists_pkey PRIMARY KEY (id_playlist);


--
-- Name: project_tasks project_tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.project_tasks
    ADD CONSTRAINT project_tasks_pkey PRIMARY KEY (id_task);


--
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id_project);


--
-- Name: property_listing property_listing_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.property_listing
    ADD CONSTRAINT property_listing_pkey PRIMARY KEY (id_property);


--
-- Name: quiz_results quiz_results_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quiz_results
    ADD CONSTRAINT quiz_results_pkey PRIMARY KEY (id_quiz);


--
-- Name: restexamplecrud restexamplecrud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.restexamplecrud
    ADD CONSTRAINT restexamplecrud_pkey PRIMARY KEY (id);


--
-- Name: ruangan ruangan_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ruangan
    ADD CONSTRAINT ruangan_pkey PRIMARY KEY (id_ruangan);


--
-- Name: vocabulary vocabulary_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vocabulary
    ADD CONSTRAINT vocabulary_pkey PRIMARY KEY (id_vocab);


--
-- Name: idx_face_data_nim; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_face_data_nim ON public.face_data USING btree (nim);


--
-- Name: absensi_divisi absensi_divisi_id_ruangan_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.absensi_divisi
    ADD CONSTRAINT absensi_divisi_id_ruangan_fkey FOREIGN KEY (id_ruangan) REFERENCES public.ruangan(id_ruangan);


--
-- Name: absensi_divisi absensi_divisi_nim_anggota_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.absensi_divisi
    ADD CONSTRAINT absensi_divisi_nim_anggota_fkey FOREIGN KEY (nim_anggota) REFERENCES public.anggota(nim);


--
-- Name: anggota anggota_id_divisi_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.anggota
    ADD CONSTRAINT anggota_id_divisi_fkey FOREIGN KEY (id_divisi) REFERENCES public.divisi(id_divisi);


--
-- Name: expense_items expense_items_id_report_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expense_items
    ADD CONSTRAINT expense_items_id_report_fkey FOREIGN KEY (id_report) REFERENCES public.expense_reports(id_report) ON DELETE CASCADE;


--
-- Name: face_data fk_face_anggota; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.face_data
    ADD CONSTRAINT fk_face_anggota FOREIGN KEY (nim) REFERENCES public.anggota(nim) ON DELETE CASCADE;


--
-- Name: playlist_tracks playlist_tracks_id_playlist_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.playlist_tracks
    ADD CONSTRAINT playlist_tracks_id_playlist_fkey FOREIGN KEY (id_playlist) REFERENCES public.playlists(id_playlist) ON DELETE CASCADE;


--
-- Name: playlist_tracks playlist_tracks_id_track_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.playlist_tracks
    ADD CONSTRAINT playlist_tracks_id_track_fkey FOREIGN KEY (id_track) REFERENCES public.music_tracks(id_track) ON DELETE CASCADE;


--
-- Name: project_tasks project_tasks_id_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.project_tasks
    ADD CONSTRAINT project_tasks_id_project_fkey FOREIGN KEY (id_project) REFERENCES public.projects(id_project) ON DELETE CASCADE;


--
-- Name: quiz_results quiz_results_id_course_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quiz_results
    ADD CONSTRAINT quiz_results_id_course_fkey FOREIGN KEY (id_course) REFERENCES public.language_courses(id_course) ON DELETE CASCADE;


--
-- Name: vocabulary vocabulary_id_course_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vocabulary
    ADD CONSTRAINT vocabulary_id_course_fkey FOREIGN KEY (id_course) REFERENCES public.language_courses(id_course) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

