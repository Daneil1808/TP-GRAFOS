import java.util.*;

/**
 * Classe principal que contém o método principal para executar o programa.
 * O programa lê a entrada do usuário, constrói o grafo e executa diversas operações sobre ele,
 * como verificação de conectividade, bipartição, eulerianidade, entre outros.
 */
public class Main {

    /**
     * Método principal que lê a entrada do usuário, cria o grafo e executa as funções solicitadas.
     *
     * @param args Argumentos da linha de comando (não utilizados neste programa).
     */
    public static void main(String[] args) {
        // Cria um Scanner para ler a entrada do usuário
        Scanner scanner = new Scanner(System.in);

        // Leitura do número de vértices e arestas
        int numVertices = scanner.nextInt(); // Lê o número de vértices do grafo
        int numArestas = scanner.nextInt(); // Lê o número de arestas do grafo
        scanner.nextLine(); // Consome a quebra de linha após os inteiros

        // Leitura do tipo de grafo (direcionado ou não)
        String tipoGrafo = scanner.nextLine().trim(); // Lê a linha contendo o tipo do grafo e remove espaços em branco
        boolean isDirecionado = tipoGrafo.equals("direcionado"); // Define se o grafo é direcionado ou não

        // Inicialização das estruturas de dados
        // Mapeia vértices para suas listas de adjacência
        Map<Integer, List<Integer>> listaAdjacencia = new HashMap<>();
        // Mapeia arestas (representadas como pares de vértices) para seus pesos
        Map<List<Integer>, Integer> pesos = new HashMap<>();
        // Mapeia arestas (representadas como pares de vértices) para seus IDs
        Map<List<Integer>, Integer> idsArestas = new HashMap<>();
        // Conjunto de todos os vértices do grafo
        Set<Integer> vertices = new HashSet<>();

        // Inicializa a lista de adjacência para cada vértice e adiciona todos os vértices ao conjunto
        for (int i = 0; i < numVertices; i++) {
            listaAdjacencia.put(i, new ArrayList<>()); // Cada vértice tem uma lista de adjacência inicializada como uma lista vazia
            vertices.add(i); // Adiciona o vértice ao conjunto de vértices
        }

        // Leitura das arestas e suas informações
        for (int i = 0; i < numArestas; i++) {
            if (scanner.hasNextInt()) { // Verifica se há mais inteiros para ler
                int idAresta = scanner.nextInt(); // Lê o ID da aresta
                int verticeU = scanner.nextInt(); // Lê o vértice de origem da aresta
                int verticeV = scanner.nextInt(); // Lê o vértice de destino da aresta
                int pesoAresta = scanner.nextInt(); // Lê o peso da aresta
                scanner.nextLine(); // Consome a quebra de linha após os inteiros

                // Adiciona a aresta à lista de adjacência do vértice de origem
                if (listaAdjacencia.containsKey(verticeU)) {
                    listaAdjacencia.get(verticeU).add(verticeV);
                } else {
                    listaAdjacencia.put(verticeU, new ArrayList<>(Collections.singletonList(verticeV)));
                }

                // Se o grafo não é direcionado, adiciona também a aresta no sentido inverso
                if (!isDirecionado) {
                    if (listaAdjacencia.containsKey(verticeV)) {
                        listaAdjacencia.get(verticeV).add(verticeU);
                    } else {
                        listaAdjacencia.put(verticeV, new ArrayList<>(Collections.singletonList(verticeU)));
                    }
                }

                // Adiciona o peso e o ID da aresta no mapa de pesos e IDs
                pesos.put(Arrays.asList(verticeU, verticeV), pesoAresta);
                idsArestas.put(Arrays.asList(verticeU, verticeV), idAresta);
                if (!isDirecionado) {
                    pesos.put(Arrays.asList(verticeV, verticeU), pesoAresta); // Adiciona o peso da aresta no sentido inverso para grafos não direcionados
                    idsArestas.put(Arrays.asList(verticeV, verticeU), idAresta); // Adiciona o ID da aresta no sentido inverso para grafos não direcionados
                }
            }
        }

        // Execução das funções solicitadas e impressão dos resultados
        // Verifica se o grafo é conexo
        System.out.println(verificarConexo(listaAdjacencia, vertices, isDirecionado) ? "1" : "0");
        // Verifica se o grafo é bipartido
        System.out.println(verificarBipartido(listaAdjacencia, isDirecionado) ? "1" : "0");
        // Verifica se o grafo é euleriano
        System.out.println(verificarEuleriano(listaAdjacencia, isDirecionado) ? "1" : "0");
        // Verifica se o grafo possui ciclo
        System.out.println(possuiCiclo(listaAdjacencia) ? "1" : "0");
        // Lista os componentes conexos do grafo
        System.out.println(listarComponentesConexas(listaAdjacencia));
        // Lista os componentes fortemente conexos, se o grafo for direcionado; caso contrário, imprime -1
        System.out.println(isDirecionado ? formatarListasEncadeadas(listarComponentesFortementeConexas(listaAdjacencia)) : "-1");
        // Lista a trilha euleriana, se existir
        System.out.println(listarTrilhaEuleriana(listaAdjacencia, isDirecionado));
        // Lista os vértices de articulação do grafo
        System.out.println(listarVerticesArticulacao(listaAdjacencia));
        // Lista as arestas ponte do grafo
        System.out.println(listarArestasPonte(listaAdjacencia));
        // Gera e lista a árvore de profundidade
        System.out.println(gerarArvoreProfundidade(listaAdjacencia, idsArestas));
        // Gera e lista a árvore de largura
        System.out.println(gerarArvoreLargura(listaAdjacencia, idsArestas));
        // Gera e lista a árvore geradora mínima
        System.out.println(gerarArvoreGeradoraMinima(listaAdjacencia, pesos, idsArestas));
        // Lista a ordenação topológica, se o grafo for direcionado; caso contrário, imprime -1
        System.out.println(isDirecionado ? ordenarTopologicamente(listaAdjacencia) : "-1");
        // Calcula e lista o caminho mínimo entre o vértice 0 e o vértice (numVertices - 1), se possível
        System.out.println(!isDirecionado && podeCalcularCaminhoMinimo(listaAdjacencia, 0, numVertices - 1) ?
                calcularCaminhoMinimo(listaAdjacencia, pesos, 0, numVertices - 1) : "-1");
        // Calcula e lista o fluxo máximo entre o vértice 0 e o vértice (numVertices - 1), se o grafo for direcionado; caso contrário, imprime -1
        System.out.println(isDirecionado ? calcularFluxoMaximo(listaAdjacencia, pesos, 0, numVertices - 1) : "-1");
        // Gera e lista o fecho transitivo, se o grafo for direcionado; caso contrário, imprime -1
        System.out.println(isDirecionado ? gerarFechoTransitivo(listaAdjacencia, 0) : "-1");

        // Fecha o scanner para liberar os recursos
        scanner.close();
    }

