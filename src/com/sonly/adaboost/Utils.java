package com.sonly.adaboost;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class: Utils
 * Description:  provide necessary calculate method
 * Company: HUST
 * @author Sonly
 * Date: 2017年5月24日
 */
public class Utils {
	/**
	 * Method: loadDataSet
	 * Description: load data set
	 * @param filename
	 * @return
	 * @throws IOException
	 */
    public static ArrayList<ArrayList<Double>> loadDataSet(String filename) throws IOException{
        ArrayList<ArrayList<Double>> dataSet=new ArrayList<ArrayList<Double>>();
        FileInputStream fis=new FileInputStream(filename);
        InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
        BufferedReader br=new BufferedReader(isr);
        String line="";
        
        while((line=br.readLine())!=null){
            ArrayList<Double> data=new ArrayList<Double>();
            String[] s=line.split(" ");
            
            for(int i=0;i<s.length-1;i++){
                data.add(Double.parseDouble(s[i]));
            }
            dataSet.add(data);
        }
        br.close();
        
        return  dataSet;
    }
    
    /**
     * Method: loadLabelSet
     * Description: obtain label from data set
     * @param filename
     * @return
     * @throws NumberFormatException
     * @throws IOException
     */
    public static ArrayList<Integer> loadLabelSet(String filename) throws NumberFormatException, IOException{
        ArrayList<Integer> labelSet=new ArrayList<Integer>();
        
        FileInputStream fis=new FileInputStream(filename);
        InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
        BufferedReader br=new BufferedReader(isr);
        String line="";
        
        while((line=br.readLine())!=null){
            String[] s=line.split(" ");
            labelSet.add(Integer.parseInt(s[s.length-1]));
        }
        br.close();
        
        return labelSet;
    }
    
    /**
     * Method: showDataSet
     * Description: print data set
     * @param dataSet
     */
    public static void showDataSet(ArrayList<ArrayList<Double>> dataSet){
        for(ArrayList<Double> data:dataSet){
            System.out.println(data);
        }
    }
    
    /**
     * Method: getMax
     * Description: get the max value to calculate step_length
     * @param dataSet
     * @param index
     * @return
     */
    public static double getMax(ArrayList<ArrayList<Double>> dataSet,int index){
        double max=-9999.0;
        for(ArrayList<Double> data:dataSet){
            if(data.get(index)>max){
                max=data.get(index);
            }
        }
        return max;
    }
    
    /**
     * Method: getMin
     * Description: get the min value to calculate step_length
     * @param dataSet
     * @param index
     * @return
     */
    public static double getMin(ArrayList<ArrayList<Double>> dataSet,int index){
        double min=9999.0;
        for(ArrayList<Double> data:dataSet){
            if(data.get(index)<min){
                min=data.get(index);
            }
        }
        return min;
    }
    
    //获取数据集中以该feature为特征，以thresh和conditions为value的叶子节点的决策树进行划分后得到的预测类别
    /**
     * Method: getClassify
     * Description: get the predict category
     * @param dataSet
     * @param feature
     * @param thresh
     * @param condition
     * @return
     */
    public static ArrayList<Integer> getClassify(ArrayList<ArrayList<Double>> dataSet,int feature,double thresh,String condition){
        ArrayList<Integer> labelList=new ArrayList<Integer>();
        if(condition.compareTo("lt")==0){
            for(ArrayList<Double> data:dataSet){
                if(data.get(feature)<=thresh){
                    labelList.add(1);
                }else{
                    labelList.add(-1);
                }
            }
        }else{
            for(ArrayList<Double> data:dataSet){
                if(data.get(feature)>=thresh){
                    labelList.add(1);
                }else{
                    labelList.add(-1);
                }
            }
        }
        return labelList;
    }
    
    /**
     * Method: getError
     * Description: get the weighted error between real category and predict category
     * @param fake
     * @param real
     * @param weights
     * @return
     */
    public static double getError(ArrayList<Integer> fake,ArrayList<Integer> real,ArrayList<Double> weights){
        double error=0;

        for(int i = 0; i < fake.size(); i++)
            if(fake.get(i)!=real.get(i))
                error+=weights.get(i);
        
        return error;
    }
    
