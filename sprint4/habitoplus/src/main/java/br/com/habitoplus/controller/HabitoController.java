package br.com.habitoplus.controller;

import br.com.habitoplus.dto.ErroResponse;
import br.com.habitoplus.dto.HabitoRequest;
import br.com.habitoplus.dto.HabitoResponse;
import br.com.habitoplus.service.HabitoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST responsável pelos endpoints de hábitos saudáveis.
 * Todos os endpoints requerem token JWT Bearer.
 */
@RestController
@RequestMapping("/api/v1/habitos")
@Tag(name = "Hábitos", description = "CRUD completo de registros de hábitos saudáveis")
public class HabitoController {

    @Autowired
    private HabitoService service;

    @Operation(summary = "Registrar hábito", description = "Cria um novo registro e calcula Milhas de Saúde.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado com sucesso",
                    content = @Content(schema = @Schema(implementation = HabitoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido")
    })
    @PostMapping
    public ResponseEntity<HabitoResponse> registrar(@RequestBody @Valid HabitoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarHabito(request));
    }

    @Operation(summary = "Listar todos os hábitos")
    @GetMapping
    public ResponseEntity<List<HabitoResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar hábito por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Encontrado"),
            @ApiResponse(responseCode = "404", description = "Não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HabitoResponse> buscarPorId(
            @Parameter(description = "ID do hábito", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Listar hábitos por colaborador")
    @GetMapping("/colaborador/{colaboradorId}")
    public ResponseEntity<List<HabitoResponse>> listarPorColaborador(
            @Parameter(description = "ID do colaborador", example = "COL-001") @PathVariable String colaboradorId) {
        return ResponseEntity.ok(service.listarPorColaborador(colaboradorId));
    }

    @Operation(summary = "Atualizar hábito")
    @PutMapping("/{id}")
    public ResponseEntity<HabitoResponse> atualizar(
            @PathVariable Long id, @RequestBody @Valid HabitoRequest request) {
        return ResponseEntity.ok(service.atualizarHabito(id, request));
    }

    @Operation(summary = "Remover hábito", description = "Requer perfil ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão (requer ADMIN)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarHabito(id);
        return ResponseEntity.noContent().build();
    }
}
