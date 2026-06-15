package br.com.habitoplus.enums;

/**
 * Enum que representa os tipos de atividade suportados pelo sistema.
 * Cada tipo possui um multiplicador de pontos associado.
 */
public enum TipoAtividade {

    CORRIDA(10, "Corrida ou caminhada acelerada"),
    CAMINHADA(5, "Caminhada leve"),
    MUSCULACAO(8, "Musculação ou treino de força"),
    YOGA(6, "Yoga ou alongamento"),
    MEDITACAO(4, "Meditação ou mindfulness"),
    CICLISMO(9, "Ciclismo ou spinning"),
    NATACAO(10, "Natação"),
    OUTROS(3, "Outras atividades físicas");

    private final int multiplicadorPontos;
    private final String descricao;

    TipoAtividade(int multiplicadorPontos, String descricao) {
        this.multiplicadorPontos = multiplicadorPontos;
        this.descricao = descricao;
    }

    public int getMultiplicadorPontos() {
        return multiplicadorPontos;
    }

    public String getDescricao() {
        return descricao;
    }
}