    /**
     * Verifica se o grafo é conexo.
     *
     * @param listaAdjacencia Mapa de listas de adjacência do grafo.
     * @param vertices Conjunto de todos os vértices do grafo.
     * @param isDirecionado Indica se o grafo é direcionado (true) ou não direcionado (false).
     * @return true se o grafo for conexo, false caso contrário.
     */
    public static Boolean verificarConexo(Map<Integer, List<Integer>> listaAdjacencia, Set<Integer> vertices, boolean isDirecionado) {
        if (vertices.isEmpty()) {
            return true; // Considerar grafo vazio como conexo
        }

        // Função auxiliar para fazer a BFS e alcançar todos os vértices
        Set<Integer> bfsVisitados = new HashSet<>();
        Queue<Integer> bfsFila = new LinkedList<>();
        int verticeInicial = vertices.iterator().next();
        bfsFila.add(verticeInicial);
        bfsVisitados.add(verticeInicial);

        // Realiza a BFS a partir do vértice inicial
        while (!bfsFila.isEmpty()) {
            int vertice = bfsFila.poll();
            for (int vizinho : listaAdjacencia.getOrDefault(vertice, new ArrayList<>())) {
                if (!bfsVisitados.contains(vizinho)) {
                    bfsVisitados.add(vizinho);
                    bfsFila.add(vizinho);
                }
            }
        }

        if (!isDirecionado) {
            // Verificação para grafos não direcionados
            return bfsVisitados.size() == vertices.size(); // Se todos os vértices foram visitados, o grafo é conexo
        } else {
            // Verificação para grafos direcionados
            // Verifica a conectividade direta
            if (bfsVisitados.size() != vertices.size()) {
                return false; // Se nem todos os vértices foram visitados, o grafo não é conexo
            }

            // Inverte as arestas para verificar a conectividade em sentido reverso
            Map<Integer, List<Integer>> listaAdjacenciaInversa = new HashMap<>();
            for (int vertice : listaAdjacencia.keySet()) {
                listaAdjacenciaInversa.putIfAbsent(vertice, new ArrayList<>());
                for (int vizinho : listaAdjacencia.get(vertice)) {
                    listaAdjacenciaInversa.putIfAbsent(vizinho, new ArrayList<>());
                    listaAdjacenciaInversa.get(vizinho).add(vertice);
                }
            }

            Set<Integer> visitadosInverso = new HashSet<>();
            Queue<Integer> filaInversa = new LinkedList<>();
            filaInversa.add(verticeInicial);
            visitadosInverso.add(verticeInicial);

            // Realiza a BFS no grafo com arestas invertidas
            while (!filaInversa.isEmpty()) {
                int vertice = filaInversa.poll();
                for (int vizinho : listaAdjacenciaInversa.getOrDefault(vertice, new ArrayList<>())) {
                    if (!visitadosInverso.contains(vizinho)) {
                        visitadosInverso.add(vizinho);
                        filaInversa.add(vizinho);
                    }
                }
            }

            // Verifica a conectividade reversa
            return visitadosInverso.size() == vertices.size(); // Se todos os vértices foram visitados no grafo inverso, é fortemente conexo
        }
    }

    /**
     * Verifica se o grafo é bipartido.
     *
     * @param listaAdjacencia Mapa de listas de adjacência do grafo.
     * @param isDirecionado Indica se o grafo é direcionado (true) ou não direcionado (false).
     * @return true se o grafo for bipartido, false caso contrário.
     */
    public static Boolean verificarBipartido(Map<Integer, List<Integer>> listaAdjacencia, boolean isDirecionado) {
        if (isDirecionado) {
            return false; // Bipartição não é aplicável para grafos direcionados
        }

        // Mapa para armazenar a cor de cada vértice
        Map<Integer, Integer> cores = new HashMap<>();

        for (int vertice : listaAdjacencia.keySet()) {
            // Se o vértice ainda não foi visitado
            if (!cores.containsKey(vertice)) {
                Queue<Integer> fila = new LinkedList<>();
                fila.add(vertice);
                cores.put(vertice, 0); // Atribui a cor 0 ao vértice inicial

                while (!fila.isEmpty()) {
                    int atual = fila.poll();
                    for (int vizinho : listaAdjacencia.get(atual)) {
                        if (!cores.containsKey(vizinho)) {
                            // Atribui a cor oposta ao vizinho
                            cores.put(vizinho, 1 - cores.get(atual));
                            fila.add(vizinho);
                        } else if (cores.get(vizinho).equals(cores.get(atual))) {
                            // Se um vizinho tem a mesma cor que o vértice atual, o grafo não é bipartido
                            return false;
                        }
                    }
                }
            }
        }
        return true; // O grafo é bipartido se não houver conflitos de cores
    }

    /**
     * Verifica se o grafo é euleriano.
     * <p>
     * Um grafo euleriano é um grafo que possui um ciclo que percorre cada aresta exatamente uma vez.
     * Existem duas condições a serem verificadas, dependendo se o grafo é direcionado ou não:
     * <ul>
     *     <li>Para grafos direcionados: O grafo é euleriano se, para cada vértice, o grau de entrada é igual ao grau de saída.</li>
     *     <li>Para grafos não direcionados: O grafo é euleriano se todos os vértices tiverem grau par.</li>
     * </ul>
     * </p>
     *
     * @param listaAdjacencia Mapa onde as chaves são vértices e os valores são listas de vértices adjacentes.
     * @param isDirecionado Indica se o grafo é direcionado (true) ou não direcionado (false).
     * @return true se o grafo for euleriano, false caso contrário.
     */
    public static Boolean verificarEuleriano(Map<Integer, List<Integer>> listaAdjacencia, boolean isDirecionado) {
        if (isDirecionado) {
            // Verifica se o grafo é euleriano (direcionado)
            Map<Integer, Integer> grauEntrada = new HashMap<>();
            Map<Integer, Integer> grauSaida = new HashMap<>();

            // Calcula o grau de entrada e o grau de saída de cada vértice
            for (int vertice : listaAdjacencia.keySet()) {
                grauSaida.put(vertice, listaAdjacencia.get(vertice).size());
                for (int vizinho : listaAdjacencia.get(vertice)) {
                    grauEntrada.put(vizinho, grauEntrada.getOrDefault(vizinho, 0) + 1);
                }
            }

            // Verifica se o grau de entrada é igual ao grau de saída para cada vértice
            for (int vertice : listaAdjacencia.keySet()) {
                if (!grauEntrada.getOrDefault(vertice, 0).equals(grauSaida.get(vertice))) {
                    return false; // Se houver vértices com grau de entrada diferente do grau de saída, o grafo não é euleriano
                }
            }

            return true; // O grafo é euleriano se todos os vértices tiverem grau de entrada igual ao grau de saída
        } else {
            // Verifica se o grafo é euleriano (não direcionado)
            int impares = 0;

            // Conta o número de vértices com grau ímpar
            for (int vertice : listaAdjacencia.keySet()) {
                if (listaAdjacencia.get(vertice).size() % 2 != 0) {
                    impares++;
                }
            }

            return impares == 0; // O grafo é euleriano se todos os vértices tiverem grau par
        }
    }

