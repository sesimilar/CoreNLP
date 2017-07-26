package test;

import edu.stanford.nlp.trees.GrammaticalRelation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 吴滔 on 2017/7/26.
 */
public class EdgesTagTable {

    private Map<String,Boolean> edgesTag;

    public EdgesTagTable(){
        this.edgesTag = new HashMap<>();
        this.edgesTag.put("nsubj",true);
        this.edgesTag.put("dobj",true);
        this.edgesTag.put("neg",true);
//        this.edgesTag.put("",true);
//        this.edgesTag.put("",true);
    }
    public boolean checkRelation(String key){
        if(this.edgesTag.containsKey(key))
            return  true;
        else
            return false;
    }
}
