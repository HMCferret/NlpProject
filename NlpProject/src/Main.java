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

		listCategories.add("Science_Fiction");//Training time: 394652ms ~ 6 mins
		listCategories.add("Love_Stories");//Training time: 10563837ms ~ 2.93 hours
		listCategories.add("Mystery");//Training time: 7092996 ~ 1.97 hours
		listCategories.add("Poetry");//Training time: 3132876ms ~ 52 mins
		listCategories.add("Short_Stories");//Training time: 2913532ms ~ 49 mins
		listCategories.add("Fiction");//Training time: 8005732ms ~ 2.22 hours
		listCategories.add("Historical_Fiction");//Training time: 12934728ms ~ 3.60 hours
		listCategories.add("Adventure_Stories");//Training time: 7756539ms ~ 2.15 hours
		
		for(String item:listCategories){
			SVMTrain svmt = new SVMTrain();
			svmt.Train(item);
		}
	}
}