    /**
     * Verifica se o grafo possui ciclo usando busca em profundidade (DFS).
     * <p>
     * Um ciclo em um grafo é uma sequência de vértices onde o primeiro vértice é igual ao último, e cada aresta
     * na sequência é única. Este método utiliza DFS para detectar a presença de ciclos em um grafo não direcionado.
     * </p>
     *
     * @param listaAdjacencia Mapa onde as chaves são vértices e os valores são listas de vértices adjacentes.
     * @return true se o grafo possuir um ciclo, false caso contrário.
     */
    public static Boolean possuiCiclo(Map<Integer, List<Integer>> listaAdjacencia) {
        Set<Integer> visitados = new HashSet<>();
        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice) && dfsCiclo(vertice, -1, visitados, listaAdjacencia)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método auxiliar para verificar a presença de ciclos utilizando DFS.
     * <p>
     * Este método é chamado recursivamente para percorrer o grafo e identificar ciclos.
     * Um ciclo é identificado se encontramos um vértice que já foi visitado e não é o pai do vértice atual.
     * </p>
     *
     * @param vertice O vértice atual sendo explorado.
     * @param pai O vértice pai do vértice atual na DFS.
     * @param visitados Conjunto de vértices já visitados durante a exploração.
     * @param listaAdjacencia Mapa onde as chaves são vértices e os valores são listas de vértices adjacentes.
     * @return true se um ciclo for detectado, false caso contrário.
     */
    private static Boolean dfsCiclo(int vertice, int pai, Set<Integer> visitados, Map<Integer, List<Integer>> listaAdjacencia) {
        visitados.add(vertice);
        for (int vizinho : listaAdjacencia.get(vertice)) {
            if (!visitados.contains(vizinho)) {
                if (dfsCiclo(vizinho, vertice, visitados, listaAdjacencia)) {
                    return true;
                }
            } else if (vizinho != pai) {
                return true;
            }
        }
        return false;
    }


    /**
     * Lista os componentes conexos do grafo.
     *
     * @param listaAdjacencia Mapa de listas de adjacência do grafo.
     * @return String formatada contendo os componentes conexos.
     */
    public static String listarComponentesConexas(Map<Integer, List<Integer>> listaAdjacencia) {
        List<List<Integer>> componentes = new ArrayList<>();
        Set<Integer> visitados = new HashSet<>();

        // Ordena as chaves do mapa para garantir a ordem lexicográfica
        List<Integer> vertices = new ArrayList<>(listaAdjacencia.keySet());
        Collections.sort(vertices);

        for (int vertice : vertices) {
            if (!visitados.contains(vertice)) {
                List<Integer> componente = new ArrayList<>();
                Queue<Integer> fila = new LinkedList<>();
                fila.add(vertice);
                visitados.add(vertice);

                while (!fila.isEmpty()) {
                    int atual = fila.poll();
                    componente.add(atual);
                    for (int vizinho : listaAdjacencia.get(atual)) {
                        if (!visitados.contains(vizinho)) {
                            visitados.add(vizinho);
                            fila.add(vizinho);
                        }
                    }
                }
                Collections.sort(componente);
                componentes.add(componente);
            }
        }

        // Ordena a lista de componentes em ordem lexicográfica
        // Utiliza um comparador para comparar listas lexicograficamente
        componentes.sort((c1, c2) -> {
            int minLength = Math.min(c1.size(), c2.size());
            for (int i = 0; i < minLength; i++) {
                int comp = Integer.compare(c1.get(i), c2.get(i));
                if (comp != 0) return comp;
            }
            return Integer.compare(c1.size(), c2.size());
        });

        // Converte a lista de componentes em uma string formatada
        StringBuilder sb = new StringBuilder();
        for (List<Integer> componente : componentes) {
            sb.append(formatarListaComEspacos(componente)).append("\n");
        }

        return sb.toString().trim(); // Remove a última nova linha desnecessária
    }


