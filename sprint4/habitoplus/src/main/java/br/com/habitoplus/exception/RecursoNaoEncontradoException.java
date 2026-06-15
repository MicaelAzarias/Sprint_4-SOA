package br.com.habitoplus.exception;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado no banco de dados.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
