package Demo;

import java.io.*;

import java.util.*;

import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;

public class StanfordCoreNlpDemo {

  public static void main(String[] args) throws IOException {
    PrintWriter out;
    if (args.length > 1) {
      out = new PrintWriter(new PrintStream(args[1]), true);
    } else {
      out = new PrintWriter(System.out, true);
    }
    PrintWriter xmlOut = null;
    if (args.length > 2) {
      xmlOut = new PrintWriter(args[2]);
    }

    StanfordCoreNLP pipeline = new StanfordCoreNLP();
    Annotation annotation;
    //if (args.length > 0) {
    //  annotation = new Annotation(IOUtils.slurpFileNoExceptions(args[0]));
    //} else {
      annotation = new Annotation("Kosgi Santosh sent an email to Stanford University.\n \"Die hard\" he said \"Live strong.\" He said \"Wow if only this works!\" He loves his father");
      annotation = new Annotation("I'll go to bed in an hour.");
      //}
    System.out.println("input is ready");
    
    pipeline.annotate(annotation);
    
    pipeline.prettyPrint(annotation, out);
    if (xmlOut != null) {
      pipeline.xmlPrint(annotation, xmlOut);
    }

    out.println(annotation.get(CoreAnnotations.TextAnnotation.class));
    // An Annotation is a Map and you can get and use the various analyses individually.
    // For instance, this gets the parse tree of the first sentence in the text.
    out.println();
    // The toString() method on an Annotation just prints the text of the Annotation
    // But you can see what is in it with other methods like toShorterString()
    out.println("The top level annotation");
    out.println(annotation.toShorterString());
    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
    for(int i=0; i<sentences.size(); ++i) {
      ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(i);
      out.println("The first sentence is:");
      out.println(sentence.toShorterString());
      Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
      out.println();
      out.println("The first sentence tokens are:");
      for (CoreMap token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
        ArrayCoreMap aToken = (ArrayCoreMap) token;
        out.println(aToken.keySet());
        //out.println(aToken.get(CoreAnnotations.ValueAnnotation.class));
        out.println(aToken.get(CoreAnnotations.TextAnnotation.class));
        //out.println(aToken.get(CoreAnnotations.OriginalTextAnnotation.class));
        out.println(aToken.get(CoreAnnotations.ParagraphAnnotation.class));
        //System.out.println(aToken.get(CorefCoreAnnotations.CorefClusterAnnotation.class));
      }
      
      
      out.println("The first sentence parse tree is:");
      tree.pennPrint(out);
      out.println("The first sentence basic dependencies are:"); 
      /*
      System.out.println(sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class).toString("plain"));
      out.println("The first sentence collapsed, CC-processed dependencies are:");
      SemanticGraph graph = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
      System.out.println(graph.toString("plain"));*/
      
    }
  }

}
