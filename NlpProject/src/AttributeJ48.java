import java.util.ArrayList;
import java.util.List;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;

public class AttributeJ48 {
	private FastVector atts;
	private FastVector attValsNumeric;
	private FastVector attValsBool;
	private FastVector attValsPercent;
	private Instances data;
	private List<CandidateQuote> cq;
	private List<CandidateQuoteResult> cqr;
	private int range;
	private int rangeLength;
	private double[] vals;
	
	
	public AttributeJ48(List<CandidateQuote> cq, int range, int rangeLength)
	{
		this.cq=cq;
		this.range=range;
		this.rangeLength=rangeLength;
	}
	
	public List<CandidateQuoteResult> Create() throws Exception {
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
	    
	    atts.addElement(new Attribute("distance",attValsNumeric));
	    
	    atts.addElement(new Attribute("commaPresence",attValsBool));
	    atts.addElement(new Attribute("periodPresence",attValsBool));
	    atts.addElement(new Attribute("punctuationPresence",attValsBool));
	    
	    atts.addElement(new Attribute("ordinal",attValsNumeric));
	    
	    atts.addElement(new Attribute("proportion",attValsPercent));
	    
	    atts.addElement(new Attribute("numNameCd",attValsNumeric));
	    atts.addElement(new Attribute("numQuoteCd",attValsNumeric));
	    atts.addElement(new Attribute("numWordCd",attValsNumeric));
	    
	    atts.addElement(new Attribute("numNameQt",attValsNumeric));
	    atts.addElement(new Attribute("numQuoteQt",attValsNumeric));
	    atts.addElement(new Attribute("numWordQt",attValsNumeric));
	    
	    atts.addElement(new Attribute("numNameOtr",attValsNumeric));
	    atts.addElement(new Attribute("numQuoteOtr",attValsNumeric));
	    atts.addElement(new Attribute("numWordOtr",attValsNumeric));
	    
	    atts.addElement(new Attribute("numAppreance",attValsNumeric));

	    atts.addElement(new Attribute("exVerbPresence",attValsBool));
	    atts.addElement(new Attribute("personPresence",attValsBool));
	    
	    atts.addElement(new Attribute("quoteLength",attValsNumeric));
	    atts.addElement(new Attribute("quotePosition",attValsNumeric));
	    atts.addElement(new Attribute("otherCharPresence",attValsBool));
	    atts.addElement(new Attribute("candidatePresence",attValsBool));
	    
	    data = new Instances("Attribution Quoted Speech", atts, 0);
	    
	    return AddData();
	}
  
	  private List<CandidateQuoteResult> AddData() throws Exception
	  {		
		  for(CandidateQuote item:cq)
		  {
			  vals = new double[data.numAttributes()];
			  
			  AddValue(0, item.distance);
			  
			  AddValue(1, item.commaPresence);
			  AddValue(2, item.periodPresence);
			  AddValue(3, item.punctuationPresence);
			  
			  AddValue(4, item.ordinal);
			  
			  AddValue(5, item.proportion);
			  
			  AddValue(6, item.numNameCd);
			  AddValue(7, item.numQuoteCd);
			  AddValue(8, item.numWordCd);
			  
			  AddValue(9, item.numNameQt);
			  AddValue(10, item.numQuoteQt);
			  AddValue(11, item.numWordCd);
			  
			  AddValue(12, item.numNameOtr);
			  AddValue(13, item.numQuoteOtr);
			  AddValue(14, item.numWordOtr);
			  
			  AddValue(15, item.numAppearance);
			  
			  AddValue(16, item.expVerbPresence);
			  AddValue(17, item.personPresence);
			 
			  AddValue(18, item.quoteLength);
			  AddValue(19, item.quotePosition);
			  AddValue(20, item.otherCharPresence);
			  AddValue(21, item.candidatePresence);
			  
			  data.add(new Instance(1.0,vals));
		  }
		  
		  return Classifier(data,data);
	  }
	  
	  private List<CandidateQuoteResult> Classifier(Instances trainString, Instances testString) throws Exception
	  {
		  cqr = new ArrayList<CandidateQuoteResult>();
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
				CandidateQuote cqResult = cq.get(i);
				CandidateQuoteResult cqrResult;
				if(actual.equals(predicted))
				{	
					cqrResult = new CandidateQuoteResult(cqResult,true);
					count++;
				}
				else
				{
					cqrResult = new CandidateQuoteResult(cqResult,false);
				}
				cqr.add(cqrResult);
			}
			System.out.println("Accuracy: " + String.format("%.2f",1.0*count/test.numInstances()*100) + "%");
			return cqr;
	  }
	  
	  public Instances ShowData()
	  {
		  return data;
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
}