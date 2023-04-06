

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Path.PathDocument;
import TestingPerformanceRochio.RochioValueTester;
import TestingPerformanceRochio.SearchRochioTester;
import cranfield_collection.CranfieldDocument;
import indexer.CranfieldIndexer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.lucene.document.TextField;
import DocumentResult.DocumentResultProbabilistic;
import DocumentResult.DocumentResultRochio;
import TestingPerformanceProbabilistic.ProbabilisticValueTester;
import TestingPerformanceProbabilistic.SearchProbabilisticTest;
import indexer.ReadDocumentIndex;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import searcher.SearchCranfieldProbabilisticController;
import searcher.SearchCranfieldRochioController;

import searcher.SearcherRochio;
import searcher.SearcherProbabilistic;

/**
 *
 * @author richa
 */
public class FXMLDocumentController implements Initializable {

//    @FXML
//    private TableView<String> tableResultIndex;
//    @FXML
//    private TableColumn<String> resultColumn;
    @FXML
    private Button indexButton;

    @FXML
    private Label labelProcess;

    @FXML
    private Label waktuIndex;

    @FXML
    private Label markedDocument;

    @FXML
    private Label jumlahDokumenTerindeks;

    @FXML
    private javafx.scene.control.TextField queryBar;

    @FXML
    private javafx.scene.control.TextField queryBarProbabilistic;

    @FXML
    private javafx.scene.control.TextField termLimit;

    @FXML
    private javafx.scene.control.TextField factorTfIdf;

    @FXML
    private javafx.scene.control.TextField muField;

    @FXML
    private javafx.scene.control.TextField alphaField;

    @FXML
    private Button searchButton;

    @FXML
    private Button hasilIndexButton;

    @FXML
    private Button improveButton;

    @FXML
    private Button markAsRelevantButton;

    @FXML
    private Button searchProbabilistic;

    @FXML
    private Button probabilisticFeedback;

    @FXML
    private Button testingButtonRochio;

    @FXML
    private Button graphicPrecisionRochioButton;

    @FXML
    private Button graphicRecallRochioButton;

    @FXML
    private Button testingButtonProbabilistic;

    @FXML
    private Button graphicPrecisionProbabilistic;

    @FXML
    private Button graphicRecallProbabilistic;

    @FXML
    private RadioButton radio1;

    @FXML
    private RadioButton radio2;

    @FXML
    private RadioButton radio3;

    @FXML
    private TableView<CranfieldDocument> tabel_index;

    @FXML
    private TableView<DocumentResultRochio> tableDokumen;

    @FXML
    private TableView<DocumentResultProbabilistic> tableProbabilistic;

    @FXML
    private TableView<RochioValueTester> tableTestingRochioBefore;

    @FXML
    private TableView<RochioValueTester> tableTestingRochioAfter;

    @FXML
    private TableView<ProbabilisticValueTester> tableProbabilisticBefore;

    @FXML
    private TableView<ProbabilisticValueTester> tableProbabilisticAfter;

    @FXML
    private TableColumn<CranfieldDocument, Integer> kolomIdDokumenIndex;

    @FXML
    private TableColumn<CranfieldDocument, String> kolomJudulDokumenIndex;

    @FXML
    private TableColumn<CranfieldDocument, String> kolomPengarang;

    @FXML
    private TableColumn<CranfieldDocument, String> kolomBibliography;

    @FXML
    private TableColumn<CranfieldDocument, String> kolomIsiDokumen;

    @FXML
    private TableColumn<DocumentResultRochio, Integer> kolomIdDokumen;

    @FXML
    private TableColumn<DocumentResultRochio, String> kolomJudulDokumen;

    @FXML
    private TableColumn<DocumentResultRochio, Float> kolomDocScore;

    @FXML
    private TableColumn<DocumentResultRochio, String> kolomCheckBox;

    @FXML
    private TableColumn<DocumentResultProbabilistic, String> idDokumenProbabilistic;

    @FXML
    private TableColumn<DocumentResultProbabilistic, Integer> judulDokumenProbabilistic;