    /**
     * Method: buildStump
     * Description: build a single node decision tree with minimum error rate, whose information stored by Stump;
     * @param dataSet
     * @param labelSet
     * @param weights
     * @param n
     * @return
     */
    public static Stump buildStump(ArrayList<ArrayList<Double>> dataSet,ArrayList<Integer> labelSet,ArrayList<Double> weights,int n){
        int featureNum=dataSet.get(0).size();
        
        int rowNum=dataSet.size();
        Stump stump=new Stump();
        double minError=999.0;
        System.out.println("iterate:"+ n);
        for(int i=0;i<featureNum;i++){
            double min=getMin(dataSet,i);
            double max=getMax(dataSet,i);    
            double step=(max-min)/(rowNum);
            for(double j=min-step;j<=max+step;j=j+step){
                String[] conditions={"lt","gt"};//如果是lt，表示如果小于阀值则为正类，如果是gt，表示如果大于阀值则为正类
                for(String condition:conditions){
                    ArrayList<Integer> labelList=getClassify(dataSet,i,j,condition);
                    
                    double error=Utils.getError(labelList,labelSet,weights);
                    if(error<minError){
                        minError=error;
                        stump.dim=i;
                        stump.thresh=j;
                        stump.condition=condition;
                        stump.error=minError;
                        stump.labelList=labelList;
                        stump.factor=0.5*(Math.log((1-error)/error));
                    }
                    
                }
            }
            
        }
        
        return stump;
    }
    
    /**
     * Method: getInitWeights
     * Description: initialize weight by average value
     * @param n
     * @return
     */
    public static ArrayList<Double> getInitWeights(int n){
        double weight=1.0/n;
        ArrayList<Double> weights=new ArrayList<Double>();
        for(int i=0;i<n;i++){
            weights.add(weight);
        }
        return weights;
    }
    
    /**
     * Method: updateWeights
     * Description: update the weights of sample
     * @param stump
     * @param labelList
     * @param weights
     * @return
     */
    public static ArrayList<Double> updateWeights(Stump stump,ArrayList<Integer> labelList,ArrayList<Double> weights){
        double Z=0;
        ArrayList<Double> newWeights=new ArrayList<Double>();
        int row=labelList.size();
        double e=Math.E;
        double factor=stump.factor;
        for(int i=0;i<row;i++){
            Z+=weights.get(i)*Math.pow(e,-factor*labelList.get(i)*stump.labelList.get(i));
        }
        
        
        for(int i=0;i<row;i++){
            double weight=weights.get(i)*Math.pow(e,-factor*labelList.get(i)*stump.labelList.get(i))/Z;
            newWeights.add(weight);
        }
        return newWeights;
    }
 
    /**
     * Method: InitAccWeightError
     * Description: accumulate weight error
     * @param n
     * @return
     */
    public static ArrayList<Double> InitAccWeightError(int n){
        ArrayList<Double> accError=new ArrayList<Double>();
        for(int i=0;i<n;i++){
            accError.add(0.0);
        }
        return accError;
    }
    
    /**
     * Method: accWeightError
     * Description: calculate weight error
     * @param accerror
     * @param stump
     * @return
     */
    public static ArrayList<Double> accWeightError(ArrayList<Double> accerror,Stump stump){
        ArrayList<Integer> t=stump.labelList;
        double factor=stump.factor;
        ArrayList<Double> newAccError=new ArrayList<Double>();
        for(int i=0;i<t.size();i++){
            double a=accerror.get(i)+factor*t.get(i);
            newAccError.add(a);
        }
        return newAccError;
    }
    
    /**
     * Method: calErrorRate
     * Description: calculate error rate
     * @param accError
     * @param labelList
     * @return
     */
    public static double calErrorRate(ArrayList<Double> accError,ArrayList<Integer> labelList){
        int wrong=0;
        for(int i=0;i<accError.size();i++){
            if(accError.get(i)>0){
                if(labelList.get(i)==-1){
                    wrong++;
                }
            }else if(labelList.get(i)==1){
                wrong++;
            }
        }
        double error=wrong*1.0/accError.size();
        return error;
    }
    
    /**
     * Method: showStumpList
     * Description: print stump list 
     * @param G
     */
    public static void showStumpList(ArrayList<Stump> G){
        for(Stump s:G){
            System.out.println(s);
            System.out.println("----------------------------------------------------");
        }
    }
}
