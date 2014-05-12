import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import weka.core.Instances;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;


public class Main {
	
	public static void main(String[] args) throws Exception {
//		Baseline bl = new Baseline();
//		bl.Run();
		
		List<String> listCategories = new ArrayList<String>();

//		listCategories.add("Love_Stories");
//		listCategories.add("Mystery");
//		listCategories.add("Poetry");
//		listCategories.add("Science_Fiction");
//		listCategories.add("Short_Stories");
//		listCategories.add("Adventure_Stories");
		listCategories.add("Fiction");
//		listCategories.add("Historical_Fiction");
		
		for(String item:listCategories){
			SVMTrain svmt = new SVMTrain();
			svmt.Train(item);
		}
	}
}
