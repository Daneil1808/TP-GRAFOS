import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.jgrapht.graph.DefaultEdge;

public class VisualizadorGrafo {
    private org.jgrapht.Graph<Integer, DefaultEdge> grafo;

    public VisualizadorGrafo(org.jgrapht.Graph<Integer, DefaultEdge> grafo) {
        this.grafo = grafo;
    }

    public void exibirGrafo() {
        // Criar o grafo para o GraphStream
        Graph graph = new SingleGraph("Grafo");

        // Adicionar vértices e arestas ao GraphStream
        for (Integer vertice : grafo.vertexSet()) {
            graph.addNode(vertice.toString());
        }

        for (DefaultEdge aresta : grafo.edgeSet()) {
            Integer source = grafo.getEdgeSource(aresta);
            Integer target = grafo.getEdgeTarget(aresta);
            graph.addEdge(source.toString() + "-" + target.toString(), source.toString(), target.toString());
        }

        // Configurar a janela do visualizador
        Viewer viewer = graph.display();
        viewer.enableAutoLayout();
    }
}
