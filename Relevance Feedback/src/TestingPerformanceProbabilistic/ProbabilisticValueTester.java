/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingPerformanceProbabilistic;

/**
 *
 * @author richa
 */
public class ProbabilisticValueTester {
    private int id;
    private String precision;
    private String recall;
    
    public ProbabilisticValueTester(int id, String precision, String recall){
        this.id = id;
        this.precision = precision;
        this.recall = recall;
    }

    public int getId() {
        return id;
    }

    public String getPrecision() {
        return precision;
    }

    public String getRecall() {
        return recall;
    }
    
    
}
