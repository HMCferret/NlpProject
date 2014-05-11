import java.util.ArrayList;
import java.util.List;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;

public class AJ48 {
	private FastVector atts;
	private FastVector attValsNumeric;
	private FastVector attValsBool;
	private FastVector attValsPercent;
	private Instances dataTrain;
	private Instances dataTest;
	private List<ConversationalNetworkFeature> train;
	private List<ConversationalNetworkFeature> test;
//	private List<CandidateQuoteResult> cqr;
	private int range;
	private int rangeLength;
	private double[] vals;
	
	public AJ48(List<ConversationalNetworkFeature> train, List<ConversationalNetworkFeature> test, int range, int rangeLength)
	{
		this.train=train;
		this.test=test;
		this.range=range;
		this.rangeLength=rangeLength;
	}
	
	public void Create() throws Exception {
	    atts = new FastVector();
	    attValsNumeric = new FastVector();
	    attValsBool = new FastVector();
	    attValsPercent = new FastVector();
	    
	    attValsBool.addElement("0.0");
	    attValsBool.addElement("1.0");
	    for(Integer i = 0; i < range; i+=rangeLength)
	    {
	    	String val = i.toString() + "-" + ((Integer)(i+rangeLength)).toString();
	    	attValsNumeric.addElement(val);
	    }
	    
	    for(Integer i = 0; i < 100; i++)
	    {
	    	String val = i.toString() + "-" + ((Integer)(i+1)).toString();
	    	attValsPercent.addElement(val);
	    }
	    //1
	    atts.addElement(new Attribute("numChar",attValsNumeric));
	    atts.addElement(new Attribute("numSpeakingChar",attValsNumeric));
	    
	    //2
	    atts.addElement(new Attribute("mostFqtChar",attValsPercent));
	    
	    //3
	    atts.addElement(new Attribute("numQuote",attValsNumeric));	    
	    atts.addElement(new Attribute("quoteProportion",attValsPercent));
	    
	    //4
	    atts.addElement(new Attribute("num3Clique",attValsNumeric));
	    //atts.addElement(new Attribute("num4Clique",attValsNumeric));
	    
	    //5
	    atts.addElement(new Attribute("avgDegree",attValsPercent));
	    
	    //6
	    atts.addElement(new Attribute("graphDensity",attValsPercent));
	    
	    atts.addElement(new Attribute("success",attValsBool));
	    
	    dataTrain = new Instances("Conversational Network Feature", atts, 0);
	    dataTest = new Instances("Conversational Network Feature", atts, 0);
	    
	    AddData(train,true, dataTrain.numAttributes());
	    AddData(test,false, dataTest.numAttributes());
	    
	    Classifier(dataTrain,dataTest);
	    
	}
  
	  private void AddData(List<ConversationalNetworkFeature> cnf,boolean train, int length) throws Exception
	  {		
		  for(ConversationalNetworkFeature item:cnf)
		  {
			  vals = new double[length];
			  
			  AddValue(0, item.numChar);
			  
			  AddValue(1, item.numSpeakingChar);
			  AddValue(2, item.mostFqtChar);
			  AddValue(3, item.numQuote);
			  
			  AddValue(4, item.quoteProportion);
			  
			  AddValue(5, item.num3Clique);
			  
			  AddValue(6, item.avgDegree);
//			  System.out.println(item.avgDegree);
			  AddValue(7, item.graphDensity);
			  AddValue(8, item.success);
			  
			  if(train){
				  dataTrain.add(new Instance(1.0,vals));
			  }
			  else{
				  dataTest.add(new Instance(1.0,vals));
			  }
		  }
	  }
	  
	  private void Classifier(Instances trainString, Instances testString) throws Exception
	  {
//		  cqr = new ArrayList<CandidateQuoteResult>();
		  Instances train = trainString;
			 int cIdx=train.numAttributes()-1; 
			 train.setClassIndex(cIdx);
			 
			 Instances test = testString;
			 cIdx=test.numAttributes()-1; 
			 test.setClassIndex(cIdx);
			 
			// filter
			Remove rm = new Remove();
			rm.setAttributeIndices("1");  // remove 1st attribute
			
			// classifier
			J48 j48 = new J48();
			j48.setUnpruned(true);        // using an unpruned J48
			// meta-classifier
			FilteredClassifier fc = new FilteredClassifier();
			fc.setFilter(rm);
			fc.setClassifier(j48);
			// train and make predictions
			fc.buildClassifier(train);
			int count=0;
			String actual;
			String predicted;
			for (int i = 0; i < test.numInstances(); i++) {
				double pred = fc.classifyInstance(test.instance(i));
				actual = test.classAttribute().value((int) test.instance(i).classValue());
				predicted = test.classAttribute().value((int) pred);
				System.out.print("ID: " + test.instance(i).value(0));
				System.out.print(", actual: " + actual);
				System.out.println(", predicted: " + predicted);
				if(actual.equals(predicted))
				{	
					count++;
				}
			}
			System.out.println("Accuracy: " + String.format("%.2f",1.0*count/test.numInstances()*100) + "%");
	  }
	  
	  private void AddValue(Integer i, Object value)
	  {
		  if(value instanceof Integer){
			  Integer t = 0;
			  Integer va = (Integer)value;
			  while(va >=t)
				  t+=rangeLength;
			  String val = ((Integer)(t-rangeLength)).toString() + "-" + t.toString();
			  vals[i]=attValsNumeric.indexOf(val);
		  } else if(value instanceof Boolean){
			  Boolean va = (Boolean)value;
			  if(va.equals(false))
				  vals[i] = attValsBool.indexOf("0.0");
			  else
				  vals[i] = attValsBool.indexOf("1.0");
		  } else{
			  Integer t = 0;
			  Double va = (Double)value;
			  while(va >=t)
				  t+=1;
			  String val = ((Integer)(t-1)).toString() + "-" + t.toString();
			  vals[i]=attValsPercent.indexOf(val);
		  }
	  }
	  
	  public void ShowData()
	  {
		  System.out.println("Training:");
		  System.out.println(dataTrain);
		  System.out.println("Testing:");
		  System.out.println(dataTest);
	  }
}