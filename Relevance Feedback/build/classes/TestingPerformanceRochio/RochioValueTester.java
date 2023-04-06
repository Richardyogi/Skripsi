/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingPerformanceRochio;

import java.util.ArrayList;

/**
 *
 * @author richa
 */
public class RochioValueTester {

    private int id;
    private String percentagePrecision;
    private String percentageRecall;

    public RochioValueTester(int id, String percentagePrecision, String percentageRecall) {
        this.id = id;
        this.percentagePrecision = percentagePrecision;
        this.percentageRecall = percentageRecall;
    }

    public int getId() {
        return id;
    }
    
    public String getPercentagePrecision() {
        return percentagePrecision;
    }

    public String getPercentageRecall() {
        return percentageRecall;
    }
     
}
