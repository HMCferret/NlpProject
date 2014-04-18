import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;


public class StandfordWrapper {
	
	public static void annotate(Annotation annotation, PrintWriter out)
	{
		StanfordCoreNLP pipeline = new StanfordCoreNLP();
		pipeline.annotate(annotation);
		if(out != null)
			pipeline.prettyPrint(annotation, out);
	}
	
	
	public static Set<String> getNames(Annotation annotation)
	{
		 List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		 Set<String> names = new TreeSet<String>();
		 for(int i=0; i<sentences.size(); ++i)
		 {
		      ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(i);    
		      //Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		      String name = null;
		      for (CoreMap token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
		    	  ArrayCoreMap aToken = (ArrayCoreMap) token;
		    	  String ne = aToken.get(CoreAnnotations.NamedEntityTagAnnotation.class);
		    	  String txt = aToken.get(CoreAnnotations.TextAnnotation.class);
		    	  if(ne.equals("PERSON"))
		    	  {
		    		  //System.out.println();
		    		  if(name != null)
		    			  name = name + " " + txt;
		    		  else 
		    			  name = txt;
		    	  }
		    	  else
		    	  {
		    		  if(name != null)
		    		  {
		    			  names.add(name);
		    			  name = null;
		    		  }
		    	  }
		      }
		      
		  }
		 return names;
	}
}
