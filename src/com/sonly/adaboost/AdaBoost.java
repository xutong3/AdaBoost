package com.sonly.adaboost;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class: AdaBoost
 * Description: to execute AdaBoost algorithm
 * Company: HUST
 * @author Sonly
 * Date: 2017Äê5ÔÂ23ÈÕ
 */
public class AdaBoost {
	
	/**
	 * Method: AdaBoostTrain
	 * Description: train AdaBoost
	 * @param dataSet
	 * @param labelList
	 * @return
	 */
    public static ArrayList<Stump> AdaBoostTrain(ArrayList<ArrayList<Double>> dataSet,ArrayList<Integer> labelList){
        int row=labelList.size();
        ArrayList<Double> weights=Utils.getInitWeights(row);
        ArrayList<Stump> G=new ArrayList<Stump>();
        ArrayList<Double> accError=Utils.InitAccWeightError(row);
        int n=1;
        while(true){
            Stump stump=Utils.buildStump(dataSet,labelList,weights,n);
            G.add(stump);
            weights=Utils.updateWeights(stump,labelList,weights);
            accError=Utils.accWeightError(accError,stump);//
            double error=Utils.calErrorRate(accError,labelList);
            if(error<0.001){
                break;
            }
            n++;
        }
        return G;
    }
    
    /**
     * Method: main
     * Description: the entrance of AdaBoost
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String file="data.txt";
        ArrayList<ArrayList<Double>> dataSet=Utils.loadDataSet(file);
        ArrayList<Integer> labelSet=Utils.loadLabelSet(file);
        ArrayList<Stump> G=AdaBoostTrain(dataSet,labelSet);
        Utils.showStumpList(G);
        System.out.println("finished");
    }
}
