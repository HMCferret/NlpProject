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
	
	
	
}
