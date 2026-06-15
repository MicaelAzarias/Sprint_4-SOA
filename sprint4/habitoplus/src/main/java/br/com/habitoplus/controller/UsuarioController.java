package br.com.habitoplus.controller;

import br.com.habitoplus.dto.UsuarioRequest;
import br.com.habitoplus.dto.UsuarioResponse;
import br.com.habitoplus.service.UsuarioService;
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
 * Controller REST para gerenciamento de colaboradores.
 * Todos os endpoints requerem token JWT Bearer.
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Colaboradores", description = "CRUD de colaboradores do sistema HábitoPlus")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Operation(summary = "Cadastrar colaborador", description = "Cria um novo colaborador no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request));
    }

    @Operation(summary = "Listar todos os colaboradores")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar colaborador por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colaborador encontrado"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado")
    })
    @GetMapping("/{colaboradorId}")
    public ResponseEntity<UsuarioResponse> buscarPorColaboradorId(
            @Parameter(description = "ID do colaborador", example = "COL-001")
            @PathVariable String colaboradorId) {
        return ResponseEntity.ok(service.buscarPorColaboradorId(colaboradorId));
    }

    @Operation(summary = "Remover colaborador", description = "Remove o colaborador e todos os seus hábitos. Requer perfil ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão (requer ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado")
    })
    @DeleteMapping("/{colaboradorId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do colaborador", example = "COL-001")
            @PathVariable String colaboradorId) {
        service.deletar(colaboradorId);
        return ResponseEntity.noContent().build();
    }
}
