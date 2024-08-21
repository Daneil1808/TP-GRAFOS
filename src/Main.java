import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Leitura do número de vértices e arestas
        int numVertices = scanner.nextInt();
        int numArestas = scanner.nextInt();
        scanner.nextLine();

        // Leitura do tipo de grafo
        String tipoGrafo = scanner.next();
        boolean isDirecionado = tipoGrafo.equals("direcionado");

        // Inicialização das estruturas de dados
        Map<Integer, List<Integer>> listaAdjacencia = new HashMap<>();
        Map<List<Integer>, Integer> pesos = new HashMap<>();
        Map<List<Integer>, Integer> idsArestas = new HashMap<>();
        Set<Integer> vertices = new HashSet<>();

        // Inicializa a lista de adjacência para cada vértice
        for (int i = 0; i < numVertices; i++) {
            listaAdjacencia.put(i, new ArrayList<>());
            vertices.add(i);
        }

        // Leitura das arestas
        for (int i = 0; i < numArestas; i++) {
            int idAresta = scanner.nextInt();
            int verticeU = scanner.nextInt();
            int verticeV = scanner.nextInt();
            int pesoAresta = scanner.nextInt();
            scanner.nextLine();

            // Adiciona a aresta na lista de adjacência
            if (listaAdjacencia.containsKey(verticeU)) {
                listaAdjacencia.get(verticeU).add(verticeV);
            } else {
                listaAdjacencia.put(verticeU, new ArrayList<>(Collections.singletonList(verticeV)));
            }

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
                pesos.put(Arrays.asList(verticeV, verticeU), pesoAresta);
                idsArestas.put(Arrays.asList(verticeV, verticeU), idAresta);
            }
        }

        // Execução das funções solicitadas
        if(isDirecionado) {
            System.out.println(verificarConexo(listaAdjacencia, vertices));
        }
        System.out.println(verificarBipartido(listaAdjacencia));
        System.out.println(verificarEuleriano(listaAdjacencia));
        System.out.println(possuiCiclo(listaAdjacencia));
        System.out.println(listarComponentesConexas(listaAdjacencia));
        if(isDirecionado) {
            System.out.println(formatarListasEncadeadas(listarComponentesFortementeConexas(listaAdjacencia)));
        }
        System.out.println(listarTrilhaEuleriana(listaAdjacencia));
        System.out.println(listarVerticesArticulacao(listaAdjacencia));
        System.out.println(formatarListasEncadeadas(listarArestasPonte(listaAdjacencia)));
        System.out.println(gerarArvoreProfundidade(listaAdjacencia, idsArestas));
        System.out.println(gerarArvoreLargura(listaAdjacencia, idsArestas));
        System.out.println(gerarArvoreGeradoraMinima(listaAdjacencia, pesos, idsArestas));
        if(isDirecionado) { // Ordenação topológica não está disponível para grafos não direcionados
            System.out.println(ordenarTopologicamente(listaAdjacencia));
        }
        if (vertices.contains(0) && vertices.contains(numVertices - 1)) {
            if (podeCalcularCaminhoMinimo(listaAdjacencia, 0, numVertices - 1)) {
                System.out.println(calcularCaminhoMinimo(listaAdjacencia, pesos, 0, numVertices - 1));
            }
        }
        if(isDirecionado) {  // Fluxo maximo e Fecho transitivo não está disponível para grafos não direcionados
            System.out.println(calcularFluxoMaximo(listaAdjacencia, pesos, 0, numVertices - 1));
            System.out.println(gerarFechoTransitivo(listaAdjacencia, 0));
        }


        scanner.close();
    }

    // Função para verificar se o grafo é conexo usando BFS
    public static Integer verificarConexo(Map<Integer, List<Integer>> listaAdjacencia, Set<Integer> vertices) {
        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> fila = new LinkedList<>();

        int verticeInicial = vertices.iterator().next();
        fila.add(verticeInicial);
        visitados.add(verticeInicial);

        while (!fila.isEmpty()) {
            int vertice = fila.poll();
            for (int vizinho : listaAdjacencia.get(vertice)) {
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    fila.add(vizinho);
                }
            }
        }

        return visitados.size() == vertices.size() ? 1 : 0;
    }

    // Função para verificar se o grafo é bipartido usando BFS
    public static Integer verificarBipartido(Map<Integer, List<Integer>> listaAdjacencia) {
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
                            return 0;
                        }
                    }
                }
            }
        }
        return 1;
    }

    // Função para verificar se o grafo é Euleriano
    public static Integer verificarEuleriano(Map<Integer, List<Integer>> listaAdjacencia) {
        int impares = 0;

        for (int vertice : listaAdjacencia.keySet()) {
            if (listaAdjacencia.get(vertice).size() % 2 != 0) {
                impares++;
            }
        }

        return impares == 0 ? 1 : 0; // Euleriano se todos os vértices têm grau par
    }

    // Função para verificar se o grafo possui ciclo usando DFS
    public static Integer possuiCiclo(Map<Integer, List<Integer>> listaAdjacencia) {
        Set<Integer> visitados = new HashSet<>();
        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice) && dfsCiclo(vertice, -1, visitados, listaAdjacencia)) {
                return 1;
            }
        }
        return 0;
    }

    private static boolean dfsCiclo(int vertice, int pai, Set<Integer> visitados, Map<Integer, List<Integer>> listaAdjacencia) {
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
        visitados.add(vertice);
        for (int vizinho : listaAdjacencia.get(vertice)) {
            if (!visitados.contains(vizinho)) {
                dfsEmpilhar(vizinho, visitados, listaAdjacencia, pilha);
            }
        }
        pilha.push(vertice);
    }

    private static void dfsComponentes(int vertice, Set<Integer> visitados, Map<Integer, List<Integer>> listaAdjacencia, List<Integer> componente) {
        visitados.add(vertice);
        componente.add(vertice);
        for (int vizinho : listaAdjacencia.get(vertice)) {
            if (!visitados.contains(vizinho)) {
                dfsComponentes(vizinho, visitados, listaAdjacencia, componente);
            }
        }
    }

    private static Map<Integer, List<Integer>> transporGrafo(Map<Integer, List<Integer>> listaAdjacencia) {
        Map<Integer, List<Integer>> grafoTransposto = new HashMap<>();
        for (int vertice : listaAdjacencia.keySet()) {
            grafoTransposto.putIfAbsent(vertice, new ArrayList<>());
            for (int vizinho : listaAdjacencia.get(vertice)) {
                grafoTransposto.putIfAbsent(vizinho, new ArrayList<>());
                grafoTransposto.get(vizinho).add(vertice);
            }
        }
        return grafoTransposto;
    }

    // Função para listar uma trilha Euleriana
    public static String listarTrilhaEuleriana(Map<Integer, List<Integer>> listaAdjacencia) {
        LinkedList<Integer> trilha = new LinkedList<>();
        Map<Integer, Iterator<Integer>> iteradores = new HashMap<>();
        Stack<Integer> pilha = new Stack<>();

        // Inicializar iteradores para cada vértice
        for (int vertice : listaAdjacencia.keySet()) {
            iteradores.put(vertice, listaAdjacencia.get(vertice).iterator());
        }

        int verticeInicial = listaAdjacencia.keySet().iterator().next();
        pilha.push(verticeInicial);

        while (!pilha.isEmpty()) {
            int vertice = pilha.peek();
            if (iteradores.get(vertice).hasNext()) {
                pilha.push(iteradores.get(vertice).next());
            } else {
                trilha.addFirst(vertice);
                pilha.pop();
            }
        }

        return construirString(trilha);
    }

    // Função para listar vértices de articulação usando DFS
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
        Collections.sort(resultado);
        return construirString(resultado);
    }

    private static void dfsArticulacao(int vertice, int pai, Set<Integer> visitados, Map<Integer, Integer> descoberta,
                                       Map<Integer, Integer> menor, int[] tempo, Set<Integer> articulacoes,
                                       Map<Integer, List<Integer>> listaAdjacencia) {
        visitados.add(vertice);
        descoberta.put(vertice, tempo[0]);
        menor.put(vertice, tempo[0]);
        tempo[0] += 1;
        int filhos = 0;

        for (int vizinho : listaAdjacencia.get(vertice)) {
            if (vizinho == pai) continue;

            if (!visitados.contains(vizinho)) {
                filhos++;
                dfsArticulacao(vizinho, vertice, visitados, descoberta, menor, tempo, articulacoes, listaAdjacencia);
                menor.put(vertice, Math.min(menor.get(vertice), menor.get(vizinho)));

                if (pai != -1 && menor.get(vizinho) >= descoberta.get(vertice)) {
                    articulacoes.add(vertice);
                }
            } else {
                menor.put(vertice, Math.min(menor.get(vertice), descoberta.get(vizinho)));
            }
        }

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
        int[] tempo = {0};

        for (int vertice : listaAdjacencia.keySet()) {
            if (!visitados.contains(vertice)) {
                dfsPonte(vertice, -1, visitados, descoberta, menor, tempo, pontes, listaAdjacencia);
            }
        }

        return pontes;
    }

    private static void dfsPonte(int vertice, int pai, Set<Integer> visitados, Map<Integer, Integer> descoberta,
                                 Map<Integer, Integer> menor, int[] tempo, List<List<Integer>> pontes,
                                 Map<Integer, List<Integer>> listaAdjacencia) {
        visitados.add(vertice);
        descoberta.put(vertice, tempo[0]);
        menor.put(vertice, tempo[0]);
        tempo[0] += 1;

        for (int vizinho : listaAdjacencia.get(vertice)) {
            if (vizinho == pai) continue;

            if (!visitados.contains(vizinho)) {
                dfsPonte(vizinho, vertice, visitados, descoberta, menor, tempo, pontes, listaAdjacencia);
                menor.put(vertice, Math.min(menor.get(vertice), menor.get(vizinho)));

                if (menor.get(vizinho) > descoberta.get(vertice)) {
                    pontes.add(Arrays.asList(vertice, vizinho));
                }
            } else {
                menor.put(vertice, Math.min(menor.get(vertice), descoberta.get(vizinho)));
            }
        }
    }

    // Função auxiliar para formatar listas encadeadas como uma string com espaços entre os valores
    public static String formatarListasEncadeadas(List<List<Integer>> pontes) {
        StringBuilder sb = new StringBuilder();
        for (List<Integer> aresta : pontes) {
            sb.append(aresta.get(0)).append(" ").append(aresta.get(1)).append("\n");
        }
        return sb.toString().trim();
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

        return resultado.length() > 0 ? resultado.toString().trim() : "-1";
    }

    private static void dfsProfundidade(int u, Map<Integer, List<Integer>> listaAdjacencia, Set<Integer> visitados, List<Integer> arestasUsadas, Map<List<Integer>, Integer> idsArestas) {
        visitados.add(u);
        for (int v : listaAdjacencia.get(u)) {
            if (!visitados.contains(v)) {
                // Adiciona o ID da aresta ao resultado
                List<Integer> aresta = Arrays.asList(u, v);
                if (idsArestas.containsKey(aresta)) {
                    arestasUsadas.add(idsArestas.get(aresta));
                }
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

        // Criação de uma lista de arestas com os pesos e IDs
        for (Map.Entry<List<Integer>, Integer> entrada : pesos.entrySet()) {
            List<Integer> aresta = entrada.getKey();
            int peso = entrada.getValue();
            int id = idsArestas.getOrDefault(aresta, -1);
            arestas.add(new Edge(aresta.get(0), aresta.get(1), peso, id));
        }

        // Ordena as arestas pelo peso
        arestas.sort(Comparator.comparingInt(a -> a.peso));

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

    // 1. Ordenação Topológica
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

    // 2. Caminho Mínimo
    public static String calcularCaminhoMinimo(Map<Integer, List<Integer>> listaAdjacencia, Map<List<Integer>, Integer> pesos, int origem, int destino) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{origem, 0});

        Map<Integer, Integer> distancias = new HashMap<>();
        Map<Integer, Integer> predecessores = new HashMap<>();

        for (int vertice : listaAdjacencia.keySet()) {
            distancias.put(vertice, Integer.MAX_VALUE);
            predecessores.put(vertice, -1);
        }
        distancias.put(origem, 0);

        while (!pq.isEmpty()) {
            int[] atual = pq.poll();
            int vertice = atual[0];
            int distancia = atual[1];

            if (distancia > distancias.get(vertice)) continue;

            for (int vizinho : listaAdjacencia.get(vertice)) {
                int novaDistancia = distancia + pesos.getOrDefault(Arrays.asList(vertice, vizinho), Integer.MAX_VALUE);
                if (novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, vertice);
                    pq.add(new int[]{vizinho, novaDistancia});
                }
            }
        }

        List<Integer> caminho = new ArrayList<>();
        for (Integer v = destino; v != -1; v = predecessores.get(v)) {
            caminho.add(v);
        }
        Collections.reverse(caminho);

        return construirString(caminho);
    }


    // 3. Fluxo Máximo
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


    private static Map<Integer, Integer> bfsCapacidade(Map<Integer, List<Integer>> listaAdjacencia, Map<List<Integer>, Integer> capacidade, int origem, int destino) {
        Map<Integer, Integer> caminho = new HashMap<>();
        Queue<Integer> fila = new LinkedList<>();
        Set<Integer> visitado = new HashSet<>();

        fila.add(origem);
        visitado.add(origem);
        caminho.put(origem, null);

        while (!fila.isEmpty()) {
            int u = fila.poll();
            if (u == destino) break;

            for (int v : listaAdjacencia.getOrDefault(u, Collections.emptyList())) {
                List<Integer> aresta = Arrays.asList(u, v);
                if (!visitado.contains(v) && capacidade.getOrDefault(aresta, 0) > 0) {
                    fila.add(v);
                    visitado.add(v);
                    caminho.put(v, u);
                }
            }
        }

        return caminho;
    }

    // 4. Fecho Transitivo
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

    // Função para verificar se há um caminho entre os vértices usando BFS
    private static boolean podeCalcularCaminhoMinimo(Map<Integer, List<Integer>> adj, int origem, int destino) {
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

    private static String construirString(List<Integer> list) {
        // Construa a string representando o caminho
        StringBuilder caminhoString = new StringBuilder();
        for (Integer v : list) {
            caminhoString.append(v).append(" ");
        }

        return caminhoString.toString().trim(); // Remove o espaço final extra
    }


    public static String formatarListaComEspacos(List<Integer> lista) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            sb.append(lista.get(i));
            if (i < lista.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    // Classe que implementa a estrutura de dados Union-Find (ou Disjoint Set Union).
    private static class UnionFind {
        private int[] pai;
        private int[] rank;

        UnionFind(int tamanho) {
            pai = new int[tamanho];
            rank = new int[tamanho];
            for (int i = 0; i < tamanho; i++) {
                pai[i] = i;
                rank[i] = 0;
            }
        }

        int encontrar(int x) {
            if (pai[x] != x) {
                pai[x] = encontrar(pai[x]);
            }
            return pai[x];
        }

        void unir(int x, int y) {
            int raizX = encontrar(x);
            int raizY = encontrar(y);
            if (raizX != raizY) {
                if (rank[raizX] > rank[raizY]) {
                    pai[raizY] = raizX;
                } else if (rank[raizX] < rank[raizY]) {
                    pai[raizX] = raizY;
                } else {
                    pai[raizY] = raizX;
                    rank[raizX]++;
                }
            }
        }
    }
}