    /**
     * Lista os componentes fortemente conexos de um grafo utilizando o algoritmo de Kosaraju.
     * O grafo deve ser representado por uma lista de adjacência.
     *
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo (vértices -> lista de vizinhos).
     * @return Lista de listas de inteiros, onde cada lista representa um componente fortemente conexo.
     */
    public static List<List<Integer>> listarComponentesFortementeConexas(Map<Integer, List<Integer>> listaAdjacencia) {
        List<List<Integer>> componentes = new ArrayList<>();
        Stack<Integer> pilha = new Stack<>();
        Set<Integer> visitados = new HashSet<>();

        // Passo 1: Realizar uma DFS para empilhar os vértices na ordem do término da visita.
        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice)) {
                dfsEmpilhar(vertice, visitados, listaAdjacencia, pilha);
            }
        }

        // Passo 2: Transpor o grafo (inverter as direções das arestas).
        Map<Integer, List<Integer>> grafoTransposto = transporGrafo(listaAdjacencia);

        // Passo 3: Realizar uma DFS no grafo transposto na ordem dos vértices empilhados.
        visitados.clear();
        while (!pilha.isEmpty()) {
            int vertice = pilha.pop();
            if (!visitados.contains(vertice)) {
                List<Integer> componente = new ArrayList<>();
                dfsComponentes(vertice, visitados, grafoTransposto, componente);
                Collections.sort(componente); // Ordena os vértices do componente para garantir consistência.
                componentes.add(componente);
            }
        }

        return componentes;
    }

    /**
     * Realiza uma busca em profundidade (DFS) e empilha os vértices na ordem de término.
     *
     * @param vertice         O vértice atual sendo visitado.
     * @param visitados       Conjunto de vértices que já foram visitados.
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo.
     * @param pilha           Pilha onde os vértices são empilhados na ordem de término.
     */
    private static void dfsEmpilhar(int vertice, Set<Integer> visitados, Map<Integer, List<Integer>> listaAdjacencia, Stack<Integer> pilha) {
        // Marca o vértice como visitado
        visitados.add(vertice);
        // Itera sobre todos os vizinhos do vértice
        for (int vizinho : listaAdjacencia.get(vertice)) {
            // Se o vizinho ainda não foi visitado, realiza a chamada recursiva
            if (!visitados.contains(vizinho)) {
                dfsEmpilhar(vizinho, visitados, listaAdjacencia, pilha);
            }
        }
        pilha.push(vertice); // Adiciona o vértice à pilha após visitar todos os seus vizinhos.
    }

    /**
     * Realiza uma busca em profundidade (DFS) no grafo transposto para encontrar os componentes fortemente conexos.
     *
     * @param vertice         O vértice atual sendo visitado.
     * @param visitados       Conjunto de vértices que já foram visitados.
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo transposto.
     * @param componente      Lista onde os vértices do componente atual são armazenados.
     */
    private static void dfsComponentes(int vertice, Set<Integer> visitados, Map<Integer, List<Integer>> listaAdjacencia, List<Integer> componente) {
        visitados.add(vertice);
        componente.add(vertice); // Adiciona o vértice ao componente atual.
        for (int vizinho : listaAdjacencia.get(vertice)) {
            if (!visitados.contains(vizinho)) {
                dfsComponentes(vizinho, visitados, listaAdjacencia, componente);
            }
        }
    }

    /**
     * Transpõe o grafo, ou seja, inverte as direções das arestas.
     *
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo original.
     * @return Mapa que representa a lista de adjacência do grafo transposto.
     */
    private static Map<Integer, List<Integer>> transporGrafo(Map<Integer, List<Integer>> listaAdjacencia) {
        Map<Integer, List<Integer>> grafoTransposto = new HashMap<>();
        for (int vertice : listaAdjacencia.keySet()) {
            grafoTransposto.putIfAbsent(vertice, new ArrayList<>());
            for (int vizinho : listaAdjacencia.get(vertice)) {
                grafoTransposto.putIfAbsent(vizinho, new ArrayList<>());
                grafoTransposto.get(vizinho).add(vertice); // Adiciona a aresta inversa no grafo transposto.
            }
        }
        return grafoTransposto;
    }

    /**
     * Lista uma trilha euleriana de um grafo se existir. Caso contrário, retorna "-1".
     *
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo.
     * @param isDirecionado   Indica se o grafo é direcionado.
     * @return String representando a trilha euleriana ou "-1" se não existir.
     */
    public static String listarTrilhaEuleriana(Map<Integer, List<Integer>> listaAdjacencia, boolean isDirecionado) {
        if (!verificarEuleriano(listaAdjacencia, isDirecionado)) {
            return "-1"; // Retorna "-1" se o grafo não for euleriano.
        }

        LinkedList<Integer> trilha = new LinkedList<>();
        Map<Integer, Iterator<Integer>> iteradores = new HashMap<>();
        Stack<Integer> pilha = new Stack<>();
        Set<String> arestasUsadas = new HashSet<>();

        // Inicializa iteradores para cada vértice.
        for (int vertice : listaAdjacencia.keySet()) {
            iteradores.put(vertice, listaAdjacencia.get(vertice).iterator());
        }

        // Encontra um vértice inicial que possui arestas.
        int verticeInicial = listaAdjacencia.keySet().iterator().next();
        pilha.push(verticeInicial);

        while (!pilha.isEmpty()) {
            int vertice = pilha.peek();
            Iterator<Integer> iterador = iteradores.get(vertice);

            if (iterador.hasNext()) {
                int proximoVertice = iterador.next();
                String chaveAresta = gerarChaveAresta(vertice, proximoVertice);

                if (!arestasUsadas.contains(chaveAresta)) {
                    arestasUsadas.add(chaveAresta); // Marca a aresta como usada.
                    pilha.push(proximoVertice); // Adiciona o próximo vértice à pilha.
                }
            } else {
                trilha.addFirst(vertice); // Adiciona o vértice ao início da trilha.
                pilha.pop(); // Remove o vértice da pilha.
            }
        }

        return construirString(trilha); // Constrói a string representando a trilha.
    }

    /**
     * Gera uma chave única para uma aresta no formato "u-v" ou "v-u" para evitar duplicatas.
     *
     * @param u Vértice inicial da aresta.
     * @param v Vértice final da aresta.
     * @return String representando a chave da aresta.
     */
    private static String gerarChaveAresta(int u, int v) {
        return Math.min(u, v) + "-" + Math.max(u, v); // Garante que a aresta seja representada de forma consistente.
    }

    /**
     * Lista os vértices de articulação de um grafo.
     *
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo.
     * @return String representando os vértices de articulação, separados por espaços.
     */
    public static String listarVerticesArticulacao(Map<Integer, List<Integer>> listaAdjacencia) {
        Set<Integer> articulacoes = new HashSet<>();
        Map<Integer, Integer> descoberta = new HashMap<>();
        Map<Integer, Integer> menor = new HashMap<>();
        Set<Integer> visitados = new HashSet<>();
        int[] tempo = {0};

        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice)) {
                dfsArticulacao(vertice, -1, visitados, descoberta, menor, tempo, articulacoes, listaAdjacencia);
            }
        }

        List<Integer> resultado = new ArrayList<>(articulacoes);
        Collections.sort(resultado); // Ordena os vértices de articulação para garantir consistência.

        return construirString(resultado); // Constrói a string representando os vértices de articulação.
    }

    /**
     * Realiza uma busca em profundidade (DFS) para encontrar vértices de articulação.
     *
     * @param vertice         O vértice atual.
     * @param pai             O vértice pai na DFS.
     * @param visitados       Conjunto de vértices visitados.
     * @param descoberta      Mapa de tempos de descoberta dos vértices.
     * @param menor           Mapa de menor tempo de alcance dos vértices.
     * @param tempo           Tempo atual da DFS.
     * @param articulacoes    Conjunto de vértices de articulação encontrados.
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo.
     */
    private static void dfsArticulacao(int vertice, int pai, Set<Integer> visitados, Map<Integer, Integer> descoberta,
                                       Map<Integer, Integer> menor, int[] tempo, Set<Integer> articulacoes,
                                       Map<Integer, List<Integer>> listaAdjacencia) {
        visitados.add(vertice);
        descoberta.put(vertice, tempo[0]);
        menor.put(vertice, tempo[0]);
        tempo[0]++;
        int filhos = 0;

        for (int vizinho : listaAdjacencia.get(vertice)) {
            if (!visitados.contains(vizinho)) {
                filhos++;
                dfsArticulacao(vizinho, vertice, visitados, descoberta, menor, tempo, articulacoes, listaAdjacencia);
                menor.put(vertice, Math.min(menor.get(vertice), menor.get(vizinho)));

                // Verifica se o vértice é um ponto de articulação.
                if (pai == -1 && filhos > 1) {
                    articulacoes.add(vertice);
                }

                if (pai != -1 && menor.get(vizinho) >= descoberta.get(vertice)) {
                    articulacoes.add(vertice);
                }
            } else if (vizinho != pai) {
                menor.put(vertice, Math.min(menor.get(vertice), descoberta.get(vizinho)));
            }
        }
    }

    /**
     * Realiza uma busca em profundidade (DFS) para encontrar as arestas ponte.
     *
     * @param u               O vértice atual.
     * @param pai             O vértice pai na DFS.
     * @param visitados       Conjunto de vértices visitados.
     * @param descoberta      Mapa de tempos de descoberta dos vértices.
     * @param menor           Mapa de menor tempo de alcance dos vértices.
     * @param tempo           Tempo atual da DFS.
     * @param pontes          Lista de arestas ponte encontradas.
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo.
     */
    private static void dfsPonte(int u, int pai, Set<Integer> visitados,
                                 Map<Integer, Integer> descoberta, Map<Integer, Integer> menor,
                                 int[] tempo, List<List<Integer>> pontes,
                                 Map<Integer, List<Integer>> listaAdjacencia) {
        visitados.add(u);
        descoberta.put(u, tempo[0]);
        menor.put(u, tempo[0]);
        tempo[0]++;

        for (int v : listaAdjacencia.get(u)) {
            if (!visitados.contains(v)) {
                dfsPonte(v, u, visitados, descoberta, menor, tempo, pontes, listaAdjacencia);
                menor.put(u, Math.min(menor.get(u), menor.get(v)));

                // Verifica se a aresta é uma aresta ponte.
                if (menor.get(v) > descoberta.get(u)) {
                    pontes.add(Arrays.asList(u, v));
                }
            } else if (v != pai) {
                menor.put(u, Math.min(menor.get(u), descoberta.get(v)));
            }
        }
    }

    /**
     * Lista as arestas ponte de um grafo.
     *
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo.
     * @return String representando as arestas ponte, separadas por espaços.
     */
    public static String listarArestasPonte(Map<Integer, List<Integer>> listaAdjacencia) {
        List<List<Integer>> pontes = new ArrayList<>();
        Map<Integer, Integer> descoberta = new HashMap<>();
        Map<Integer, Integer> menor = new HashMap<>();
        Set<Integer> visitados = new HashSet<>();
        int[] tempo = {0};

        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice)) {
                dfsPonte(vertice, -1, visitados, descoberta, menor, tempo, pontes, listaAdjacencia);
            }
        }

        List<String> resultado = new ArrayList<>();
        for (List<Integer> ponte : pontes) {
            int u = ponte.get(0);
            int v = ponte.get(1);
            if (u > v) {
                int temp = u;
                u = v;
                v = temp;
            }
            resultado.add(u + " " + v);
        }
        Collections.sort(resultado); // Ordena as arestas ponte para garantir consistência.

        return String.join(" ", resultado); // Constrói a string representando as arestas ponte.
    }