    @FXML
    private TableColumn<DocumentResultProbabilistic, Double> docScoreProbabilistic;

    @FXML
    private TableColumn<RochioValueTester, Integer> idQueryRochioColumnBefore;

    @FXML
    private TableColumn<RochioValueTester, Integer> idQueryRochioColumnAfter;

    @FXML
    private TableColumn<RochioValueTester, String> precisionBeforeRochioColumn;

    @FXML
    private TableColumn<RochioValueTester, String> recallBeforeRochioColumn;

    @FXML
    private TableColumn<RochioValueTester, String> precisionAfterRochioColumn;

    @FXML
    private TableColumn<RochioValueTester, String> recallAfterRochioColumn;

    @FXML
    private TableColumn<ProbabilisticValueTester, Integer> idQueryBeforeProbabilistic;

    @FXML
    private TableColumn<ProbabilisticValueTester, String> precisionBeforeProbabilistic;

    @FXML
    private TableColumn<ProbabilisticValueTester, String> recallBeforeProbabilistic;

    @FXML
    private TableColumn<ProbabilisticValueTester, Integer> idQueryAfterProbabilistic;

    @FXML
    private TableColumn<ProbabilisticValueTester, String> precisionAfterProbabilistic;

    @FXML
    private TableColumn<ProbabilisticValueTester, String> recallAfterProbabilistic;

    @FXML
    private ChoiceBox document_selector;

    ObservableList<CranfieldDocument> dataIndex;
    ObservableList<DocumentResultRochio> dataRochio;
    ObservableList<DocumentResultRochio> docRelevant;
    ObservableList<DocumentResultProbabilistic> dataProbabilistic;

    ObservableList<RochioValueTester> dataValueListBeforeRochio;
    ObservableList<RochioValueTester> dataValueListAfterRochio;
    ObservableList<ProbabilisticValueTester> dataValueListBeforeProbabilistic;
    ObservableList<ProbabilisticValueTester> dataValueListAfterProbabilistic;

    private ToggleGroup tg;

    private SearchCranfieldRochioController sc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tg = new ToggleGroup();
        radio1.setToggleGroup(tg);
        radio1.setSelected(true);
        radio2.setToggleGroup(tg);
        radio3.setToggleGroup(tg);

