import java.util.*;

public class Main {
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
        System.out.println(verificarConexo(listaAdjacencia, vertices) ? "1" : "0");
        // Verifica se o grafo é bipartido
        System.out.println(verificarBipartido(listaAdjacencia) ? "1" : "0");
        // Verifica se o grafo é euleriano
        System.out.println(verificarEuleriano(listaAdjacencia) ? "1" : "0");
        // Verifica se o grafo possui ciclo
        System.out.println(possuiCiclo(listaAdjacencia) ? "1" : "0");
        // Lista os componentes conexos do grafo
        System.out.println(listarComponentesConexas(listaAdjacencia));
        // Lista os componentes fortemente conexos, se o grafo for direcionado; caso contrário, imprime -1
        System.out.println(isDirecionado ? formatarListasEncadeadas(listarComponentesFortementeConexas(listaAdjacencia)) : "-1");
        // Lista a trilha euleriana, se existir
        System.out.println(listarTrilhaEuleriana(listaAdjacencia));
        // Lista os vértices de articulação do grafo
        System.out.println(listarVerticesArticulacao(listaAdjacencia));
        // Lista as arestas ponte do grafo
        System.out.println(formatarListasEncadeadas(listarArestasPonte(listaAdjacencia)));
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


    // Função para verificar se o grafo é conexo usando BFS
    public static Boolean verificarConexo(Map<Integer, List<Integer>> listaAdjacencia, Set<Integer> vertices) {
        if (vertices.isEmpty()) {
            return true; // Considerar grafo vazio como conexo
        }

        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> fila = new LinkedList<>();

        // Pegando um vértice inicial arbitrário
        int verticeInicial = vertices.iterator().next();
        fila.add(verticeInicial);
        visitados.add(verticeInicial);

        // Realiza a BFS
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
        return visitados.size() == vertices.size();
    }

    // Função para verificar se o grafo é bipartido usando BFS
    public static Boolean verificarBipartido(Map<Integer, List<Integer>> listaAdjacencia) {
        Map<Integer, Integer> cores = new HashMap<>();

        for (int vertice : listaAdjacencia.keySet()) {
            if (!cores.containsKey(vertice)) {
                Queue<Integer> fila = new LinkedList<>();
                fila.add(vertice);
                cores.put(vertice, 0);

                while (!fila.isEmpty()) {
                    int atual = fila.poll();
                    for (int vizinho : listaAdjacencia.get(atual)) {
                        if (!cores.containsKey(vizinho)) {
                            cores.put(vizinho, 1 - cores.get(atual));
                            fila.add(vizinho);
                        } else if (cores.get(vizinho).equals(cores.get(atual))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    // Função para verificar se o grafo é Euleriano
    public static Boolean verificarEuleriano(Map<Integer, List<Integer>> listaAdjacencia) {
        int impares = 0;

        for (int vertice : listaAdjacencia.keySet()) {
            if (listaAdjacencia.get(vertice).size() % 2 != 0) {
                impares++;
            }
        }

        return impares == 0; // Euleriano se todos os vértices têm grau par
    }

    // Função para verificar se o grafo possui ciclo usando DFS
    public static Boolean possuiCiclo(Map<Integer, List<Integer>> listaAdjacencia) {
        Set<Integer> visitados = new HashSet<>();
        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice) && dfsCiclo(vertice, -1, visitados, listaAdjacencia)) {
                return true;
            }
        }
        return false;
    }

    //Encontra ciclos por meio de um DFS
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

    // Função para listar componentes conexas usando BFS
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


    // Função para listar componentes fortemente conexas usando o algoritmo de Kosaraju
    public static List<List<Integer>> listarComponentesFortementeConexas(Map<Integer, List<Integer>> listaAdjacencia) {
        List<List<Integer>> componentes = new ArrayList<>();
        Stack<Integer> pilha = new Stack<>();
        Set<Integer> visitados = new HashSet<>();

        // Passo 1: Fazer a primeira DFS e empilhar os vértices
        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice)) {
                dfsEmpilhar(vertice, visitados, listaAdjacencia, pilha);
            }
        }

        // Passo 2: Inverter o grafo
        Map<Integer, List<Integer>> grafoTransposto = transporGrafo(listaAdjacencia);

        // Passo 3: Fazer a segunda DFS no grafo transposto
        visitados.clear();
        while (!pilha.isEmpty()) {
            int vertice = pilha.pop();
            if (!visitados.contains(vertice)) {
                List<Integer> componente = new ArrayList<>();
                dfsComponentes(vertice, visitados, grafoTransposto, componente);
                Collections.sort(componente);
                componentes.add(componente);
            }
        }

        return componentes;
    }

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

