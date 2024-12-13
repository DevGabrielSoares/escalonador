import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Processo {
    int id;
    int tempoChegada;
    int tempoExecucao;
    int tempoEspera;
    int tempoTurnaround;

    public Processo(int id, int tempoChegada, int tempoExecucao) {
        this.id = id;
        this.tempoChegada = tempoChegada;
        this.tempoExecucao = tempoExecucao;
        this.tempoEspera = 0;
        this.tempoTurnaround = 0;
    }

    public static List<Processo> gerarProcessos(int n) {
        List<Processo> processos = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= n; i++) {
            int tempoChegada = random.nextInt(10); 
            int tempoExecucao = random.nextInt(10) + 1;
            processos.add(new Processo(i, tempoChegada, tempoExecucao));
        }

        return processos;
    }
}

class EscalonadorSJF {
    public static List<Processo> escalonarSJF(List<Processo> processos) {
        Random random = new Random();
        List<Processo> processosOrdenados = new ArrayList<>(processos);
        processosOrdenados.sort(Comparator.comparingInt(p -> p.tempoChegada));
        List<Processo> filaProntos = new ArrayList<>();
        List<Processo> resultado = new ArrayList<>();
        int tempoAtual = 0;

        while (!processosOrdenados.isEmpty() || !filaProntos.isEmpty()) {
            while (!processosOrdenados.isEmpty() && processosOrdenados.get(0).tempoChegada <= tempoAtual) {
                filaProntos.add(processosOrdenados.remove(0));
            }

            if (!filaProntos.isEmpty()) {
                filaProntos.sort(Comparator.comparingInt(p -> p.tempoExecucao));
                Processo atual = filaProntos.remove(0);

                atual.tempoEspera = tempoAtual - atual.tempoChegada;
                tempoAtual += atual.tempoExecucao;
                atual.tempoTurnaround = atual.tempoEspera + atual.tempoExecucao;

                if(random.nextInt(10) < 2){
                    atual.tempoTurnaround += 2;
                    tempoAtual += 2;
                }

                resultado.add(atual);
            } else {
                tempoAtual++;
            }
        }

        return resultado;
    }

    public static void imprimirResultados(List<Processo> processos) {
        System.out.println("Processo | Tempo Chegada | Burst Time | Tempo Espera | Tempo Turnaround");
        for (Processo p : processos) {
            System.out.printf("%s         | %d             | %d          | %d           | %d%n",
                p.id, p.tempoChegada, p.tempoExecucao, p.tempoEspera, p.tempoTurnaround);
        }

        double mediaEspera = processos.stream().mapToDouble(p -> p.tempoEspera).average().orElse(0.0);
        double mediaTurnaround = processos.stream().mapToDouble(p -> p.tempoTurnaround).average().orElse(0.0);

        System.out.printf("Tempo médio de espera: %.2f%n", mediaEspera);
        System.out.printf("Tempo médio de turnaround: %.2f%n", mediaTurnaround);
    }

    public static void exportarParaCSV(List<Processo> processos, String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.append("Processo,Tempo Chegada,Burst Time,Tempo Espera,Tempo Turnaround\n");
            for (Processo p : processos) {
                writer.append(String.format("%d,%d,%d,%d,%d\n",
                    p.id, p.tempoChegada, p.tempoExecucao, p.tempoEspera, p.tempoTurnaround));
            }

            double mediaEspera = processos.stream().mapToDouble(p -> p.tempoEspera).average().orElse(0.0);
            double mediaTurnaround = processos.stream().mapToDouble(p -> p.tempoTurnaround).average().orElse(0.0);

            writer.append(String.format("\nMédia,,,%s,%.2f\n", "Espera", mediaEspera));
            writer.append(String.format("Média,,,%s,%.2f\n", "Turnaround", mediaTurnaround));
            System.out.println("Arquivo CSV gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar o arquivo CSV: " + e.getMessage());
        }
    }
}
