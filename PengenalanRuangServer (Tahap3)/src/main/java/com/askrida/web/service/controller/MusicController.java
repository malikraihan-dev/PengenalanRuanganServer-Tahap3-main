package com.askrida.web.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.askrida.web.service.model.MusicTrack;
import com.askrida.web.service.model.Playlist;
import com.askrida.web.service.repository.MusicRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicRepository musicRepo;

    // Page
    @GetMapping("")
    public String musicPage(Model model) {
        try {
            List<MusicTrack> tracks = musicRepo.getAllTracks();
            List<Playlist> playlists = musicRepo.getAllPlaylists();
            model.addAttribute("tracks", tracks);
            model.addAttribute("playlists", playlists);
            model.addAttribute("totalTracks", tracks.size());
            model.addAttribute("totalPlaylists", playlists.size());
            model.addAttribute("genres", musicRepo.getAllGenres());
        } catch (Exception e) {
            model.addAttribute("tracks", java.util.Collections.emptyList());
            model.addAttribute("playlists", java.util.Collections.emptyList());
            model.addAttribute("totalTracks", 0);
            model.addAttribute("totalPlaylists", 0);
            model.addAttribute("genres", java.util.Collections.emptyList());
        }
        return "music";
    }

    // ===== TRACK API =====
    @GetMapping("/api/tracks")
    @ResponseBody
    public ResponseEntity<List<MusicTrack>> getAllTracks() {
        return ResponseEntity.ok(musicRepo.getAllTracks());
    }

    @GetMapping("/api/tracks/{id}")
    @ResponseBody
    public ResponseEntity<MusicTrack> getTrackById(@PathVariable int id) {
        return ResponseEntity.ok(musicRepo.getTrackById(id));
    }

    @GetMapping("/api/tracks/search")
    @ResponseBody
    public ResponseEntity<List<MusicTrack>> searchTracks(@RequestParam String keyword) {
        return ResponseEntity.ok(musicRepo.searchTracks(keyword));
    }

    @GetMapping("/api/tracks/genre/{genre}")
    @ResponseBody
    public ResponseEntity<List<MusicTrack>> getTracksByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(musicRepo.getTracksByGenre(genre));
    }

    @PostMapping("/api/tracks/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addTrack(@RequestBody MusicTrack track) {
        Map<String, Object> result = new HashMap<>();
        try {
            musicRepo.addTrack(track);
            result.put("success", true);
            result.put("message", "Lagu berhasil ditambahkan");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/tracks/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTrack(@RequestBody MusicTrack track) {
        Map<String, Object> result = new HashMap<>();
        try {
            musicRepo.updateTrack(track);
            result.put("success", true);
            result.put("message", "Lagu berhasil diperbarui");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/tracks/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteTrack(@RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            musicRepo.deleteTrack(body.get("idTrack"));
            result.put("success", true);
            result.put("message", "Lagu berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/tracks/play/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> playTrack(@PathVariable int id) {
        Map<String, Object> result = new HashMap<>();
        try {
            musicRepo.incrementPlayCount(id);
            result.put("success", true);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // ===== PLAYLIST API =====
    @GetMapping("/api/playlists")
    @ResponseBody
    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        return ResponseEntity.ok(musicRepo.getAllPlaylists());
    }

    @PostMapping("/api/playlists/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addPlaylist(@RequestBody Playlist playlist) {
        Map<String, Object> result = new HashMap<>();
        try {
            musicRepo.addPlaylist(playlist);
            result.put("success", true);
            result.put("message", "Playlist berhasil dibuat");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/playlists/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePlaylist(@RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            musicRepo.deletePlaylist(body.get("idPlaylist"));
            result.put("success", true);
            result.put("message", "Playlist berhasil dihapus");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/api/playlists/{id}/tracks")
    @ResponseBody
    public ResponseEntity<List<MusicTrack>> getPlaylistTracks(@PathVariable int id) {
        return ResponseEntity.ok(musicRepo.getTracksByPlaylist(id));
    }

    @PostMapping("/api/playlists/{id}/add-track")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addTrackToPlaylist(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            musicRepo.addTrackToPlaylist(id, body.get("idTrack"));
            result.put("success", true);
            result.put("message", "Lagu ditambahkan ke playlist");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/api/playlists/{id}/remove-track")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeTrackFromPlaylist(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            musicRepo.removeTrackFromPlaylist(id, body.get("idTrack"));
            result.put("success", true);
            result.put("message", "Lagu dihapus dari playlist");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Gagal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
