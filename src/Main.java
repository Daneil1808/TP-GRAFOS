import java.util.*;

/**
 * Classe principal que contém o método principal para executar o programa.
 * O programa lê a entrada do usuário, constrói o grafo e executa as funções do trabalho prático,
 * Daniel Assis Gonçalves
 * Franciele Rodrigues Ferreira
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

        // Leitura da lista de funções a serem executadas
        List<Integer> funcoes = new ArrayList<>();
        String linhaFuncoes = scanner.nextLine().trim();
        Scanner linhaScanner = new Scanner(linhaFuncoes);
        while (linhaScanner.hasNextInt()) {
            funcoes.add(linhaScanner.nextInt());
        }
        linhaScanner.close();

        int numVertices = 0;
        int numArestas = 0;
        if(scanner.hasNextInt()) {
            // Leitura do número de vértices e arestas
            numVertices = scanner.nextInt(); // Lê o número de vértices do grafo
            numArestas = scanner.nextInt(); // Lê o número de arestas do grafo
            scanner.nextLine(); // Consome a quebra de linha após os inteiros
        }

        // Leitura do tipo de grafo (direcionado ou não)
        String tipoGrafo = scanner.nextLine().trim(); // Lê a linha contendo o tipo do grafo e remove espaços em branco
        boolean isDirecionado = tipoGrafo.equals("direcionado"); // Define se o grafo é direcionado ou não

        // Inicialização das estruturas de dados
        Map<Integer, List<Integer>> listaAdjacencia = new HashMap<>();
        Map<List<Integer>, Integer> pesos = new HashMap<>();
        Map<List<Integer>, Integer> idsArestas = new HashMap<>();
        Set<Integer> vertices = new HashSet<>();

        // Inicializa a lista de adjacência para cada vértice e adiciona todos os vértices ao conjunto
        for (int i = 0; i < numVertices; i++) {
            listaAdjacencia.put(i, new ArrayList<>()); // Inicializa cada vértice com uma lista vazia
            vertices.add(i); // Adiciona o vértice ao conjunto de vértices
        }

        // Leitura das arestas e suas informações
        for (int i = 0; i < numArestas; i++) {
            if (scanner.hasNextInt()) {
                int idAresta = scanner.nextInt(); // Lê o ID da aresta
                int verticeU = scanner.nextInt(); // Lê o vértice de origem da aresta
                int verticeV = scanner.nextInt(); // Lê o vértice de destino da aresta
                int pesoAresta = scanner.nextInt(); // Lê o peso da aresta
                scanner.nextLine(); // Consome a quebra de linha após os inteiros

                // Adiciona a aresta à lista de adjacência do vértice de origem
                listaAdjacencia.get(verticeU).add(verticeV);

                // Se o grafo não é direcionado, adiciona também a aresta no sentido inverso
                if (!isDirecionado) {
                    listaAdjacencia.get(verticeV).add(verticeU);
                }

                // Adiciona o peso e o ID da aresta no mapa de pesos e IDs
                List<Integer> aresta = Arrays.asList(verticeU, verticeV);
                pesos.put(aresta, pesoAresta);
                idsArestas.put(aresta, idAresta);
                if (!isDirecionado) {
                    List<Integer> arestaInversa = Arrays.asList(verticeV, verticeU);
                    pesos.put(arestaInversa, pesoAresta); // Adiciona o peso da aresta no sentido inverso para grafos não direcionados
                    idsArestas.put(arestaInversa, idAresta); // Adiciona o ID da aresta no sentido inverso para grafos não direcionados
                }
            }
        }

        // Executa as funções solicitadas no trabalho prático
        for (int funcao: funcoes) {

            switch (funcao) {
                // Verifica se o grafo é conexo
                case 0:
                    System.out.println(verificarConexo(listaAdjacencia, numVertices, isDirecionado) ? "1" : "0");
                    break;

                // Verifica se o grafo é bipartido
                case 1:
                    System.out.println(verificarBipartido(listaAdjacencia, isDirecionado) ? "1" : "0");
                    break;

                // Verifica se o grafo é euleriano
                case 2:
                    System.out.println(verificarEuleriano(listaAdjacencia, isDirecionado) ? "1" : "0");
                    break;

                // Verifica se o grafo possui ciclo
                case 3:
                    System.out.println(possuiCiclo(listaAdjacencia) ? "1" : "0");
                    break;

                // Lista os componentes conexos do grafo
                case 4:
                    System.out.println(listarComponentesConexas(listaAdjacencia));
                    break;

                // Lista os componentes fortemente conexos (para grafos direcionados)
                // Imprime -1 para grafos não direcionados
                case 5:
                    System.out.println(isDirecionado ? listarComponentesFortementeConexas(listaAdjacencia) : "-1");
                    break;

                // Lista a trilha euleriana, se existir
                case 6:
                    System.out.println(listarTrilhaEuleriana(listaAdjacencia, isDirecionado));
                    break;

                // Lista os vértices de articulação do grafo
                case 7:
                    System.out.println(listarVerticesArticulacao(listaAdjacencia));
                    break;

                // Lista as arestas ponte do grafo
                case 8:
                    System.out.println(listarArestasPonte(listaAdjacencia));
                    break;

                // Gera e lista a árvore de profundidade
                case 9:
                    System.out.println(gerarArvoreProfundidade(listaAdjacencia, idsArestas));
                    break;

                // Gera e lista a árvore de largura
                case 10:
                    System.out.println(gerarArvoreLargura(listaAdjacencia, idsArestas));
                    break;

                // Gera e lista a árvore geradora mínima
                case 11:
                    System.out.println(gerarArvoreGeradoraMinima(listaAdjacencia, pesos, idsArestas));
                    break;

                // Lista a ordenação topológica (para grafos direcionados)
                // Imprime -1 para grafos não direcionados
                case 12:
                    System.out.println(isDirecionado ? ordenarTopologicamente(listaAdjacencia) : "-1");
                    break;

                // Calcula e lista o caminho mínimo entre o vértice 0 e o vértice (numVertices - 1)
                // Se o grafo for direcionado ou não for possível calcular, imprime -1
                case 13:
                    System.out.println(!isDirecionado && podeCalcularCaminhoMinimo(listaAdjacencia, 0, numVertices - 1) ?
                            calcularCaminhoMinimo(listaAdjacencia, pesos, 0, numVertices - 1) : "-1");
                    break;

                // Calcula e lista o fluxo máximo entre o vértice 0 e o vértice (numVertices - 1)
                // Imprime -1 para grafos não direcionados
                case 14:
                    System.out.println(isDirecionado ? calcularFluxoMaximo(listaAdjacencia, pesos, 0, numVertices - 1) : "-1");
                    break;

                // Gera e lista o fecho transitivo (para grafos direcionados)
                // Imprime -1 para grafos não direcionados
                case 15:
                    System.out.println(isDirecionado ? gerarFechoTransitivo(listaAdjacencia, 0) : "-1");
                    break;

                default:
                    break;
            }
        }
        //Fecha o scanner do teclado
        scanner.close();
    }

    /**
     * Verifica se o grafo é conexo.
     *
     * @param listaAdjacencia Mapa de listas de adjacência do grafo.
     * @param numVertices Número de vértices do grafo.
     * @return true se o grafo for conexo, false caso contrário.
     */
    private static Boolean verificarConexo(Map<Integer, List<Integer>> listaAdjacencia, int numVertices, boolean isDirecionado) {
        if(!isDirecionado) {
            Set<Integer> visitados = new HashSet<>();
            Queue<Integer> fila = new LinkedList<>();

            // Começa a busca a partir do vértice 0
            int verticeInicial = 0;
            fila.add(verticeInicial);
            visitados.add(verticeInicial);

            while (!fila.isEmpty()) {
                int vertice = fila.poll();
                for (int vizinho : listaAdjacencia.getOrDefault(vertice, new ArrayList<>())) {
                    if (!visitados.contains(vizinho)) {
                        visitados.add(vizinho);
                        fila.add(vizinho);
                    }
                }
            }

            // Verifica se todos os vértices foram visitados
            return visitados.size() == numVertices;// Passo 1: Executar DFS a partir de um vértice
        }else {
            // Passo 1: Executar DFS a partir de um vértice
            Set<Integer> visitados = new HashSet<>();
            dfs(listaAdjacencia, 0, visitados);

            if (visitados.size() != numVertices) return false;

            // Passo 2: Inverter arestas e verificar conectividade novamente
            Map<Integer, List<Integer>> listaAdjacenciaInvertida = inverterArestas(listaAdjacencia);
            visitados.clear();
            dfs(listaAdjacenciaInvertida, 0, visitados);

            return visitados.size() == numVertices;
        }

    }

    /**
     * Realiza uma busca em profundidade (Depth-First Search - DFS) em um grafo representado por uma lista de adjacência.
     *
     * @param listaAdjacencia Um mapa onde as chaves são vértices e os valores são listas de vértices adjacentes.
     * @param vertice O vértice a partir do qual a busca em profundidade deve ser iniciada.
     * @param visitados Um conjunto que mantém o controle dos vértices já visitados durante a busca.
     */
    private static void dfs(Map<Integer, List<Integer>> listaAdjacencia, int vertice, Set<Integer> visitados) {
        visitados.add(vertice);
        for (int vizinho : listaAdjacencia.getOrDefault(vertice, new ArrayList<>())) {
            if (!visitados.contains(vizinho)) {
                dfs(listaAdjacencia, vizinho, visitados);
            }
        }
    }

    /**
     * Inverte as arestas de um grafo representado por uma lista de adjacência.
     * Para um grafo direcionado, este método troca a direção de todas as arestas.
     *
     * @param listaAdjacencia Um mapa onde as chaves são vértices e os valores são listas de vértices adjacentes.
     * @return Um novo mapa onde as arestas são invertidas em relação ao grafo original.
     */
    private static Map<Integer, List<Integer>> inverterArestas(Map<Integer, List<Integer>> listaAdjacencia) {
        Map<Integer, List<Integer>> invertido = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : listaAdjacencia.entrySet()) {
            int vertice = entry.getKey();
            List<Integer> vizinhos = entry.getValue();
            if (!invertido.containsKey(vertice)) {
                invertido.put(vertice, new ArrayList<>());
            }
            for (int vizinho : vizinhos) {
                if (!invertido.containsKey(vizinho)) {
                    invertido.put(vizinho, new ArrayList<>());
                }
                invertido.get(vizinho).add(vertice);
            }
        }
        return invertido;
    }

    /**
     * Verifica se o grafo é bipartido.
     *
     * @param listaAdjacencia Mapa de listas de adjacência do grafo
     * @param direcionado Verifica se o grafo é direcionado
     * @return true se o grafo for bipartido, false caso contrário.
     */
    private static Boolean verificarBipartido(Map<Integer, List<Integer>> listaAdjacencia, boolean direcionado) {
        Map<Integer, Integer> cores = new HashMap<>(); // Mapeia o vértice para sua cor (0 ou 1)

        for (Integer vertice : listaAdjacencia.keySet()) {
            if (!cores.containsKey(vertice)) {
                if (!bipartidoBFS(listaAdjacencia, vertice, cores, direcionado)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Verifica se um grafo é bipartido usando busca em largura (BFS).
     * Um grafo é bipartido se é possível colorir seus vértices com duas cores de forma que
     * nenhum par de vértices adjacentes compartilhem a mesma cor.
     *
     * @param listaAdjacencia Um mapa onde as chaves são vértices e os valores são listas de vértices adjacentes.
     * @param start O vértice a partir do qual a busca em largura deve ser iniciada.
     * @param cores Um mapa que associa cada vértice a uma cor (0 ou 1). Inicialmente, este mapa está vazio.
     * @param direcionado Um valor booleano indicando se o grafo é direcionado (true) ou não direcionado (false).
     * @return Um valor booleano indicando se o grafo é bipartido (true) ou não (false).
     */
    private static Boolean bipartidoBFS(Map<Integer, List<Integer>> listaAdjacencia, int start, Map<Integer, Integer> cores, boolean direcionado) {
        Queue<Integer> fila = new LinkedList<>();
        fila.add(start);
        cores.put(start, 0); // Começa com a cor 0

        while (!fila.isEmpty()) {
            int vertice = fila.poll();
            int corAtual = cores.get(vertice);

            for (int vizinho : listaAdjacencia.getOrDefault(vertice, new ArrayList<>())) {
                if (!cores.containsKey(vizinho)) {
                    // Atribui a cor oposta ao vizinho
                    cores.put(vizinho, 1 - corAtual);
                    fila.add(vizinho);
                } else if (cores.get(vizinho).equals(corAtual)) {
                    // Encontrou um conflito de cores, indicando que o grafo não é bipartido
                    return false;
                }
                // Se for não direcionado, precisamos verificar a aresta de volta
                if (!direcionado) {
                    if (!cores.containsKey(vertice)) {
                        cores.put(vertice, 1 - cores.get(vizinho));
                    } else if (cores.get(vertice).equals(cores.get(vizinho))) {
                        return false;
                    }
                }
            }
        }
        return true;
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
    public static boolean verificarEuleriano(Map<Integer, List<Integer>> listaAdjacencia, boolean isDirecionado) {
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
            int impares = 0;

            // Conta o número de vértices com grau ímpar
            for (int vertice : listaAdjacencia.keySet()) {
                if (calcularGrauGrafoNaoDirecionado(listaAdjacencia, vertice) % 2 != 0) {
                    impares++;
                }
            }

            // Verifica a conectividade do grafo
            boolean conectado = isConectado(listaAdjacencia);

            // O grafo é euleriano se todos os vértices tiverem grau par e o grafo for conexo
            return impares == 0 && conectado;
        }
    }

    private static boolean isConectado(Map<Integer, List<Integer>> listaAdjacencia) {
        if (listaAdjacencia.isEmpty()) {
            return false; // Um grafo vazio não é conectado
        }

        Set<Integer> visitados = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();

        // Pegue o primeiro vértice
        int primeiroVertice = listaAdjacencia.keySet().iterator().next();
        stack.push(primeiroVertice);

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (visitados.add(v)) {
                for (int vizinho : listaAdjacencia.get(v)) {
                    if (!visitados.contains(vizinho)) {
                        stack.push(vizinho);
                    }
                }
            }
        }

        // Verifica se todos os vértices foram visitados
        return visitados.size() == listaAdjacencia.size();
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
        // Usado para rastrear quais vértices já foram visitados
        Set<Integer> visitados = new HashSet<>();
        // Lista para armazenar todos os componentes conexos encontrados
        List<List<Integer>> componentes = new ArrayList<>();

        // Ordena os vértices para garantir a ordem lexicográfica
        List<Integer> vertices = new ArrayList<>(listaAdjacencia.keySet());
        Collections.sort(vertices);

        for (int vertice : vertices) {
            if (!visitados.contains(vertice)) {
                // Cria uma nova lista para armazenar o componente atual
                List<Integer> componente = new ArrayList<>();
                Queue<Integer> fila = new LinkedList<>();
                fila.add(vertice);
                visitados.add(vertice);

                while (!fila.isEmpty()) {
                    int atual = fila.poll();
                    componente.add(atual);
                    // Adiciona todos os vizinhos não visitados na fila
                    for (int vizinho : listaAdjacencia.get(atual)) {
                        if (!visitados.contains(vizinho)) {
                            visitados.add(vizinho);
                            fila.add(vizinho);
                        }
                    }
                }
                // Ordena o componente atual e adiciona à lista de componentes
                Collections.sort(componente);
                componentes.add(componente);
            }
        }

        // Se há apenas um componente, o grafo é totalmente conexo
        if (componentes.size() == 1) {
            return "-1";
        }

        // Ordena a lista de componentes em ordem lexicográfica
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
            sb.append(formatarListaComEspacos(componente)).append(" ");
        }

        return sb.toString().trim(); // Remove a última nova linha desnecessária
    }

    /**
     * Calcula o grau de um vértice específico em um grafo não direcionado.
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo.
     * @param vertice O vértice para o qual o grau deve ser calculado.
     * @return O grau do vértice especificado. Retorna -1 se o vértice não estiver presente no grafo.
     */
    public static int calcularGrauGrafoNaoDirecionado(Map<Integer, List<Integer>> listaAdjacencia, int vertice) {
        // Verifica se o vértice está presente na lista de adjacência
        if (!listaAdjacencia.containsKey(vertice)) {
            return -1; // Retorna -1 se o vértice não estiver presente
        }

        // Calcula o grau do vértice
        List<Integer> vizinhos = listaAdjacencia.get(vertice);
        return vizinhos.size(); // O grau do vértice é o número de vizinhos
    }

    /**
     * Lista os componentes fortemente conexos de um grafo utilizando o algoritmo de Kosaraju.
     * O grafo deve ser representado por uma lista de adjacência.
     *
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo (vértices -> lista de vizinhos).
     * @return Lista de listas de inteiros, onde cada lista representa um componente fortemente conexo.
     */
    public static String listarComponentesFortementeConexas(Map<Integer, List<Integer>> listaAdjacencia) {
        List<List<Integer>> componentes = new ArrayList<>();
        Set<Integer> visitados = new HashSet<>();
        Stack<Integer> pilha = new Stack<>();
        Map<Integer, List<Integer>> grafoInvertido = new HashMap<>();

        // Inicializa o grafo invertido
        for (int vertice : listaAdjacencia.keySet()) {
            grafoInvertido.put(vertice, new ArrayList<>());
        }
        for (int vertice : listaAdjacencia.keySet()) {
            for (int vizinho : listaAdjacencia.get(vertice)) {
                grafoInvertido.get(vizinho).add(vertice);
            }
        }

        // Passo 1: Realiza um DFS e preenche a pilha com a ordem de término
        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice)) {
                dfsParaPilha(listaAdjacencia, vertice, visitados, pilha);
            }
        }

        // Passo 2: Realiza um DFS no grafo invertido usando a ordem da pilha
        visitados.clear();
        while (!pilha.isEmpty()) {
            int vertice = pilha.pop();
            if (!visitados.contains(vertice)) {
                List<Integer> componente = new ArrayList<>();
                dfsParaComponente(grafoInvertido, vertice, visitados, componente);
                componentes.add(componente);
            }
        }

        // Ordena a lista de componentes em ordem lexicográfica
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
            sb.append(formatarListaComEspacos(componente)).append(" ");
        }

        return sb.toString().trim(); // Remove a última nova linha desnecessária
    }

    /**
     * Realiza uma busca em profundidade (DFS) no grafo e empilha os vértices na ordem de conclusão.
     *
     * @param grafo O grafo representado como um mapa de listas de adjacência, onde a chave é um vértice
     *              e o valor é a lista de vértices adjacentes.
     * @param vertice O vértice a partir do qual a busca em profundidade é iniciada.
     * @param visitados O conjunto de vértices que já foram visitados durante a busca.
     * @param pilha A pilha onde os vértices são empilhados após a visita aos seus vizinhos.
     */
    private static void dfsParaPilha(Map<Integer, List<Integer>> grafo, int vertice, Set<Integer> visitados, Stack<Integer> pilha) {
        // Marca o vértice atual como visitado
        visitados.add(vertice);

        // Recorre pelos vizinhos do vértice atual
        for (int vizinho : grafo.get(vertice)) {
            // Se o vizinho não foi visitado, realiza uma chamada recursiva
            if (!visitados.contains(vizinho)) {
                dfsParaPilha(grafo, vizinho, visitados, pilha);
            }
        }

        // Empilha o vértice após ter visitado todos os seus vizinhos
        pilha.push(vertice);
    }

    /**
     * Realiza uma busca em profundidade (DFS) no grafo para encontrar todos os vértices em um componente conexo.
     *
     * @param grafo O grafo representado como um mapa de listas de adjacência, onde a chave é um vértice
     *              e o valor é a lista de vértices adjacentes.
     * @param vertice O vértice a partir do qual a busca em profundidade é iniciada.
     * @param visitados O conjunto de vértices que já foram visitados durante a busca.
     * @param componente A lista onde os vértices do componente conexo são armazenados.
     */
    private static void dfsParaComponente(Map<Integer, List<Integer>> grafo, int vertice, Set<Integer> visitados, List<Integer> componente) {
        // Marca o vértice atual como visitado
        visitados.add(vertice);

        // Adiciona o vértice ao componente conexo
        componente.add(vertice);

        // Recorre pelos vizinhos do vértice atual
        for (int vizinho : grafo.get(vertice)) {
            // Se o vizinho não foi visitado, realiza uma chamada recursiva
            if (!visitados.contains(vizinho)) {
                dfsParaComponente(grafo, vizinho, visitados, componente);
            }
        }
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

    /**
     * Gera a árvore de profundidade a partir do grafo.
     * @param listaAdjacencia Mapa que representa a lista de adjacência do grafo.
     * @param idsArestas Mapa que relaciona arestas com seus IDs.
     * @return String contendo os IDs das arestas da árvore de profundidade.
     */
    public static String gerarArvoreProfundidade(Map<Integer, List<Integer>> listaAdjacencia, Map<List<Integer>, Integer> idsArestas) {
        Set<Integer> visitados = new HashSet<>();
        Set<Integer> arestasArvore = new LinkedHashSet<>(); // Usar LinkedHashSet para evitar duplicatas e manter a ordem de inserção
        Stack<Integer> pilha = new Stack<>();
        Map<Integer, Integer> pai = new HashMap<>(); // Mapa para rastrear o pai de cada vértice

        // Inicia a travessia a partir do vértice 0
        pilha.push(0);
        visitados.add(0);
        pai.put(0, null);

        while (!pilha.isEmpty()) {
            int vertice = pilha.pop();
            List<Integer> vizinhos = new ArrayList<>(listaAdjacencia.get(vertice));
            Collections.sort(vizinhos); // Ordena para garantir a ordem lexicográfica

            for (int vizinho : vizinhos) {
                if (!visitados.contains(vizinho)) {
                    // Adiciona a aresta à lista de arestas da árvore
                    List<Integer> arestaDireta = Arrays.asList(vertice, vizinho);
                    List<Integer> arestaInversa = Arrays.asList(vizinho, vertice);

                    if (idsArestas.containsKey(arestaDireta)) {
                        arestasArvore.add(idsArestas.get(arestaDireta));
                    } else if (idsArestas.containsKey(arestaInversa)) {
                        // Para grafos não direcionados, verifica a aresta no sentido inverso
                        arestasArvore.add(idsArestas.get(arestaInversa));
                    }

                    // Marca o vizinho como visitado e o adiciona à pilha
                    visitados.add(vizinho);
                    pilha.push(vizinho);
                    pai.put(vizinho, vertice);
                }
            }
        }

        // Ordena os IDs das arestas para a saída correta
        List<Integer> sortedArestas = new ArrayList<>(arestasArvore);
        Collections.sort(sortedArestas);

        return String.join(" ", sortedArestas.stream().map(String::valueOf).toArray(String[]::new));
    }

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
        PriorityQueue<No> prioridade = new PriorityQueue<>(Comparator.comparingInt(no -> no.distancia));

        // Inicializa os valores das distâncias e predecessores
        for (int vertice : adj.keySet()) {
            distancias.put(vertice, Integer.MAX_VALUE);
            predecessores.put(vertice, null);
        }
        distancias.put(origem, 0);
        prioridade.add(new No(origem, 0));

        while (!prioridade.isEmpty()) {
            No atual = prioridade.poll();

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
                    prioridade.add(new No(vizinho, novaDistancia));
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
    private static class No {
        int id;
        int distancia;

        /**
         * Construtor da classe Node.
         *
         * @param id O identificador do nó.
         * @param distancia A distância associada ao nó.
         */
        No(int id, int distancia) {
            this.id = id;
            this.distancia = distancia;
        }
    }
}