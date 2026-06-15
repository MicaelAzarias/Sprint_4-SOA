package br.com.habitoplus.exception;

import br.com.habitoplus.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Handler global de exceções.
 * Intercepta erros e retorna respostas HTTP padronizadas com mensagens legíveis.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata erros de validação dos campos da requisição (@Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ErroResponse erro = ErroResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Erro de Validação")
                .mensagem(mensagem)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(erro);
    }

    /**
     * Trata recurso não encontrado (404).
     */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroResponse erro = ErroResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .erro("Recurso Não Encontrado")
                .mensagem(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    /**
     * Trata erros genéricos não capturados pelos demais handlers.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleErroGenerico(Exception ex) {
        ErroResponse erro = ErroResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .erro("Erro Interno do Servidor")
                .mensagem("Ocorreu um erro inesperado. Entre em contato com o suporte.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