        // Após visitar todos os vizinhos, adiciona o vértice na pilha
        pilha.push(vertice);
    }


    private static void dfsComponentes(int vertice, Set<Integer> visitados, Map<Integer, List<Integer>> listaAdjacencia, List<Integer> componente) {
        // Marca o vértice como visitado
        visitados.add(vertice);
        // Adiciona o vértice à lista da componente atual
        componente.add(vertice);

        // Itera sobre todos os vizinhos do vértice
        for (int vizinho : listaAdjacencia.get(vertice)) {
            // Se o vizinho ainda não foi visitado, realiza a chamada recursiva
            if (!visitados.contains(vizinho)) {
                dfsComponentes(vizinho, visitados, listaAdjacencia, componente);
            }
        }
    }


    private static Map<Integer, List<Integer>> transporGrafo(Map<Integer, List<Integer>> listaAdjacencia) {
        // Cria um novo mapa para armazenar o grafo transposto
        Map<Integer, List<Integer>> grafoTransposto = new HashMap<>();

        // Itera sobre todos os vértices no grafo original
        for (int vertice : listaAdjacencia.keySet()) {
            // Garante que o vértice está presente no grafo transposto
            grafoTransposto.putIfAbsent(vertice, new ArrayList<>());

            // Itera sobre todos os vizinhos do vértice
            for (int vizinho : listaAdjacencia.get(vertice)) {
                // Garante que o vizinho está presente no grafo transposto
                grafoTransposto.putIfAbsent(vizinho, new ArrayList<>());
                // Adiciona o vértice à lista de adjacência do vizinho no grafo transposto
                grafoTransposto.get(vizinho).add(vertice);
            }
        }
        return grafoTransposto;
    }


    // Função para listar uma trilha Euleriana
    public static String listarTrilhaEuleriana(Map<Integer, List<Integer>> listaAdjacencia) {
        if (!verificarEuleriano(listaAdjacencia)) {
            return "-1";
        }

        LinkedList<Integer> trilha = new LinkedList<>();
        Map<Integer, Iterator<Integer>> iteradores = new HashMap<>();
        Stack<Integer> pilha = new Stack<>();
        Set<String> arestasUsadas = new HashSet<>();

        // Inicializa iteradores para cada vértice
        for (int vertice : listaAdjacencia.keySet()) {
            iteradores.put(vertice, listaAdjacencia.get(vertice).iterator());
        }

        // Encontra um vértice inicial que possui arestas
        int verticeInicial = listaAdjacencia.keySet().iterator().next();
        pilha.push(verticeInicial);

        while (!pilha.isEmpty()) {
            int vertice = pilha.peek();
            Iterator<Integer> iterador = iteradores.get(vertice);

            if (iterador.hasNext()) {
                int proximoVertice = iterador.next();
                String chaveAresta = gerarChaveAresta(vertice, proximoVertice);

                if (!arestasUsadas.contains(chaveAresta)) {
                    arestasUsadas.add(chaveAresta);
                    pilha.push(proximoVertice);
                }
            } else {
                trilha.addFirst(vertice);
                pilha.pop();
            }
        }

        return construirString(trilha);
    }

    private static String gerarChaveAresta(int u, int v) {
        return Math.min(u, v) + "-" + Math.max(u, v);
    }


    // Função para listar vértices de articulação usando DFS
    public static String listarVerticesArticulacao(Map<Integer, List<Integer>> listaAdjacencia) {
        Set<Integer> articulacoes = new HashSet<>();
        Map<Integer, Integer> descoberta = new HashMap<>();
        Map<Integer, Integer> menor = new HashMap<>();
        Set<Integer> visitados = new HashSet<>();
        int[] tempo = {0};

        // Realiza a DFS para encontrar vértices de articulação
        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice)) {
                dfsArticulacao(vertice, -1, visitados, descoberta, menor, tempo, articulacoes, listaAdjacencia);
            }
        }

        // Converte o conjunto de vértices de articulação para uma lista e ordena
        List<Integer> resultado = new ArrayList<>(articulacoes);
        Collections.sort(resultado);

        // Constrói e retorna a string representando os vértices de articulação
        return construirString(resultado);
    }

    private static void dfsArticulacao(int vertice, int pai, Set<Integer> visitados, Map<Integer, Integer> descoberta,
                                       Map<Integer, Integer> menor, int[] tempo, Set<Integer> articulacoes,
                                       Map<Integer, List<Integer>> listaAdjacencia) {
        // Marca o vértice como visitado e define o tempo de descoberta
        visitados.add(vertice);
        descoberta.put(vertice, tempo[0]);
        menor.put(vertice, tempo[0]);
        tempo[0] += 1;
        int filhos = 0;

        // Itera sobre todos os vizinhos do vértice
        for (int vizinho : listaAdjacencia.get(vertice)) {
            // Ignora o pai do vértice na DFS
            if (vizinho == pai) continue;

            if (!visitados.contains(vizinho)) {
                filhos++;
                // Realiza a DFS recursiva para o vizinho
                dfsArticulacao(vizinho, vertice, visitados, descoberta, menor, tempo, articulacoes, listaAdjacencia);

                // Atualiza o menor tempo de alcance do vértice
                menor.put(vertice, Math.min(menor.get(vertice), menor.get(vizinho)));

                // Verifica se o vértice é um vértice de articulação
                if (pai != -1 && menor.get(vizinho) >= descoberta.get(vertice)) {
                    articulacoes.add(vertice);
                }
            } else {
                // Atualiza o menor tempo de alcance baseado no vizinho já visitado
                menor.put(vertice, Math.min(menor.get(vertice), descoberta.get(vizinho)));
            }
        }

        // Verifica o caso especial para a raiz da DFS
        if (pai == -1 && filhos > 1) {
            articulacoes.add(vertice);
        }
    }

    // Função para listar arestas ponte usando DFS
    public static List<List<Integer>> listarArestasPonte(Map<Integer, List<Integer>> listaAdjacencia) {
        List<List<Integer>> pontes = new ArrayList<>();
        Map<Integer, Integer> descoberta = new HashMap<>();
        Map<Integer, Integer> menor = new HashMap<>();
        Set<Integer> visitados = new HashSet<>();
        int[] tempo = {0}; // Armazena o tempo de descoberta

        // Itera sobre todos os vértices, para o caso de o grafo não ser conectado
        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice)) {
                dfsPonte(vertice, -1, visitados, descoberta, menor, tempo, pontes, listaAdjacencia);
            }
        }

        return pontes;
    }

    //Encontra pontes por meio de um DFS
    private static void dfsPonte(int u, int pai, Set<Integer> visitados,
                                 Map<Integer, Integer> descoberta, Map<Integer, Integer> menor,
                                 int[] tempo, List<List<Integer>> pontes,
                                 Map<Integer, List<Integer>> listaAdjacencia) {
        visitados.add(u);
        descoberta.put(u, tempo[0]);
        menor.put(u, tempo[0]);
        tempo[0]++;

        for (int v : listaAdjacencia.get(u)) {
            if (v == pai) {
                continue; // Ignora a aresta de retorno ao pai
            }
            if (!visitados.contains(v)) {
                dfsPonte(v, u, visitados, descoberta, menor, tempo, pontes, listaAdjacencia);

                // Atualiza o menor valor alcançável
                menor.put(u, Math.min(menor.get(u), menor.get(v)));

                // Verifica se a aresta (u, v) é uma ponte
                if (menor.get(v) > descoberta.get(u)) {
                    pontes.add(Arrays.asList(u, v));
                }
            } else {
                // Atualiza menor[u] para o valor de descoberta de v
                menor.put(u, Math.min(menor.get(u), descoberta.get(v)));
            }
        }
    }

    // Função auxiliar para formatar listas encadeadas como uma string com espaços entre os valores
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

    // Função para realizar uma busca em profundidade (DFS) em um grafo e registrar as arestas usadas.
    private static void dfsProfundidade(int u, Map<Integer, List<Integer>> listaAdjacencia, Set<Integer> visitados, List<Integer> arestasUsadas, Map<List<Integer>, Integer> idsArestas) {
        // Marca o vértice atual como visitado.
        visitados.add(u);

        // Itera sobre todos os vizinhos do vértice atual.
        for (int v : listaAdjacencia.get(u)) {
            // Verifica se o vizinho ainda não foi visitado.
            if (!visitados.contains(v)) {
                // Cria uma lista representando a aresta entre u e v.
                List<Integer> aresta = Arrays.asList(u, v);
                // Se o mapa de IDs de arestas contém a aresta, adiciona o ID da aresta à lista de arestas usadas.
                if (idsArestas.containsKey(aresta)) {
                    arestasUsadas.add(idsArestas.get(aresta));
                }
                // Chama a função recursivamente para explorar o vizinho.
                dfsProfundidade(v, listaAdjacencia, visitados, arestasUsadas, idsArestas);
            }
        }
    }

    // Função para gerar árvore de largura usando BFS
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
                    // Adiciona o ID da aresta ao resultado
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
    private static String gerarArvoreGeradoraMinima(Map<Integer, List<Integer>> listaAdjacencia, Map<List<Integer>, Integer> pesos, Map<List<Integer>, Integer> idsArestas) {
        List<Edge> arestas = new ArrayList<>();
        Set<Integer> pesosUnicos = new HashSet<>();

        // Criação de uma lista de arestas com os pesos e IDs
        for (Map.Entry<List<Integer>, Integer> entrada : pesos.entrySet()) {
            List<Integer> aresta = entrada.getKey();
            int peso = entrada.getValue();
            int id = idsArestas.getOrDefault(aresta, -1);
            arestas.add(new Edge(aresta.get(0), aresta.get(1), peso, id));
            pesosUnicos.add(peso);
        }

        // Verifica se há mais de um peso único
        if (pesosUnicos.size() == 1) {
            return "-1";  // Retorna -1 se todas as arestas têm o mesmo peso
        }

        // Ordena as arestas pelo peso
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
    public static String ordenarTopologicamente(Map<Integer, List<Integer>> listaAdjacencia) {

        // Contagem de graus de entrada
        Map<Integer, Integer> grauEntrada = new HashMap<>();
        for (int vertice : listaAdjacencia.keySet()) {
            grauEntrada.put(vertice, 0);
        }
        for (int vertice : listaAdjacencia.keySet()) {
            for (int vizinho : listaAdjacencia.get(vertice)) {
                grauEntrada.put(vizinho, grauEntrada.get(vizinho) + 1);
            }
        }

        // Inicializa a fila com vértices de grau de entrada zero
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

        // Verifica se há um ciclo
        if (resultado.size() != listaAdjacencia.size()) {
            return ""; // Retorna uma string vazia se houver um ciclo
        }

        // Construa a string representando a ordenação topológica
        StringBuilder resultadoString = new StringBuilder();
        for (Integer v : resultado) {
            resultadoString.append(v).append(" ");
        }

        return resultadoString.toString().trim(); // Remove o espaço final extra
    }

    // Função que calcula o caminho mínimo usando o algoritmo de Dijkstra
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

    // Função auxiliar para construir o caminho mais curto a partir dos predecessores
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

    // Função para verificar se é possível calcular o caminho mínimo entre dois vértices
    private static Boolean podeCalcularCaminhoMinimo(Map<Integer, List<Integer>> adj, int origem, int destino) {
        if (origem == destino) return true; // Caminho trivial se origem e destino são o mesmo vértice

        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> fila = new LinkedList<>();
        fila.add(origem);
        visitados.add(origem);

        while (!fila.isEmpty()) {
            int verticeAtual = fila.poll();

            if (verticeAtual == destino) {
                return true;
            }

            for (int vizinho : adj.getOrDefault(verticeAtual, Collections.emptyList())) {
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    fila.add(vizinho);
                }
            }
        }
        return false;
    }

    //Cálculo do Fluxo Máximo
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

    // Função que realiza uma busca em largura (BFS) para encontrar um caminho com capacidade positiva entre dois vértices.
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


    // Geração do Fecho Transitivo
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

    private static String construirString(List<Integer> list) {
        // Construa a string representando o caminho
        StringBuilder caminhoString = new StringBuilder();
        for (Integer v : list) {
            caminhoString.append(v).append(" ");
        }

        return caminhoString.toString().trim(); // Remove o espaço final extra
    }


    // Função que formata uma lista de inteiros em uma string com espaços entre os elementos.
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

    // Classe que implementa a estrutura de dados Union-Find (ou Disjoint Set Union).
    private static class UnionFind {
        // Array que armazena o pai de cada vértice.
        private final int[] pai;
        // Array que armazena o rank (profundidade) das árvores.
        private final int[] rank;

        // Construtor da classe UnionFind.
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

        // Método para encontrar a raiz do conjunto que contém o vértice x.
        int encontrar(int x) {
            // Se o pai de x não for x, continua a busca recursivamente.
            if (pai[x] != x) {
                // Atualiza o pai de x diretamente para a raiz encontrada (compressão de caminho).
                pai[x] = encontrar(pai[x]);
            }
            // Retorna a raiz do conjunto.
            return pai[x];
        }

        // Método para unir os conjuntos que contêm os vértices x e y.
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

    // Classe auxiliar para representar os nós da fila de prioridade
    private static class Node {
        int id;
        int distancia;

        Node(int id, int distancia) {
            this.id = id;
            this.distancia = distancia;
        }
    }
}

