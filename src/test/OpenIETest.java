package test;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.OpenIE;
import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by 吴滔 on 2017/7/25.
 */
public class OpenIETest {
    public static void main(String[] args) throws Exception {

        // Create the Stanford CoreNLP pipeline
        InputStream input = new FileInputStream(new File("D:\\IdeaProjects\\CoreNLP\\src\\edu\\stanford\\nlp\\pipeline\\StanfordCoreNLP-chinese.properties"));
//        InputStream input = new FileInputStream(new File("\\edu\\stanford\\nlp\\pipeline\\StanfordCoreNLP-chinese.properties"));
        Properties props = new Properties();
        props.load(input);
        input.close();
//        Properties props = PropertiesUtils.asProperties(
//                "annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie"
//        );
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Annotate an example document.
        String text;
        if (args.length > 0) {
            text = IOUtils.slurpFile(args[0]);
        } else {
//            text = "克林顿说，华盛顿将逐步落实对韩国的经济援助。"
//                    + "金大中对克林顿的讲话报以掌声：克林顿总统在会谈中重申，他坚定地支持韩国摆脱经济危机。";
            text = "各方都避免采取可能升级局势的行动。";
        }
        Annotation doc = new Annotation(text);
        pipeline.annotate(doc);

        // Loop over sentences in the document
        int sentNo = 0;
        for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("Sentence #" + ++sentNo + ": " + sentence.get(CoreAnnotations.TextAnnotation.class));

            Queue<IndexedWord> verbs = new LinkedList<IndexedWord>();
            // Print SemanticGraph
            //System.out.println(sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.LIST));
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class);
            IndexedWord root = dependencies.getFirstRoot();
            verbs.add(root);
            EdgesTagTable tags = new EdgesTagTable();
            List<IndexedWord> backbone = new ArrayList<>();
            while (!verbs.isEmpty()) {
                List<SemanticGraphEdge> outEdges = dependencies.getOutEdgesSorted(verbs.poll());
                for (SemanticGraphEdge outEdge : outEdges) {
                    if (outEdge.getTarget().tag().startsWith("V")) {
                        verbs.add(outEdge.getTarget());
                    }
                    else if (!tags.checkRelation(outEdge.getRelation().toString())) {
                        dependencies.removeEdge(outEdge);
                    }else if (outEdge.getRelation().toString().equals("nsubj")|| outEdge.getRelation().toString().equals("dobj")){
                        backbone.add(outEdge.getTarget());
                    }
                }
            }
            for (IndexedWord word: backbone){
                     List<SemanticGraphEdge> outEdges = dependencies.getOutEdgesSorted(word);
                     if(outEdges.size()!=0){
                         for(SemanticGraphEdge edge: outEdges){
                             dependencies.removeEdge(edge);
                         }
                     }
            }
            System.out.print(dependencies.toString(SemanticGraph.OutputFormat.LIST));


//            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//            tree.pennPrint();
            // Get the OpenIE triples for the sentence
//            Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);


            // Print the triples
//            for (RelationTriple triple : triples) {
//                System.out.println(triple.confidence + "\t" +
//                        triple.subjectLemmaGloss() + "\t" +
//                        triple.relationLemmaGloss() + "\t" +
//                        triple.objectLemmaGloss());
//            }

            // Alternately, to only run e.g., the clause splitter:
//            List<SentenceFragment> clauses = new OpenIE(props).clausesInSentence(sentence);
//            for (SentenceFragment clause : clauses) {
//                System.out.println(clause.parseTree.toString(SemanticGraph.OutputFormat.LIST));
//            }
//            System.out.println();
        }
    }
}
