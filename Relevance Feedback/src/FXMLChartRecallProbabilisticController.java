/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import TestingPerformanceProbabilistic.ProbabilisticValueTester;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author richa
 */
public class FXMLChartRecallProbabilisticController implements Initializable {
    
    @FXML
    private LineChart chart;
    
    private ObservableList<ProbabilisticValueTester> dataValueListBefore;
    private ObservableList<ProbabilisticValueTester> dataValueListAfter;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            chart.getXAxis().setLabel("IdQuery");
            chart.getYAxis().setLabel("Recall");
            XYChart.Series beforeRecall = new XYChart.Series();
            XYChart.Series afterRecall = new XYChart.Series();
            

            beforeRecall.setName("Recall Before Feedback");
            afterRecall.setName("Recall after Feedback");

            for (int i = 0; i < dataValueListBefore.size(); i++) {
                beforeRecall.getData().add(new XYChart.Data(String.valueOf(dataValueListBefore.get(i).getId()), Double.parseDouble(dataValueListBefore.get(i).getRecall())));
            }
           
            for (int i = 0; i < dataValueListBefore.size(); i++) {
                afterRecall.getData().add(new XYChart.Data(String.valueOf(dataValueListBefore.get(i).getId()), Double.parseDouble(dataValueListAfter.get(i).getRecall())));
            }
            
            chart.getData().addAll(beforeRecall, afterRecall);
        });
    }

    public void setObservableList(ObservableList<ProbabilisticValueTester> dataValueListBefore, ObservableList<ProbabilisticValueTester> dataValueListAfter) {
        this.dataValueListBefore = dataValueListBefore;
        this.dataValueListAfter = dataValueListAfter;
    }

}
