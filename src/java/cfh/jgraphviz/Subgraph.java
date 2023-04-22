/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface Subgraph extends StatementList<Subgraph>, Source, Target {

}

/**
 * @author Carlos F. Heuberger, 2023-03-06
 *
 */
class SubgraphImpl implements Subgraph {

    @Override
    public Subgraph graphs(GraphAttr first, GraphAttr... defaults) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Subgraph nodes(NodeAttr first, NodeAttr... defaults) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Subgraph edges(EdgeAttr first, EdgeAttr... defaults) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Subgraph add(Node node) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Subgraph add(Edge edge) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Subgraph add(Subgraph subgraph) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Edge to(Target target) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Edge from(Source source) {
        // TODO Auto-generated method stub
        return null;
    }
    
}