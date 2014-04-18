import java.util.List;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class AttributeJ48 {
	private FastVector atts;
	private FastVector attValsNumeric;
	private FastVector attValsBool;
	private FastVector attValsPercent;
	private Instances data;
	//private Integer num;
	private List<CandidateQuote> cq;
	private int range;
	private int rangeLength;
	private double[] vals;
	
	
	public AttributeJ48(List<CandidateQuote> cq, int range, int rangeLength)
	{
		this.cq=cq;
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
	    
	    
		/*
	    
	    for(Integer i = 0; i < range; i+=rangeLength)
	    {
	    	String val = ((Integer)(2*i)).toString() + "-" + ((Integer)(2*i+2)).toString();
	    	attVals.addElement(val);
	    	
	    	String val2 = i.toString() + "-" + ((Integer)(i+1)).toString();
	    	attVals2.addElement(val2);
	    }
	    
	    
	    attVals3.addElement("0.0");
	    attVals3.addElement("1.0");
	    
	    for(int i = 0; i < num; i++){
	    	switch(i){
	    	case 4:
	    		atts.addElement(new Attribute("att" + (i+1),attVals2));
	    		break;
	    	case 8:
	    		atts.addElement(new Attribute("att" + (i+1),attVals3));
	    		break;
	    	default:
	    		atts.addElement(new Attribute("att" + (i+1),attVals));
	    		break;
	    	}
	    }
	    
	    	
	    data = new Instances("Attribution Quoted Speech", atts, 0);*/
	}
  
	  public void AddData() throws Exception
	  {		
		  for(CandidateQuote item:cq)
		  {
			  vals = new double[data.numAttributes()];
			  
			  AddNumeric(0, item.distance);
			  
			  AddNumeric(1, item.commaPresence);
			  AddNumeric(2, item.periodPresence);
			  AddNumeric(3, item.punctuationPresence);
			  
			  AddNumeric(4, item.ordinal);
			  
			  AddNumeric(5, item.proportion);
			  
			  AddNumeric(6, item.numNameCd);
			  AddNumeric(7, item.numQuoteCd);
			  AddNumeric(8, item.numWordCd);
			  
			  AddNumeric(9, item.numNameQt);
			  AddNumeric(10, item.numQuoteQt);
			  AddNumeric(11, item.numWordCd);
			  
			  AddNumeric(12, item.numNameOtr);
			  AddNumeric(13, item.numQuoteOtr);
			  AddNumeric(14, item.numWordOtr);
			  
			  AddNumeric(15, item.numAppearance);
			  
			  AddNumeric(16, item.expVerbPresence);
			  AddNumeric(17, item.personPresence);
			 
			  AddNumeric(18, item.quoteLength);
			  AddNumeric(19, item.quotePosition);
			  AddNumeric(20, item.otherCharPresence);
			  AddNumeric(21, item.candidatePresence);
			  
			  data.add(new Instance(1.0,vals));
		  }
		  
		  /*
		    double[] vals;
		    int i;
		    
		    vals = new double[data.numAttributes()];
		    // - numeric
		    for(i = 0;i < data2.length;i++){
		    	if(i == 4){
		    		Integer t = 0;
			    	while(data2[i] >= t)
			    		t+=1;
			    	String val = ((Integer)(t-1)).toString() + "-" + t.toString();
		    		vals[i] = attVals2.indexOf(val);

		    	} else if (i == 8){
		    		String val = ((Double)data2[i]).toString();
		    		vals[i] = attVals3.indexOf(val);
		    	} else{
		    	Integer t = 0;
		    	while(data2[i] >= t)
		    		t+=2;
		    	String val = ((Integer)(t-2)).toString() + "-" + t.toString();
		    	vals[i] = attVals.indexOf(val);
		    }
		    }
		    data.add(new Instance(1.0,vals));*/
	  }
	  
	  public Instances ShowData()
	  {
		  return data;
	  }
	  
	  private void AddNumeric(Integer i, Object value)
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