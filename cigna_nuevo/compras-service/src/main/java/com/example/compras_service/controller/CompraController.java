package com.example.compras_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.example.compras_service.dto.CompraDTO;
import com.example.compras_service.model.Compra;
import com.example.compras_service.service.CompraService;

@RestController
@RequestMapping("/compras")
public class CompraController {
    private final CompraService compraService;
    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<CompraDTO> crearCompra(
        @Valid @RequestBody CompraDTO compraDto, 
        @RequestHeader("Authorization") String token) {
        try {
            logger.info("POST /compras - Creando compra: idUsuario={}, idServicio={}",
                    compraDto.getIdUsuario(), compraDto.getIdServicio());
            Compra nuevaCompra = compraService.guardar(compraDto.toModel(), token);
            logger.info("Compra creada exitosamente id={}", nuevaCompra.getId());
            return ResponseEntity.ok(CompraDTO.fromModel(nuevaCompra));
        } catch (Exception e) {
            logger.error("Error al crear compra: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<CompraDTO>> listarCompras() {
        logger.info("GET /compras - Listando compras");
        List<Compra> compras = compraService.listar();
        List<CompraDTO> dtos = compras.stream().map(CompraDTO::fromModel).collect(Collectors.toList());
        logger.info("Total compras listadas: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> obtenerCompra(@PathVariable Long id) {
        logger.info("GET /compras/{} - Obteniendo compra", id);
        try {
            Compra compra = compraService.obtenerPorId(id);
            logger.info("Compra obtenida id={}", id);
            return ResponseEntity.ok(CompraDTO.fromModel(compra));
        } catch (Exception e) {
            logger.error("Error al obtener compra id={}: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeCompra(@PathVariable Long id) {
        logger.info("GET /compras/{}/exists", id);
        return ResponseEntity.ok(compraService.existePorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraDTO> actualizarCompra(
        @PathVariable Long id, 
        @Valid @RequestBody CompraDTO compraDto,
        @RequestHeader ("Authorization") String token) {
        logger.info("PUT /compras/{} - Actualizando compra", id);
        try {
            Compra actualizado = compraService.actualizar(id, compraDto.toModel(), token);
            logger.info("Compra actualizada exitosamente id={}", id);
            return ResponseEntity.ok(CompraDTO.fromModel(actualizado));
        } catch (Exception e) {
            logger.error("Error al actualizar compra id={}: {}", id, e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCompra(@PathVariable Long id) {
        logger.info("DELETE /compras/{} - Eliminando compra", id);
        try {
            compraService.eliminar(id);
            logger.info("Compra eliminada exitosamente id={}", id);
            return ResponseEntity.ok("Compra Eliminada Exitosamente");
        } catch (Exception e) {
            logger.error("Error al eliminar compra id={}: {}", id, e.getMessage());
            throw e;
        }
    }
}