        document_selector.getItems().add("D:/Skripsi/Indexing/Indexing Document/cranfield/cran.all.1400");
    }

    @FXML
    public void processIndexing() {
        labelProcess.setText("Indexing Document");
        indexButton.setDisable(true);
        hasilIndexButton.setDisable(true);
        String documentPath = (String) document_selector.getValue();
        if (documentPath == null) {
            labelProcess.setText("Corpus Doesn't Exist");
            indexButton.setDisable(false);
            hasilIndexButton.setDisable(false);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CranfieldIndexer cranIndexing = new CranfieldIndexer(documentPath);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                cranIndexing.indexingDocument();
                                String avgTime = String.format("%.5f", cranIndexing.getSpeed());
                                String jumlahDokumen = String.valueOf(cranIndexing.getCountDocument());
                                labelProcess.setText("Document Telah Terindeks");
                                waktuIndex.setText(avgTime + " doc/ms");
                                jumlahDokumenTerindeks.setText(jumlahDokumen + " dokumen");
                            } catch (IOException ex) {
                                labelProcess.setText("Dokumen tidak ada");
                            }
                            indexButton.setDisable(false);
                            hasilIndexButton.setDisable(false);
                        }

                    });
                } catch (IOException e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            labelProcess.setText("Dokumen tidak ada");
                            indexButton.setDisable(false);
                            hasilIndexButton.setDisable(false);
                        }
                    });
                }
            }

        });
        thread.start();
    }

    @FXML
    public void getDocumentIndex() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    indexButton.setDisable(true);
                    hasilIndexButton.setDisable(true);

                    ReadDocumentIndex rd = new ReadDocumentIndex();
                    ArrayList<CranfieldDocument> res = new ArrayList<>();
                    res = rd.getAllDocument();
                    dataIndex = FXCollections.observableArrayList();
                    for (int i = 0; i < res.size(); i++) {
                        int id = res.get(i).getId();
                        String judul = res.get(i).getJudul();
                        String pengarang = res.get(i).getPengarang();
                        String bibliography = res.get(i).getBibliography();
                        String isi = res.get(i).getIsi();
                        dataIndex.add(new CranfieldDocument(id, judul, pengarang, bibliography, isi));
                    }
                    tabel_index.getItems().clear();
                    kolomIdDokumenIndex.setCellValueFactory(new PropertyValueFactory<>("id"));
                    kolomJudulDokumenIndex.setCellValueFactory(new PropertyValueFactory<>("judul"));
                    kolomPengarang.setCellValueFactory(new PropertyValueFactory<>("pengarang"));
                    kolomBibliography.setCellValueFactory(new PropertyValueFactory<>("bibliography"));
                    kolomIsiDokumen.setCellValueFactory(new PropertyValueFactory<>("isi"));

                    tabel_index.setItems(dataIndex);
                } catch (Exception e) {

                }
                return null;
            }

            @Override
            protected void succeeded() {
                indexButton.setDisable(false);
                hasilIndexButton.setDisable(false);
            }

            @Override
            protected void failed() {
                labelProcess.setText("Document belum terindeks");
                indexButton.setDisable(false);
                hasilIndexButton.setDisable(false);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    public void getDocumentBySearch() {

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    searchButton.setDisable(true);
                    improveButton.setDisable(true);
                    markAsRelevantButton.setDisable(true);

                    String Query = queryBar.getText().toString();
                    SearcherRochio searcher = new SearcherRochio(PathDocument.CranIndexDir);
                    ArrayList<DocumentResultRochio> res = new ArrayList<>();
                    res = searcher.search(Query);

                    dataRochio = FXCollections.observableArrayList();
                    for (int i = 0; i < res.size(); i++) {
                        int docId = res.get(i).getDocId();
                        String judul = res.get(i).getJudul();
                        float score = res.get(i).getScore();
                        dataRochio.add(new DocumentResultRochio(docId, judul, score));
                    }
                    tableDokumen.getItems().clear();
                    kolomIdDokumen.setCellValueFactory(new PropertyValueFactory<>("docId"));
                    kolomJudulDokumen.setCellValueFactory(new PropertyValueFactory<>("judul"));
                    kolomDocScore.setCellValueFactory(new PropertyValueFactory<>("score"));
                    kolomCheckBox.setCellValueFactory(new PropertyValueFactory<DocumentResultRochio, String>("selectDocument"));

                    tableDokumen.setItems(dataRochio);

                } catch (Exception E) {

                }
                return null;

            }

            @Override
            protected void succeeded() {
                searchButton.setDisable(false);
                improveButton.setDisable(false);
                markAsRelevantButton.setDisable(false);
                markedDocument.setText(" ");
            }
        };

        Thread thread = new Thread(task);
        thread.start();

    }

    @FXML
    public void getRelevantDocument() throws IOException {
        ObservableList<DocumentResultRochio> dataRelevant = FXCollections.observableArrayList();
        
        if (!dataRochio.isEmpty()) {
            dataRelevant.clear();
            for (DocumentResultRochio documentResult : dataRochio) {
                if (documentResult.getSelectDocument().isSelected()) {
                    dataRelevant.add(documentResult);
                }
            }

            sc = new SearchCranfieldRochioController();
            sc.getDocRelevant(dataRelevant);

            markedDocument.setText("Dokumen telah tertanda!");

            if (dataRelevant.isEmpty()) {
                markedDocument.setText("Tidak ada dokumen yang tertanda");
            }
        }
        else if(!docRelevant.isEmpty()){
            dataRelevant.clear();
            for (DocumentResultRochio documentResult : docRelevant) {
                if (documentResult.getSelectDocument().isSelected()) {
                    dataRelevant.add(documentResult);
                }
            }

            sc = new SearchCranfieldRochioController();
            sc.getDocRelevant(dataRelevant);

            markedDocument.setText("Dokumen telah tertanda!");

            if (dataRelevant.isEmpty()) {
                markedDocument.setText("Tidak ada dokumen yang tertanda");
            }
        }

    }

    @FXML
    public void doRochioFeedback() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    searchButton.setDisable(true);
                    improveButton.setDisable(true);
                    markAsRelevantButton.setDisable(true);

                    ArrayList<DocumentResultRochio> res = new ArrayList<>();

                    String query = queryBar.getText().toString();
