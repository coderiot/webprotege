package fu.berlin.csw.AspectPortlet.client.ui;

/**
 * Created by lars on 05.02.15.
 */
import org.semanticweb.owlapi.model.IRI;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Lars Parmakerli<br>
 * Freie Universität Berlin<br>
 * corporate semantic web<br>
 * Date: 14/08/2014
 */
public class Aspects implements Serializable {


    private static final long serialVersionUID = 1L;
    private List<IRI> aspects;
    private LinkedList<IRI> checkedAspects;
    public Aspects(){
        aspects = new LinkedList<IRI>();
    };
    public void setAspects(List<IRI> aspects) {
        this.aspects = aspects;
    }
    public List<IRI> getAspects() {
        return this.aspects;
    }
    public void addAspect(IRI iri){
        aspects.add(iri);
    }

    public void setCheckedAspects(LinkedList<IRI> checkedAspects){
        this.checkedAspects = checkedAspects;
    }

    public LinkedList<IRI> getChecketAspects(){
        return this.checkedAspects;
    }

    public void removeAspect(IRI iri){
        aspects.remove(iri);
    }



}