// Função auxiliar para formatar listas encadeadas como uma string com espaços entre os valores
    /**
     * Formata uma lista de listas de inteiros como uma string, onde cada sublista é representada por seus elementos separados por espaço.
     *
     * @param listas Lista de listas de inteiros a serem formatadas.
     * @return String representando as listas formatadas com espaços entre os elementos.
     */
    public static String formatarListasEncadeadas(List<List<Integer>> listas) {
        StringBuilder resultado = new StringBuilder();

        for (List<Integer> lista : listas) {
            if (!lista.isEmpty()) {
                // Ordena os elementos da lista para garantir uma ordem consistente
                Collections.sort(lista);
                resultado.append(lista.get(0)).append(" ").append(lista.get(1)).append("\n");
            }
        }

        return resultado.toString().trim(); // Remove o espaço ou nova linha extra no final
    }

// Função para gerar árvore de profundidade usando DFS
    /**
     * Gera uma árvore de profundidade (DFS) para o grafo representado por uma lista de adjacência.
     *
     * @param listaAdjacencia Lista de adjacência que representa o grafo.
     * @param idsArestas Mapa que relaciona as arestas aos seus IDs.
     * @return String contendo os IDs das arestas usadas na árvore de profundidade.
     */
    private static String gerarArvoreProfundidade(Map<Integer, List<Integer>> listaAdjacencia, Map<List<Integer>, Integer> idsArestas) {
        StringBuilder resultado = new StringBuilder();
        Set<Integer> visitados = new HashSet<>();
        List<Integer> arestasUsadas = new ArrayList<>();

        dfsProfundidade(0, listaAdjacencia, visitados, arestasUsadas, idsArestas);

        for (Integer id : arestasUsadas) {
            resultado.append(id).append(" ");
        }

        return !resultado.isEmpty() ? resultado.toString().trim() : "-1";
    }

    /**
     * Realiza uma busca em profundidade (DFS) em um grafo e registra as arestas usadas.
     *
     * @param u Vértice atual na DFS.
     * @param listaAdjacencia Lista de adjacência que representa o grafo.
     * @param visitados Conjunto de vértices visitados durante a DFS.
     * @param arestasUsadas Lista para armazenar os IDs das arestas usadas.
     * @param idsArestas Mapa que relaciona as arestas aos seus IDs.
     */
    private static void dfsProfundidade(int u, Map<Integer, List<Integer>> listaAdjacencia, Set<Integer> visitados, List<Integer> arestasUsadas, Map<List<Integer>, Integer> idsArestas) {
        visitados.add(u);

        for (int v : listaAdjacencia.get(u)) {
            if (!visitados.contains(v)) {
                List<Integer> aresta = Arrays.asList(u, v);
                if (idsArestas.containsKey(aresta)) {
                    arestasUsadas.add(idsArestas.get(aresta));
                }
                dfsProfundidade(v, listaAdjacencia, visitados, arestasUsadas, idsArestas);
            }
        }
    }

