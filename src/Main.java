import java.util.List;

public class Main {
    public static void main(String[] args) {
        int n = 1000;
        List<Processo> processos = Processo.gerarProcessos(n);

        List<Processo> processosEscalonados = EscalonadorSJF.escalonarSJF(processos);

        EscalonadorSJF.imprimirResultados(processosEscalonados);
        EscalonadorSJF.exportarParaCSV(processosEscalonados, "resultados_escalonamento.csv");
    }
}
