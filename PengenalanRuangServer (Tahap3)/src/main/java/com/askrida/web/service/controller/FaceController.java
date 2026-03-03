package com.askrida.web.service.controller;

import com.askrida.web.service.model.FaceAbsensiRequest;
import com.askrida.web.service.model.FaceData;
import com.askrida.web.service.model.Rest;
import com.askrida.web.service.model.RestResult;
import com.askrida.web.service.repository.FaceRepository;
import com.askrida.web.service.repository.RepositoryTes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller untuk fitur Face Recognition.
 * 
 * Endpoints:
 * - POST   /api/face/register    -> Simpan face descriptor baru
 * - GET    /api/face/all         -> Ambil semua face data (untuk training di client)
 * - GET    /api/face/{nim}       -> Ambil face data per NIM
 * - DELETE /api/face/{idFace}    -> Hapus satu face data
 * - DELETE /api/face/nim/{nim}   -> Hapus semua face data per NIM
 * - POST   /api/face/absensi    -> Absensi via face recognition
 * - GET    /api/face/count/{nim} -> Hitung jumlah foto wajah per NIM
 */
@RestController
@RequestMapping("/api/face")
public class FaceController {

    @Autowired
    private FaceRepository faceRepository;

    @Autowired
    private RepositoryTes repositoryTes;

    /**
     * Simpan face descriptor baru.
     * Client mengirim: nim, label (nama), faceDescriptor (JSON array 128 float), imageData (base64)
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerFace(@RequestBody FaceData faceData) {
        Map<String, Object> response = new HashMap<>();
        try {
            faceRepository.saveFaceData(faceData);
            int count = faceRepository.countFaceByNim(faceData.getNim());
            response.put("success", true);
            response.put("message", "Data wajah berhasil disimpan untuk " + faceData.getLabel());
            response.put("totalFoto", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gagal menyimpan data wajah: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Ambil semua face data untuk di-load di client sebagai training data.
     * Client akan membangun FaceMatcher dari data ini.
     */
    @GetMapping("/all")
    public ResponseEntity<List<FaceData>> getAllFaceData() {
        return ResponseEntity.ok(faceRepository.getAllFaceData());
    }

    /**
     * Ambil face data berdasarkan NIM.
     */
    @GetMapping("/{nim}")
    public ResponseEntity<List<FaceData>> getFaceByNim(@PathVariable String nim) {
        return ResponseEntity.ok(faceRepository.getFaceDataByNim(nim));
    }

    /**
     * Hapus satu data wajah.
     */
    @DeleteMapping("/{idFace}")
    public ResponseEntity<Map<String, Object>> deleteFace(@PathVariable int idFace) {
        Map<String, Object> response = new HashMap<>();
        try {
            faceRepository.deleteFaceData(idFace);
            response.put("success", true);
            response.put("message", "Data wajah berhasil dihapus");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gagal menghapus: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Hapus semua data wajah per NIM.
     */
    @DeleteMapping("/nim/{nim}")
    public ResponseEntity<Map<String, Object>> deleteFaceByNim(@PathVariable String nim) {
        Map<String, Object> response = new HashMap<>();
        try {
            faceRepository.deleteFaceDataByNim(nim);
            response.put("success", true);
            response.put("message", "Semua data wajah NIM " + nim + " berhasil dihapus");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gagal menghapus: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Absensi via face recognition.
     * Setelah client mengenali wajah, kirim NIM dan confidence level ke sini.
     * Server akan memproses absensi (masuk/keluar) sama seperti absensi manual.
     */
    @PostMapping("/absensi")
    public ResponseEntity<Map<String, Object>> faceAbsensi(@RequestBody FaceAbsensiRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Log absensi wajah
            faceRepository.logFaceAbsensi(request.getNim(), request.getConfidence());

            // Proses absensi (masuk/keluar) menggunakan logic yang sudah ada
            Rest rest = new Rest();
            rest.setKey(request.getNim());
            RestResult result = repositoryTes.handleAbsensi(rest);

            response.put("success", true);
            response.put("nim", request.getNim());
            response.put("confidence", request.getConfidence());
            response.put("keterangan", result.getKeterangan());
            response.put("nama", result.getNama());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gagal absensi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Hitung jumlah foto wajah per NIM.
     */
    @GetMapping("/count/{nim}")
    public ResponseEntity<Map<String, Object>> countFaces(@PathVariable String nim) {
        Map<String, Object> response = new HashMap<>();
        response.put("nim", nim);
        response.put("count", faceRepository.countFaceByNim(nim));
        return ResponseEntity.ok(response);
    }
}