// Função para gerar árvore de largura usando BFS
    /**
     * Gera uma árvore de largura (BFS) para o grafo representado por uma lista de adjacência.
     *
     * @param listaAdjacencia Lista de adjacência que representa o grafo.
     * @param idsArestas Mapa que relaciona as arestas aos seus IDs.
     * @return String contendo os IDs das arestas usadas na árvore de largura.
     */
    private static String gerarArvoreLargura(Map<Integer, List<Integer>> listaAdjacencia, Map<List<Integer>, Integer> idsArestas) {
        StringBuilder resultado = new StringBuilder();
        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> fila = new LinkedList<>();
        List<Integer> arestasUsadas = new ArrayList<>();

        fila.add(0);
        visitados.add(0);

        while (!fila.isEmpty()) {
            int u = fila.poll();
            for (int v : listaAdjacencia.get(u)) {
                if (!visitados.contains(v)) {
                    List<Integer> aresta = Arrays.asList(u, v);
                    if (idsArestas.containsKey(aresta)) {
                        arestasUsadas.add(idsArestas.get(aresta));
                    }
                    visitados.add(v);
                    fila.add(v);
                }
            }
        }

        for (Integer id : arestasUsadas) {
            resultado.append(id).append(" ");
        }

        return !resultado.isEmpty() ? resultado.toString().trim() : "-1";
    }

// Função para gerar árvore geradora mínima usando algoritmo de Kruskal
    /**
     * Gera uma árvore geradora mínima para o grafo representado por uma lista de adjacência usando o algoritmo de Kruskal.
     *
     * @param listaAdjacencia Lista de adjacência que representa o grafo.
     * @param pesos Mapa que relaciona as arestas aos seus pesos.
     * @param idsArestas Mapa que relaciona as arestas aos seus IDs.
     * @return String contendo os IDs das arestas da árvore geradora mínima.
     */
    private static String gerarArvoreGeradoraMinima(Map<Integer, List<Integer>> listaAdjacencia, Map<List<Integer>, Integer> pesos, Map<List<Integer>, Integer> idsArestas) {
        List<Edge> arestas = new ArrayList<>();
        Set<Integer> pesosUnicos = new HashSet<>();

        for (Map.Entry<List<Integer>, Integer> entrada : pesos.entrySet()) {
            List<Integer> aresta = entrada.getKey();
            int peso = entrada.getValue();
            int id = idsArestas.getOrDefault(aresta, -1);
            arestas.add(new Edge(aresta.get(0), aresta.get(1), peso, id));
            pesosUnicos.add(peso);
        }

        if (pesosUnicos.size() == 1) {
            return "-1";  // Retorna -1 se todas as arestas têm o mesmo peso
        }

        arestas.sort(Comparator.comparingInt((Edge a) -> a.peso).thenComparingInt(a -> a.id));

        int numVertices = listaAdjacencia.size();
        UnionFind uf = new UnionFind(numVertices);
        StringBuilder resultado = new StringBuilder();

        for (Edge e : arestas) {
            if (uf.encontrar(e.u) != uf.encontrar(e.v)) {
                uf.unir(e.u, e.v);
                resultado.append(e.id).append(" ");
            }
        }

        return !resultado.isEmpty() ? resultado.toString().trim() : "-1";
    }

    /**
     * Classe que representa uma aresta em um grafo com peso e ID.
     */
    private static class Edge {
        int u, v, peso, id;

        Edge(int u, int v, int peso, int id) {
            this.u = u;
            this.v = v;
            this.peso = peso;
            this.id = id;
        }
    }

