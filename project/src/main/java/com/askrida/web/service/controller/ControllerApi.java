
package com.askrida.web.service.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.askrida.web.service.model.AbsensiCheckResult;
import com.askrida.web.service.model.AnggotaResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.askrida.web.service.repository.RepositoryTes;
import com.askrida.web.service.dao.RestDaoImp;
import com.askrida.web.service.model.Rest;
import com.askrida.web.service.model.RestResult;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("rest")
public class ControllerApi {

    @Autowired
    private RestDaoImp restService;

    @Autowired
    private RepositoryTes repTes;

    @GetMapping("/{id}")
    public ResponseEntity<RestResult> getById(@PathVariable int id) {
        return ResponseEntity.ok(repTes.getRestById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<RestResult>> getAll() {
        return ResponseEntity.ok(repTes.getAll());
    }

    @PostMapping("/masuk")
    public ResponseEntity<RestResult> addRest(@RequestBody Rest rest) throws SQLException {
        repTes.addRest(rest);
        RestResult result = repTes.getRestById(repTes.lastestInput());
        result.setKeterangan("jam masuk telah berhasil");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/keluar")
    public ResponseEntity<RestResult> updateRest(@RequestBody Rest rest) throws SQLException {
        restService.updateRest(rest, rest.getId());
        RestResult result = restService.getRestById(rest.getId());
        result.setKeterangan("jam keluar telah berhasil");
        return ResponseEntity.ok(result);
    }



@PostMapping("/tambahanggota")
public ResponseEntity<RestResult> addAnggota(@RequestBody Rest rest) {
    RestResult result = new RestResult();
    try {
        repTes.addAnggota(rest.getKey(), rest.getNama(), rest.getKelas(), rest.getIdDivisi());
        result.setKey(rest.getKey());
        result.setNama(rest.getNama());
        result.setKeterangan("Data Anggota Berhasil Ditambahkan");
        return ResponseEntity.ok(result);
    } catch (Exception e) {
        result.setKeterangan("Terjadi kesalahan: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}

@PostMapping("/tambahanggotabatch")
public ResponseEntity<RestResult> addAnggotaBatch(@RequestBody List<Rest> anggotaList) {
    RestResult result = new RestResult();
    try {
        repTes.addAnggotaBatch(anggotaList);
        result.setKeterangan("Semua anggota berhasil ditambahkan");
        return ResponseEntity.ok(result);
    } catch (Exception e) {
        result.setKeterangan("Terjadi kesalahan: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

}


@PostMapping("/updateanggota")
public ResponseEntity<RestResult> updateAnggota(@RequestBody Rest rest) throws SQLException {
    repTes.updateAnggota(rest.getKey(), rest.getNama(), rest.getKelas(), rest.getIdDivisi());
    RestResult result = new RestResult();
    result.setKey(rest.getKey());
    result.setNama(rest.getNama());
    result.setKeterangan("Data Anggota Berhasil Diupdate");
    return ResponseEntity.ok(result);
}



    public RestDaoImp getRestService() {
		return restService;
	}

	public void setRestService(RestDaoImp restService) {
		this.restService = restService;
	}

	public RepositoryTes getRepTes() {
		return repTes;
	}

	public void setRepTes(RepositoryTes repTes) {
		this.repTes = repTes;
	}

	@PostMapping("/deletedata")
    public ResponseEntity<RestResult> deleteRestById(@RequestBody Rest rest) throws SQLException {
        RestResult result = restService.getRestById(rest.getId());
        result.setKeterangan("Data Berhasil Dihapus");
        restService.deleteRestById(rest.getId());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResult> updateRest(@PathVariable int id, @RequestBody Rest rest) throws SQLException {
        restService.updateRest(rest, id);
        return ResponseEntity.ok(restService.getRestById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult> deleteRest(@PathVariable int id) throws SQLException {
        RestResult result = restService.getRestById(id);
        restService.deleteRestById(id);
        return ResponseEntity.ok(result);
 
    }


@PostMapping("/absensi")
public ResponseEntity<RestResult> absensi(@RequestBody Rest rest) {
    try {
        RestResult result = repTes.handleAbsensi(rest);
        if (result.getKeterangan() != null && result.getKeterangan().toLowerCase().contains("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    } catch (Exception e) {
        RestResult errorResult = new RestResult();
        errorResult.setKeterangan("Terjadi kesalahan: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
}





@PostMapping("/cekabsensibatch")
public ResponseEntity<List<AbsensiCheckResult>> checkAbsensiBatch(@RequestBody List<String> keys) {
    List<AbsensiCheckResult> found = repTes.checkAbsensiDetails(keys);
    return ResponseEntity.ok(found);
}

@GetMapping("/anggota")
@ResponseBody
public ResponseEntity<List<AnggotaResult>> getAllAnggota() {
    return ResponseEntity.ok(repTes.getAllAnggota());
}

@PostMapping("/deleteanggota")
public ResponseEntity<RestResult> deleteAnggota(@RequestBody Rest rest) {
    RestResult result = new RestResult();
    try {
        repTes.deleteAnggota(rest.getKey());
        result.setKey(rest.getKey());
        result.setKeterangan("Data Anggota Berhasil Dihapus");
        return ResponseEntity.ok(result);
    } catch (Exception e) {
        result.setKeterangan("Terjadi kesalahan: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}



}
