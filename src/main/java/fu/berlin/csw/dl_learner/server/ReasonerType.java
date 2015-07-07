package fu.berlin.csw.dl_learner.server;

/**
 * Created by lars on 11.06.15.
 */
public enum ReasonerType {

    /* Sparql Reasoner can be used with an external knowledge source */
    SPARQL_REASONER,

    /* Hermit Reasoner can be used with the local OWL Ontology */
    HERMIT_REASONER,


    /* Pellet Reasoner can be used with the local OWL Ontology */
    PELLET_REASONER

}