// Ordenação Topológica de um grafo
    /**
     * Realiza a ordenação topológica de um grafo representado por uma lista de adjacência.
     *
     * @param listaAdjacencia Lista de adjacência que representa o grafo.
     * @return String contendo a ordenação topológica dos vértices ou uma string vazia se houver um ciclo.
     */
    public static String ordenarTopologicamente(Map<Integer, List<Integer>> listaAdjacencia) {
        Map<Integer, Integer> grauEntrada = new HashMap<>();
        for (int vertice : listaAdjacencia.keySet()) {
            grauEntrada.put(vertice, 0);
        }
        for (int vertice : listaAdjacencia.keySet()) {
            for (int vizinho : listaAdjacencia.get(vertice)) {
                grauEntrada.put(vizinho, grauEntrada.get(vizinho) + 1);
            }
        }

        Queue<Integer> fila = new LinkedList<>();
        for (int vertice : grauEntrada.keySet()) {
            if (grauEntrada.get(vertice) == 0) {
                fila.add(vertice);
            }
        }

        List<Integer> resultado = new ArrayList<>();
        while (!fila.isEmpty()) {
            int vertice = fila.poll();
            resultado.add(vertice);
            for (int vizinho : listaAdjacencia.getOrDefault(vertice, Collections.emptyList())) {
                grauEntrada.put(vizinho, grauEntrada.get(vizinho) - 1);
                if (grauEntrada.get(vizinho) == 0) {
                    fila.add(vizinho);
                }
            }
        }

        if (resultado.size() != listaAdjacencia.size()) {
            return ""; // Retorna uma string vazia se houver um ciclo
        }

        StringBuilder resultadoString = new StringBuilder();
        for (Integer v : resultado) {
            resultadoString.append(v).append(" ");
        }

        return resultadoString.toString().trim(); // Remove o espaço final extra
    }

    /**
     * Calcula o caminho mínimo entre dois vértices em um grafo com pesos usando o algoritmo de Dijkstra.
     *
     * @param adj Lista de adjacência que representa o grafo.
     * @param pesos Mapa que relaciona as arestas aos seus pesos.
     * @param origem Vértice de origem para o cálculo do caminho.
     * @param destino Vértice de destino para o cálculo do caminho.
     * @return String representando o caminho mínimo entre origem e destino, ou "-1" se não houver caminho.
     */
    private static String calcularCaminhoMinimo(Map<Integer, List<Integer>> adj, Map<List<Integer>, Integer> pesos, int origem, int destino) {
        // Inicializa as estruturas de dados para o algoritmo de Dijkstra
        Map<Integer, Integer> distancias = new HashMap<>();
        Map<Integer, Integer> predecessores = new HashMap<>();
        PriorityQueue<Node> prioridade = new PriorityQueue<>(Comparator.comparingInt(node -> node.distancia));

        // Inicializa os valores das distâncias e predecessores
        for (int vertice : adj.keySet()) {
            distancias.put(vertice, Integer.MAX_VALUE);
            predecessores.put(vertice, null);
        }
        distancias.put(origem, 0);
        prioridade.add(new Node(origem, 0));

        while (!prioridade.isEmpty()) {
            Node atual = prioridade.poll();

            // Se o nó atual é o destino, construa o caminho e retorne
            if (atual.id == destino) {
                return construirCaminho(predecessores, origem, destino);
            }

            // Explore os vizinhos do vértice atual
            for (int vizinho : adj.getOrDefault(atual.id, Collections.emptyList())) {
                int pesoAresta = pesos.getOrDefault(Arrays.asList(atual.id, vizinho), Integer.MAX_VALUE);
                int novaDistancia = distancias.get(atual.id) + pesoAresta;

                if (novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, atual.id);
                    prioridade.add(new Node(vizinho, novaDistancia));
                }
            }
        }

        // Se o destino não foi alcançado, retorne -1
        return "-1";
    }

    /**
     * Função auxiliar para construir o caminho mais curto a partir dos predecessores.
     *
     * @param predecessores Mapa de predecessores onde a chave é um vértice e o valor é o vértice anterior no caminho.
     * @param origem Vértice de origem para reconstruir o caminho.
     * @param destino Vértice de destino para reconstruir o caminho.
     * @return String representando o caminho mínimo entre origem e destino, ou "-1" se não houver caminho.
     */
    private static String construirCaminho(Map<Integer, Integer> predecessores, int origem, int destino) {
        LinkedList<Integer> caminho = new LinkedList<>();
        Integer vertice = destino;

        // Reconstruir o caminho a partir do destino até a origem
        while (vertice != null) {
            caminho.addFirst(vertice);
            vertice = predecessores.get(vertice);
        }

        // Se o caminho construído não inclui a origem, retorna -1
        if (caminho.getFirst() == origem) {
            return String.join(" ", caminho.stream().map(String::valueOf).toArray(String[]::new));
        } else {
            return "-1";
        }
    }

    /**
     * Função para verificar se é possível calcular o caminho mínimo entre dois vértices.
     *
     * @param adj Mapa de adjacência do grafo.
     * @param origem Vértice de origem para verificar a conectividade.
     * @param destino Vértice de destino para verificar a conectividade.
     * @return Boolean indicando se é possível calcular o caminho mínimo (true) ou não (false).
     */
    private static Boolean podeCalcularCaminhoMinimo(Map<Integer, List<Integer>> adj, int origem, int destino) {
        // Verifica se a origem e o destino são o mesmo vértice
        if (origem == destino) {
            return true; // Um vértice pode sempre alcançar a si mesmo
        }

        // Conjunto para rastrear quais vértices foram visitados
        Set<Integer> visitados = new HashSet<>();
        // Fila para implementar a busca em largura (BFS)
        Queue<Integer> fila = new LinkedList<>();

        // Adiciona o vértice de origem à fila e marca como visitado
        fila.add(origem);
        visitados.add(origem);

        // Executa a busca em largura para encontrar o destino
        while (!fila.isEmpty()) {
            // Remove um vértice da fila para processar
            int verticeAtual = fila.poll();

            // Verifica se o vértice atual é o destino
            if (verticeAtual == destino) {
                return true; // Encontrou o destino, caminho mínimo é possível
            }

            // Adiciona todos os vizinhos do vértice atual à fila se não foram visitados
            for (int vizinho : adj.getOrDefault(verticeAtual, Collections.emptyList())) {
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho); // Marca o vizinho como visitado
                    fila.add(vizinho); // Adiciona o vizinho à fila para processamento futuro
                }
            }
        }

        // Se sair do loop e não encontrar o destino, não é possível calcular o caminho mínimo
        return false;
    }

    /**
     * Calcula o fluxo máximo em um grafo direcionado usando o algoritmo de Ford-Fulkerson.
     *
     * @param listaAdjacencia Mapa de adjacência do grafo.
     * @param pesos Mapa de pesos das arestas do grafo.
     * @param origem Vértice de origem para o fluxo.
     * @param destino Vértice de destino para o fluxo.
     * @return Valor do fluxo máximo.
     */
    private static Integer calcularFluxoMaximo(Map<Integer, List<Integer>> listaAdjacencia, Map<List<Integer>, Integer> pesos, int origem, int destino) {
        Map<List<Integer>, Integer> capacidade = new HashMap<>(pesos);
        int fluxoMaximo = 0;

        while (true) {
            // Encontre o caminho de capacidade usando a lista de adjacência
            Map<Integer, Integer> caminho = bfsCapacidade(listaAdjacencia, capacidade, origem, destino);
            if (caminho.isEmpty()) break;

            // Encontre o fluxo mínimo ao longo do caminho encontrado
            int fluxo = Integer.MAX_VALUE;
            for (int v = destino; v != origem; v = caminho.get(v)) {
                int u = caminho.get(v);
                fluxo = Math.min(fluxo, capacidade.getOrDefault(Arrays.asList(u, v), 0));
            }

            // Atualize as capacidades residuais e adicione o fluxo ao fluxo máximo
            for (int v = destino; v != origem; v = caminho.get(v)) {
                int u = caminho.get(v);
                List<Integer> aresta = Arrays.asList(u, v);
                capacidade.put(aresta, capacidade.get(aresta) - fluxo);
                List<Integer> arestaInversa = Arrays.asList(v, u);
                capacidade.put(arestaInversa, capacidade.getOrDefault(arestaInversa, 0) + fluxo);
            }

            fluxoMaximo += fluxo;
        }

        return fluxoMaximo;
    }

    /**
     * Função que realiza uma busca em largura (BFS) para encontrar um caminho com capacidade positiva entre dois vértices.
     *
     * @param listaAdjacencia Mapa de adjacência do grafo.
     * @param capacidade Mapa de capacidades das arestas do grafo.
     * @param origem Vértice de origem para a busca.
     * @param destino Vértice de destino para a busca.
     * @return Mapa representando o caminho encontrado, onde a chave é um vértice e o valor é o vértice anterior no caminho.
     */
    private static Map<Integer, Integer> bfsCapacidade(Map<Integer, List<Integer>> listaAdjacencia, Map<List<Integer>, Integer> capacidade, int origem, int destino) {
        // Mapa para armazenar o caminho encontrado, onde a chave é um vértice e o valor é o vértice anterior no caminho.
        Map<Integer, Integer> caminho = new HashMap<>();
        // Fila para a busca em largura.
        Queue<Integer> fila = new LinkedList<>();
        // Conjunto para rastrear os vértices visitados.
        Set<Integer> visitado = new HashSet<>();

        // Adiciona o vértice de origem à fila e marca como visitado.
        fila.add(origem);
        visitado.add(origem);
        // Marca a origem no mapa de caminho com valor nulo, indicando que não há vértice anterior para a origem.
        caminho.put(origem, null);

        // Executa a busca em largura.
        while (!fila.isEmpty()) {
            // Remove o vértice da frente da fila.
            int u = fila.poll();
            // Se o vértice atual for o destino, termina a busca.
            if (u == destino) break;

            // Itera sobre todos os vizinhos do vértice atual.
            for (int v : listaAdjacencia.getOrDefault(u, Collections.emptyList())) {
                // Cria uma lista representando a aresta entre u e v.
                List<Integer> aresta = Arrays.asList(u, v);
                // Verifica se o vizinho ainda não foi visitado e se a capacidade da aresta é positiva.
                if (!visitado.contains(v) && capacidade.getOrDefault(aresta, 0) > 0) {
                    // Adiciona o vizinho à fila e marca como visitado.
                    fila.add(v);
                    visitado.add(v);
                    // Adiciona o vizinho no mapa de caminho, com o vértice atual como o vértice anterior.
                    caminho.put(v, u);
                }
            }
        }

        // Retorna o mapa de caminho encontrado.
        return caminho;
    }

    /**
     * Gera o fecho transitivo para um grafo direcionado.
     *
     * @param listaAdjacencia Mapa de adjacência do grafo.
     * @param verticeEscolhido Vértice a partir do qual o fecho transitivo é gerado.
     * @return String representando o fecho transitivo do vértice escolhido.
     */
    public static String gerarFechoTransitivo(Map<Integer, List<Integer>> listaAdjacencia, int verticeEscolhido) {
        Set<Integer> fecho = new HashSet<>();
        fecho.addAll(listaAdjacencia.keySet());

        boolean[] visitado = new boolean[fecho.size()];
        Queue<Integer> fila = new LinkedList<>();
        fila.add(verticeEscolhido);
        visitado[verticeEscolhido] = true;

        while (!fila.isEmpty()) {
            int vertice = fila.poll();
            for (int vizinho : listaAdjacencia.getOrDefault(vertice, Collections.emptyList())) {
                if (!visitado[vizinho]) {
                    visitado[vizinho] = true;
                    fila.add(vizinho);
                }
            }
        }

        Set<Integer> resultado = new HashSet<>();
        for (int i = 0; i < visitado.length; i++) {
            if (visitado[i]) {
                resultado.add(i);
            }
        }

        // Construa a string representando o fecho transitivo
        StringBuilder fechoString = new StringBuilder();
        for (Integer v : resultado) {
            fechoString.append(v).append(" ");
        }

        return fechoString.toString().trim(); // Remove o espaço final extra
    }

    /**
     * Constrói uma string com os elementos da lista separados por espaços.
     *
     * @param lista Lista de elementos para construir a string.
     * @return String representando os elementos da lista separados por espaços.
     */
    private static String construirString(List<?> lista) {
        StringBuilder sb = new StringBuilder();
        for (Object item : lista) {
            sb.append(item).append(" ");
        }
        return sb.toString().trim(); // Remove o espaço extra no final da string.
    }

    /**
     * Formata uma lista de inteiros em uma string com espaços entre os elementos.
     *
     * @param lista A lista de inteiros a ser formatada.
     * @return Uma string contendo os elementos da lista separados por espaços.
     */
    public static String formatarListaComEspacos(List<Integer> lista) {
        // Cria um StringBuilder para construir a string de forma eficiente.
        StringBuilder sb = new StringBuilder();
        // Itera sobre cada elemento da lista.
        for (int i = 0; i < lista.size(); i++) {
            // Adiciona o elemento atual ao StringBuilder.
            sb.append(lista.get(i));
            // Adiciona um espaço após o elemento, exceto após o último elemento.
            if (i < lista.size() - 1) {
                sb.append(" ");
            }
        }
        // Converte o StringBuilder para uma string e retorna a string resultante.
        return sb.toString();
    }

    /**
     * Implementa a estrutura de dados Union-Find (ou Disjoint Set Union) para gerenciar conjuntos disjuntos.
     */
    private static class UnionFind {
        // Array que armazena o pai de cada vértice.
        private final int[] pai;
        // Array que armazena o rank (profundidade) das árvores.
        private final int[] rank;

        /**
         * Construtor da classe UnionFind.
         *
         * @param tamanho O número de vértices na estrutura Union-Find.
         */
        UnionFind(int tamanho) {
            // Inicializa o array pai e rank com o tamanho fornecido.
            pai = new int[tamanho];
            rank = new int[tamanho];
            // Inicializa cada vértice como seu próprio pai e o rank como 0.
            for (int i = 0; i < tamanho; i++) {
                pai[i] = i;
                rank[i] = 0;
            }
        }

        /**
         * Encontra a raiz do conjunto que contém o vértice x.
         *
         * @param x O vértice para o qual encontrar a raiz.
         * @return A raiz do conjunto que contém o vértice x.
         */
        int encontrar(int x) {
            // Se o pai de x não for x, continua a busca recursivamente.
            if (pai[x] != x) {
                // Atualiza o pai de x diretamente para a raiz encontrada (compressão de caminho).
                pai[x] = encontrar(pai[x]);
            }
            // Retorna a raiz do conjunto.
            return pai[x];
        }

        /**
         * Une os conjuntos que contêm os vértices x e y.
         *
         * @param x O primeiro vértice.
         * @param y O segundo vértice.
         */
        void unir(int x, int y) {
            // Encontra as raízes dos conjuntos de x e y.
            int raizX = encontrar(x);
            int raizY = encontrar(y);
            // Se as raízes forem diferentes, une os conjuntos.
            if (raizX != raizY) {
                // Une o conjunto com menor rank ao conjunto com maior rank.
                if (rank[raizX] > rank[raizY]) {
                    pai[raizY] = raizX;
                } else if (rank[raizX] < rank[raizY]) {
                    pai[raizX] = raizY;
                } else {
                    // Se os ranks forem iguais, une arbitrariamente e incrementa o rank da nova raiz.
                    pai[raizY] = raizX;
                    rank[raizX]++;
                }
            }
        }
    }

    /**
     * Classe auxiliar para representar os nós da fila de prioridade com seus identificadores e distâncias.
     */
    private static class Node {
        int id;
        int distancia;

        /**
         * Construtor da classe Node.
         *
         * @param id O identificador do nó.
         * @param distancia A distância associada ao nó.
         */
        Node(int id, int distancia) {
            this.id = id;
            this.distancia = distancia;
        }
    }
}

