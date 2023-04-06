

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import TestingPerformanceRochio.RochioValueTester;
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
public class FXMLChartPrecisionRochioController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private LineChart chart;

    private ObservableList<RochioValueTester> dataValueListBefore;
    private ObservableList<RochioValueTester> dataValueListAfter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            chart.getXAxis().setLabel("IdQuery");
            chart.getYAxis().setLabel("Precision");
            XYChart.Series beforePrecision = new XYChart.Series();
            XYChart.Series afterPrecision = new XYChart.Series();
            

            beforePrecision.setName("Precision Before Feedback");
            afterPrecision.setName("Precision after Feedback");

            for (int i = 0; i < dataValueListBefore.size(); i++) {
                beforePrecision.getData().add(new XYChart.Data(String.valueOf(dataValueListBefore.get(i).getId()), Double.parseDouble(dataValueListBefore.get(i).getPercentagePrecision())));
            }
           
            for (int i = 0; i < dataValueListAfter.size(); i++) {
                afterPrecision.getData().add(new XYChart.Data(String.valueOf(dataValueListBefore.get(i).getId()), Double.parseDouble(dataValueListAfter.get(i).getPercentagePrecision())));
            }
            
            chart.getData().addAll(beforePrecision, afterPrecision);
        });
    }

    public void setObservableList(ObservableList<RochioValueTester> dataValueListBefore, ObservableList<RochioValueTester> dataValueListAfter) {
        this.dataValueListBefore = dataValueListBefore;
        this.dataValueListAfter = dataValueListAfter;
    }
}
