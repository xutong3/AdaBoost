package com.sonly.adaboost;

import java.util.ArrayList;

/**
 * Class: SampleNode
 * Description: sample class 
 * Company: HUST
 * @author Sonly
 * Date: 2017��5��24��
 */
public class Stump {
	public int dim;
    public double thresh;
    public String condition;
    public double error;
    public ArrayList<Integer> labelList;
    double factor;
    
    public String toString(){
        return "dim is "+dim+"\nthresh is "+thresh+"\ncondition is "+condition+"\nerror is "+error+"\nfactor is "+factor+"\nlabel is "+labelList;
    }
}
