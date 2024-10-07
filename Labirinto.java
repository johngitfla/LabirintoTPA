import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Labirinto {

    private static int[][] labirinto;
    private static boolean[][] visitado;
    private static int linhas, colunas;
    private static List<int[]> caminho = new ArrayList<>();

    public static void main(String[] args) {
        String filePath = "src/labirinto.csv";
        labirinto = lerLabirintoCSV(filePath);
        if (labirinto == null) {
            System.out.println("Erro ao carregar o labirinto.");
            return;
        }

        linhas = labirinto.length;
        colunas = labirinto[0].length;
        visitado = new boolean[linhas][colunas];

        // Inicia a busca a partir da posição [0, 0]
        if (buscarCaminho(0, 0)) {
            imprimirLabirinto();
        } else {
            System.out.println("Nenhum caminho encontrado.");
        }
    }

    private static int[][] lerLabirintoCSV(String filePath) {
        List<int[]> labirintoTemp = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                // Na primeira linha, remover o BOM se presente
                if (primeiraLinha && linha.charAt(0) == '\uFEFF') {
                    linha = linha.substring(1); // Remove o caractere BOM
                }
                primeiraLinha = false;

                // Divide a linha nos valores separados por ';'
                String[] valores = linha.split(";");
                int[] linhaLabirinto = Arrays.stream(valores).mapToInt(Integer::parseInt).toArray();
                labirintoTemp.add(linhaLabirinto);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return labirintoTemp.toArray(new int[0][]);
    }

    private static boolean buscarCaminho(int x, int y) {
        // Se a posição for fora dos limites ou uma parede ou já visitada, retorna falso
        if (x < 0 || x >= linhas || y < 0 || y >= colunas || labirinto[x][y] == 0 || visitado[x][y]) {
            return false;
        }

        // Marca a posição como visitada
        visitado[x][y] = true;
        caminho.add(new int[]{x, y});

        // Se chegou à saída, retorna verdadeiro
        if (x == linhas - 1 && y == colunas - 1) {
            return true;
        }

        // Tenta ir para as quatro direções: cima, direita, baixo, esquerda
        if (buscarCaminho(x - 1, y) || buscarCaminho(x, y + 1) || buscarCaminho(x + 1, y) || buscarCaminho(x, y - 1)) {
            return true;
        }

        // Se não encontrou caminho, desfaz a posição do caminho
        caminho.remove(caminho.size() - 1);
        return false;
    }

    private static void imprimirLabirinto() {
        char[][] labirintoFinal = new char[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (labirinto[i][j] == 1) {
                    labirintoFinal[i][j] = '0'; 
                } else {
                    labirintoFinal[i][j] = ' '; 
                }
            }
        }

        // Marca o caminho com 'X'
        for (int[] pos : caminho) {
            labirintoFinal[pos[0]][pos[1]] = 'X';
        }

        // Imprime o labirinto final
        for (char[] linha : labirintoFinal) {
            for (char c : linha) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
}
