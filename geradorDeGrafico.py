import pandas as pd
import matplotlib.pyplot as plt

arquivo_csv = "resultados_escalonamento.csv"
df = pd.read_csv(arquivo_csv)

print(df.head())

plt.figure(figsize=(10, 6))
plt.plot(df['Processo'], df['Burst Time'], label='Burst Time', marker='o')
plt.plot(df['Processo'], df['Tempo Espera'], label='Tempo de Espera', marker='x')
plt.plot(df['Processo'], df['Tempo Turnaround'], label='Turnaround', marker='s')
plt.xlabel('Processo')
plt.ylabel('Tempo')
plt.title('Comparação de Tempos')
plt.legend()
plt.grid(True)
plt.savefig("comparacao_tempos.png")
plt.show()

plt.figure(figsize=(8, 5))
plt.hist(df['Tempo Espera'], bins=15, color='skyblue', edgecolor='black')
plt.xlabel('Tempo de Espera')
plt.ylabel('Frequência')
plt.title('Distribuição do Tempo de Espera')
plt.grid(axis='y')
plt.savefig("histograma_tempo_espera.png")
plt.show()