//                    SearchCranfieldRochioFeedback sc = new SearchCranfieldRochioFeedback();

                    sc.resetWeightTfIdf();

                    res = sc.doRochioFeedback(query);

                    docRelevant = FXCollections.observableArrayList();
                    for (int i = 0; i < res.size(); i++) {
                        int docId = res.get(i).getDocId();
                        String judul = res.get(i).getJudul();
                        float score = res.get(i).getScore();
                        docRelevant.add(new DocumentResultRochio(docId, judul, score));
                    }
                    tableDokumen.getItems().clear();
                    kolomIdDokumen.setCellValueFactory(new PropertyValueFactory<>("docId"));
                    kolomJudulDokumen.setCellValueFactory(new PropertyValueFactory<>("judul"));
                    kolomDocScore.setCellValueFactory(new PropertyValueFactory<>("score"));
                    kolomCheckBox.setCellValueFactory(new PropertyValueFactory<>("selectDocument"));

                    tableDokumen.setItems(docRelevant);

                } catch (Exception E) {
                    System.out.println(E);
                }
                return null;

            }

            @Override
            protected void succeeded() {
                searchButton.setDisable(false);
                improveButton.setDisable(false);
                markAsRelevantButton.setDisable(false);
                markedDocument.setText("");
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void searchProbabilistic() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    searchProbabilistic.setDisable(true);
                    probabilisticFeedback.setDisable(true);

                    String Query = queryBarProbabilistic.getText().toString();
                    SearcherProbabilistic searcher = new SearcherProbabilistic(PathDocument.CranIndexDir);
                    ArrayList<DocumentResultProbabilistic> res = new ArrayList<>();
                    res = searcher.search(Query);

                    dataProbabilistic = FXCollections.observableArrayList();
                    for (int i = 0; i < res.size(); i++) {
                        int docId = res.get(i).getDocId();
                        String judul = res.get(i).getJudul();
                        double score = res.get(i).getScore();
                        dataProbabilistic.add(new DocumentResultProbabilistic(docId, judul, score));
                    }
                    tableProbabilistic.getItems().clear();
                    idDokumenProbabilistic.setCellValueFactory(new PropertyValueFactory<>("docId"));
                    judulDokumenProbabilistic.setCellValueFactory(new PropertyValueFactory<>("judul"));
                    docScoreProbabilistic.setCellValueFactory(new PropertyValueFactory<>("score"));

                    tableProbabilistic.setItems(dataProbabilistic);

                } catch (Exception E) {

                }
                return null;

            }

            @Override
            protected void succeeded() {
                searchProbabilistic.setDisable(false);
                probabilisticFeedback.setDisable(false);
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    public void doProbabilisticFeedback() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    searchProbabilistic.setDisable(true);
                    probabilisticFeedback.setDisable(true);

                    ArrayList<DocumentResultProbabilistic> res = new ArrayList<>();

                    String query = queryBarProbabilistic.getText().toString();
//                    SearchCranfieldRochioFeedback sc = new SearchCranfieldRochioFeedback();

                    SearchCranfieldProbabilisticController sp = new SearchCranfieldProbabilisticController();
                    res = sp.doProbabilisticFeedback(query);

                    ObservableList<DocumentResultProbabilistic> dataRelevant = FXCollections.observableArrayList();
                    for (int i = 0; i < res.size(); i++) {
                        int docId = res.get(i).getDocId();
                        String judul = res.get(i).getJudul();
                        double score = res.get(i).getScore();

                        dataRelevant.add(new DocumentResultProbabilistic(docId, judul, score));
                    }
                    tableProbabilistic.getItems().clear();
                    idDokumenProbabilistic.setCellValueFactory(new PropertyValueFactory<>("docId"));
                    judulDokumenProbabilistic.setCellValueFactory(new PropertyValueFactory<>("judul"));
                    docScoreProbabilistic.setCellValueFactory(new PropertyValueFactory<>("score"));

                    tableProbabilistic.setItems(dataRelevant);

                } catch (Exception E) {
                    System.out.println(E);
                }
                return null;

            }

            @Override
            protected void succeeded() {
                searchProbabilistic.setDisable(false);
                probabilisticFeedback.setDisable(false);
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    public void doTestingRochio() {

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    testingButtonRochio.setDisable(true);
                    graphicPrecisionRochioButton.setDisable(true);
                    graphicRecallRochioButton.setDisable(true);

                    RadioButton selectedBoostQuery = (RadioButton) tg.getSelectedToggle();
                    double boost = Double.parseDouble(selectedBoostQuery.getText());
                    int batasTerm = Integer.parseInt(termLimit.getText());
                    double factorWeight = Double.parseDouble(factorTfIdf.getText());
                    SearchRochioTester sr = new SearchRochioTester(batasTerm, boost, factorWeight);
                    ArrayList<RochioValueTester> cranValBefore = new ArrayList<>();
                    ArrayList<RochioValueTester> cranValAfter = new ArrayList<>();
                    cranValBefore = sr.getTestSearchResult();
                    cranValAfter = sr.getTestFeedbackResult();

                    dataValueListBeforeRochio = FXCollections.observableArrayList();
                    dataValueListAfterRochio = FXCollections.observableArrayList();

                    for (int i = 0; i < cranValBefore.size(); i++) {
                        int queryId = cranValBefore.get(i).getId();
                        String precisionBefore = cranValBefore.get(i).getPercentagePrecision();

                        String recallBefore = cranValBefore.get(i).getPercentageRecall();

                        dataValueListBeforeRochio.add(new RochioValueTester(queryId, precisionBefore, recallBefore));
                    }

                    tableTestingRochioBefore.getItems().clear();
                    idQueryRochioColumnBefore.setCellValueFactory(new PropertyValueFactory<>("id"));
                    precisionBeforeRochioColumn.setCellValueFactory(new PropertyValueFactory<>("percentagePrecision"));
                    recallBeforeRochioColumn.setCellValueFactory(new PropertyValueFactory<>("percentageRecall"));
                    tableTestingRochioBefore.getItems().setAll(dataValueListBeforeRochio);

                    for (int i = 0; i < cranValAfter.size(); i++) {
                        int queryId = cranValAfter.get(i).getId();
                        String precisionBefore = cranValAfter.get(i).getPercentagePrecision();

                        String recallBefore = cranValAfter.get(i).getPercentageRecall();

                        dataValueListAfterRochio.add(new RochioValueTester(queryId, precisionBefore, recallBefore));
                    }

                    tableTestingRochioAfter.getItems().clear();
                    idQueryRochioColumnAfter.setCellValueFactory(new PropertyValueFactory<>("id"));
                    precisionAfterRochioColumn.setCellValueFactory(new PropertyValueFactory<>("percentagePrecision"));
                    recallAfterRochioColumn.setCellValueFactory(new PropertyValueFactory<>("percentageRecall"));
                    tableTestingRochioAfter.getItems().setAll(dataValueListAfterRochio);

                } catch (Exception E) {
                    System.out.println(E);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                testingButtonRochio.setDisable(false);
                graphicPrecisionRochioButton.setDisable(false);
                graphicRecallRochioButton.setDisable(false);
            }

        };

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void seeGraphicPrecisionRochio() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLChartPrecisionRochio.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        FXMLChartPrecisionRochioController controller = fxmlLoader.getController();
        controller.setObservableList(dataValueListBeforeRochio, dataValueListAfterRochio);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Precision Graph");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    private void seeGraphicRecallRochio() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLChartRecallRochio.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        FXMLChartRecallRochioController controller = fxmlLoader.getController();
        controller.setObservableList(dataValueListBeforeRochio, dataValueListAfterRochio);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Recall Graph");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    private void doTestingProbabilistic() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    testingButtonProbabilistic.setDisable(true);
                    graphicPrecisionProbabilistic.setDisable(true);
                    graphicRecallProbabilistic.setDisable(true);

                    double muValue = Double.parseDouble(muField.getText());
                    double alphaValue = Double.parseDouble(alphaField.getText());

                    SearchProbabilisticTest spSearch = new SearchProbabilisticTest(alphaValue, muValue);
                    SearchProbabilisticTest spProb = new SearchProbabilisticTest(alphaValue, muValue);
                    ArrayList<ProbabilisticValueTester> cranValBefore = new ArrayList<>();
                    ArrayList<ProbabilisticValueTester> cranValAfter = new ArrayList<>();

                    cranValBefore = spSearch.getTestSearchResult();
                    cranValAfter = spProb.getTestProbabilisticResult();

                    dataValueListBeforeProbabilistic = FXCollections.observableArrayList();
                    dataValueListAfterProbabilistic = FXCollections.observableArrayList();

                    for (int i = 0; i < cranValBefore.size(); i++) {
                        int queryId = cranValBefore.get(i).getId();
                        String precisionBefore = cranValBefore.get(i).getPrecision();

                        String recallBefore = cranValBefore.get(i).getRecall();

                        dataValueListBeforeProbabilistic.add(new ProbabilisticValueTester(queryId, precisionBefore, recallBefore));
                    }
                    tableProbabilisticBefore.getItems().clear();
                    idQueryBeforeProbabilistic.setCellValueFactory(new PropertyValueFactory<>("id"));
                    precisionBeforeProbabilistic.setCellValueFactory(new PropertyValueFactory<>("precision"));
                    recallBeforeProbabilistic.setCellValueFactory(new PropertyValueFactory<>("recall"));
                    tableProbabilisticBefore.getItems().setAll(dataValueListBeforeProbabilistic);

                    for (int i = 0; i < cranValAfter.size(); i++) {
                        int queryId = cranValAfter.get(i).getId();
                        String precisionAfter = cranValAfter.get(i).getPrecision();

                        String recallAfter = cranValAfter.get(i).getRecall();

                        dataValueListAfterProbabilistic.add(new ProbabilisticValueTester(queryId, precisionAfter, recallAfter));
                    }
                    tableProbabilisticAfter.getItems().clear();
                    idQueryAfterProbabilistic.setCellValueFactory(new PropertyValueFactory<>("id"));
                    precisionAfterProbabilistic.setCellValueFactory(new PropertyValueFactory<>("precision"));
                    recallAfterProbabilistic.setCellValueFactory(new PropertyValueFactory<>("recall"));
                    tableProbabilisticAfter.getItems().setAll(dataValueListAfterProbabilistic);
                } catch (Exception e) {
                    System.out.println(e);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                testingButtonProbabilistic.setDisable(false);
                graphicPrecisionProbabilistic.setDisable(false);
                graphicRecallProbabilistic.setDisable(false);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void seeGraphicPrecisionProbabilistic() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLChartPrecisionProbabilistic.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        FXMLChartPrecisionProbabilisticController controller = fxmlLoader.getController();
        controller.setObservableList(dataValueListBeforeProbabilistic, dataValueListAfterProbabilistic);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Precision Graph");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    private void seeGraphicRecallProbabilistic() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLChartRecallProbabilistic.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        FXMLChartRecallProbabilisticController controller = fxmlLoader.getController();
        controller.setObservableList(dataValueListBeforeProbabilistic, dataValueListAfterProbabilistic);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Recall Graph");
        stage.setScene(new Scene(root1));
        stage.show();

    }
}
